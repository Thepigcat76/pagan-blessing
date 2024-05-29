package com.pigdad.paganbless.compat;

import com.klikli_dev.modonomicon.data.BookDataManager;
import com.klikli_dev.modonomicon.registry.DataComponentRegistry;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.items.PaganGuideItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class ModonomiconCompat {
    public static ItemStack getItemStack() {
        ResourceLocation id = BookDataManager.get().getBook(new ResourceLocation("paganbless:pagan_guide")).getId();
        ItemStack itemStack = new ItemStack(PBItems.PAGAN_GUIDE.get());
        itemStack.set(DataComponentRegistry.BOOK_ID, id);
        return itemStack;
    }

    public static Supplier<Item> registerItem() {
        return PBItems.registerItem("pagan_guide", () -> new PaganGuideItem(new Item.Properties().stacksTo(1)));
    }
}
