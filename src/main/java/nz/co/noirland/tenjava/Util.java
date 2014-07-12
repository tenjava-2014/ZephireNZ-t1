package nz.co.noirland.tenjava;

import nz.co.noirland.tenjava.nms.NMSHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class Util {

    public static boolean isUpper(int rawSlot, InventoryView view) {
        return rawSlot == view.convertSlot(rawSlot);
    }

    /**
     * Adds the enchantment effect to any item.
     * @param item Item to have enchant effect added
     * @return ItemStack with added enchant effect
     */
    public static ItemStack enchant(ItemStack item) {
        return NMSHandler.inst().addEnchantGlow(item);
    }

    /**
     * Use a chance out of 100 to give a random number.
     * @param chance Chance out of 100
     * @return random true/false, weighted by chance
     */
    public static boolean roll(int chance) {
        return chance < rand(100);

    }

    /**
     * Simple random generator to go from 0-max
     * @param max Maximum (non-inclusive) to go to.
     * @return random double between 0 and max.
     */
    public static double rand(double max) {
          return Math.random() * max;
    }

    /**
     * Finds all entities that are in the (circular) radius given from a given point.
     * @param loc Center of search area
     * @param radius Radius of search area
     * @return List of all entities in the area
     */
    public static Set<Entity> entitiesInRadius(Location loc, int radius) {
        radius *= radius;
        Set<Entity> ret = new HashSet<Entity>();
        for(Entity e : loc.getWorld().getEntities()) {
            if(e.getLocation().distanceSquared(loc) <= radius) {
                ret.add(e);
            }
        }

        return ret;
    }

    /**
     * Spawns a explosion effect on the given location.
     * @param loc
     */
    public static void smokeEffect(Location loc) {
        NMSHandler.inst().spawnParticlesForRadius("hugeexplosion", loc, 1, 0.05F, 5, 64 * 64);
    }
}
