package nz.co.noirland.tenjava;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

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
}
