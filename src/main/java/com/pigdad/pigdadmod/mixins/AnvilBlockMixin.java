package com.pigdad.pigdadmod.mixins;

import com.pigdad.pigdadmod.PigDadMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    @Inject(
            method = "onLand",
            at = @At("TAIL")
    )
    public void anvilLanding(Level level, BlockPos blockPos, BlockState blockState, BlockState oldBlockState, FallingBlockEntity fallingBlockEntity, CallbackInfo callbackInfoLevel) {
        List<Entity> entities = level.getEntities(EntityType.ITEM.create(level), new AABB(blockPos));
        for (Entity entity : entities) {
            if (entity instanceof ItemEntity itemEntity) {
                if (itemEntity.getItem().is(Items.DIAMOND)) {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                    level.addFreshEntity(new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(Items.EMERALD)));
                }
            }
        }
    }
}
