package com.pigdad.paganbless.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.registries.blockentities.ContainerBlockEntity;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class RenderUtils {
    // From Mystical agriculate. thank you, Blake <3
    public static void renderFloatingItem(ItemStack stack, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.2D, 0.5D);
            float scale = stack.getItem() instanceof BlockItem ? 0.95F : 0.75F;
            poseStack.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            poseStack.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            minecraft.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, combinedLight, combinedOverlay, poseStack, multiBufferSource, minecraft.level, 0);
            poseStack.popPose();
        }
    }
}
