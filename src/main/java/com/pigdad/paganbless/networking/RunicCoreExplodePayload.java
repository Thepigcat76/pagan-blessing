package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record RunicCoreExplodePayload(BlockPos blockPos) implements CustomPacketPayload {
    public static final Type<RunicCoreExplodePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(PaganBless.MODID, "runic_core_explode_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RunicCoreExplodePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            RunicCoreExplodePayload::blockPos,
            RunicCoreExplodePayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
