package rtg.config;

import java.io.File;

import rtg.config.rtg.ConfigRTG;
import rtg.config.vanilla.ConfigVanilla;

public class ConfigManager
{
    
    public static File rtgConfigFile;
    public static File vanillaConfigFile;

    private ConfigRTG configRTG = new ConfigRTG();
    public ConfigRTG rtg() {
        return configRTG;
    }
    
    public static void init(String configpath)
    {
    
        rtgConfigFile = new File(configpath + "rtg.cfg");
        vanillaConfigFile = new File(configpath + "biomes/vanilla.cfg");
        
        ConfigRTG.init(rtgConfigFile);
        
        ConfigVanilla.init(vanillaConfigFile);
    }
}
