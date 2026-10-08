//? if >=26.1 {
//? if <26.1.2 {
package net.vg.sleepcycle.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;

/** Loads the narrowly scoped early-NeoForge event descriptor adapter. */
@Mixin(targets = "dev.architectury.event.forge.EventHandlerImplCommon", remap = false)
public abstract class ArchitecturyBlockEventMixin {
}
//? }
//? }
