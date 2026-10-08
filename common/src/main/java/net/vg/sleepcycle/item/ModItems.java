package net.vg.sleepcycle.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.block.SleepingBagBlock;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);

    public static final Map<DyeColor, RegistrySupplier<Block>> SLEEPING_BAGS = new LinkedHashMap<>();
    public static final Map<DyeColor, RegistrySupplier<Item>> SLEEPING_BAG_ITEMS = new LinkedHashMap<>();

    static {
        for (DyeColor color : DyeColor.values()) {
            String name = "sleeping_bag_" + color.getName();
            SLEEPING_BAGS.put(color, BLOCKS.register(name,
                    () -> new SleepingBagBlock(color, BlockBehaviour.Properties.of()
                            //? if >=26.1 {
                            .setId(blockKey(name))
                            //? }
                            .strength(0.2F)
                            .noOcclusion()
                            .dynamicShape())));
            SLEEPING_BAG_ITEMS.put(color, ITEMS.register(name,
                    () -> new SleepingBagItem(SLEEPING_BAGS.get(color).get(), new Item.Properties()
                            //? if >=26.1 {
                            .setId(itemKey(name))
                            //? }
                            .stacksTo(1))));
        }
    }

    private static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
    }

    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, id(name));
    }

    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, id(name));
    }

    public static void register() {
        BLOCKS.register();
        ITEMS.register();
        SLEEPING_BAG_ITEMS.values().forEach(item ->
                CreativeTabRegistry.appendStack(CreativeModeTabs.FUNCTIONAL_BLOCKS, () -> new ItemStack(item.get())));
    }
}
