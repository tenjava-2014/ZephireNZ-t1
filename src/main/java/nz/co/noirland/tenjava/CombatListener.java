package nz.co.noirland.tenjava;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;

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

}
