package nz.co.noirland.tenjava;

import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig {

    private FileConfiguration config;
    private static PluginConfig inst;

    public static PluginConfig inst() {
        if(inst == null) {
            inst = new PluginConfig();
        }
        return inst;
    }

    private PluginConfig() {
        this.config = BukCombatPlugin.inst().getConfig();
    }

}
