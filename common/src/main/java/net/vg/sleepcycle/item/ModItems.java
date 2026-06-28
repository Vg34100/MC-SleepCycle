package net.vg.sleepcycle.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.block.SleepingBagBlock;

public class ModItems {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);

    private static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
    }

    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, id(name));
    }

    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, id(name));
    }

    public static final RegistrySupplier<Block> SLEEPING_BAG = BLOCKS.register("sleeping_bag",
            () -> new SleepingBagBlock(DyeColor.BLACK, BlockBehaviour.Properties.of()
                    .setId(blockKey("sleeping_bag"))
                    .strength(0.2F)
                    .noOcclusion()
                    .dynamicShape()));

    public static final RegistrySupplier<Item> SLEEPING_BAG_ITEM = ITEMS.register("sleeping_bag",
            () -> new SleepingBagItem(SLEEPING_BAG.get(), new Item.Properties()
                    .setId(itemKey("sleeping_bag"))
                    .arch$tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                    .stacksTo(1)));

    public static void register() {
        BLOCKS.register();
        ITEMS.register();
    }
}