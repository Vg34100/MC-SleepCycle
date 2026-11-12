package net.vg.sleepcycle.util;

import net.minecraft.resources.Identifier;
import net.vg.sleepcycle.Constants;

public final class ModIds {
    private ModIds() {}

    public static Identifier id(String path) {
        //? if >=1.21 {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
        //? } else {
        /*return new Identifier(Constants.MOD_ID, path);
        *///? }
    }
}
