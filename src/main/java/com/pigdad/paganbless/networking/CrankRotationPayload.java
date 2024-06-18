package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CrankRotationPayload(BlockPos blockPos, int rotation) implements CustomPacketPayload {
    public static final Type<CrankRotationPayload> TYPE = new Type<>(new ResourceLocation(PaganBless.MODID, "crank_rotation_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrankRotationPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CrankRotationPayload::blockPos,
            ByteBufCodecs.INT,
            CrankRotationPayload::rotation,
            CrankRotationPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
