package com.pigdad.paganbless.registries.recipes;

import com.google.gson.JsonObject;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.utils.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class RunicRitualRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "runic_ritual";
    private final ItemStack output;
    private final Item runeBlock;
    private final ResourceLocation id;

    public RunicRitualRecipe(ResourceLocation id, ItemStack output, @NotNull Item runeBlock) {
        this.output = output;
        this.runeBlock = runeBlock;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer p_44002_, Level level) {
        return true;
    }

    public boolean matchesRunes(Item block, Level level) {
        if (level.isClientSide()) return false;

        return runeBlock.equals(block);
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    public Item getRuneBlock() {
        return runeBlock;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
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
                new ResourceLocation(PaganBless.MODID, NAME);

        @Override
        public @NotNull RunicRitualRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            Item runeItem = RecipeUtils.parseItem(json.get("runeBlock"));

            PaganBless.LOGGER.debug("rune item: {}", runeItem);

            return new RunicRitualRecipe(id, output, runeItem);
        }

        @Override
        public RunicRitualRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Item runeBlock = buf.readItem().getItem();
            ItemStack output = buf.readItem();
            return new RunicRitualRecipe(id, output, runeBlock);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RunicRitualRecipe recipe) {
            buf.writeItem(recipe.runeBlock.getDefaultInstance());

            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
