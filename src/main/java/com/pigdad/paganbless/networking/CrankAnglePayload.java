package com.pigdad.paganbless.networking;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CrankAnglePayload(BlockPos blockPos, int angle) implements CustomPacketPayload {
    public static final Type<CrankAnglePayload> TYPE = new Type<>(new ResourceLocation(PaganBless.MODID, "crank_angle_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrankAnglePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CrankAnglePayload::blockPos,
            ByteBufCodecs.INT,
            CrankAnglePayload::angle,
            CrankAnglePayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
