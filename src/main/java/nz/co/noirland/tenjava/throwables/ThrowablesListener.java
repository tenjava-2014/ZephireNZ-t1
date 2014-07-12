package nz.co.noirland.tenjava.throwables;

import nz.co.noirland.tenjava.BukCombatPlugin;
import nz.co.noirland.tenjava.PluginConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ThrowablesListener implements Listener {

    private final HashMap<ProjectileSource, ThrowableType> thrown= new HashMap<ProjectileSource, ThrowableType>();

    PluginConfig config = PluginConfig.inst();
    BukCombatPlugin plugin = BukCombatPlugin.inst();

    /**
     * Smoke bomb listener. Detects when a smoke bomb has hit the ground.
     */
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Snowball)) return;
        Snowball ball = (Snowball) event.getEntity();
        final Location loc = event.getEntity().getLocation();

        if(ball.hasMetadata("smoke-bomb")) {
            new SmokeBombEffect(loc);
        } else if(ball.hasMetadata("flash-bang")) {
            if(event.getEntity().getShooter() instanceof Player) {
                new FlashBangEffect(loc);
            }
        }
    }

    /**
     * Because we can't check what item was used to create a snowball, we instead
     * have to tag all snowballs around a player as being a smoke bomb.
     */
    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if(!ThrowableType.isThrowable(event.getItem())) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ThrowableType type = ThrowableType.getType(event.getItem());
        thrown.put(p, type);
        new BukkitRunnable() {
            @Override
            public void run() {
                thrown.remove(p);
            }
        }.runTaskLater(plugin, 0);
    }

    @EventHandler
    public void onThrown(ProjectileLaunchEvent event) {
        Projectile e = event.getEntity();
        if(!(e instanceof Snowball)) return;
        if(!thrown.containsKey(e.getShooter())) return;
        switch(thrown.get(e.getShooter())) {
            case SMOKE_BOMB:
                e.setMetadata("smoke-bomb", new FixedMetadataValue(plugin, true));
            case FLASH_BANG:
                e.setMetadata("flash-bang", new FixedMetadataValue(plugin, true));
        }
    }

}
