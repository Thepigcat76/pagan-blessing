package com.pigdad.paganbless.datagen;

import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

public class PBLootTableProvider extends BlockLootSubProvider {
    private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();

    protected PBLootTableProvider(HolderLookup.Provider provider) {
        super(Collections.emptySet(), FeatureFlags.VANILLA_SET, provider);
    }

    @Override
    protected void add(@NotNull Block block, @NotNull LootTable.Builder table) {
        //Overwrite the core register method to add to our list of known blocks
        super.add(block, table);
        knownBlocks.add(block);
    }

    @Override
    protected void generate() {
        dropSelf(PBBlocks.IMBUING_CAULDRON.get());
        dropSelf(PBBlocks.HERBALIST_BENCH.get());
        dropSelf(PBBlocks.WINCH.get());
        dropSelf(PBBlocks.CRANK.get());
        dropSelf(PBBlocks.RUNIC_CORE.get());

        dropSelf(PBBlocks.WICAN_WARD.get());

        dropSelf(PBBlocks.EMPTY_INCENSE.get());
        dropOther(PBBlocks.LAVENDER_INCENSE.get(), PBBlocks.EMPTY_INCENSE.get());
        dropOther(PBBlocks.RUE_INCENSE.get(), PBBlocks.EMPTY_INCENSE.get());

        dropSelf(PBBlocks.RUNE_SLAB_INERT.get());
        dropSelf(PBBlocks.RUNE_SLAB_AMETHYST.get());
        dropSelf(PBBlocks.RUNE_SLAB_QUARTZ.get());
        dropSelf(PBBlocks.RUNE_SLAB_LAPIS.get());
        dropSelf(PBBlocks.RUNE_SLAB_EMERALD.get());
        dropSelf(PBBlocks.RUNE_SLAB_DIAMOND.get());
        dropSelf(PBBlocks.RUNE_SLAB_QUARTZ.get());

        dropSelf(PBBlocks.HANGING_LAVENDER.get());
        dropSelf(PBBlocks.HANGING_RUE.get());
        dropSelf(PBBlocks.DRIED_HANGING_LAVENDER.get());
        dropSelf(PBBlocks.DRIED_HANGING_RUE.get());

        dropHerbPlant(PBBlocks.BELLADONNA_PLANT.get(), PBItems.BELLADONNA.get());
        dropHerbPlant(PBBlocks.RUE_PLANT.get(), PBItems.RUE.get());
        dropHerbPlant(PBBlocks.MUGWORT_PLANT.get(), PBItems.MUGWORT.get());
        dropHerbPlant(PBBlocks.MANDRAKE_ROOT_PLANT.get(), PBItems.MANDRAKE_ROOT.get());
        dropHerbPlant(PBBlocks.LAVENDER_PLANT.get(), PBItems.LAVENDER.get());
        dropHerbPlant(PBBlocks.HAGS_TAPER_PLANT.get(), PBItems.HAG_TAPER.get());

        for (Supplier<Block> block : PBBlocks.WOOD_BLOCKS) {
            dropSelf(block.get());
        }

        add(PBBlocks.BLACK_THORN_LEAVES.get(), createLeavesDrops(PBBlocks.BLACK_THORN_LEAVES.get(), PBBlocks.BLACK_THORN_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(PBBlocks.BLACK_THORN_SAPLING.get());

        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(Blocks.REDSTONE_ORE, this.createSilkTouchDispatchTable(Blocks.REDSTONE_ORE, this.applyExplosionDecay(Blocks.REDSTONE_ORE, LootItem.lootTableItem(Items.REDSTONE)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F)))
                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
        )).withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1f))
                .add(LootItem.lootTableItem(PBItems.CINNABAR.get()))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))));
    }

    private void dropHerbPlant(Block block, ItemLike item) {
        add(block, this.createShearsDispatchTable(block, this.applyExplosionDecay(block, (LootItem.lootTableItem(item)
                .when(LootItemRandomChanceCondition.randomChance(0.125F)))
                .apply(ApplyBonusCount.addUniformBonusCount(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 2))))
        );
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    private LootItemCondition.Builder hasShearsOrSilkTouch() {
        return HAS_SHEARS.or(this.hasSilkTouch());
    }
}
