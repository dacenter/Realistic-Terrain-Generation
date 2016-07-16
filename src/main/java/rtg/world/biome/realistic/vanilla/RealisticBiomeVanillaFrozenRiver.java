package rtg.world.biome.realistic.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

import rtg.api.biome.BiomeConfig;
import rtg.world.biome.deco.DecoBaseBiomeDecorations;
import rtg.world.gen.surface.vanilla.SurfaceVanillaFrozenRiver;
import rtg.world.gen.terrain.vanilla.TerrainVanillaFrozenRiver;

public class RealisticBiomeVanillaFrozenRiver extends RealisticBiomeVanillaBase
{
	public static Block topBlock = Biomes.frozenRiver.topBlock;
	public static Block fillerBlock = Biomes.frozenRiver.fillerBlock;

	public RealisticBiomeVanillaFrozenRiver(BiomeConfig config)
	{
		super(config, 
			Biome.frozenRiver,
			Biome.frozenRiver,
			new TerrainVanillaFrozenRiver(),
			new SurfaceVanillaFrozenRiver(config)
		);

        this.waterSurfaceLakeChance = 0;
        this.lavaSurfaceLakeChance = 0;
		
		DecoBaseBiomeDecorations decoBaseBiomeDecorations = new DecoBaseBiomeDecorations();
		this.addDeco(decoBaseBiomeDecorations);
	}
}
