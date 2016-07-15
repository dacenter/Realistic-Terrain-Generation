package rtg.world.gen.surface.vanilla;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.biome.BiomeConfig;
import rtg.api.biome.vanilla.config.BiomeConfigVanillaExtremeHillsEdge;
import rtg.util.CellNoise;
import rtg.util.CliffCalculator;
import rtg.util.MathUtils;
import rtg.util.OpenSimplexNoise;
import rtg.world.gen.surface.SurfaceBase;

public class SurfaceVanillaExtremeHillsEdge extends SurfaceBase
{
    
    private IBlockState mixBlockTop;
    private byte mixBlockTopMeta;
    private IBlockState mixBlockFill;
    private byte mixBlockFillMeta;
    private float width;
    private float height;
    private float smallW;
    private float smallS;
    
    public SurfaceVanillaExtremeHillsEdge(BiomeConfig config, IBlockState top, IBlockState filler, IBlockState mixTop, IBlockState mixFill, float mixWidth,
        float mixHeight, float smallWidth, float smallStrength)
    {
    
        super(config, top, filler);

        mixBlockTop = this.getConfigBlock(config, BiomeConfigVanillaExtremeHillsEdge.surfaceMixBlockId, mixTop);
        mixBlockFill = this.getConfigBlock(config, BiomeConfigVanillaExtremeHillsEdge.surfaceMixFillerBlockId, mixFill);
        
        width = mixWidth;
        height = mixHeight;
        smallW = smallWidth;
        smallS = smallStrength;
    }
    
    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
        float c = CliffCalculator.calc(x, y, noise);
        boolean cliff = c > 1.4f ? true : false;
        boolean mix = false;
        
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
                
                if (cliff)
                {
                    if (depth > -1 && depth < 2)
                    {
                        if (rand.nextInt(3) == 0) {
                            
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), hcCobble(world, i, j, x, y, k));
                        }
                        else {
                            
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), hcStone(world, i, j, x, y, k));
                        }
                    }
                    else if (depth < 10)
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), hcStone(world, i, j, x, y, k));
                    }
                }
                else
                {
                    if (depth == 0 && k > 61)
                    {
                        if (simplex.noise2(i / width, j / width) + simplex.noise2(i / smallW, j / smallW) * smallS > height)
                        {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), mixBlockTop);
                            mix = true;
                        }
                        else
                        {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
                        }
                    }
                    else if (depth < 4)
                    {
                        if (mix)
                        {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), mixBlockFill);
                        }
                        else
                        {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
                        }
                    }
                }
            }
        }
    }
}
