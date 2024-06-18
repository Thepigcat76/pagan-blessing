package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.registries.blockentities.IncenseBlockEntity;
import com.pigdad.paganbless.api.blocks.IncenseBlock;
import com.pigdad.paganbless.utils.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class IncenseBERenderer implements BlockEntityRenderer<IncenseBlockEntity> {
    public IncenseBERenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(IncenseBlockEntity incenseBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        int rotation = incenseBlockEntity.getBlockState().getValue(IncenseBlock.ROTATION);
        float degrees = RotationSegment.convertToDegrees(rotation);
        {
            poseStack.pushPose();
            RenderUtils.rotateCentered(poseStack, Axis.YN, degrees);
            RenderUtils.renderBlockModel(incenseBlockEntity.getBlockState(), poseStack, multiBufferSource, i, i1);
            poseStack.popPose();
        }
    }
}
