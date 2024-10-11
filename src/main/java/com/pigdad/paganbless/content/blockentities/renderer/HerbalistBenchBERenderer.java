package com.pigdad.paganbless.content.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.api.blocks.RotatableEntityBlock;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.content.blockentities.HerbalistBenchBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.joml.Vector3f;

public class HerbalistBenchBERenderer implements BlockEntityRenderer<HerbalistBenchBlockEntity> {
    public HerbalistBenchBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(HerbalistBenchBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        Direction facing = blockEntity.getBlockState().getValue(RotatableEntityBlock.FACING);
        ItemStack boardStack = blockEntity.getItemHandler().getStackInSlot(0);
        ItemStack toolStack = blockEntity.getItemHandler().getStackInSlot(1);

        Vector3f toolPos = switch (facing) {
            case NORTH -> new Vector3f(-0.3f, 1f, 0.4f);
            case EAST -> new Vector3f(0.6f, 1f, -0.3f);
            case SOUTH -> new Vector3f(1.35f, 1f, 0.6f);
            case WEST -> new Vector3f(0.4f, 1f, 1.35f);
            // Default branch is unreachable
            default -> null;
        };

        Vector3f boardItemPos = switch (facing) {
            case NORTH -> new Vector3f(0.5f, 1.075f, 0.4f);
            case EAST -> new Vector3f(0.6f, 1.075f, 0.5f);
            case SOUTH -> new Vector3f(0.5f, 1.075f, 0.6f);
            case WEST -> new Vector3f(0.4f, 1.075f, 0.5f);
            default -> null;
        };

        renderItem(blockEntity, poseStack, multiBufferSource, facing, renderer, boardStack, boardItemPos.x, boardItemPos.y, boardItemPos.z, boardStack.is(PBTags.ItemTags.DRIED_HERBS));
        renderItem(blockEntity, poseStack, multiBufferSource, facing, renderer, toolStack, toolPos.x, toolPos.y, toolPos.z, false);
    }

    private static void renderItem(HerbalistBenchBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource,
                                   Direction facing, ItemRenderer renderer, ItemStack itemStack, float x, float y, float z, boolean useHangingHerbRotation) {
        if (itemStack != null && !itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            poseStack.scale(0.65f, 0.65f, 0.65f);
            float yRot = facing.getOpposite().toYRot() + (useHangingHerbRotation ? 90 : 0);
            poseStack.mulPose(Axis.YN.rotationDegrees(yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            renderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(),
                    blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }
    }

    private static int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
