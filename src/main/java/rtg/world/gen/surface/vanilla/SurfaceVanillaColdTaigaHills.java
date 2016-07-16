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

public class SurfaceVanillaColdTaigaHills extends SurfaceBase
{
    
    private boolean beach;
    private IBlockState beachBlock;
    private float min;
    
    private float sCliff = 1.5f;
    private float sHeight = 60f;
    private float sStrength = 65f;
    private float iCliff = 0.3f;
    private float iHeight = 100f;
    private float iStrength = 50f;
    private float cCliff = 1.5f;
    
    public SurfaceVanillaColdTaigaHills(BiomeConfig config, IBlockState top, IBlockState fill, boolean genBeach, IBlockState genBeachBlock, float minCliff)
    {
    
    	super(config, top, fill);
        beach = genBeach;
        beachBlock = genBeachBlock;
        min = minCliff;
    }
    
    public SurfaceVanillaColdTaigaHills(BiomeConfig config, IBlockState top, IBlockState fill, boolean genBeach, IBlockState genBeachBlock, float minCliff, float stoneCliff,
        float stoneHeight, float stoneStrength, float snowCliff, float snowHeight, float snowStrength, float clayCliff)
    {
    
        this(config, top, fill, genBeach, genBeachBlock, minCliff);
        
        sCliff = stoneCliff;
        sHeight = stoneHeight;
        sStrength = stoneStrength;
        iCliff = snowCliff;
        iHeight = snowHeight;
        iStrength = snowStrength;
        cCliff = clayCliff;
    }
    
    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
        float c = CliffCalculator.calc(x, y, noise);
        int cliff = 0;
        boolean gravel = false;
        
        IBlockState b;
        for (int k = 255; k > -1; k--)
        {
            b = primer.getBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y));
            if(b == Blocks.AIR.getDefaultState())
            {
                depth = -1;
            }
            else if(b == Blocks.STONE.getDefaultState())
            {
                depth++;
                
                if (depth == 0)
                {
                    if (k < 63)
                    {
                        if (beach)
                        {
                            gravel = true;
                        }
                    }
                    
                    float p = simplex.noise3(i / 8f, j / 8f, k / 8f) * 0.5f;
                    if (c > min && c > sCliff - ((k - sHeight) / sStrength) + p)
                    {
                        cliff = 1;
                    }
                    if (c > cCliff)
                    {
                        cliff = 2;
                    }
                    if (k > 110 + (p * 4) && c < iCliff + ((k - iHeight) / iStrength) + p)
                    {
                        cliff = 3;
                    }
                    
                    if (cliff == 1)
                    {
                        if (rand.nextInt(3) == 0) {
                            
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), hcCobble(world, i, j, x, y, k));
                        }
                        else {
                            
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), hcStone(world, i, j, x, y, k));
                        }
                    }
                    else if (cliff == 2)
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), getShadowStoneBlock(world, i, j, x, y, k));
                    }
                    else if (cliff == 3)
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.SNOW.getDefaultState());
                    }
                    else if (k < 63)
                    {
                        if (beach)
                        {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), beachBlock);
                            gravel = true;
                        }
                        else if (k < 62)
                        {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
                        }
                        else
                        {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
                        }
                    }
                    else
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.GRASS.getDefaultState());
                    }
                }
                else if (depth < 6)
                {
                    if (cliff == 1)
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), hcStone(world, i, j, x, y, k));
                    }
                    else if (cliff == 2)
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), getShadowStoneBlock(world, i, j, x, y, k));
                    }
                    else if (cliff == 3)
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.SNOW.getDefaultState());
                    }
                    else if (gravel)
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.GRAVEL.getDefaultState());
                    }
                    else
                    {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), Blocks.DIRT.getDefaultState());
                    }
                }
            }
        }
    }
}
