package rtg.world.gen.surface.vanilla;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.biome.BiomeConfig;
import rtg.util.CellNoise;
import rtg.util.CliffCalculator;
import rtg.util.MathUtils;
import rtg.util.OpenSimplexNoise;
import rtg.world.gen.surface.SurfaceBase;

public class SurfaceVanillaDesertM extends SurfaceBase
{
	private int[] claycolor = new int[100];
	private int grassRaise = 0;
	
	public SurfaceVanillaDesertM(BiomeConfig config, IBlockState top, IBlockState fill, int grassHeight)
	{
		super(config, top, fill);
		grassRaise = grassHeight;
		
		int[] c = new int[]{1, 8, 0};
		OpenSimplexNoise simplex = new OpenSimplexNoise(2L);
		
		float n;
		for(int i = 0; i < 100; i++)
		{
			n = simplex.noise1(i / 3f) * 3f + simplex.noise1(i / 1f) * 0.3f + 1.5f;
			n = n >= 3f ? 2.9f : n < 0f ? 0f : n;
			claycolor[i] = c[(int)n];
		}
	}
	
	public byte getClayColorForHeight(int k)
	{
		k -= 60;
		k = k < 0 ? 0 : k > 99 ? 99 : k;
		return (claycolor[k] == 0) ? (byte)12 : ((claycolor[k] == 1) ? (byte)0 : (byte)claycolor[k]);
	}
	
    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
		float c = CliffCalculator.calc(x, y, noise);
		boolean cliff = c > 1.3f ? true : false;
		
		for(int k = 255; k > -1; k--)
		{
			IBlockState b = primer.getBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y));
            if(b == Blocks.AIR.getDefaultState())
            {
            	depth = -1;
            }
            else if(b == Blocks.STONE.getDefaultState())
            {
            	depth++;

                if(k > 110)
                {
                    primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.STAINED_HARDENED_CLAY.getDefaultState());
                }
                else if(depth > -1 && depth < 12)
	        	{
	            	if(cliff)
	            	{
	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.STAINED_HARDENED_CLAY.getDefaultState());
	            	}
	            	else
	            	{
	        			if(depth > 4)
	        			{
		        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.STAINED_HARDENED_CLAY.getDefaultState());
	        			}
	        			else if(k > 74 + grassRaise)
	        			{

	        				if(depth == 0)
	        				{
		        				primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
	        				}
	        				else
	        				{
		        				primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
	        				}
	        			}
	        			else if(k < 62)
	        			{
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
	        			}
	        			else if(k < 62 + grassRaise)
	        			{
                            if(depth == 0)
                            {
                                primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
                            }
                            else
                            {
                                primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
                            }
	        			}
	        			else if(k < 75 + grassRaise)
	        			{
                            if(depth == 0)
                            {
                                primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
                            }
                            else
                            {
                                primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
                            }
	        			}
	        			else
	        			{
                            if(depth == 0)
                            {
                                primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
                            }
                            else
                            {
                                primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
                            }
	        			}
	            	}
        		}
        		else if(k > 63)
        		{
        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.STAINED_HARDENED_CLAY.getDefaultState());
        		}
            }
		}
	}
}