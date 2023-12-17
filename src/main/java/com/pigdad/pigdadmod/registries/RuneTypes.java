package com.pigdad.pigdadmod.registries;

import net.minecraft.world.level.block.Block;

public enum RuneTypes implements RuneType {
    CINNABAR("cinnabar"),
    DIAMOND("diamond"),
    EMERALD("emerald"),
    QUARTZ("quartz"),
    AMETHYST("amethyst"),
    LAPIS("lapis");

    private final String name;

    RuneTypes(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
