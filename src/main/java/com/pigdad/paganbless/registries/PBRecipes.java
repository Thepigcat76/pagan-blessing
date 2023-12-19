package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.recipes.AnvilSmashingRecipe;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PBRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PaganBless.MODID);

    public static final RegistryObject<RecipeSerializer<AnvilSmashingRecipe>> ANVIL_SMASHING_SERIALIZER =
            SERIALIZERS.register(AnvilSmashingRecipe.NAME, () -> AnvilSmashingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ImbuingCauldronRecipe>> IMBUING_CAULDRON_SERIALIZER =
            SERIALIZERS.register(ImbuingCauldronRecipe.NAME, () -> ImbuingCauldronRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<RunicRitualRecipe>> RUNIC_RITUAL_SERIALIZER =
            SERIALIZERS.register(RunicRitualRecipe.NAME, () -> RunicRitualRecipe.Serializer.INSTANCE);
}
