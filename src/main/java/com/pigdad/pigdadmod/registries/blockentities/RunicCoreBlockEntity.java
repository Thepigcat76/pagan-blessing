package com.pigdad.pigdadmod.registries.blockentities;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.ModBlockEntities;
import com.pigdad.pigdadmod.registries.RuneType;
import com.pigdad.pigdadmod.registries.blocks.RuneSlabBlock;
import com.pigdad.pigdadmod.registries.blocks.RunicCoreBlock;
import com.pigdad.pigdadmod.registries.items.CaptureSacrificeItem;
import com.pigdad.pigdadmod.registries.recipes.AnvilSmashingRecipe;
import com.pigdad.pigdadmod.registries.recipes.RunicRitualRecipe;
import com.pigdad.pigdadmod.utils.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Set;

public class RunicCoreBlockEntity extends BlockEntity {
    public RunicCoreBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.RUNIC_CORE.get(), p_155229_, p_155230_);
    }

    public void craftItem(Entity sacrificedEntity) {
        if (RunicCoreBlock.getRuneType(level, getBlockPos()) != null) {
            RuneType runeType = RunicCoreBlock.getRuneType(level, getBlockPos()).getFirst();
            Set<BlockPos> positions = RunicCoreBlock.getRuneType(level, getBlockPos()).getSecond();

            Optional<RunicRitualRecipe> recipe = Optional.empty();

            for (RunicRitualRecipe recipe1 : RecipeUtils.getAllRitualRecipes(level.getRecipeManager())) {
                if (recipe1.matchesRunes(runeType, level)) {
                    recipe = Optional.of(recipe1);
                    break;
                }
            }

            if (recipe.isPresent() && runeType != null) {
                if (recipe.get().matchesRunes(runeType, level) && level.getBlockState(worldPosition).getValue(RunicCoreBlock.ACTIVE)) {
                    ItemStack itemStack = recipe.get().getResultItem(level.registryAccess());

                    if (itemStack.getItem() instanceof CaptureSacrificeItem captureSacrificeItem) {
                        captureSacrificeItem.setEntity(sacrificedEntity, itemStack);
                    }

                    level.explode(sacrificedEntity, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 2.0F, Level.ExplosionInteraction.NONE);

                    level.addFreshEntity(new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY() + 10, getBlockPos().getZ(),
                            itemStack));

                    RunicCoreBlock.resetPillars(level, positions);
                }
            }
        }
    }
}
