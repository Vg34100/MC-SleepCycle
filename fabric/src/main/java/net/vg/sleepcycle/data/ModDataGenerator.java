package net.vg.sleepcycle.data;

//? if >=26.1 {
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
//? } else {
/*import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
*///? }
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
//? if >=26.1 {
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//? } else {
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
*///? }
//? if >=26.1 {
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
//? } else {
/*import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
*///? }

public class ModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.createPack().addProvider(ModModelGenerator::new);
    }

    private static class ModModelGenerator extends FabricModelProvider {
        //? if >=26.1 {
        public ModModelGenerator(FabricPackOutput output) {
        //? } else {
        /*public ModModelGenerator(FabricDataOutput output) {
        *///? }
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {}

        @Override
        public void generateItemModels(ItemModelGenerators itemModelGenerator) {}
    }
}
