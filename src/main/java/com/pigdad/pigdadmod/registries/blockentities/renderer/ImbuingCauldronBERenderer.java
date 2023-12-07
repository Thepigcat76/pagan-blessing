package com.pigdad.pigdadmod.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pigdad.pigdadmod.registries.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.pigdadmod.registries.blocks.ImbuingCauldronBlock;
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
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.joml.Matrix4f;

import java.util.Map;

public class ImbuingCauldronBERenderer implements BlockEntityRenderer<ImbuingCauldronBlockEntity> {
    private float rotation = 0;
    private static final float SIDE_MARGIN = (float) ImbuingCauldronBlock.SHAPE.bounds().min(Direction.Axis.X) + 0.01f, MIN_Y = 1 / 16f, MAX_Y = 1 - MIN_Y;


    public ImbuingCauldronBERenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(ImbuingCauldronBlockEntity blockEntity, float p_112308_, PoseStack poseStack, MultiBufferSource pBufferSource, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Map<Integer, ItemStack> itemStacks = blockEntity.getRenderStack();

        for (int i : itemStacks.keySet()) {
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
                // outputs
                case 5 -> {
                    poseStack.translate(0.5f, 1.05f, 0.5f);
                    poseStack.mulPose(Axis.YN.rotationDegrees(rotation));
                }
            }
            poseStack.scale(0.25f, 0.25f, 0.25f);
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(),
                    blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, pBufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }

        if (rotation < 360) {
            rotation += 0.15F;
        } else {
            rotation = 0;
        }

        IFluidHandler fluidHandler = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElseThrow(NullPointerException::new);
        FluidStack fluidStack = fluidHandler.getFluidInTank(0);
        int fluidCapacity = fluidHandler.getTankCapacity(0);
        if (fluidStack.isEmpty())
            return;

        float fillPercentage = Math.min(1, (float) fluidStack.getAmount() / fluidCapacity) / 2;
        if (fluidStack.getFluid().getFluidType().isLighterThanAir())
            renderFluid(poseStack, pBufferSource, fluidStack, fillPercentage, 1, combinedLight);
        else
            renderFluid(poseStack, pBufferSource, fluidStack, 1, fillPercentage, combinedLight);
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float alpha, float heightPercentage, int combinedLight) {
        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.translucent());
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
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
        float minU = sprite.getU(SIDE_MARGIN * 14), maxU = sprite.getU((1 - SIDE_MARGIN) * 16);
        float minV = sprite.getV(MIN_Y * 16), maxV = sprite.getV(height * 16);
        // min z
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 0, -1).endVertex();
        // max z
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 0, 1).endVertex();
        // min x
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(-1, 0, 0).endVertex();
        // max x
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(1, 0, 0).endVertex();
        // top
        if (heightPercentage < 1) {
            minV = sprite.getV(SIDE_MARGIN * 16);
            maxV = sprite.getV((1 - SIDE_MARGIN) * 16);
            buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 1, 0).endVertex();
        }
    }
}
