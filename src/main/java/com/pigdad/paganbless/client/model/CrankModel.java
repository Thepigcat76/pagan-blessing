package com.pigdad.paganbless.client.model;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class CrankModel extends DefaultedBlockGeoModel<CrankBlockEntity> {
    private final ResourceLocation CRANK_MODEL = this.buildFormattedModelPath(new ResourceLocation(PaganBless.MODID, "crank"));
    private final ResourceLocation CRANK_TEXTURE = this.buildFormattedTexturePath(new ResourceLocation(PaganBless.MODID, "crank"));
    private final ResourceLocation CRANK_ANIMATIONS = this.buildFormattedAnimationPath(new ResourceLocation(PaganBless.MODID, "crank"));

    public CrankModel() {
        super(new ResourceLocation(PaganBless.MODID, "crank"));
    }

    public ResourceLocation getAnimationResource(CrankBlockEntity animatable) {
        return this.CRANK_ANIMATIONS;
    }

    public ResourceLocation getModelResource(CrankBlockEntity animatable) {
        return this.CRANK_MODEL;
    }

    public ResourceLocation getTextureResource(CrankBlockEntity animatable) {
        return this.CRANK_TEXTURE;
    }

    public RenderType getRenderType(CrankBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(this.getTextureResource(animatable));
    }
}
