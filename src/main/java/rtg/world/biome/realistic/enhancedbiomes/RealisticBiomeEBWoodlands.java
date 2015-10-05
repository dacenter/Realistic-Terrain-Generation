package rtg.world.biome.realistic.enhancedbiomes;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import rtg.config.ConfigEB;
import rtg.world.biome.BiomeBase;
import rtg.world.gen.surface.enhancedbiomes.SurfaceEBWoodlands;
import rtg.world.gen.terrain.enhancedbiomes.TerrainEBWoodlands;

public class RealisticBiomeEBWoodlands extends RealisticBiomeEBBase
{	
	public RealisticBiomeEBWoodlands(BiomeGenBase ebBiome)
	{
		super(
			ebBiome, BiomeBase.climatizedBiome(BiomeGenBase.river, BiomeBase.Climate.TEMPERATE),
			new TerrainEBWoodlands(),
			new SurfaceEBWoodlands(ebBiome.topBlock, ebBiome.fillerBlock, Blocks.stone, Blocks.cobblestone)
		);
		
		this.setRealisticBiomeName("EB Woodlands");
		this.biomeWeight = ConfigEB.weightEBWoodlands;
	}
}