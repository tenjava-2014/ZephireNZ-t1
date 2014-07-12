package nz.co.noirland.tenjava.hardness;

import nz.co.noirland.tenjava.BukCombatPlugin;
import nz.co.noirland.tenjava.PluginConfig;
import nz.co.noirland.tenjava.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CloakListener implements Listener {

    private final PluginConfig config = PluginConfig.inst();
    private final HashMap<UUID, ItemStack> cloaked = new HashMap<UUID, ItemStack>();

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
     * Manually disable enchanting if it's a cloak item.
     */
    @EventHandler
    public void onPreEnchant(PrepareItemEnchantEvent event) {
        List<String> lore = event.getItem().getItemMeta().getLore();
        if (lore != null && lore.contains(BukCombatPlugin.CLOAK_NAME)) {
            event.setCancelled(true);
        }
    }

    /**
     * Cloaking listener.
     */
    @EventHandler
    public void onChangeSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if(!event.isSneaking()) {
            ItemStack chest = cloaked.remove(player.getUniqueId());
            if(chest != null) {
                removeEffects(player, chest);
            }
            return;
        }
        ItemStack chest = player.getInventory().getChestplate();
        if(chest == null || chest.getType() == Material.AIR) return;
        if(!chest.hasItemMeta()) return;
        if(!chest.getItemMeta().hasLore()) return;

        List<String> lore = chest.getItemMeta().getLore();
        if (!lore.contains(BukCombatPlugin.CLOAK_NAME)) {
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, config.getCloakTime() * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, config.getCloakTime() * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (config.getCloakTime() + 15)* 20, 2));

        cloaked.put(player.getUniqueId(), chest);
        player.getInventory().setChestplate(null);
    }

    /**
     * Prevent cloaked players from staying cloaked.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ItemStack chest = cloaked.remove(player.getUniqueId());
        if(chest != null) {
            removeEffects(player, chest);
        }
    }

    /**
     * Remove all effects from the player, add Weakness, break item, give back item.
     */
    private void removeEffects(Player player, ItemStack chest) {
        chest.setDurability((short)(chest.getDurability() + 100));
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 15 * 20, 2));

        if(chest.getDurability() >= chest.getType().getMaxDurability()) {
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, (float) (0.7 + Util.rand(0.5)));
        }else {
            player.getInventory().setChestplate(chest);
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, (float) (1.0 + Util.rand(0.5)));
        }
    }
}
