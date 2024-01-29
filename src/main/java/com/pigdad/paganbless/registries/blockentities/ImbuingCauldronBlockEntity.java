package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.utils.IngredientWithCount;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImbuingCauldronBlockEntity extends BlockEntity {
    private static final int INPUT0 = 0;
    private static final int INPUT1 = 1;
    private static final int INPUT2 = 2;
    private static final int INPUT3 = 3;
    private static final int INPUT4 = 4;
    private static final int OUTPUT = 5;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Override
        public int getSlots() {
            return 6;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot != OUTPUT;
        }
    };

    private final FluidTank fluidTank = new FluidTank(2000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public Map<Integer, ItemStack> getRenderStack() {
        Map<Integer, ItemStack> toReturn = new HashMap<>();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack itemStack = itemHandler.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                toReturn.put(i, itemStack);
            }
        }
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

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
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
        Optional<RecipeHolder<ImbuingCauldronRecipe>> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().value().getResultItem(null);

        for (IngredientWithCount ingredient : recipe.get().value().getIngredientsWithCount()) {
            ItemStack itemStack = ingredient.ingredient().getItems()[0];
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i).is(itemStack.getItem()) && itemHandler.getStackInSlot(i).getCount() >= itemStack.getCount()) {
                    itemHandler.extractItem(i, ingredient.count(), false);
                    break;
                } else if (itemHandler.getStackInSlot(i).is(PBTags.Item.HERBS) && itemHandler.getStackInSlot(i).getCount() >= itemStack.getCount()) {
                    itemHandler.extractItem(i, ingredient.count(), false);
                    break;
                }
            }
        }
        fluidTank.drain(recipe.get().value().getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
        this.itemHandler.setStackInSlot(OUTPUT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT).getCount() + result.getCount()));
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

        return recipe.map(imbuingCauldronRecipe -> imbuingCauldronRecipe.value().matchesFluid(fluidTank.getFluidInTank(0), level)).orElse(false);
    }

    private Optional<RecipeHolder<ImbuingCauldronRecipe>> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(ImbuingCauldronRecipe.Type.INSTANCE, inventory, level);
    }


    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("imbuing_cauldron_progress", progress);
        pTag = fluidTank.writeToNBT(pTag);
        super.saveAdditional(pTag);
    }

    public FluidStack getFluid() {
        return fluidTank.getFluid();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        fluidTank.readFromNBT(pTag);
        progress = pTag.getInt("imbuing_cauldron_progress");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }
}
