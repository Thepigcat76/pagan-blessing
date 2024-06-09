package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.registries.blockentities.JarBlockEntity;
import com.pigdad.paganbless.registries.blocks.JarBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.Iterator;

public class JarBERenderer implements BlockEntityRenderer<JarBlockEntity> {
    public JarBERenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(JarBlockEntity blockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource pBufferSource, int combinedLight, int combinedOverlay) {
        {
            poseStack.pushPose();
            DecoratedPotBlockEntity.WobbleStyle decoratedpotblockentity$wobblestyle = blockEntity.lastWobbleStyle;
            if (decoratedpotblockentity$wobblestyle != null && blockEntity.getLevel() != null) {
                float f = ((float)(blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + pPartialTick) / (float)decoratedpotblockentity$wobblestyle.duration;
                if (f >= 0.0F && f <= 1.0F) {
                    float f1;
                    float f2;
                    if (decoratedpotblockentity$wobblestyle == DecoratedPotBlockEntity.WobbleStyle.POSITIVE) {
                        f2 = f * 6.2831855F;
                        float f3 = -1.5F * (Mth.cos(f2) + 0.5F) * Mth.sin(f2 / 2.0F);
                        poseStack.rotateAround(Axis.XP.rotation(f3 * 0.015625F), 0.5F, 0.0F, 0.5F);
                        float f4 = Mth.sin(f2);
                        poseStack.rotateAround(Axis.ZP.rotation(f4 * 0.015625F), 0.5F, 0.0F, 0.5F);
                    } else {
                        f1 = Mth.sin(-f * 3.0F * 3.1415927F) * 0.125F;
                        f2 = 1.0F - f;
                        poseStack.rotateAround(Axis.YP.rotation(f1 * f2), 0.5F, 0.0F, 0.5F);
                    }
                }
            }
            renderBlockModel(blockEntity, poseStack, pBufferSource, combinedLight, combinedOverlay);
            poseStack.popPose();
        }

        {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack itemStack = blockEntity.getItemHandler().get().getStackInSlot(0);
            renderItems(itemStack, itemRenderer, poseStack, pBufferSource, getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, blockEntity.getBlockState().getValue(JarBlock.FACING));
        }
    }

    private static void renderBlockModel(JarBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource pBufferSource, int combinedLight, int combinedOverlay) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel bakedmodel = blockRenderer.getBlockModel(blockEntity.getBlockState());
        int i = blockRenderer.blockColors.getColor(blockEntity.getBlockState(), null, null, 0);
        float f = (float) (i >> 16 & 255) / 255.0F;
        float f1 = (float) (i >> 8 & 255) / 255.0F;
        float f2 = (float) (i & 255) / 255.0F;

        for (RenderType rt : bakedmodel.getRenderTypes(blockEntity.getBlockState(), RandomSource.create(42L), ModelData.EMPTY)) {
            blockRenderer.modelRenderer.renderModel(poseStack.last(), pBufferSource.getBuffer(RenderTypeHelper.getEntityRenderType(rt, false)), blockEntity.getBlockState(), bakedmodel, f, f1, f2, combinedLight, combinedOverlay, ModelData.EMPTY, rt);
        }
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

