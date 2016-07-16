package rtg.world.gen.surface;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.biome.BiomeConfig;
import rtg.config.rtg.ConfigRTG;
import rtg.util.CellNoise;
import rtg.util.MathUtils;
import rtg.util.OpenSimplexNoise;

public class SurfaceRiverOasis extends SurfaceBase
{
	// Cut-off noise size. The bigger, the larger the cut-off scale is
	private float cutOffScale;
	// Cut-off noise amplitude. The bigger, the more effect cut-off is going to have in the grass
	private float cutOffAmplitude;

	public SurfaceRiverOasis(BiomeConfig config) 
	{
		super(config, Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState());

		this.cutOffScale = ConfigRTG.riverCutOffScale;
		this.cutOffAmplitude = ConfigRTG.riverCutOffAmplitude;
	}
	
    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
		IBlockState b;
		int highestY;
		for (highestY = 255; highestY > 0; highestY--)
		{
			b = primer.getBlockState(MathUtils.globalToLocal(x), highestY, MathUtils.globalToLocal(y));
			if(b != Blocks.AIR.getDefaultState())
				break;
		}

		float amplitude = 0.25f;
		float noiseValue = 	(simplex.octave(0).noise2(i / 21f, j / 21f) * amplitude/1f);
		noiseValue += 	(simplex.octave(1).noise2(i / 12f, j / 12f) * amplitude/2f);

		// Large scale noise cut-off
		float noiseNeg = (simplex.octave(2).noise2(i / cutOffScale, j / cutOffScale) * cutOffAmplitude);
		noiseValue -= noiseNeg;

		// Height cut-off
		if(highestY > 62)
			noiseValue -= (highestY - 62) * (1 / 20f);

		if(river > 0.50 && river * 1.1 + noiseValue  > 0.79)
		{
			for(int k = 255; k > -1; k--)
			{
				b = primer.getBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y));
	            if(b == Blocks.AIR.getDefaultState())
	            {
	            	depth = -1;
	            }
	            else if(b != Blocks.WATER.getDefaultState())
	            {
	            	depth++;

	        		if(depth == 0 && k > 61)
	        		{
	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.GRASS.getDefaultState());
	        		}
	        		else if(depth < 4)
	        		{
	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.DIRT.getDefaultState());
	        		}
	        		else if(depth > 4)
	        		{
	        			return;
	        		}
	            }
			}
		}
	}
}
