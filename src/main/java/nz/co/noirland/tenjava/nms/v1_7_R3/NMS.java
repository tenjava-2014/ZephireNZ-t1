package nz.co.noirland.tenjava.nms.v1_7_R3;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;
import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;
import nz.co.noirland.tenjava.Util;
import nz.co.noirland.tenjava.nms.NMSHandler;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMS extends NMSHandler {

    public ItemStack addEnchantGlow(ItemStack item) {
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

    public void spawnParticlesForRadius(String effect, Location loc, int size, float speed, int count, int radius) {
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        Packet packet = new PacketPlayOutWorldParticles(effect, (float)x, (float)y, (float)z,
                (float)size, (float)size, (float)size, speed, count);
        for(Entity e : Util.entitiesInRadius(loc, radius)) {
            if(!(e instanceof Player)) continue;
            ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
