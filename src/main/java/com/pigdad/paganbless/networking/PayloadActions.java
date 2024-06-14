package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.registries.blockentities.IncenseBlockEntity;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PayloadActions {
    public static void runicCoreRecipeSync(RunicCoreRecipePayload payload, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockEntity blockEntity = level.getBlockEntity(payload.runicCorePos());
        if (blockEntity instanceof RunicCoreBlockEntity runicCoreBlockEntity) {
            runicCoreBlockEntity.setRunRecipe(payload.runRecipe());
            runicCoreBlockEntity.setRuneSlabs(payload.blockPoses());
        }
    }

    public static void runicCoreExplodeSync(RunicCoreExplodePayload payload, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockPos blockPos = payload.blockPos();
        level.explode(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100.0f, Level.ExplosionInteraction.NONE);
    }

    public static void incenseBurningSync(IncenseBurningPayload payload, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockPos blockPos = payload.blockPos();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof IncenseBlockEntity incenseBlockEntity) {
            incenseBlockEntity.setBurning(payload.burning());
            incenseBlockEntity.setBurningProgress(payload.burningProgress());
        }
    }
}
