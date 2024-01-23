package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RunicCoreBlockEntity extends BlockEntity {
    public RunicCoreBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.RUNIC_CORE.get(), p_155229_, p_155230_);
    }

    /*
    public void craftItem(Entity sacrificedEntity) {
        if (level.isClientSide()) return;

        Set<BlockPos> runes = RunicCoreBlock.getRuneType(level, getBlockPos()).getFirst();
        if (runes != null) {
            Item runeBlock = level.getBlockState(runes.stream().toList().get(0)).getBlock().asItem();
            Set<BlockPos> positions = RunicCoreBlock.getRuneType(level, getBlockPos()).getFirst();

            Optional<RunicRitualRecipe> recipe = Optional.empty();

            for (RecipeHolder<RunicRitualRecipe> recipe1 : RecipeUtils.getAllRitualRecipes(level.getRecipeManager())) {
                if (recipe1.value().matchesRunes(runeBlock, level)) {
                    recipe = Optional.of(recipe1.value());
                    break;
                }
            }

            if (recipe.isPresent() && runeBlock != Items.AIR) {
                if (recipe.get().matchesRunes(runeBlock, level)) {
                    if (level.getBlockState(worldPosition).getValue(RunicCoreBlock.ACTIVE)) {
                        ItemStack itemStack = recipe.get().getResultItem(level.registryAccess());

                        if (itemStack.getItem() instanceof CaptureSacrificeItem captureSacrificeItem) {
                            captureSacrificeItem.setEntity(sacrificedEntity, itemStack);
                        }

                        level.explode(sacrificedEntity, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 2.0F, Level.ExplosionInteraction.NONE);

                        level.addFreshEntity(new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY() + 10, getBlockPos().getZ(),
                                itemStack));

                        RunicCoreBlock.resetPillars(level, positions);
                    } else {
                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Runic core is not activated, do so with a black thorn staff"));
                    }
                } else {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("Rune layout does not match any recipes"));
                }
            }
        } else {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(RunicCoreBlock.getRuneType(level, getBlockPos()).getSecond()));
        }
    }
     */
}
