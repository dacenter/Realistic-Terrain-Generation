package rtg.world.gen.surface.vanilla;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.biome.BiomeConfig;
import rtg.api.biome.vanilla.config.BiomeConfigVanillaDeepOcean;
import rtg.util.CellNoise;
import rtg.util.MathUtils;
import rtg.util.OpenSimplexNoise;
import rtg.world.gen.surface.SurfaceBase;

public class SurfaceVanillaDeepOcean extends SurfaceBase
{
    
    private IBlockState mixBlock;
    private byte mixBlockMeta;
    private float width;
    private float height;
    private float mixCheck;
    
    public SurfaceVanillaDeepOcean(BiomeConfig config, IBlockState top, IBlockState filler, IBlockState mix, float mixWidth, float mixHeight)
    {
    
        super(config, top, filler);
        
        mixBlock = this.getConfigBlock(config, BiomeConfigVanillaDeepOcean.surfaceMixBlockId, mix);
        
        width = mixWidth;
        height = mixHeight;
    }
    
    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
        for (int k = 255; k > -1; k--)
        {
            IBlockState b = primer.getBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y));
            if(b == Blocks.AIR.getDefaultState())
            {
                depth = -1;
            }
            else if(b == Blocks.STONE.getDefaultState())
            {
                depth++;
                
                if (depth == 0 && k > 0 && k < 63)
                {
                    mixCheck = simplex.noise2(i / width, j / width);
                    
                    if (mixCheck > height) {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), mixBlock);
                    }
                    else
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
                    }
                }
                else if (depth < 4 && k < 63)
                {
                    primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
                }
            }
        }
    }
}
