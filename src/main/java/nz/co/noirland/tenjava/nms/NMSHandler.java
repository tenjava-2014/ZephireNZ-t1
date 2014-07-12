package nz.co.noirland.tenjava.nms;

import nz.co.noirland.tenjava.BukCombatPlugin;
import nz.co.noirland.tenjava.nms.v1_7_3_R3.NMS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public abstract class NMSHandler {

    private static NMSHandler inst;

    public static NMSHandler inst() {
        if(inst == null) {
            inst = getNMS();
        }
        return inst;
    }

    private static NMSHandler getNMS() {
        String pName = Bukkit.getServer().getClass().getPackage().getName();
        String ver = pName.substring(pName.lastIndexOf('.') + 1);
        try {
            Class<?> clazz = Class.forName("nz.co.noirland.tenjava.nms." + ver + ".NMS");
            if(NMSHandler.class.isAssignableFrom(clazz)) {
                return (NMS) clazz.getConstructor().newInstance();
            }
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            Logger log = BukCombatPlugin.inst().getLogger();
            log.severe("Minecraft version " + ver + " unsupported!");
            Bukkit.getPluginManager().disablePlugin(BukCombatPlugin.inst());
            return null;
        }
    }

    public abstract ItemStack addEnchantGlow(ItemStack item);

    public abstract void spawnParticlesForRadius(String effect, Location loc, int size, float speed, int count, int radius);

}
