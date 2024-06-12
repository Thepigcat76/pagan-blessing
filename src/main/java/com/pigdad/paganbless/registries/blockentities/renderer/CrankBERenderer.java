package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import com.pigdad.paganbless.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CrankBERenderer implements BlockEntityRenderer<CrankBlockEntity> {
    public CrankBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(CrankBlockEntity crankBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        {
            poseStack.pushPose();
            RenderUtils.renderBlockModel(crankBlockEntity.getBlockState(), poseStack, multiBufferSource, i, i1);
            poseStack.popPose();
        }
    }
}
