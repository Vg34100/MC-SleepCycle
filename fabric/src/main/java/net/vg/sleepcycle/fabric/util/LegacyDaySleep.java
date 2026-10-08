//? if <26.1 {
/*package net.vg.sleepcycle.fabric.util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.world.InteractionResult;
import net.vg.sleepcycle.config.ModConfigs;

// Fabric owns the legacy isDay redirects; its event covers both sleep entry
// and the Player tick wake check without competing for those injection sites.
public final class LegacyDaySleep implements ModInitializer {
    @Override
    public void onInitialize() {
        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, pos, vanillaResult) ->
                ModConfigs.ALLOW_DAY_SLEEPING ? InteractionResult.SUCCESS : InteractionResult.PASS);
    }
}
*///? }
