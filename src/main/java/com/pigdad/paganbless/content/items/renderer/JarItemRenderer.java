package com.pigdad.paganbless.content.items.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.utils.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class JarItemRenderer extends BlockEntityWithoutLevelRenderer {
    public JarItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        RenderUtils.renderBlockModel(state, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        pPoseStack.popPose();
        CustomData tag = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (tag != null) {
            this.renderContent(tag, pDisplayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        }
    }

    protected void renderContent(CustomData tag, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if (tag.contains("itemhandler")) {
            ItemStackHandler itemStackHandler = new ItemStackHandler();
            itemStackHandler.deserializeNBT(Minecraft.getInstance().level.registryAccess(), tag.copyTag().getCompound("itemhandler"));
            ItemStack itemStack = itemStackHandler.getStackInSlot(0);
            renderItems(itemStack, Minecraft.getInstance().getItemRenderer(), poseStack, buffer, combinedLightIn, combinedOverlayIn);

        }
    }

    public static void renderItems(ItemStack itemStack, ItemRenderer itemRenderer, PoseStack poseStack, MultiBufferSource pBufferSource, int light, int overlay) {
        int renderedAmount = itemStack.getCount() / 8 + 1;
        for (int i = 0; i < renderedAmount; i++) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.07f + i / 20f, 0.5f);
            poseStack.scale(0.45f, 0.45f, 0.45f);
            poseStack.mulPose(Axis.XP.rotationDegrees(270));
            BakedModel model = itemRenderer.getModel(itemStack, null, null, 0);
            itemRenderer.render(itemStack, ItemDisplayContext.FIXED, true, poseStack, pBufferSource, light, overlay, model);
            poseStack.popPose();
        }
    }

}
