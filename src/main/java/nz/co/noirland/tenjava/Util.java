package nz.co.noirland.tenjava;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;
import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class Util {

    public static boolean isUpper(int rawSlot, InventoryView view) {
        return rawSlot == view.convertSlot(rawSlot);
    }

    /**
     * Uses NMS code to add the enchanted glow to an item.
     *
     * TODO: Abstractify NMS code, for safety
     * @param item ItemStack to add glow to
     * @return ItemStack with glow added
     */
    public static ItemStack enchant(ItemStack item) {
        net.minecraft.server.v1_7_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nms.hasTag()) {
            tag = new NBTTagCompound();
            nms.setTag(tag);
        }
        if (tag == null)
            tag = nms.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static boolean roll(int chance) {
        return chance < rand(100);

    }

    public static double rand(double max) {
          return Math.random() * max;
    }

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

    public static void smokeEffect(Location loc) {
        int r = 1;
        int pRadius = 64 * 64;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        Packet packet = new PacketPlayOutWorldParticles("hugeexplosion", (float)x, (float)y, (float)z,
                (float)r, (float)r, (float)r, 0.05F, 5);
        for(Entity e : entitiesInRadius(loc, pRadius)) {
            if(!(e instanceof Player)) continue;
            ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
