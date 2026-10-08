package net.vg.sleepcycle.effect;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.vg.sleepcycle.Constants;

public class ModEffects {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Constants.MOD_ID, Registries.MOB_EFFECT);

    public static final ResourceKey<MobEffect> WELL_RESTED_KEY = ResourceKey.create(Registries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "well_rested"));
    public static final ResourceKey<MobEffect> TIRED_KEY = ResourceKey.create(Registries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "tired"));

    public static final RegistrySupplier<MobEffect> WELL_RESTED = MOB_EFFECTS.register(idPath(WELL_RESTED_KEY),
            () -> new WellRestedEffect(MobEffectCategory.BENEFICIAL, 13458603));
    public static final RegistrySupplier<MobEffect> TIRED = MOB_EFFECTS.register(idPath(TIRED_KEY),
            () -> new TiredEffect(MobEffectCategory.HARMFUL, 2039587));

    private static String idPath(ResourceKey<MobEffect> key) {
        //? if >=26.1 {
        return key.identifier().getPath();
        //? } else {
        /*return key.location().getPath();
        *///? }
    }

    public static void register() {
        MOB_EFFECTS.register();
        Constants.LOGGER.info("Registering Effects for " + Constants.MOD_ID);
    }

    public static Holder<MobEffect> getWellRestedHolder() {
        //? if >=26.1 {
        return BuiltInRegistries.MOB_EFFECT.getOrThrow(WELL_RESTED_KEY);
        //? } else {
        /*return BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(WELL_RESTED_KEY);
        *///? }
    }

    public static Holder<MobEffect> getTiredHolder() {
        //? if >=26.1 {
        return BuiltInRegistries.MOB_EFFECT.getOrThrow(TIRED_KEY);
        //? } else {
        /*return BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(TIRED_KEY);
        *///? }
    }
}