package com.pigdad.paganbless.api.blocks;

import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.networking.IncenseBurningPayload;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.registries.blockentities.IncenseBlockEntity;
import com.pigdad.paganbless.registries.items.ChoppedHerbItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public abstract class IncenseBlock extends BaseEntityBlock implements TickingBlock<IncenseBlockEntity> {
    public static final EnumProperty<IncenseStates> INCENSE_STATE = EnumProperty.create("incense_state", IncenseStates.class);
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final BooleanProperty BURNING = BooleanProperty.create("burning");

    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 6.0, 12.0);

    public IncenseBlock(Properties pProperties) {
        super(pProperties.lightLevel(state -> {
            if (state.getValue(INCENSE_STATE) == IncenseStates.ASH) {
                return 5;
            } else if (state.getValue(BURNING) && state.getValue(INCENSE_STATE) != IncenseStates.ASH) {
                return 12;
            }
            return 0;
        }));
        this.registerDefaultState(this.defaultBlockState().setValue(ROTATION, 0).setValue(INCENSE_STATE, IncenseStates.ONE).setValue(BURNING, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext)
                .setValue(BURNING, false)
                .setValue(ROTATION, RotationSegment.convertToSegment(pContext.getRotation()))
                .setValue(INCENSE_STATE, IncenseStates.ONE);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(INCENSE_STATE, ROTATION, BURNING));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new IncenseBlockEntity(blockPos, blockState);
    }

    @Override
    public BlockEntityType<IncenseBlockEntity> getBlockEntityType() {
        return PBBlockEntities.INCENSE.get();
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {

            IncenseStates incenseState = pState.getValue(INCENSE_STATE);
            IncenseBlockEntity blockEntity = (IncenseBlockEntity) pLevel.getBlockEntity(pPos);

            if (incenseState == IncenseStates.ONE) {
                if (!blockEntity.isBurning()) {
                    if (pStack.is(PBTags.ItemTags.FIRE_LIGHTER)) {
                        pLevel.playSound(null, (double) pPos.getX() + 0.5, (double) pPos.getY() + 0.5, (double) pPos.getZ() + 0.5,
                                SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1F, 1F);
                        blockEntity.setBurning(true);
                        pLevel.setBlockAndUpdate(pPos, pState.setValue(BURNING, true));
                        blockEntity.setBurningProgress(PBConfig.incenseTime);
                        PacketDistributor.sendToAllPlayers(new IncenseBurningPayload(pPos, blockEntity.isBurning(), blockEntity.getBurningProgress()));
                        pStack.hurtAndBreak(1, pPlayer, LivingEntity.getSlotForHand(pHand));
                    } else if (!pStack.is(this.getIncenseItem())) {
                        Block block = PBBlocks.EMPTY_INCENSE.get();
                        if (pStack.getItem() instanceof ChoppedHerbItem choppedHerbItem) {
                            block = choppedHerbItem.getIncenseBlock();
                            pStack.shrink(1);
                        }
                        ItemHandlerHelper.giveItemToPlayer(pPlayer, this.getIncenseItem().getDefaultInstance());
                        pLevel.setBlockAndUpdate(pPos, block.defaultBlockState().setValue(ROTATION, pState.getValue(ROTATION)));
                    }
                }
            }
        }

        if (pStack.is(PBItems.BLACK_THORN_STAFF.get())) {
            ParticleUtils.spawnParticles(pLevel, pPos, 100, getRange(pLevel, pPos, pState), 0, true, ParticleTypes.HAPPY_VILLAGER);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public void clientTick(Level level, BlockPos blockPos, BlockState blockState, IncenseBlockEntity blockEntity) {
        IncenseStates state = blockState.getValue(INCENSE_STATE);

        if (blockEntity.isBurning()) {
            if (state == IncenseStates.ASH) {
                level.addParticle(ParticleTypes.SMOKE, blockPos.getX() + .5f, blockPos.getY() + .3f, blockPos.getZ() + .5f, 0.0f, 0.0f, 0.0f);
            } else {
                level.addParticle(ParticleTypes.SMOKE, blockPos.getX() + .5f, blockPos.getY() + .3f, blockPos.getZ() + .5f, 0.0f, 0.0f, 0.0f);
                level.addParticle(ParticleTypes.FLAME, blockPos.getX() + .5f, blockPos.getY() + .3f, blockPos.getZ() + .5f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    @Override
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState, IncenseBlockEntity blockEntity) {
        IncenseStates incenseState = blockState.getValue(INCENSE_STATE);

        int burnStage = incenseState.getBurnStage();
        int burningProgress = blockEntity.getBurningProgress();
        int nextStateChange = (PBConfig.incenseTime / 4) * burnStage;

        if (burningProgress == nextStateChange) {
            decrIncenseState(level, blockPos, blockState);
        }

        if (burningProgress != 0) {
            blockEntity.setBurningProgress(burningProgress - 1);
            PacketDistributor.sendToAllPlayers(new IncenseBurningPayload(blockPos, blockEntity.isBurning(), blockEntity.getBurningProgress()));
            effectTick(level, blockPos, blockState);
        }

        if (incenseState == IncenseStates.ASH && burningProgress == 0 && blockEntity.isBurning()) {
            blockEntity.setBurning(false);
            blockEntity.setBurningProgress(0);
            level.setBlockAndUpdate(blockPos, blockState.setValue(BURNING, false));
            level.setBlockAndUpdate(blockPos, PBBlocks.EMPTY_INCENSE.get().defaultBlockState().setValue(ROTATION, blockState.getValue(ROTATION)));
            PacketDistributor.sendToAllPlayers(new IncenseBurningPayload(blockPos, false, 0));
            level.playSound(null, (double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5,
                    SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1F, 1F);
        }

    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return PBBlocks.EMPTY_INCENSE.get().asItem().getDefaultInstance();
    }

    public abstract void effectTick(Level level, BlockPos blockPos, BlockState blockState);

    public abstract int getRange(Level level, BlockPos blockPos, BlockState blockState);

    public abstract Item getIncenseItem();

    private static void decrIncenseState(Level level, BlockPos blockPos, BlockState blockState) {
        IncenseStates state = switch (blockState.getValue(INCENSE_STATE)) {
            case ONE -> IncenseStates.TWO;
            case TWO -> IncenseStates.THREE;
            case THREE -> IncenseStates.FOUR;
            case FOUR -> IncenseStates.ASH;
            default -> blockState.getValue(INCENSE_STATE);
        };
        level.setBlockAndUpdate(blockPos, blockState.setValue(INCENSE_STATE, state));
    }

    public enum IncenseStates implements StringRepresentable {
        ASH("ash"),
        ONE("one"),
        TWO("two"),
        THREE("three"),
        FOUR("four");

        private final String name;

        IncenseStates(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public int getBurnStage() {
            return switch (this) {
                case ONE -> 4;
                case TWO -> 3;
                case THREE -> 2;
                case FOUR -> 1;
                default -> 0;
            };
        }
    }
}
