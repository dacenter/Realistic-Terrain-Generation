package rtg.world.gen.surface.vanilla;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.biome.BiomeConfig;
import rtg.api.biome.vanilla.config.BiomeConfigVanillaSavanna;
import rtg.config.rtg.ConfigRTG;
import rtg.util.CanyonColour;
import rtg.util.CellNoise;
import rtg.util.CliffCalculator;
import rtg.util.MathUtils;
import rtg.util.OpenSimplexNoise;
import rtg.world.gen.surface.SurfaceBase;

public class SurfaceVanillaSavanna extends SurfaceBase {

    private IBlockState mixBlock;
    private byte mixBlockMeta;
    private float width;
    private float height;

    public SurfaceVanillaSavanna(BiomeConfig config, IBlockState top, IBlockState filler, IBlockState mix, float mixWidth, float mixHeight)
    {

    	super(config, top, filler);

        mixBlock = this.getConfigBlock(config, BiomeConfigVanillaSavanna.surfaceMixBlockId, mix);

        width = mixWidth;
        height = mixHeight;
    }

    @Override
	public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    
        float c = CliffCalculator.calc(x, y, noise);
        boolean cliff = c > 1.4f;

        for (int k = 255; k > -1; k--) {
        	IBlockState b = primer.getBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y));
            if (b == Blocks.AIR.getDefaultState()) {
                depth = -1;
            } else if (b == Blocks.STONE.getDefaultState()) {
                depth++;

                if (cliff) {
                    if (!ConfigRTG.stoneSavannas) {
        			    primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), CanyonColour.SAVANNA.getBlockForHeight(i, k, j).getStateFromMeta(CanyonColour.SAVANNA.getMetaForHeight(i, k, j)));
                    }
                } else {
                    if (depth == 0 && k > 61) {
                        if (simplex.noise2(i / width, j / width) > height) // > 0.27f, i / 12f
                        {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), mixBlock);
                        } else {
                            primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), topBlock);
                        }
                    } else if (depth < 4) {
                        primer.setBlockState(MathUtils.globalToLocal(x), k, MathUtils.globalToLocal(y), fillerBlock);
                    }
                }
            }
        }
    }
}