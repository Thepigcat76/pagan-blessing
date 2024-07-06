package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record WinchPayload(BlockPos blockPos, int distance, boolean liftDown) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WinchPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PaganBless.MODID, "winch_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, WinchPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            WinchPayload::blockPos,
            ByteBufCodecs.INT,
            WinchPayload::distance,
            ByteBufCodecs.BOOL,
            WinchPayload::liftDown,
            WinchPayload::new
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
