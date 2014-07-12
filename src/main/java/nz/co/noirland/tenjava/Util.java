package nz.co.noirland.tenjava;

import org.bukkit.inventory.InventoryView;

public class Util {

    public static boolean isUpper(int rawSlot, InventoryView view) {
        return rawSlot == view.convertSlot(rawSlot);
    }

}
