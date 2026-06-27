package net.vg.sleepcycle.sounds;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.vg.sleepcycle.Constants;

public class ModSounds {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Constants.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> WELL_RESTED_SOUND = registerSoundEvent("well_rested");
    public static final RegistrySupplier<SoundEvent> TIRED_SOUND = registerSoundEvent("tired");

    private static RegistrySupplier<SoundEvent> registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register() {
        SOUND_EVENTS.register();
        Constants.LOGGER.info("Registering Sounds for " + Constants.MOD_ID);
    }
}