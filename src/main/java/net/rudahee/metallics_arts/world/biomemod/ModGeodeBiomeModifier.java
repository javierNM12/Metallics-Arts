package net.rudahee.metallics_arts.world.biomemod;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record ModGeodeBiomeModifier (HolderSet<Biome> biomes, Holder<PlacedFeature> feature) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        // add a feature to all specified biomes
        if (phase == Phase.ADD) {
            builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ModBiomeModifier.GEODE_CODEC.get();
    }
}
