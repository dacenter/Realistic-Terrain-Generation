package rtg;

import java.util.ArrayList;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import rtg.api.event.BiomeConfigEvent;
import rtg.config.BiomeConfigManager;
import rtg.config.ConfigManager;
import rtg.event.EventManagerRTG;
import rtg.reference.ModInfo;
import rtg.util.Logger;
import rtg.util.RealisticBiomePresenceTester;
import rtg.world.WorldTypeRTG;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaBase;
import rtg.world.gen.structure.MapGenScatteredFeatureRTG;
import rtg.world.gen.structure.MapGenVillageRTG;


//@Mod(modid = "RTG", name = "Realistic Terrain Generaton", version = "2.0.0", dependencies = "required-after:Forge@[10.13.4.1448,)", acceptableRemoteVersions = "*")
@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = "required-after:Forge@[" + ModInfo.FORGE_DEP + ",)" + ModInfo.MOD_DEPS, acceptableRemoteVersions = "*")
public class RTG {

    @Instance(ModInfo.MOD_ID)
    public static RTG instance;
    public static String configPath;
    public static WorldTypeRTG worldtype;
    public static EventManagerRTG eventMgr;

    private ConfigManager configManager = new ConfigManager();

    public ConfigManager configManager(int dimension) {
        return configManager;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {    
        instance = this;
        
        MapGenStructureIO.registerStructure(MapGenScatteredFeatureRTG.Start.class, "rtg_MapGenScatteredFeatureRTG");
        MapGenStructureIO.registerStructure(MapGenVillageRTG.Start.class, "rtg_MapGenVillageRTG");

        Logger.info("[FMLPreInitializationEvent] Creating RTG's EventManager");
        eventMgr = new EventManagerRTG();

        MinecraftForge.EVENT_BUS.post(new BiomeConfigEvent.Pre());
        
        // This MUST get called before the config is initialised.
        BiomeConfigManager.initBiomeConfigs();
        
        MinecraftForge.EVENT_BUS.post(new BiomeConfigEvent.Post());
        
        configPath = event.getModConfigurationDirectory() + "/RTG/";
        ConfigManager.init(configPath);
        
        worldtype = new WorldTypeRTG("RTG");
    }
    
//  @EventHandler public void init(FMLInitializationEvent event) {}

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

        RealisticBiomeVanillaBase.addBiomes();

        RealisticBiomePresenceTester.doBiomeCheck();
    }

    public void runOnServerClose(Runnable action) {
        serverCloseActions.add(action);
    }

    public void runOnNextServerCloseOnly(Runnable action) {
        serverCloseActions.add(action);
    }

    private ArrayList<Runnable> oneShotServerCloseActions = new ArrayList<>();
    private ArrayList<Runnable> serverCloseActions = new ArrayList<>();
    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        for (Runnable action: serverCloseActions) {
            action.run();
        }
        for (Runnable action: oneShotServerCloseActions) {
            action.run();
        }
        oneShotServerCloseActions.clear();

        // TODO: Port the latest event manager stuff.
        /*if (eventMgr.isRegistered()) {
            Logger.info("Unregistering RTG's Terrain Event Handlers...");
            RTG.eventMgr.unRegisterEventHandlers();
            if (!eventMgr.isRegistered()) Logger.info("RTG's Terrain Event Handlers have been unregistered successfully.");
        }*/

    }
}
