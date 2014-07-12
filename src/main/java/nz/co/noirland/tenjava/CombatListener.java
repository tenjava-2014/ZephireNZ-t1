package nz.co.noirland.tenjava;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;

public class CombatListener implements Listener {

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
