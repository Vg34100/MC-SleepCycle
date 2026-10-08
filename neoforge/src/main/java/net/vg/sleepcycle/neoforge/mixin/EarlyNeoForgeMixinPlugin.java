//? if >=26.1 {
//? if <26.1.2 {
package net.vg.sleepcycle.neoforge.mixin;

import java.util.List;
import java.util.Set;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/** Architectury 20 names the later flat event; these loaders expose its old nested name. */
public final class EarlyNeoForgeMixinPlugin implements IMixinConfigPlugin {
    private static final String TARGET = "dev.architectury.event.forge.EventHandlerImplCommon";
    private static final String NEW_EVENT = "net/neoforged/neoforge/event/level/block/BreakBlockEvent";
    private static final String OLD_EVENT = "net/neoforged/neoforge/event/level/BlockEvent$BreakEvent";

    @Override
    public void postApply(String name, ClassNode target, String mixin, IMixinInfo info) {
        if (!TARGET.equals(name)) throw new IllegalStateException("Unexpected compatibility target: " + name);
        Remapper remapper = new Remapper(Opcodes.ASM9) {
            @Override
            public String map(String internalName) {
                return NEW_EVENT.equals(internalName) ? OLD_EVENT : internalName;
            }
        };
        int changed = 0;
        for (int i = 0; i < target.methods.size(); i++) {
            MethodNode method = target.methods.get(i);
            if (!method.name.equals("event") || !method.desc.equals("(L" + NEW_EVENT + ";)V")) continue;
            // Both event APIs provide the same getters and cancellation method.
            // ASM remaps only this handler's descriptor, owners, frames and locals.
            MethodNode adapted = new MethodNode(Opcodes.ASM9, method.access, method.name,
                    remapper.mapMethodDesc(method.desc), remapper.mapSignature(method.signature, false),
                    method.exceptions.toArray(String[]::new));
            method.accept(new MethodRemapper(adapted, remapper));
            target.methods.set(i, adapted);
            changed++;
        }
        if (changed != 1) throw new IllegalStateException("Expected one Architectury break-event handler, got " + changed);
    }

    @Override public void onLoad(String mixinPackage) { }
    @Override public String getRefMapperConfig() { return null; }
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) { return true; }
    @Override public void acceptTargets(Set<String> mine, Set<String> others) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String name, ClassNode target, String mixin, IMixinInfo info) { }
}
//? }
//? }
