package nz.co.noirland.tenjava.throwables;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class FlashBang extends ItemStack {

    public static final String SMOKE_BOMB_NAME = ChatColor.GOLD + "Flashbang";
    public static final ArrayList<String> lore = new ArrayList<String>();

    static {
        lore.add("Throw the flash bang on the");
        lore.add("ground and scare the living");
        lore.add("daylights out of your foes!");
    }

    public FlashBang() {
        super(Material.SNOW_BALL, 1, (short) 32);
        ItemMeta meta = getItemMeta();

        meta.setDisplayName(SMOKE_BOMB_NAME);
        meta.setLore(lore);
        setItemMeta(meta);
    }

}
