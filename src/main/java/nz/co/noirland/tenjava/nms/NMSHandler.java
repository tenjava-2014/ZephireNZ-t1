package nz.co.noirland.tenjava.nms;

import nz.co.noirland.tenjava.BukCombatPlugin;
import nz.co.noirland.tenjava.nms.v1_7_R3.NMS;
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
            Logger log = BukCombatPlugin.inst().getLogger();
            log.severe("Minecraft version " + ver + " unsupported!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(BukCombatPlugin.inst());
            return null;
        }
    }

    /**
     * Add an enchantment glow to an item.
     */
    public abstract ItemStack addEnchantGlow(ItemStack item);

    /**
     * Spawns a bunch of particles in the given area
     * @param effect Effect to use
     * @param loc Location of center of effect
     * @param size Radius to randomly spawn effects in
     * @param speed How quickly effect disappears
     * @param count  Number of effects to spawn
     * @param radius Radius to find players to show to.
     */
    public abstract void spawnParticlesForRadius(String effect, Location loc, int size, float speed, int count, int radius);

}
