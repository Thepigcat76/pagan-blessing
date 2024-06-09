package com.pigdad.paganbless.registries.items.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blockentities.renderer.JarBERenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
//        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
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
            JarBERenderer.renderItems(itemStack, Minecraft.getInstance().getItemRenderer(), poseStack, buffer, combinedLightIn, combinedOverlayIn, Direction.NORTH);

        }
    }
}
