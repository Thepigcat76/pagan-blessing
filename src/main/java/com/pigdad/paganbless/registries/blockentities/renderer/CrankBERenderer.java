package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import com.pigdad.paganbless.utils.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CrankBERenderer implements BlockEntityRenderer<CrankBlockEntity> {
    public CrankBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(CrankBlockEntity crankBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        {
            PaganBless.LOGGER.debug("Rendering crank");
            poseStack.pushPose();
            double tick = System.currentTimeMillis() / 800.0D;
            RenderUtils.rotateCentered(poseStack, Axis.ZP, (float) ((tick * 40.0D) % 360));
            RenderUtils.renderBlockModel(crankBlockEntity.getBlockState(), poseStack, multiBufferSource, i, i1);
            poseStack.popPose();
        }
    }
}
