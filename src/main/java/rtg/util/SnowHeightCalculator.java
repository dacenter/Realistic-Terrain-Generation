package rtg.util;

import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;

public class SnowHeightCalculator 
{
	public static void calc(int x, int y, int k, ChunkPrimer primer, float[] noise)
	{
		if(k < 254)
		{
			byte h = (byte) ((noise[y * 16 + x] - ((int) noise[y * 16 + x])) * 8);
			
			if(h > 7)
			{
				primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.SNOW_LAYER.getDefaultState());
				primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.SNOW_LAYER.getStateFromMeta(7));
			}
			else
			{
				primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.SNOW_LAYER.getStateFromMeta(h));
			}
		}
	}
}