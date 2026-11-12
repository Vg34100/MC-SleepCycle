package net.vg.sleepcycle.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModProperties {
    private ModProperties() {}

    public static Item.Properties item(String path) {
        var properties = new Item.Properties();
        //? if >=26.1 {
        properties.setId(ResourceKey.create(Registries.ITEM, ModIds.id(path)));
        //? }
        return properties;
    }

    public static BlockBehaviour.Properties block(String path) {
        var properties = BlockBehaviour.Properties.of();
        //? if >=26.1 {
        properties.setId(ResourceKey.create(Registries.BLOCK, ModIds.id(path)));
        //? }
        return properties;
    }
}
