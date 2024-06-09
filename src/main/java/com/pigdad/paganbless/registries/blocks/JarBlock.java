package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.blockentities.JarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class JarBlock extends RotatableEntityBlock {
    public JarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Stream.of(
                Block.box(5, 11.5, 5, 11, 13.5, 11),
                Block.box(5, 9.5, 5, 11, 10.5, 11),
                Block.box(4.5, 10.5, 4.5, 11.5, 12.5, 11.5),
                Block.box(4, 0, 4, 12, 9.5, 12)
        ).reduce(Shapes::or).get();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
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
        jarBlockEntity.getItemHandler().ifPresent(handler -> {
            ItemStack itemInHand = pPlayer.getItemInHand(pHand);
            if (itemInHand.isEmpty()) {
                ItemHandlerHelper.giveItemToPlayer(pPlayer, handler.getStackInSlot(0).copyAndClear());
                jarBlockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
            } else {
                if (handler.getStackInSlot(0).isEmpty() || handler.getStackInSlot(0).is(itemInHand.getItem())) {
                    handler.insertItem(0, itemInHand.copyAndClear(), false);
                    jarBlockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.POSITIVE);
                }
            }
        });
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = new ItemStack(this);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof JarBlockEntity jarBlockEntity) {
            jarBlockEntity.saveToItem(stack, level.registryAccess());
        }
        return stack;
    }
}
