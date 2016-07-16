package rtg.world.biome.realistic.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

import rtg.api.biome.BiomeConfig;
import rtg.api.biome.vanilla.config.BiomeConfigVanillaForestHills;
import rtg.world.biome.deco.collection.DecoCollectionForest;
import rtg.world.gen.surface.vanilla.SurfaceVanillaForestHills;
import rtg.world.gen.terrain.vanilla.TerrainVanillaForestHills;

public class RealisticBiomeVanillaForestHills extends RealisticBiomeVanillaBase
{
    
    public static Block topBlock = Biomes.forestHills.topBlock;
    public static Block fillerBlock = Biomes.forestHills.fillerBlock;
    
    public RealisticBiomeVanillaForestHills(BiomeConfig config)
    {
    
        super(config, 
            Biome.forestHills,
            Biome.river,
            new TerrainVanillaForestHills(),
            new SurfaceVanillaForestHills(config, Blocks.grass, Blocks.dirt, false, null, 0f, 1.5f, 60f, 65f, 1.5f, Blocks.dirt, (byte)2, 0.15f)
        );
        this.noLakes=true;

		this.addDecoCollection(new DecoCollectionForest(this.config._boolean(BiomeConfigVanillaForestHills.decorationLogsId)));
    }
}
