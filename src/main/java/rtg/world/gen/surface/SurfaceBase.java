package rtg.world.gen.surface;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.biome.BiomeConfig;
import rtg.config.rtg.ConfigRTG;
import rtg.util.CellNoise;
import rtg.util.OpenSimplexNoise;

public class SurfaceBase
{
    protected IBlockState topBlock;
    protected IBlockState fillerBlock;
	protected BiomeConfig biomeConfig;

    public SurfaceBase(BiomeConfig config, IBlockState top, IBlockState fill)
    {
        if (config == null) throw new RuntimeException("Biome config in SurfaceBase is NULL.");
        
        biomeConfig = config;

        topBlock = top;
        fillerBlock = fill;
        
        this.assignUserConfigs(config, top, fill);
    }
	
    public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
	{
    	
	}
	
    protected IBlockState getShadowStoneBlock(World world, int i, int j, int x, int y, int k)
    {
        return Block.getBlockFromName(ConfigRTG.shadowStoneBlockId).getStateFromMeta((byte)ConfigRTG.shadowStoneBlockByte);
    }
	
    protected IBlockState getShadowDesertBlock(World world, int i, int j, int x, int y, int k)
    {
        return Block.getBlockFromName(ConfigRTG.shadowDesertBlockId).getStateFromMeta((byte)ConfigRTG.shadowDesertBlockByte);
    }
    
    protected IBlockState hcStone(World world, int worldX, int worldZ, int chunkX, int chunkZ, int worldY)
    {
        return Blocks.STONE.getDefaultState();
    }
    
    protected IBlockState hcCobble(World world, int worldX, int worldZ, int chunkX, int chunkZ, int worldY)
    { 
        return Blocks.COBBLESTONE.getDefaultState();
    }
    
    public IBlockState getTopBlock()
    {
        return this.topBlock;
    }
    
    public IBlockState getFillerBlock()
    {
        return this.fillerBlock;
    }
    
    private void assignUserConfigs(BiomeConfig config, IBlockState top, IBlockState fill)
    {
        String userTopBlock = config._string(BiomeConfig.surfaceTopBlockId);
        String userTopBlockMeta = config._string(BiomeConfig.surfaceTopBlockMetaId);
        String userFillerBlock = config._string(BiomeConfig.surfaceFillerBlockId);
        String userFillerBlockMeta = config._string(BiomeConfig.surfaceFillerBlockMetaId);
        
        try {
        	topBlock = Block.getBlockFromName(userTopBlock).getStateFromMeta(Byte.valueOf(userTopBlockMeta));
        }
        catch (Exception e) {
            topBlock = top;
        }
        
        try {
        	fillerBlock = Block.getBlockFromName(userFillerBlock).getStateFromMeta(Byte.valueOf(userFillerBlockMeta));
        }
        catch (Exception e) {
        	fillerBlock = fill;
        }
    }
    
    protected IBlockState getConfigBlock(BiomeConfig config, String propertyId, IBlockState blockDefault)
    {
    	IBlockState blockReturn = blockDefault;
        String userBlockId = config._string(propertyId);
        
        try {
            blockReturn = Block.getBlockFromName(userBlockId).getDefaultState(); // TODO: Incorporate config metas.
        }
        catch (Exception e) {
            blockReturn = blockDefault;
        }

        return blockReturn;
    }
}
