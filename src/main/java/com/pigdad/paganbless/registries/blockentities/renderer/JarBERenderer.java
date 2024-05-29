package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blockentities.JarBlockEntity;
import com.pigdad.paganbless.registries.blocks.JarBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import java.util.List;

public class JarBERenderer implements BlockEntityRenderer<JarBlockEntity> {
    public JarBERenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(JarBlockEntity blockEntity, float p_112308_, PoseStack poseStack, MultiBufferSource pBufferSource, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = blockEntity.getItemHandler().get().getStackInSlot(0);
        renderItems(itemStack, itemRenderer, poseStack, pBufferSource, getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, blockEntity.getBlockState().getValue(JarBlock.FACING));
    }

    public static void renderItems(ItemStack itemStack, ItemRenderer itemRenderer, PoseStack poseStack, MultiBufferSource pBufferSource, int light, int overlay, Direction direction) {
        int renderedAmount = itemStack.getCount() / 8 + 1;
        for (int i = 0; i < renderedAmount; i++) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.07f + i / 20f, 0.5f);
            poseStack.scale(0.45f, 0.45f, 0.45f);
            poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(270));
            BakedModel model = itemRenderer.getModel(itemStack, null, null, 0);
            itemRenderer.render(itemStack, ItemDisplayContext.FIXED, true, poseStack, pBufferSource, light, overlay, model);
            poseStack.popPose();
        }
    }

    private static int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}

