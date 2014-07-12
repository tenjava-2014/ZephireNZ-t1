package nz.co.noirland.tenjava;

import org.bukkit.plugin.java.JavaPlugin;

public class BukCombatPlugin extends JavaPlugin {

    private static BukCombatPlugin inst;

    public static BukCombatPlugin inst() {
        return inst;
    }

    @Override
    public void onEnable() {
        inst = this;
        getServer().getPluginManager().registerEvents(new CombatListener(), this);
    }

}
