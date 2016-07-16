package rtg.world.biome.realistic.vanilla;

import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import rtg.api.biome.BiomeConfig;
import rtg.util.CellNoise;
import rtg.util.OpenSimplexNoise;
import rtg.world.biome.deco.DecoBaseBiomeDecorations;
import rtg.world.gen.surface.vanilla.SurfaceVanillaRiver;
import rtg.world.gen.terrain.vanilla.TerrainVanillaRiver;

public class RealisticBiomeVanillaRiver extends RealisticBiomeVanillaBase
{
    public static Biome biome = Biomes.RIVER;
    public static Biome river = Biomes.RIVER;
    
	public RealisticBiomeVanillaRiver(BiomeConfig config)
	{
	    super(config, biome, river,
			new TerrainVanillaRiver(),
			new SurfaceVanillaRiver(config)
		);

        this.waterSurfaceLakeChance = 0;
        this.lavaSurfaceLakeChance = 0;
		
		DecoBaseBiomeDecorations decoBaseBiomeDecorations = new DecoBaseBiomeDecorations();
		this.addDeco(decoBaseBiomeDecorations);
	}

        /**
     * This method is used by DecoBaseBiomeDecorations to allow the base biome to decorate itself.
     */
    @Override
    public void rDecorateSeedBiome(World world, Random rand, int chunkX, int chunkY, OpenSimplexNoise simplex, CellNoise cell, float strength, float river, Biome seedBiome) {

        if (strength > 0.3f) {
            int previousReeds = Biomes.RIVER.theBiomeDecorator.reedsPerChunk;
            int previousLilies = Biomes.RIVER.theBiomeDecorator.waterlilyPerChunk;
            Biomes.RIVER.theBiomeDecorator.reedsPerChunk = 4;
            Biomes.RIVER.theBiomeDecorator.waterlilyPerChunk = 0;
            seedBiome.decorate(world, rand, new BlockPos(chunkX, 0, chunkY));
            Biomes.RIVER.theBiomeDecorator.reedsPerChunk = previousReeds;
            Biomes.RIVER.theBiomeDecorator.waterlilyPerChunk = previousLilies;
        }
        else {
            rOreGenSeedBiome(world, rand, chunkX, chunkY, simplex, cell, strength, river, seedBiome);
        }
    }
}
