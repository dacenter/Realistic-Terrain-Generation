package rtg.world.biome.realistic.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import rtg.api.biome.BiomeConfig;
import rtg.api.biome.vanilla.config.BiomeConfigVanillaSavanna;
import rtg.world.biome.deco.collection.DecoCollectionDesertRiver;
import rtg.world.biome.deco.collection.DecoCollectionSavanna;
import rtg.world.gen.surface.vanilla.SurfaceVanillaSavanna;
import rtg.world.gen.terrain.vanilla.TerrainVanillaSavanna;

public class RealisticBiomeVanillaSavanna extends RealisticBiomeVanillaBase
{
    
    public static Block topBlock = Biomes.savanna.topBlock;
    public static Block fillerBlock = Biomes.savanna.fillerBlock;
    
    public RealisticBiomeVanillaSavanna(BiomeConfig config)
    {
    
        super(config, 
            BiomeGenBase.savanna,
            BiomeGenBase.river,
            new TerrainVanillaSavanna(),
            new SurfaceVanillaSavanna(config, topBlock, fillerBlock, topBlock, 13f, 0.27f)
        );

        this.addDecoCollection(new DecoCollectionDesertRiver());
        this.addDecoCollection(new DecoCollectionSavanna(this.config._boolean(BiomeConfigVanillaSavanna.decorationLogsId)));
    }
}
