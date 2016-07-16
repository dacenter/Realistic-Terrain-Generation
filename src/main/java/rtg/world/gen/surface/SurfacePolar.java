package rtg.world.gen.surface;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.biome.BiomeConfig;
import rtg.util.CellNoise;
import rtg.util.MathUtils;
import rtg.util.OpenSimplexNoise;
import rtg.util.SnowHeightCalculator;

public class SurfacePolar extends SurfaceBase
{
	public SurfacePolar(BiomeConfig config, IBlockState top, IBlockState fill) 
	{
	    super(config, top, fill);
	}
	
    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
		boolean water = false;
		boolean riverPaint = false;
		boolean grass = false;
		
		if(river > 0.05f && river + (simplex.noise2(i / 10f, j / 10f) * 0.1f) > 0.86f)
		{
			riverPaint = true;
			
			if(simplex.noise2(i / 12f, j / 12f) > 0.25f)
			{
				grass = true;
			}
		}
		
		IBlockState b;
		for(int k = 255; k > -1; k--)
		{
			b = primer.getBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y));
            if(b == Blocks.AIR.getDefaultState())
            {
            	depth = -1;
            }
            else if(b == Blocks.STONE.getDefaultState())
            {
            	depth++;

            	if(riverPaint)
            	{
            		if(grass && depth < 4)
            		{
    	        		primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.DIRT.getDefaultState());
            		}
            		else if(depth == 0)
            		{
                        if (rand.nextInt(2) == 0) {
                            
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), hcStone(world, i, j, x, y, k));
                        }
                        else {
                            
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), hcCobble(world, i, j, x, y, k));
                        }
            		}
            	}
        		else if(depth > -1 && depth < 9)
        		{
        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.SNOW.getDefaultState());
            		if(depth == 0 && k > 61 && k < 254)
            		{
            			SnowHeightCalculator.calc(x, y, k, primer, noise);
            		}
        		}
            }
            else if(!water && b == Blocks.WATER.getDefaultState())
            {
    			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.ICE.getDefaultState());
            	water = true;
            }
		}
	}
}
