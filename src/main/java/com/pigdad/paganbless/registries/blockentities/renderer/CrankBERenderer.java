package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import com.pigdad.paganbless.registries.blocks.CrankBlock;
import com.pigdad.paganbless.utils.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class CrankBERenderer implements BlockEntityRenderer<CrankBlockEntity> {
    public CrankBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(CrankBlockEntity crankBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        {
            poseStack.pushPose();
            float angle = crankBlockEntity.getIndependentAngle(v);
            Direction facing = crankBlockEntity.getBlockState().getValue(CrankBlock.FACING);
            RenderUtils.rotateCentered(poseStack, facing, angle);
            RenderUtils.renderBlockModel(crankBlockEntity.getBlockState(), poseStack, multiBufferSource, i, i1);
            poseStack.popPose();
        }
    }
}
