package net.vg.sleepcycle.block.entity;


import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.item.ModItems;

import java.util.Set;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<SleepingBagBlockEntity>> SLEEPING_BAG = BLOCK_ENTITIES.register("sleeping_bag",
            () -> new BlockEntityType<>(SleepingBagBlockEntity::new, Set.of(ModItems.SLEEPING_BAG.get())));

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}
