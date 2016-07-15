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

public class SurfaceGeneric extends SurfaceBase
{

	public SurfaceGeneric(BiomeConfig config, IBlockState top, IBlockState filler)
	{
		super(config, top, filler);
	}
	
    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
		for(int k = 255; k > -1; k--)
		{
			IBlockState b = primer.getBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y));
			
            if (b == Blocks.AIR.getDefaultState())
            {
            	depth = -1;
            }
            else if (b == Blocks.STONE.getDefaultState())
            {
            	depth++;

        		if (depth == 0 && k > 61)
        		{
        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
        		}
        		else if (depth < 4)
        		{
        			primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
        		}
            }
		}
	}
}
