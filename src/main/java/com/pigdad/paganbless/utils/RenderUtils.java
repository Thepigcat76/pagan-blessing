package com.pigdad.paganbless.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

public final class RenderUtils {
    // From Mystical agriculture. Thank you, Blake <3
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

    public static void renderBlockModel(BlockState blockState, PoseStack poseStack, MultiBufferSource pBufferSource, int combinedLight, int combinedOverlay) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel bakedmodel = blockRenderer.getBlockModel(blockState);
        int i = blockRenderer.blockColors.getColor(blockState, null, null, 0);
        float f = (float) (i >> 16 & 255) / 255.0F;
        float f1 = (float) (i >> 8 & 255) / 255.0F;
        float f2 = (float) (i & 255) / 255.0F;

        for (RenderType rt : bakedmodel.getRenderTypes(blockState, RandomSource.create(42L), ModelData.EMPTY)) {
            blockRenderer.modelRenderer.renderModel(poseStack.last(), pBufferSource.getBuffer(RenderTypeHelper.getEntityRenderType(rt, false)), blockState, bakedmodel, f, f1, f2, combinedLight, combinedOverlay, ModelData.EMPTY, rt);
        }
    }
}
