package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.recipes.AnvilSmashingRecipe;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PBRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, PaganBless.MODID);

    public static final Supplier<RecipeSerializer<AnvilSmashingRecipe>> ANVIL_SMASHING_SERIALIZER =
            SERIALIZERS.register(AnvilSmashingRecipe.NAME, () -> AnvilSmashingRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<ImbuingCauldronRecipe>> IMBUING_CAULDRON_SERIALIZER =
            SERIALIZERS.register(ImbuingCauldronRecipe.NAME, () -> ImbuingCauldronRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<RunicRitualRecipe>> RUNIC_RITUAL_SERIALIZER =
            SERIALIZERS.register(RunicRitualRecipe.NAME, () -> RunicRitualRecipe.Serializer.INSTANCE);

}
