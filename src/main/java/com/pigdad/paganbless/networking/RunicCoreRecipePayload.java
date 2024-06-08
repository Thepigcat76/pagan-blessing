package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record RunicCoreRecipePayload(BlockPos runicCorePos, boolean runRecipe, List<BlockPos> blockPoses) implements CustomPacketPayload {
    public static final Type<RunicCoreRecipePayload> TYPE = new Type<>(new ResourceLocation(PaganBless.MODID, "runic_core_recipe_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RunicCoreRecipePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            RunicCoreRecipePayload::runicCorePos,
            ByteBufCodecs.BOOL,
            RunicCoreRecipePayload::runRecipe,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()),
            RunicCoreRecipePayload::blockPoses,
            RunicCoreRecipePayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
