package com.pigdad.pigdadmod.registries.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.RuneType;
import com.pigdad.pigdadmod.registries.RuneTypes;
import com.pigdad.pigdadmod.utils.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RunicRitualRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "runic_ritual";
    private final ItemStack output;
    private final String runeType;
    private final ResourceLocation id;

    public RunicRitualRecipe(ResourceLocation id, ItemStack output, String runeType) {
        this.output = output;
        this.runeType = runeType;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer p_44002_, Level level) {
        return true;
    }

    public boolean matchesRunes(RuneType runeType, Level level) {
        if (level.isClientSide()) return false;

        return runeType.getName().equals(this.runeType);
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    public String getRuneType() {
        return runeType;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<RunicRitualRecipe> {
        private Type() {
        }

        public static final RunicRitualRecipe.Type INSTANCE = new RunicRitualRecipe.Type();
        public static final String ID = NAME;
    }

    public static class Serializer implements RecipeSerializer<RunicRitualRecipe> {
        public static final RunicRitualRecipe.Serializer INSTANCE = new RunicRitualRecipe.Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(PigDadMod.MODID, NAME);

        @Override
        public @NotNull RunicRitualRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            String runeType = json.get("runeType").getAsString();

            return new RunicRitualRecipe(id, output, runeType);
        }

        @Override
        public RunicRitualRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String runeType = new String(bytes);
            ItemStack output = buf.readItem();
            return new RunicRitualRecipe(id, output, runeType);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RunicRitualRecipe recipe) {
            buf.writeBytes(recipe.getRuneType().getBytes());

            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
