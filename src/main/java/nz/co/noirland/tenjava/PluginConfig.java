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

    /**
     * Chance out of 100 of hardened being added to item.
     * @return integer chance
     */
    public int getHardnessChance() {
        return config.getInt("enchants.hardened.chance", 30);
    }

    public int getHardnessMult() {
        return config.getInt("enchant.hardness.multiplier", 3);
    }

}
