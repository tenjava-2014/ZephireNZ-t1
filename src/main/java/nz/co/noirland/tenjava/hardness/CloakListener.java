package nz.co.noirland.tenjava.hardness;

import nz.co.noirland.tenjava.BukCombatPlugin;
import nz.co.noirland.tenjava.PluginConfig;
import nz.co.noirland.tenjava.Util;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class CloakListener implements Listener {

    private final PluginConfig config = PluginConfig.inst();
    private final Set<UUID> cloaked = new HashSet<UUID>();

    /**
     * Cloak enchantment generator
     */
    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        if(event.getExpLevelCost() != 30) return;

        if(!EnchantmentTarget.ARMOR_TORSO.includes(item.getType())) return;
        if(!Util.roll(config.getCloakChance())) return;

        event.getEnchantsToAdd().clear();
        List<String> lore = new ArrayList<String>();
        ItemMeta meta = item.getItemMeta();
        if(meta.hasLore()) {
            lore = item.getItemMeta().getLore();
        }
        lore.add(0, BukCombatPlugin.CLOAK_NAME);
        meta.setLore(lore);
        item.setItemMeta(meta);
//        Util.enchant(item);
    }

    /**
     * Cloaking listener.
     * @param event
     */
    @EventHandler
    public void onChangeSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if(!event.isSneaking()) {
            if(cloaked.remove(player.getUniqueId())) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5* 20, 2));
            }
            return;
        }
        ItemStack armour = player.getInventory().getChestplate();
        if(!armour.hasItemMeta()) return;
        if(!armour.getItemMeta().hasLore()) return;

        List<String> lore = armour.getItemMeta().getLore();
        if (!lore.contains(BukCombatPlugin.CLOAK_NAME)) {
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, config.getCloakTime() * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, config.getCloakTime() * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (config.getCloakTime() + 5)* 20, 2));
    }
}
