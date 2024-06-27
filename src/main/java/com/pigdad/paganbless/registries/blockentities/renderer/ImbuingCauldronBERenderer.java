package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.paganbless.registries.blocks.ImbuingCauldronBlock;
import com.pigdad.paganbless.utils.Utils;
import com.pigdad.paganbless.utils.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.joml.Matrix4f;

import java.util.Map;

public class ImbuingCauldronBERenderer implements BlockEntityRenderer<ImbuingCauldronBlockEntity> {
    private static final float SIDE_MARGIN = (float) ImbuingCauldronBlock.SHAPE.min(Direction.Axis.X) + 0.1f;
    private static final float MIN_Y = 2 / 16f;
    private static final float MAX_Y = 1 - MIN_Y;

    public ImbuingCauldronBERenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(ImbuingCauldronBlockEntity blockEntity, float pPartialTicks, PoseStack poseStack, MultiBufferSource pBufferSource, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Map<Integer, ItemStack> itemStacks = blockEntity.getRenderStack();

        // Items
        for (int i : itemStacks.keySet()) {
            if (i == 5) {
                RenderUtils.renderFloatingItem(blockEntity.getItemHandler().getStackInSlot(5), poseStack, pBufferSource, combinedLight, combinedOverlay);
                continue;
            }
            ItemStack itemStack = itemStacks.get(i);
            poseStack.pushPose();
            switch (i) {
                // inputs
                case 0 -> {
                    poseStack.translate(0.34f, 0.49f, 0.62f);
                    poseStack.mulPose(Axis.YN.rotationDegrees(120));
                }
                case 1 -> {
                    poseStack.translate(0.4f, 0.47f, 0.30f);
                    poseStack.mulPose(Axis.YN.rotationDegrees(240));
                }
                case 2 -> {
                    poseStack.translate(0.53f, 0.48f, 0.66f);
                    poseStack.mulPose(Axis.YN.rotationDegrees(7));
                }
                case 3 -> {
                    poseStack.translate(0.6f, 0.45f, 0.3f);
                    poseStack.mulPose(Axis.YN.rotationDegrees(30));
                }
                case 4 -> {
                    poseStack.translate(0.66f, 0.5f, 0.5f);
                    poseStack.mulPose(Axis.YN.rotationDegrees(353));
                }
            }
            poseStack.scale(0.25f, 0.25f, 0.25f);
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
                    getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY,
                    poseStack, pBufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }

        // Stirring stick
        {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.4, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getIndependentAngle(pPartialTicks)));
            itemRenderer.renderStatic(PBItems.STIRRING_STICK.get().getDefaultInstance(), ItemDisplayContext.FIXED,
                    getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY,
                    poseStack, pBufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }

        // Fluids
        try {
            IFluidHandler fluidHandler = Utils.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity);
            FluidStack fluidStack = fluidHandler.getFluidInTank(0);
            int fluidCapacity = fluidHandler.getTankCapacity(0);
            if (fluidStack.isEmpty())
                return;

            float fillPercentage = Math.min(1, (float) fluidStack.getAmount() / fluidCapacity) / 2;
            if (fluidStack.getFluid().getFluidType().isLighterThanAir())
                renderFluid(poseStack, pBufferSource, fluidStack, fillPercentage, 1, combinedLight);
            else
                renderFluid(poseStack, pBufferSource, fluidStack, 1, fillPercentage, combinedLight);
        } catch (Exception ignored) {
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float alpha, float heightPercentage, int combinedLight) {
        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.translucent());
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        //noinspection deprecation
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluidTypeExtensions.getStillTexture(fluidStack));
        int color = fluidTypeExtensions.getTintColor();
        alpha *= (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;

        renderQuads(poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, heightPercentage, combinedLight);
    }

    private static void renderQuads(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float heightPercentage, int light) {
        float height = MIN_Y + (MAX_Y - MIN_Y) * heightPercentage;
        float minU = sprite.getU(SIDE_MARGIN), maxU = sprite.getU((1 - SIDE_MARGIN));
        float minV = sprite.getV(MIN_Y), maxV = sprite.getV(height);
        // min z
        buffer.addVertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 0, -1);
        // max z
        buffer.addVertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 0, 1);
        // min x
        buffer.addVertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(-1, 0, 0);
        // max x
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(1, 0, 0);
        // top
        if (heightPercentage < 1) {
            minV = sprite.getV(SIDE_MARGIN);
            maxV = sprite.getV(1 - SIDE_MARGIN);
            buffer.addVertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 1, 0);
            buffer.addVertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 1, 0);
            buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 1, 0);
            buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 1, 0);
        }
    }
}
