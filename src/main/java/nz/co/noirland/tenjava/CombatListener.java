package nz.co.noirland.tenjava;

import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CombatListener implements Listener {

    PluginConfig config = PluginConfig.inst();

    /**
     * EventHandler for splash potions. This will be pushed off to various things depending on what was splashed.
     * @param event
     */
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {

    }

    /**
     * Called when player drinks a potion.
     * @param event
     */
    @EventHandler
    public void onConsule(PlayerItemConsumeEvent event) {

    }

    /**
     * EventHandler for 'Hardened' custom enchant.
     * Listens for when a player is hit by another player.
     * @param event
     */
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        // Don't care about anything that aren't players
        if(event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER) return;

        Player attacker = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        ItemStack weapon = attacker.getItemInHand();
        ItemStack[] armour = target.getInventory().getArmorContents();
        short dur = 0;
        for(ItemStack item : armour) {
            List<String> lore = item.getItemMeta().getLore();
            if(lore.contains(BukCombatPlugin.HARDNESS_NAME)) {
                dur += config.getHardnessDamage();
            }
        }
        weapon.setDurability((short) (weapon.getDurability() - dur));
    }

    /**
     * For managing potion creation.
     * @param event
     */
    @EventHandler
    public void onBrew(BrewEvent event) {

    }

    @EventHandler
    public void onPrepCraft(PrepareItemCraftEvent event) {
        event.getRecipe();
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.getRecipe();
    }

    /**
     * Enchantment event for 'Hardness' custom enchant.
     * When enchanting, you have a chance of getting this enchant when using 30 levels.
     * @param event
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

    @EventHandler
    public void onClickBrew(InventoryClickEvent event) {
        if(event.getInventory().getType() != InventoryType.BREWING) return;
        BrewerInventory inv = (BrewerInventory) event.getInventory();
        if(!Util.isUpper(event.getRawSlot(), event.getView())) return;

//        event.setCurrentItem(event.getCursor());
        inv.setItem(event.getSlot(), event.getCursor());
//        event.setCursor(null);
        new UpdateInventoryTask((Player) event.getWhoClicked());
    }

}
