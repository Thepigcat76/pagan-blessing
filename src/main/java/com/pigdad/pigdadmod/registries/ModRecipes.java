package com.pigdad.pigdadmod.registries;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.recipes.AnvilSmashingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PigDadMod.MODID);

    public static final RegistryObject<RecipeSerializer<AnvilSmashingRecipe>> ANVIL_SMASHING_SERIALIZER =
            SERIALIZERS.register(AnvilSmashingRecipe.NAME, () -> AnvilSmashingRecipe.Serializer.INSTANCE);
}
