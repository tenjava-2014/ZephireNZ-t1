package nz.co.noirland.tenjava;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class CombatListener implements Listener {

    /**
     * EventHandler for splash potions. This will be pushed off to various things depending on what was splashed.
     * @param event
     */
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {

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

    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {

    }

}
