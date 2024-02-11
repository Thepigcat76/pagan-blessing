package com.pigdad.paganbless.client.renderer;

import com.pigdad.paganbless.client.model.CrankModel;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
import software.bernie.example.block.entity.FertilizerBlockEntity;
import software.bernie.example.client.model.block.FertilizerModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CrankRenderer extends GeoBlockRenderer<CrankBlockEntity> {
    public CrankRenderer() {
        super(new CrankModel());
    }
}
