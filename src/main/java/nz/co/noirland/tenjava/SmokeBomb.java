package nz.co.noirland.tenjava;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SmokeBomb extends ItemStack {

    public static final String SMOKE_BOMB_NAME = ChatColor.GOLD + "Smoke Bomb";

    public SmokeBomb() {
        super(Material.SNOW_BALL, 1, (short) 16);
        ItemMeta meta = getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add("Throw the smoke bomb on the");
        lore.add("ground and daze your foess!");

        meta.setDisplayName(SMOKE_BOMB_NAME);
        meta.setLore(lore);
        setItemMeta(meta);

    }

}
