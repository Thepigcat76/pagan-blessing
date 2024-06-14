package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record IncenseBurningPayload(BlockPos blockPos, boolean burning, int burningProgress) implements CustomPacketPayload {
    public static final Type<IncenseBurningPayload> TYPE = new Type<>(new ResourceLocation(PaganBless.MODID, "incense_burning_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, IncenseBurningPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            IncenseBurningPayload::blockPos,
            ByteBufCodecs.BOOL,
            IncenseBurningPayload::burning,
            ByteBufCodecs.INT,
            IncenseBurningPayload::burningProgress,
            IncenseBurningPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
