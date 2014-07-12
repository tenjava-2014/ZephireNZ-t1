package nz.co.noirland.tenjava.hardness;

import nz.co.noirland.tenjava.BukCombatPlugin;
import nz.co.noirland.tenjava.PluginConfig;
import nz.co.noirland.tenjava.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HardnessListener implements Listener {

    final PluginConfig config = PluginConfig.inst();

    /**
     * EventHandler for 'Hardened' custom enchant.
     * Listens for when a player is hit by another player.
     */
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        // Don't care about anything that aren't players
        if(event.getDamager().getType() != EntityType.PLAYER) return;

        Player attacker = (Player) event.getDamager();
        LivingEntity target = (LivingEntity) event.getEntity();

        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        ItemStack weapon = attacker.getItemInHand();
        if(!EnchantmentTarget.WEAPON.includes(weapon)) return;

        ItemStack[] armour = target.getEquipment().getArmorContents();
        short dur = 0;
        for(ItemStack item : armour) {
            if(!item.hasItemMeta()) continue;
            if(!item.getItemMeta().hasLore()) continue;

            List<String> lore = item.getItemMeta().getLore();
            if(lore.contains(BukCombatPlugin.HARDNESS_NAME)) {
                dur += config.getHardnessDamage();
            }
        }

        weapon.setDurability((short) (weapon.getDurability() + dur));
        attacker.playSound(target.getLocation(), Sound.ANVIL_LAND, 0.2F, (float) (1.5D + Util.rand(0.5)));
    }

    /**
     * DEBUG event for giving all zombies Hardness armour
     * (It's hard to test with only yourself :3)
     * TODO: REMOVE
     */
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if(event.getEntityType() != EntityType.ZOMBIE) return;
        event.getEntity().setCanPickupItems(true);
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        List<String> lore = new ArrayList<String>();
        ItemMeta meta = item.getItemMeta();
        if(meta.hasLore()) {
            lore = item.getItemMeta().getLore();
        }
        lore.add(0, BukCombatPlugin.HARDNESS_NAME);
        meta.setLore(lore);
        item.setItemMeta(meta);

        event.getEntity().getEquipment().setChestplate(item);
    }


    /**
     * Enchantment event for 'Hardness' custom enchant.
     * When enchanting, you have a chance of getting this enchant when using 30 levels.
     */
    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        if(event.getExpLevelCost() != 30) return; // Must be 30 levels to get

        if(!EnchantmentTarget.ARMOR.includes(item.getType())) return; // Ignore things that aren't armour
        if(!Util.roll(config.getHardnessChance())) return;

        List<String> lore = new ArrayList<String>();
        ItemMeta meta = item.getItemMeta();
        if(meta.hasLore()) {
            lore = item.getItemMeta().getLore();
        }
        lore.add(0, BukCombatPlugin.HARDNESS_NAME);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

}
