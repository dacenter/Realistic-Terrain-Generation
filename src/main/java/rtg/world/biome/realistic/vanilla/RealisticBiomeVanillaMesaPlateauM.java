package rtg.world.biome.realistic.vanilla;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import rtg.api.biome.BiomeConfig;
import rtg.util.CellNoise;
import rtg.util.OpenSimplexNoise;
import rtg.world.biome.deco.DecoCactus;
import rtg.world.biome.deco.DecoDeadBush;
import rtg.world.biome.deco.DecoReed;
import rtg.world.biome.deco.DecoShrub;
import rtg.world.biome.deco.DecoTree;
import rtg.world.biome.deco.DecoTree.TreeCondition;
import rtg.world.biome.deco.DecoTree.TreeType;
import rtg.world.gen.feature.tree.vanilla.WorldGenTreesRTG;
import rtg.world.gen.surface.SurfaceBase;
import rtg.world.gen.surface.SurfaceRiverOasis;
import rtg.world.gen.surface.vanilla.SurfaceVanillaMesaPlateauM;
import rtg.world.gen.terrain.vanilla.TerrainVanillaMesaPlateauM;

public class RealisticBiomeVanillaMesaPlateauM extends RealisticBiomeVanillaBase
{
    public static Biome biome = Biomes.MUTATED_MESA_ROCK;
    public static Biome river = Biomes.RIVER;

    public RealisticBiomeVanillaMesaPlateauM(BiomeConfig config)
    {
    
        super(config, 
            mutationBiome,
            Biome.river,
            new TerrainVanillaMesaPlateauM(true, 15f, 260f, 50f, 30f, 79f),
            new SurfaceVanillaMesaPlateauM(config, Blocks.sand, (byte)1, Blocks.sand, (byte) 1, 0)
        );
        this.noLakes=true;
        
        DecoShrub decoShrub = new DecoShrub();
        decoShrub.chance = 10;
        addDeco(decoShrub);

        DecoCactus decoCactus = new DecoCactus();
        decoCactus.strengthFactor = 25f;
        decoCactus.soilBlock = Blocks.sand;
        decoCactus.soilMeta = (byte)1;
        decoCactus.sandOnly = false;
        decoCactus.maxRiver = 0.8f;
        addDeco(decoCactus);

        DecoReed decoReed = new DecoReed();
        decoReed.loops = 5;
        decoReed.maxRiver = 0.8f;
        addDeco(decoReed);

        DecoDeadBush decoDeadBush = new DecoDeadBush();
        decoDeadBush.strengthFactor = 5f;
        addDeco(decoDeadBush);

        DecoTree decoTree = new DecoTree(new WorldGenTreesRTG());
        decoTree.loops = 20;
        decoTree.treeType = TreeType.WORLDGEN;
        decoTree.treeCondition = TreeCondition.X_DIVIDED_BY_STRENGTH;
        decoTree.distribution = new DecoTree.Distribution(24f, 1f, 0f);
        decoTree.treeConditionChance = 0;
        decoTree.treeConditionFloat = 4f;
        decoTree.treeConditionNoise = 0f;
        decoTree.minY = 74;
        addDeco(decoTree);
    }

    @Override
    public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world, Random rand,
                         OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base)
    {

        this.getSurface().paintTerrain(blocks, metadata, i, j, x, y, depth, world, rand, simplex, cell, noise, river, base);

        SurfaceBase riverSurface = new SurfaceRiverOasis(this.config);
        riverSurface.paintTerrain(blocks, metadata, i, j, x, y, depth, world, rand, simplex, cell, noise, river, base);
    }
}
