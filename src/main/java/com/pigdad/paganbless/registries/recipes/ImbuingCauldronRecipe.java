package com.pigdad.paganbless.registries.recipes;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.utils.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;

public class ImbuingCauldronRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "cauldron_imbuing";
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final FluidStack fluidStack;

    public ImbuingCauldronRecipe(ItemStack output, NonNullList<Ingredient> inputItems, FluidStack fluidStack) {
        this.inputItems = inputItems;
        this.output = output;
        this.fluidStack = fluidStack;
        if (inputItems.size() > 5) {
            throw new IllegalStateException("Amount of input items for Imbuing cauldron recipe is too high. Maximum: 5, found: " + inputItems.size());
        }
    }

    public ImbuingCauldronRecipe(ItemStack output, NonNullList<Ingredient> ingredients, String fluidType, int fluidAmount) {
        this(output, ingredients, new FluidStack(BuiltInRegistries.FLUID.get(new ResourceLocation(fluidType)), fluidAmount));
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;

        List<ItemStack> containerItems = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            containerItems.add(container.getItem(i));
        }

        List<Boolean> checked = new ArrayList<>();

        for (Ingredient inputItem : inputItems) {
            for (ItemStack item : containerItems) {
                if (inputItem.test(item)) {
                    checked.add(true);
                    break;
                }
            }
        }

        // Check if all input items match
        return checked.stream().allMatch(Boolean::booleanValue) && checked.size() == inputItems.size();
    }

    public boolean matchesFluid(FluidStack fluidStack, Level level) {
        if (level.isClientSide()) return false;

        return (fluidStack.getAmount() >= this.fluidStack.getAmount() && fluidStack.getFluid().isSame(this.fluidStack.getFluid()));
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    @Override
    public @NotNull ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return AnvilSmashingRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<ImbuingCauldronRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = NAME;
    }

    public static class Serializer implements RecipeSerializer<ImbuingCauldronRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final Codec<ImbuingCauldronRecipe> CODEC = RecordCodecBuilder.create((p_300831_) -> p_300831_.group(
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((p_300827_) -> p_300827_.getResultItem(null)),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap((p_301021_)
                                -> DataResult.success(NonNullList.of(Ingredient.EMPTY, p_301021_.toArray(Ingredient[]::new))), DataResult::success)
                        .forGetter(ImbuingCauldronRecipe::getIngredients),
                Codec.STRING.fieldOf("fluidType").forGetter((recipe) -> recipe.getFluidStack().getFluid().getFluidType().toString()),
                Codec.INT.fieldOf("fluidAmount").forGetter((recipe) -> recipe.getFluidStack().getAmount())
        ).apply(p_300831_, ImbuingCauldronRecipe::new));

        @Override
        public ImbuingCauldronRecipe fromNetwork(FriendlyByteBuf buf) {
            int inputSize = buf.readInt();
            List<Ingredient> inputs = new ArrayList<>();
            FluidStack fluidStack = buf.readFluidStack();

            for (int i = 0; i < inputSize; i++) {
                inputs.add(Ingredient.fromNetwork(buf));
            }

            NonNullList<Ingredient> ingredients = NonNullList.create();
            ingredients.addAll(inputs);

            ItemStack output = buf.readItem();
            return new ImbuingCauldronRecipe(output, ingredients, fluidStack);
        }

        @Override
        public Codec<ImbuingCauldronRecipe> codec() {
            return CODEC;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ImbuingCauldronRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            buf.writeFluidStack(recipe.fluidStack);

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }

            buf.writeItem(recipe.getResultItem(null));
        }
    }
}
