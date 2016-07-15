package rtg.util;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;


/**
 *
 * @author Zeno410
 */
public class BiomeSet {

    private boolean [] biomes = new boolean[256];


    public class Water {
        public Water() {
            biomes[Biome.getIdForBiome(Biomes.DEEP_OCEAN)] = true;
            biomes[Biome.getIdForBiome(Biomes.FROZEN_OCEAN)] = true;
            biomes[Biome.getIdForBiome(Biomes.FROZEN_RIVER)] = true;
            biomes[Biome.getIdForBiome(Biomes.OCEAN)] = true;
            biomes[Biome.getIdForBiome(Biomes.RIVER)] = true;
        }
    }

    public boolean has(int biomeID) {
        return biomes[biomeID];
    }
}