package net.vg.sleepcycle.block.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.item.ModItems;

import java.util.stream.Collectors;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<SleepingBagBlockEntity>> SLEEPING_BAG =
            BLOCK_ENTITIES.register("sleeping_bag",
                    () -> new BlockEntityType<>(
                            SleepingBagBlockEntity::new,
                            ModItems.SLEEPING_BAGS.values().stream()
                                    .map(RegistrySupplier::get)
                                    .collect(Collectors.toSet())));

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}
