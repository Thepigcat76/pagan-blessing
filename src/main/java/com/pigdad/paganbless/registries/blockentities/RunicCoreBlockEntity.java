package com.pigdad.paganbless.registries.blockentities;

import com.mojang.datafixers.util.Pair;
import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.api.blocks.ContainerBlockEntity;
import com.pigdad.paganbless.api.io.IOActions;
import com.pigdad.paganbless.networking.RunicCoreRecipePayload;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import com.pigdad.paganbless.registries.blocks.RunicCoreBlock;
import com.pigdad.paganbless.registries.items.CaptureSacrificeItem;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import com.pigdad.paganbless.utils.NbtUtils;
import com.pigdad.paganbless.utils.RunicCoreUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class RunicCoreBlockEntity extends ContainerBlockEntity {
    public static final int RITUAL_TIME = PBConfig.ritualTime;
    private Set<BlockPos> runeSlabs;
    private EntityType<?> entityType;
    private boolean runRecipe;
    private int timer;

    public RunicCoreBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.RUNIC_CORE.get(), p_155229_, p_155230_);
        addItemHandler(1, (slot, stack) -> false);
        this.runeSlabs = new HashSet<>();
    }

    public void serverTick() {
        if (this.runRecipe) {
            if (RITUAL_TIME == 0) {
                performRecipe();
            }

            if (this.timer < RITUAL_TIME) {
                this.timer++;
            } else {
                level.playSound(null, (double) worldPosition.getX() + 0.5, (double) worldPosition.getY() + 0.5, (double) worldPosition.getZ() + 0.5,
                        SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.85F, 0.5F);
                performRecipe();
                this.timer = 0;
            }
        }
    }

    public void setRunRecipe(boolean run) {
        this.runRecipe = run;
    }

    public void setRuneSlabs(List<BlockPos> runeSlabs) {
        this.runeSlabs = new HashSet<>(runeSlabs);
    }

    public void clientTick() {
        if (getBlockState().getValue(RunicCoreBlock.ACTIVE)) {
            Vec3 northPos = new Vec3(worldPosition.getX() + 0.75, worldPosition.getY() + 0.65, worldPosition.getZ() + 0.23);
            Vec3 eastPos = new Vec3(worldPosition.getX() + 0.75, worldPosition.getY() + 0.65, worldPosition.getZ() + 0.75);
            Vec3 southPos = new Vec3(worldPosition.getX() + 0.23, worldPosition.getY() + 0.65, worldPosition.getZ() + 0.75);
            Vec3 westPos = new Vec3(worldPosition.getX() + 0.23, worldPosition.getY() + 0.65, worldPosition.getZ() + 0.23);
            Vec3 pos = switch (getBlockState().getValue(RunicCoreBlock.FACING)) {
                case NORTH -> northPos;
                case SOUTH -> southPos;
                case WEST -> westPos;
                case EAST -> eastPos;
                default -> null;
            };
            level.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0.0f, 0.0f, 0.0f);
            level.addParticle(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 0.0f, 0.0f, 0.0f);
        }

        if (runRecipe) {
            for (BlockPos blockPos : runeSlabs) {
                renderRuneSlabParticles(blockPos);
            }
            renderCenterParticles();
        }
    }

    private void renderRuneSlabParticles(BlockPos blockPos) {
        BlockPos renderPos = blockPos.above(2);
        Block block = level.getBlockState(blockPos).getBlock();
        if (block instanceof RuneSlabBlock runeSlabBlock) {
            int color = runeSlabBlock.getColor();
            if (color != 0) {
                level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.color(255, color)), renderPos.getX() + 0.5f, renderPos.getY(), renderPos.getZ() + 0.5f, 0, 0, 0);
                level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.color(255, color)), renderPos.getX() + 0.5f, renderPos.getY() + 1, renderPos.getZ() + 0.5f, 0, 0, 0);
            }
        }
    }

    public void craftItem(Entity sacrificedEntity) {
        Player player = Minecraft.getInstance().player;
        Optional<Set<BlockPos>> runes = RunicCoreUtils.tryGetRunePositions(level, getBlockPos()).left();
        if (runes.isPresent()) {
            this.runeSlabs = runes.get();
            this.runRecipe = true;
            this.entityType = sacrificedEntity.getType();
            PacketDistributor.sendToAllPlayers(new RunicCoreRecipePayload(worldPosition, true, runeSlabs.stream().toList()));
            level.playSound(null, (double) worldPosition.getX() + 0.5, (double) worldPosition.getY() + 0.5, (double) worldPosition.getZ() + 0.5,
                    SoundEvents.AMBIENT_CAVE.value(), SoundSource.BLOCKS, 1F, 1F);
            level.playSound(null, (double) worldPosition.getX() + 0.5, (double) worldPosition.getY() + 0.5, (double) worldPosition.getZ() + 0.5,
                    SoundEvents.AMBIENT_CAVE.value(), SoundSource.BLOCKS, 1F, 1F);
        } else {
            player.sendSystemMessage(RunicCoreUtils.tryGetRunePositions(level, getBlockPos()).right().get());
        }
    }

    private void performRecipe() {
        Player player = Minecraft.getInstance().player;
        Item runeBlock = level.getBlockState(runeSlabs.stream().toList().get(0)).getBlock().asItem();

        Optional<RunicRitualRecipe> recipe = Optional.empty();

        for (RecipeHolder<RunicRitualRecipe> recipe1 : getAllRitualRecipes(level.getRecipeManager())) {
            if (recipe1.value().matchesRunes(runeBlock, level)) {
                recipe = Optional.of(recipe1.value());
                break;
            }
        }

        if (recipe.isPresent() && runeBlock != Items.AIR) {
            if (recipe.get().matchesRunes(runeBlock, level)) {
                if (getBlockState().getValue(RunicCoreBlock.ACTIVE)) {
                    ItemStack result = recipe.get().result().copy();

                    if (result.getItem() instanceof CaptureSacrificeItem captureSacrificeItem) {
                        captureSacrificeItem.setEntity(entityType, result);
                    }

                    ItemStackHandler stackHandler = getItemHandler();
                    ItemStack stackInSlot = stackHandler.getStackInSlot(0);
                    if (stackInSlot.isEmpty() || (stackInSlot.is(result.getItem()) && stackInSlot.getCount() < stackInSlot.getMaxStackSize())) {
                        stackHandler.setStackInSlot(0, result);
                    } else {
                        Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, stackInSlot);
                        stackHandler.setStackInSlot(0, result);
                    }

                    RunicCoreUtils.resetPillars(level, runeSlabs);
                } else {
                    player.sendSystemMessage(Component.literal("Runic core is not activated, do so with a black thorn staff"));
                }
            } else {
                player.sendSystemMessage(Component.literal("Rune layout does not match any recipes"));
            }
        }
        this.runRecipe = false;
    }

    public static List<RecipeHolder<RunicRitualRecipe>> getAllRitualRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(RunicRitualRecipe.Type.INSTANCE);
    }

    @Override
    protected void saveData(CompoundTag tag) {
        super.saveData(tag);
        if (this.entityType != null) {
            tag.putString("entity_type", BuiltInRegistries.ENTITY_TYPE.getKey(this.entityType).toString());
        }
        tag.putBoolean("run_recipe", this.runRecipe);
        NbtUtils.saveBlockPosSet(tag, "rune_slabs", this.runeSlabs);
        tag.putInt("timer", this.timer);
    }

    @Override
    protected void loadData(CompoundTag tag) {
        super.loadData(tag);
        Optional<EntityType<?>> entityType = EntityType.byString(tag.getString("entity_type"));
        entityType.ifPresent(type -> this.entityType = type);
        this.runRecipe = tag.getBoolean("run_recipe");
        this.runeSlabs = NbtUtils.loadBlockPosSet(tag, "rune_slabs");
        this.timer = tag.getInt("timer");
    }

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getItemIO() {
        return Map.of(
                Direction.DOWN, Pair.of(IOActions.EXTRACT, new int[]{0})
        );
    }

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getFluidIO() {
        return Map.of();
    }

    private void renderCenterParticles() {
    }
}
