package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.networking.CrankAnglePayload;
import com.pigdad.paganbless.networking.CrankDropPayload;
import com.pigdad.paganbless.networking.CrankRotatePayload;
import com.pigdad.paganbless.networking.CrankRotationPayload;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import com.pigdad.paganbless.registries.blocks.CrankBlock;
import com.pigdad.paganbless.registries.blocks.WinchBlock;
import com.pigdad.paganbless.utils.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class CrankBERenderer implements BlockEntityRenderer<CrankBlockEntity> {
    public CrankBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(CrankBlockEntity crankBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        {
            poseStack.pushPose();
            float angle = crankBlockEntity.getIndependentAngle(v);
            PaganBless.LOGGER.debug("Angle: {}", angle);
            RenderUtils.rotateCentered(poseStack, Axis.ZP, angle);
            RenderUtils.renderBlockModel(crankBlockEntity.getBlockState(), poseStack, multiBufferSource, i, i1);
            poseStack.popPose();
        }
    }

//    private static int calculateWinchDroppingRotation(CrankBlockEntity crankBlockEntity, int angle) {
//        BlockPos crankPos = crankBlockEntity.getBlockPos();
//        BlockState crankState = crankBlockEntity.getBlockState();
//        BlockPos winchPos = CrankBlock.getWinchPos(crankState, crankPos);
//        BlockState winchState = crankBlockEntity.getLevel().getBlockState(winchPos);
//
//        int rotation = crankState.getValue(CrankBlock.ROTATION);
//        int x = 359 / CrankBlock.CRANK_MAX_ROTATION * rotation;
//        PaganBless.LOGGER.debug("dropping, X: {}, angle: {}", x, angle);
//        int normalizedAngle = ((angle / 36 * CrankBlock.CRANK_MAX_ROTATION - 1) / 10) + 1;
//        angle = angle - 2 > 0 ? angle - 8 : 359;
//        if (winchState.getValue(WinchBlock.LIFT_DOWN)) {
//            PacketDistributor.sendToServer(new CrankAnglePayload(crankPos, angle));
//            crankBlockEntity.setAngle(angle);
//        } else {
//            PacketDistributor.sendToServer(new CrankDropPayload(crankPos, false));
//            crankBlockEntity.setDropping(false);
//            PaganBless.LOGGER.debug("Normalized angle: {}", normalizedAngle);
//            PacketDistributor.sendToServer(new CrankRotationPayload(crankPos, normalizedAngle));
//            crankBlockEntity.getLevel().setBlockAndUpdate(crankPos, crankState.setValue(CrankBlock.ROTATION, normalizedAngle));
//        }
//        return angle;
//    }
//
//    private static int calculateSimpleRotation(CrankBlockEntity crankBlockEntity, int angle) {
//        int rotation = crankBlockEntity.getBlockState().getValue(CrankBlock.ROTATION);
//        int x = 360 / CrankBlock.CRANK_MAX_ROTATION * rotation;
//        PaganBless.LOGGER.debug("X: {}, angle: {}", x, angle);
//        angle = angle < 359 ? angle + 2 : 0;
//        if (x != angle) {
//            PacketDistributor.sendToServer(new CrankAnglePayload(crankBlockEntity.getBlockPos(), angle));
//            crankBlockEntity.setAngle(angle);
//        } else {
//            PacketDistributor.sendToServer(new CrankRotatePayload(crankBlockEntity.getBlockPos(), false));
//            crankBlockEntity.setRotating(false);
//        }
//        return angle;
//    }
}
