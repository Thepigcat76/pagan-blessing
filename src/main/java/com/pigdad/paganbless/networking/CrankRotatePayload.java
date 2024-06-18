package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CrankRotatePayload(BlockPos blockPos, boolean rotate) implements CustomPacketPayload {
    public static final Type<CrankRotatePayload> TYPE = new Type<>(new ResourceLocation(PaganBless.MODID, "crank_rotate_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrankRotatePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CrankRotatePayload::blockPos,
            ByteBufCodecs.BOOL,
            CrankRotatePayload::rotate,
            CrankRotatePayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
