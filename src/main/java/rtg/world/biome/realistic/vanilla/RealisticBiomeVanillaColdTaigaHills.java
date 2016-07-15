package rtg.world.biome.realistic.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import rtg.api.biome.BiomeConfig;
import rtg.api.biome.vanilla.config.BiomeConfigVanillaColdTaigaHills;
import rtg.world.biome.deco.collection.DecoCollectionTaiga;
import rtg.world.gen.surface.vanilla.SurfaceVanillaColdTaigaHills;
import rtg.world.gen.terrain.vanilla.TerrainVanillaColdTaigaHills;

public class RealisticBiomeVanillaColdTaigaHills extends RealisticBiomeVanillaBase
{
    
    public static Block topBlock = Biomes.coldTaigaHills.topBlock;
    public static Block fillerBlock = Biomes.coldTaigaHills.fillerBlock;
    
    public RealisticBiomeVanillaColdTaigaHills(BiomeConfig config)
    {
    
        super(config, 
            Biome.coldTaigaHills,
            Biome.frozenRiver,
            new TerrainVanillaColdTaigaHills(),
            new SurfaceVanillaColdTaigaHills(config, Blocks.grass, Blocks.dirt, true, Blocks.sand, 0.2f)
        );
        this.noLakes=true;
        
        this.addDecoCollection(new DecoCollectionTaiga(this.config._boolean(BiomeConfigVanillaColdTaigaHills.decorationLogsId), 8f));
    }
}
