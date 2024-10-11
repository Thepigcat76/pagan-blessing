package com.pigdad.paganbless.content.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pigdad.paganbless.content.blockentities.IncenseBlockEntity;
import com.pigdad.paganbless.utils.rendering.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class IncenseBERenderer implements BlockEntityRenderer<IncenseBlockEntity> {
    public IncenseBERenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(IncenseBlockEntity incenseBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        {
            poseStack.pushPose();
            RenderUtils.performBlockRotation16(incenseBlockEntity.getBlockState(), poseStack);
            RenderUtils.renderBlockModel(incenseBlockEntity.getBlockState(), poseStack, multiBufferSource, i, i1);
            poseStack.popPose();
        }
    }
}
