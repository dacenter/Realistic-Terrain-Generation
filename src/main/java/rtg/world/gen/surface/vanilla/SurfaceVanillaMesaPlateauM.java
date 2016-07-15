package rtg.world.gen.surface.vanilla;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.biome.BiomeConfig;
import rtg.util.CanyonColour;
import rtg.util.CellNoise;
import rtg.util.CliffCalculator;
import rtg.util.MathUtils;
import rtg.util.OpenSimplexNoise;
import rtg.world.gen.surface.SurfaceBase;

public class SurfaceVanillaMesaPlateauM extends SurfaceBase
{
	private int grassRaise = 0;
	
	public SurfaceVanillaMesaPlateauM(BiomeConfig config, IBlockState top, IBlockState fill, int grassHeight)
	{
		super(config, top, fill);
		grassRaise = grassHeight;
	}
	
    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
		float c = CliffCalculator.calc(x, y, noise);
		boolean cliff = c > 1.3f;
        float noiseHeight = noise[y*16+x];
		
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
                float effectiveHeight = noiseHeight-(float)depth;

        		if(depth > -1 && depth < 12)
	        	{
	            	if(cliff)
	            	{
	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), CanyonColour.MESA.getBlockForHeight(i, effectiveHeight,j).getStateFromMeta(CanyonColour.MESA.getMetaForHeight(i, effectiveHeight,j)));
					}
	            	else
	            	{
	        			if(depth > 4)
	        			{
	            			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), CanyonColour.MESA.getBlockForHeight(i, effectiveHeight,j).getStateFromMeta(CanyonColour.MESA.getMetaForHeight(i, effectiveHeight,j)));
						}
	        			else if(k > 74 + grassRaise)
	        			{
	        				if(rand.nextInt(5) == 0)
	        				{
        	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.DIRT.getDefaultState());
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
	        			else if(k < 62)
	        			{
    	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.DIRT.getDefaultState());
	        			}
	        			else if(k < 62 + grassRaise)
	        			{
	        				if(depth == 0)
	        				{
        	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.GRASS.getDefaultState());
	        				}
	        				else
	        				{
        	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.DIRT.getDefaultState());
	        				}
	        			}
	        			else if(k < 75 + grassRaise)
	        			{
	        				if(depth == 0)
	        				{
		        				int r = (int)((k - (62 + grassRaise)) / 2f);
		        				if(rand.nextInt(r + 1) == 0)
		        				{
	        	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.GRASS.getDefaultState());
		        				}
		        				else if(rand.nextInt((int)(r / 2f) + 1) == 0)
		        				{
            	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.DIRT.getDefaultState());
		        				}
		        				else
		        				{
	        	        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
		        				}
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
        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), CanyonColour.MESA.getBlockForHeight(i, effectiveHeight,j).getStateFromMeta(CanyonColour.MESA.getMetaForHeight(i, effectiveHeight,j)));
				}
            }
		}
	}
}
