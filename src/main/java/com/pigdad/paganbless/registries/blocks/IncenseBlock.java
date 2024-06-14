package com.pigdad.paganbless.registries.blocks;

import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.networking.IncenseBurningPayload;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.IncenseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public abstract class IncenseBlock extends BaseEntityBlock {
    public static final EnumProperty<IncenseStates> INCENSE_STATE = EnumProperty.create("incense_state", IncenseStates.class);
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 6.0, 12.0);

    public IncenseBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(ROTATION, 0));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext)
                .setValue(ROTATION, RotationSegment.convertToSegment(pContext.getRotation()))
                .setValue(INCENSE_STATE, IncenseStates.EMPTY);
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
        super.createBlockStateDefinition(pBuilder.add(INCENSE_STATE, ROTATION));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new IncenseBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, PBBlockEntities.INCENSE.get(), pLevel.isClientSide() ? this::clientTick : this::serverTick);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            if (pStack.is(getIncenseItem()) && pState.getValue(INCENSE_STATE) == IncenseStates.EMPTY) {
                pLevel.playSound(null, (double) pPos.getX() + 0.5, (double) pPos.getY() + 0.5, (double) pPos.getZ() + 0.5,
                        SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
                pLevel.setBlockAndUpdate(pPos, pState.setValue(INCENSE_STATE, IncenseStates.ONE));
                if (!pPlayer.hasInfiniteMaterials()) {
                    pStack.shrink(1);
                }
            }

            IncenseStates incenseState = pState.getValue(INCENSE_STATE);
            IncenseBlockEntity blockEntity = (IncenseBlockEntity) pLevel.getBlockEntity(pPos);

            if (incenseState == IncenseStates.ONE) {
                if (!blockEntity.isBurning() && pStack.is(Items.FLINT_AND_STEEL)) {
                    pLevel.playSound(null, (double) pPos.getX() + 0.5, (double) pPos.getY() + 0.5, (double) pPos.getZ() + 0.5,
                            SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1F, 1F);
                    blockEntity.setBurning(true);
                    blockEntity.setBurningProgress(PBConfig.incenseTime);
                    PacketDistributor.sendToAllPlayers(new IncenseBurningPayload(pPos, blockEntity.isBurning(), blockEntity.getBurningProgress()));
                    pStack.hurtAndBreak(1, pPlayer, LivingEntity.getSlotForHand(pHand));
                }
            }
        }
        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    public void clientTick(Level level, BlockPos blockPos, BlockState blockState, IncenseBlockEntity blockEntity) {
        IncenseStates state = blockState.getValue(INCENSE_STATE);

        if (blockEntity.isBurning()) {
            if (state == IncenseStates.ASH) {
                level.addParticle(ParticleTypes.SMOKE, blockPos.getX() + .5f, blockPos.getY() + .3f, blockPos.getZ() + .5f, 0.0f, 0.0f, 0.0f);
            } else if (state != IncenseStates.EMPTY) {
                level.addParticle(ParticleTypes.SMOKE, blockPos.getX() + .5f, blockPos.getY() + .3f, blockPos.getZ() + .5f, 0.0f, 0.0f, 0.0f);
                level.addParticle(ParticleTypes.FLAME, blockPos.getX() + .5f, blockPos.getY() + .3f, blockPos.getZ() + .5f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    private void serverTick(Level level, BlockPos blockPos, BlockState blockState, IncenseBlockEntity blockEntity) {
        IncenseStates incenseState = blockState.getValue(INCENSE_STATE);

        int burnStage = incenseState.getBurnStage();
        int burningProgress = blockEntity.getBurningProgress();
        int nextStateChange = (PBConfig.incenseTime / 4) * burnStage;

        if (burningProgress == nextStateChange) {
            decrIncenseState(level, blockPos, blockState);
        }

        if (incenseState != IncenseStates.EMPTY && burningProgress != 0) {
            blockEntity.setBurningProgress(burningProgress - 1);
            PacketDistributor.sendToAllPlayers(new IncenseBurningPayload(blockPos, blockEntity.isBurning(), blockEntity.getBurningProgress()));
            level.playSound(null, (double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5,
                    SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1F, 1F);
        }

        if (incenseState == IncenseStates.ASH && burningProgress == 0 && blockEntity.isBurning()) {
            blockEntity.setBurning(false);
            blockEntity.setBurningProgress(0);
            level.setBlockAndUpdate(blockPos, blockState.setValue(INCENSE_STATE, IncenseStates.EMPTY));
            PacketDistributor.sendToAllPlayers(new IncenseBurningPayload(blockPos, false, 0));
            level.playSound(null, (double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5,
                    SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1F, 1F);
        }
    }

    public abstract void effectTick(Level level, BlockPos blockPos, BlockState blockState, IncenseBlockEntity blockEntity);

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
        EMPTY("empty"),
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
