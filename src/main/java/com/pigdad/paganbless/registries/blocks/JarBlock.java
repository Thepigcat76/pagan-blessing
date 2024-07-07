package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.api.blocks.RotatableEntityBlock;
import com.pigdad.paganbless.api.blocks.TranslucentHighlightFix;
import com.pigdad.paganbless.registries.blockentities.JarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class JarBlock extends RotatableEntityBlock implements TranslucentHighlightFix {
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(5, 11.5, 5, 11, 13.5, 11),
            Block.box(5, 9.5, 5, 11, 10.5, 11),
            Block.box(4.5, 10.5, 4.5, 11.5, 12.5, 11.5),
            Block.box(4, 0, 4, 12, 9.5, 12)
    ).reduce(Shapes::or).get();

    public JarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void spawnDestroyParticles(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState) {    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(JarBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new JarBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        JarBlockEntity jarBlockEntity = ((JarBlockEntity) pLevel.getBlockEntity(pPos));
        ItemStackHandler handler = jarBlockEntity.getItemHandler();
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        ItemStack stackInSlot = handler.getStackInSlot(0);
        if (itemInHand.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(pPlayer, stackInSlot.copyAndClear());
            jarBlockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
        } else {
            ItemStack stack = handler.insertItem(0, itemInHand.copy(), false);
            itemInHand.setCount(pPlayer.hasInfiniteMaterials() ? itemInHand.getCount() : stack.getCount());
            jarBlockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.POSITIVE);
            if (!stack.isEmpty()) {
                pLevel.playSound(null, pPos, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        if (pParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof JarBlockEntity jarBlockEntity) {
            ItemStack stack = new ItemStack(this);
            jarBlockEntity.saveToItem(stack, pParams.getLevel().registryAccess());
            return List.of(stack);
        }
        return super.getDrops(pState, pParams);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = new ItemStack(this);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof JarBlockEntity jarBlockEntity) {
            jarBlockEntity.saveToItem(stack, level.registryAccess());
        }
        return stack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ROTATION));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.getStateForPlacement(pContext);
        return state != null
                ? state.setValue(ROTATION, RotationSegment.convertToSegment(pContext.getRotation()))
                : null;
    }
}
