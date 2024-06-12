package com.pigdad.paganbless.registries.entities;

import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.items.WandItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.entity.AgeableMob.getSpeedUpSecondsWhenFeeding;

public class WandProjectileEntity extends Snowball {
    public WandProjectileEntity(EntityType<? extends Snowball> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public WandProjectileEntity(Level p_37399_, LivingEntity p_37400_) {
        super(p_37399_, p_37400_);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return PBItems.WAND_PROJECTILE.get();
    }

    protected void onHitEntity(EntityHitResult p_37404_) {
        Entity entity = p_37404_.getEntity();
        if (entity instanceof Animal animal && animal.isBaby()) {
            animal.ageUp(getSpeedUpSecondsWhenFeeding(-animal.getAge()), true);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        WandItem.applyBonemeal(level(), pResult.getBlockPos(), pResult.getDirection());
    }
}
