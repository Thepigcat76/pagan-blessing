package com.pigdad.paganbless.client.renderer;

import com.pigdad.paganbless.client.model.CrankModel;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CrankRenderer extends GeoBlockRenderer<CrankBlockEntity> {
    public CrankRenderer() {
        super(new CrankModel());
    }
}
