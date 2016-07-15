package rtg.world.biome.realistic.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import rtg.api.biome.BiomeConfig;
import rtg.world.biome.deco.DecoBaseBiomeDecorations;
import rtg.world.gen.surface.vanilla.SurfaceVanillaDeepOcean;
import rtg.world.gen.terrain.vanilla.TerrainVanillaDeepOcean;

public class RealisticBiomeVanillaDeepOcean extends RealisticBiomeVanillaBase
{
    
    public static Block topBlock = Biomes.deepOcean.topBlock;
    public static Block fillerBlock = Biomes.deepOcean.fillerBlock;
    
    public RealisticBiomeVanillaDeepOcean(BiomeConfig config)
    {
    
        super(config, 
            BiomeGenBase.deepOcean,
            BiomeGenBase.river,
            new TerrainVanillaDeepOcean(),
            new SurfaceVanillaDeepOcean(config, Blocks.gravel, Blocks.gravel, Blocks.clay, 20f, 0.1f));
        
        this.waterSurfaceLakeChance = 0;
        this.lavaSurfaceLakeChance = 0;
        this.noLakes=true;
        this.noWaterFeatures = true;

		DecoBaseBiomeDecorations decoBaseBiomeDecorations = new DecoBaseBiomeDecorations();
		this.addDeco(decoBaseBiomeDecorations);
    }
}
