package rtg.world.biome.realistic.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

import rtg.api.biome.BiomeConfig;
import rtg.world.biome.deco.DecoBaseBiomeDecorations;
import rtg.world.gen.surface.vanilla.SurfaceVanillaStoneBeach;
import rtg.world.gen.terrain.vanilla.TerrainVanillaStoneBeach;

public class RealisticBiomeVanillaStoneBeach extends RealisticBiomeVanillaBase
{	
	public static Block topBlock = Biomes.stoneBeach.topBlock;
	public static Block fillerBlock = Biomes.stoneBeach.fillerBlock;
	
	public RealisticBiomeVanillaStoneBeach(BiomeConfig config)
	{
		super(config, 
			Biome.stoneBeach,
			Biome.river,
			new TerrainVanillaStoneBeach(),
			new SurfaceVanillaStoneBeach(config, topBlock, fillerBlock, true, Blocks.gravel, 1f, 1.5f, 85f, 20f, 4f)
		);
		
		DecoBaseBiomeDecorations decoBaseBiomeDecorations = new DecoBaseBiomeDecorations();
		this.addDeco(decoBaseBiomeDecorations);
	}	
}
