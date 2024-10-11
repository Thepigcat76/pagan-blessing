package com.pigdad.paganbless.content.entities;

import com.pigdad.paganbless.registries.PBItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class EternalSnowballEntity extends Snowball {
    public EternalSnowballEntity(EntityType<? extends Snowball> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public EternalSnowballEntity(Level p_37399_, LivingEntity p_37400_) {
        super(p_37399_, p_37400_);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return PBItems.ETERNAL_SNOWBALL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37404_) {
        super.onHitEntity(p_37404_);
        Entity entity = p_37404_.getEntity();
        float i = entity instanceof Blaze ? 4F : 1.5F;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), i);
    }
}
