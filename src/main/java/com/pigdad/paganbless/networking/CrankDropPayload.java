package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CrankDropPayload(BlockPos blockPos, boolean dropping) implements CustomPacketPayload {
    public static final Type<CrankDropPayload> TYPE = new Type<>(new ResourceLocation(PaganBless.MODID, "crank_drop_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrankDropPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CrankDropPayload::blockPos,
            ByteBufCodecs.BOOL,
            CrankDropPayload::dropping,
            CrankDropPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
