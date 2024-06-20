package com.pigdad.paganbless.data;

import com.mojang.serialization.Codec;
import com.pigdad.paganbless.PaganBless;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class PBAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, PaganBless.MODID);

    // Do not try to access these directly! Use capabilities or preferably cast to container block/item/heat

    public static final Supplier<AttachmentType<Boolean>> HAS_PAGAN_GUIDE = ATTACHMENT_TYPES.register(
            "has_pagan_guide", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());
}
