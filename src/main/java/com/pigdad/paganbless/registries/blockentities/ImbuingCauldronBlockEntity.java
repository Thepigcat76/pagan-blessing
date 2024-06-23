package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.api.blocks.ContainerBlockEntity;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.utils.recipes.IngredientWithCount;
import com.pigdad.paganbless.utils.recipes.PBFluidRecipeInput;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.joml.Vector3f;

import java.util.*;

public class ImbuingCauldronBlockEntity extends ContainerBlockEntity {
    private static final int OUTPUT = 5;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public float independentAngle;
    public float chasingVelocity;
    public int inUse;
    public int speed;

    public ImbuingCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PBBlockEntities.IMBUING_CAULDRON.get(), blockPos, blockState);
        addFluidTank(2000);
        addItemHandler(6);
        this.speed = 0;
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

    public void turn() {
        this.inUse = 10;
        this.speed = 1200;
    }

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

    public void clientTick() {
        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;

        if (inUse > 0) {
            inUse--;
            if (getFluidTank().get().getFluidAmount() >= 1000) {
                Utils.spawnParticles(level, new Vector3f(worldPosition.getX(), worldPosition.getY() + .35f, worldPosition.getZ()),
                        1, 0.07f, 0, true, ParticleTypes.BUBBLE);
            }

            if (inUse == 0) {
                this.speed = 0;
            }
        }
    }

    public void serverTick() {
        if (hasRecipe()) {
            setChanged(level, worldPosition, getBlockState());

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private float getSpeed() {
        return speed;
    }

    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
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
            getFluidTank().ifPresent(tank -> tank.drain(recipe.get().value().fluidStack().copy().getAmount(), IFluidHandler.FluidAction.EXECUTE));
            itemHandler.setStackInSlot(OUTPUT, new ItemStack(result.getItem(), itemHandler.getStackInSlot(OUTPUT).getCount() + result.getCount()));
        });
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<ImbuingCauldronRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack result = recipe.get().value().getResultItem(getLevel().registryAccess());

        boolean itemIntoOutputSlot = canInsertItemIntoOutputSlot(result);
        boolean amountIntoOutputSlot = canInsertAmountIntoOutputSlot(result);
        return amountIntoOutputSlot && itemIntoOutputSlot;
    }

    private Optional<RecipeHolder<ImbuingCauldronRecipe>> getCurrentRecipe() {
        ItemStackHandler stackHandler = this.getItemHandler().get();
        List<ItemStack> itemStacks = new ArrayList<>(stackHandler.getSlots());
        for (int i = 0; i < stackHandler.getSlots() - 1; i++) {
            itemStacks.add(stackHandler.getStackInSlot(i));
        }
        PBFluidRecipeInput recipeInput = new PBFluidRecipeInput(itemStacks, this.getFluidTank().get().getFluid());

        return this.level.getRecipeManager().getRecipeFor(ImbuingCauldronRecipe.Type.INSTANCE, recipeInput, level);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack itemStack) {
        return this.getItemHandler().get().getStackInSlot(OUTPUT).isEmpty() || this.getItemHandler().get().getStackInSlot(OUTPUT).is(itemStack.getItem());
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack itemStack) {
        return this.getItemHandler().get().getStackInSlot(OUTPUT).getCount() + itemStack.getCount() <= itemStack.getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
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
