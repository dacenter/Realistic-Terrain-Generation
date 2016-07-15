package rtg.world.biome.realistic.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import rtg.api.biome.BiomeConfig;
import rtg.util.BiomeUtils;
import rtg.world.biome.deco.DecoBaseBiomeDecorations;
import rtg.world.gen.surface.vanilla.SurfaceVanillaSunflowerPlains;
import rtg.world.gen.terrain.vanilla.TerrainVanillaSunflowerPlains;

public class RealisticBiomeVanillaSunflowerPlains extends RealisticBiomeVanillaBase
{
    public static Biome biome = Biomes.MUTATED_PLAINS;
    public static Biome river = Biomes.RIVER;
    
    public RealisticBiomeVanillaSunflowerPlains(BiomeConfig config)
    {
        super(config, 
            mutationBiome,
            Biome.river,
            new TerrainVanillaSunflowerPlains(),
            new SurfaceVanillaSunflowerPlains(config, topBlock, fillerBlock)
        );
		
		DecoBaseBiomeDecorations decoBaseBiomeDecorations = new DecoBaseBiomeDecorations();
		this.addDeco(decoBaseBiomeDecorations);
    }
}
