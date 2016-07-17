package rtg.event;

import java.util.ArrayList;
import java.util.Random;
import java.util.WeakHashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import rtg.RTG;
import rtg.config.rtg.ConfigRTG;
import rtg.util.Acceptor;
import rtg.util.BiomeUtils;
import rtg.util.Logger;
import rtg.util.RandomUtil;
import rtg.world.WorldTypeRTG;
import rtg.world.biome.WorldChunkManagerRTG;
import rtg.world.biome.realistic.RealisticBiomeBase;
import rtg.world.gen.MapGenCavesRTG;
import rtg.world.gen.MapGenRavineRTG;
import rtg.world.gen.feature.tree.rtg.TreeRTG;
import rtg.world.gen.genlayer.RiverRemover;
import rtg.world.gen.structure.MapGenScatteredFeatureRTG;
import rtg.world.gen.structure.MapGenVillageRTG;

public class EventManagerRTG
{
    private final LoadChunkRTG LOAD_CHUNK_RTG = new LoadChunkRTG();
    private final GenerateMinableRTG GENERATE_MINABLE_RTG = new GenerateMinableRTG();
    private final InitBiomeGensRTG INIT_BIOME_GENS_RTG = new InitBiomeGensRTG();
    private final InitMapGenRTG INIT_MAP_GEN_RTG = new InitMapGenRTG();
    private final SaplingGrowTreeRTG SAPLING_GROW_TREE_RTG = new SaplingGrowTreeRTG();
    
    public final WorldEventRTG WORLD_EVENT_RTG = new WorldEventRTG();
    public final RTGEventRegister RTG_EVENT_REGISTER = new RTGEventRegister();

    private WeakHashMap<Integer, Acceptor<ChunkEvent.Load>> chunkLoadEvents = new WeakHashMap<>();
    private boolean registered = false;
    private long worldSeed;
    private final String EVENT_SYSTEM = "RTG Event System: ";

    public EventManagerRTG() {
        // These should be registered once, and stay registered -srs
        MinecraftForge.TERRAIN_GEN_BUS.register(RTG_EVENT_REGISTER);
        MinecraftForge.EVENT_BUS.register(WORLD_EVENT_RTG);
        Logger.info(EVENT_SYSTEM + "Initialising EventManagerRTG");
    }

    public class LoadChunkRTG
    {
        LoadChunkRTG() {
            Logger.debug(EVENT_SYSTEM + "Initialising LoadChunkRTG");
        }

        @SubscribeEvent
        public void loadChunkRTG(ChunkEvent.Load event) {
            Acceptor<ChunkEvent.Load> acceptor = chunkLoadEvents.get(event.getWorld().provider.getDimension());
            if (acceptor != null) {
                acceptor.accept(event);
            }
        }
    }

    public class GenerateMinableRTG
    {
        GenerateMinableRTG() {
            Logger.debug(EVENT_SYSTEM + "Initialising GenerateMinableRTG");
        }

        @SubscribeEvent
        public void generateMinableRTG(OreGenEvent.GenerateMinable event) {

            switch (event.getType()) {

                case COAL:
                    if (!ConfigRTG.generateOreCoal) { event.setResult(Result.DENY); }
                    return;

                case IRON:
                    if (!ConfigRTG.generateOreIron) { event.setResult(Result.DENY); }
                    return;

                case REDSTONE:
                    if (!ConfigRTG.generateOreRedstone) { event.setResult(Result.DENY); }
                    return;

                case GOLD:
                    if (!ConfigRTG.generateOreGold) { event.setResult(Result.DENY); }
                    return;

                case LAPIS:
                    if (!ConfigRTG.generateOreLapis) { event.setResult(Result.DENY); }
                    return;

                case DIAMOND:
                    if (!ConfigRTG.generateOreDiamond) { event.setResult(Result.DENY); }
                    return;
                    
                case EMERALD:
                    if (!ConfigRTG.generateOreEmerald) { event.setResult(Result.DENY); }
                    return;

                default:
                	return;
            }
        }
    }

    public class InitBiomeGensRTG
    {
        InitBiomeGensRTG()
        {
            Logger.debug(EVENT_SYSTEM + "Initialising InitBiomeGensRTG");
        }

        @SubscribeEvent
        public void initBiomeGensRTG(WorldTypeEvent.InitBiomeGens event) {

            try {
                event.setNewBiomeGens(new RiverRemover().riverLess(event.getOriginalBiomeGens()));
            } catch (ClassCastException ex) {
                //throw ex;
                // failed attempt because the GenLayers don't end with GenLayerRiverMix
            }
        }
    }

    public class InitMapGenRTG
    {
        InitMapGenRTG() {
            Logger.debug(EVENT_SYSTEM + "Initialising InitMapGenRTG");
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        public void initMapGenRTG(InitMapGenEvent event) {

            Logger.debug("event type = %s", event.getType().toString());
            Logger.debug("event originalGen = %s", event.getOriginalGen().toString());

            switch (event.getType()) {
                case SCATTERED_FEATURE:
                    event.setNewGen(new MapGenScatteredFeatureRTG());
                    break;

                case VILLAGE:
                    if (ConfigRTG.enableVillageModifications) {
                        event.setNewGen(new MapGenVillageRTG());
                    }
                    break;

                case CAVE:
                    if (ConfigRTG.enableCaveModifications) {
                        event.setNewGen(new MapGenCavesRTG());
                    }
                    break;

                case RAVINE:
                    if (ConfigRTG.enableRavineModifications) {
                        event.setNewGen(new MapGenRavineRTG());
                    }
                    break;

                default:
                	break;
            }
            
            Logger.debug("event newGen = %s", event.getNewGen().toString());
        }
    }

    public class SaplingGrowTreeRTG
    {
        SaplingGrowTreeRTG() {
            Logger.debug(EVENT_SYSTEM + "Initialising SaplingGrowTreeRTG");
        }

        @SubscribeEvent
        public void saplingGrowTreeRTG(SaplingGrowTreeEvent event) {
        	
            // Are RTG saplings enabled?
            if (!ConfigRTG.enableRTGSaplings) { return; }

            Random rand = event.getRand();
            // Should we generate a vanilla tree instead?
            if (rand.nextInt(ConfigRTG.rtgTreeChance) != 0) {

                Logger.debug("Skipping RTG tree generation.");
                return;
            }

            World world = event.getWorld();
            int x = event.getPos().getX();
            int y = event.getPos().getY();
            int z = event.getPos().getZ();

            IBlockState saplingBlock = world.getBlockState(event.getPos());

            WorldChunkManagerRTG cmr = (WorldChunkManagerRTG) world.getBiomeProvider();
            //Biome bgg = cmr.getBiomeGenAt(event.getPos());
            Biome bgg = world.getBiome(new BlockPos(event.getPos()));
            RealisticBiomeBase rb = RealisticBiomeBase.getBiome(BiomeUtils.getId(bgg));
            ArrayList<TreeRTG> biomeTrees = rb.rtgTrees;

            Logger.debug("Biome = %s", rb.baseBiome.getBiomeName());
            Logger.debug("Ground Sapling Block = %s", saplingBlock.getBlock().getLocalizedName());
            Logger.debug("Ground Sapling Meta = %d", saplingBlock.getBlock().getMetaFromState(saplingBlock));

            if (biomeTrees.size() > 0) {
                // First, let's get all of the trees in this biome that match the sapling on the ground.
                ArrayList<TreeRTG> validTrees = new ArrayList<>();

                for (int i = 0; i < biomeTrees.size(); i++) {

                    Logger.debug("Biome Tree #%d = %s", i, biomeTrees.get(i).getClass().getName());
                    Logger.debug("Biome Tree #%d Sapling Block = %s", i, biomeTrees.get(i).saplingBlock.getClass().getName());
                    Logger.debug("Biome Tree #%d Sapling Meta = %d", i, biomeTrees.get(i).saplingBlock.getBlock().getMetaFromState(biomeTrees.get(i).saplingBlock));

                    if (saplingBlock == biomeTrees.get(i).saplingBlock && saplingBlock.getBlock().getMetaFromState(saplingBlock) == biomeTrees.get(i).saplingBlock.getBlock().getMetaFromState(biomeTrees.get(i).saplingBlock)) {

                        validTrees.add(biomeTrees.get(i));
                        Logger.debug("Valid tree found!");
                    }
                }
                // If there are valid trees, then proceed; otherwise, let's get out here.
                if (validTrees.size() > 0) {
                    // Get a random tree from the list of valid trees.
                    TreeRTG tree = validTrees.get(rand.nextInt(validTrees.size()));

                    Logger.debug("Tree = %s", tree.getClass().getName());

                    // Set the trunk size if min/max values have been set.
                    if (tree.minTrunkSize > 0 && tree.maxTrunkSize > tree.minTrunkSize) {

                        tree.trunkSize = RandomUtil.getRandomInt(rand, tree.minTrunkSize, tree.maxTrunkSize);
                    }

                    // Set the crown size if min/max values have been set.
                    if (tree.minCrownSize > 0 && tree.maxCrownSize > tree.minCrownSize) {

                        tree.crownSize = RandomUtil.getRandomInt(rand, tree.minCrownSize, tree.maxCrownSize);
                    }

                    /**
                     * Set the generateFlag to what it needs to be for growing trees from saplings,
                     * generate the tree, and then set it back to what it was before.
                     *
                     * TODO: Does this affect the generation of normal RTG trees?
                     */
                    int oldFlag = tree.generateFlag;
                    tree.generateFlag = 3;
                    boolean generated = tree.generate(world, rand, new BlockPos(x, y, z));
                    tree.generateFlag = oldFlag;

                    if (generated) {

                        // Prevent the original tree from generating.
                        event.setResult(Result.DENY);

                        // Sometimes we have to remove the sapling manually because some trees grow around it, leaving the original sapling.
                        if (world.getBlockState(new BlockPos(x, y, z)) == saplingBlock) {
                            world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 2);
                        }
                    }
                }
                else Logger.debug("There are no RTG trees associated with the sapling on the ground." +
                        " Generating a vanilla tree instead.");
            }
        }
    }

    public class WorldEventRTG
    {
        WorldEventRTG() {
            Logger.debug(EVENT_SYSTEM + "Initialising WorldEventRTG");
        }

        @SubscribeEvent
        public void onWorldLoad(WorldEvent.Load event) {
        	
            // This event fires for each dimension loaded (and then one last time in which it returns 0?),
            // so initialise a field to 0 and set it to the world seed and only display it in the log once.
            if (worldSeed != event.getWorld().getSeed() && event.getWorld().getSeed() != 0) {

                worldSeed = event.getWorld().getSeed();
                Logger.info("World Seed: " + worldSeed);
            }
        }

        @SubscribeEvent
        public void onWorldUnload(WorldEvent.Unload event) {
        	
            // Reset WORLD_SEED so that it logs on the next server start if the seed is the same as the last load.
            worldSeed = 0;
        }
    }

    public class RTGEventRegister
    {
        RTGEventRegister() {
            Logger.debug(EVENT_SYSTEM + "Initialising RTGEventRegister");
        }

        @SubscribeEvent
        public void registerRTGEventHandlers(WorldTypeEvent.InitBiomeGens event) {
        	
            if (event.getWorldType() instanceof WorldTypeRTG) {
                if (!registered) {
                    Logger.info(EVENT_SYSTEM + "Registering RTG's Terrain Event Handlers...");
                    RTG.eventMgr.registerEventHandlers();
                    if (registered) Logger.info(EVENT_SYSTEM + "RTG's Terrain Event Handlers have been registered successfully.");
                }
            }
            else {
                if (registered) RTG.eventMgr.unRegisterEventHandlers();
            }
        }
    }

    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(LOAD_CHUNK_RTG);
        MinecraftForge.ORE_GEN_BUS.register(GENERATE_MINABLE_RTG);
        MinecraftForge.TERRAIN_GEN_BUS.register(INIT_BIOME_GENS_RTG);
        MinecraftForge.TERRAIN_GEN_BUS.register(INIT_MAP_GEN_RTG);
        MinecraftForge.TERRAIN_GEN_BUS.register(SAPLING_GROW_TREE_RTG);
        registered = true;
    }

    public void unRegisterEventHandlers() {
        MinecraftForge.EVENT_BUS.unregister(LOAD_CHUNK_RTG);
        MinecraftForge.ORE_GEN_BUS.unregister(GENERATE_MINABLE_RTG);
        MinecraftForge.TERRAIN_GEN_BUS.unregister(INIT_BIOME_GENS_RTG);
        MinecraftForge.TERRAIN_GEN_BUS.unregister(INIT_MAP_GEN_RTG);
        MinecraftForge.TERRAIN_GEN_BUS.unregister(SAPLING_GROW_TREE_RTG);
        registered = false;
    }

    public void setDimensionChunkLoadEvent(int dimension, Acceptor<ChunkEvent.Load> action) {
        chunkLoadEvents.put(dimension, action);
    }

    public boolean isRegistered() {
        return registered;
    }
}