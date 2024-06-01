package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.utils.IngredientWithCount;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImbuingCauldronBlockEntity extends ContainerBlockEntity {
    private static final int OUTPUT = 5;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public Map<Integer, ItemStack> getRenderStack() {
        Map<Integer, ItemStack> toReturn = new HashMap<>();
        getItemHandler().ifPresent(handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack itemStack = handler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    toReturn.put(i, itemStack);
                }
            }
        });
        return toReturn;
    }

    public boolean isActive() {
        return progress > 0;
    }

    public static int getCapacity() {
        return 2000;
    }

    public ImbuingCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PBBlockEntities.IMBUING_CAULDRON.get(), blockPos, blockState);
        addFluidTank(2000);
        addItemHandler(6);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> progress = pValue;
                    case 1 -> maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe() && fluidMatches()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        getItemHandler().ifPresent(itemHandler -> {
            Optional<RecipeHolder<ImbuingCauldronRecipe>> recipe = getCurrentRecipe();
            ItemStack result = recipe.get().value().getResultItem(null);

            for (IngredientWithCount ingredient : recipe.get().value().getIngredientsWithCount()) {
                ItemStack itemStack = ingredient.ingredient().getItems()[0];
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    if (itemHandler.getStackInSlot(i).is(itemStack.getItem()) && itemHandler.getStackInSlot(i).getCount() >= itemStack.getCount()) {
                        itemHandler.extractItem(i, ingredient.count(), false);
                        break;
                    } else if (itemHandler.getStackInSlot(i).is(PBTags.ItemTags.HERBS) && itemHandler.getStackInSlot(i).getCount() >= itemStack.getCount()) {
                        itemHandler.extractItem(i, ingredient.count(), false);
                        break;
                    }
                }
            }
            getFluidTank().ifPresent(tank -> tank.drain(recipe.get().value().fluidStack().getAmount(), IFluidHandler.FluidAction.EXECUTE));
            itemHandler.setStackInSlot(OUTPUT, new ItemStack(result.getItem(), itemHandler.getStackInSlot(OUTPUT).getCount() + result.getCount()));
        });
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<ImbuingCauldronRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack result = recipe.get().value().getResultItem(getLevel().registryAccess());

        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    public boolean fluidMatches() {
        Optional<RecipeHolder<ImbuingCauldronRecipe>> recipe = getCurrentRecipe();

        return recipe.map(imbuingCauldronRecipe -> imbuingCauldronRecipe.value().matchesFluid(getFluidTank().get().getFluidInTank(0), level)).orElse(false);
    }

    private Optional<RecipeHolder<ImbuingCauldronRecipe>> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.getItemHandler().get().getSlots());
        for (int i = 0; i < this.getItemHandler().get().getSlots(); i++) {
            inventory.setItem(i, this.getItemHandler().get().getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(ImbuingCauldronRecipe.Type.INSTANCE, inventory, level);
    }


    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getItemHandler().get().getStackInSlot(OUTPUT).isEmpty() || this.getItemHandler().get().getStackInSlot(OUTPUT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.getItemHandler().get().getStackInSlot(OUTPUT).getCount() + count <= this.getItemHandler().get().getStackInSlot(OUTPUT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    @Override
    protected void saveOther(CompoundTag pTag) {
        pTag.putInt("imbuing_cauldron_progress", progress);
    }

    @Override
    public void loadOther(CompoundTag pTag) {
        progress = pTag.getInt("imbuing_cauldron_progress");
    }
}
