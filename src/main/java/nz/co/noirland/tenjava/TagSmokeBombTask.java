package nz.co.noirland.tenjava;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class TagSmokeBombTask extends BukkitRunnable {

    private Player player;
    private BukCombatPlugin plugin = BukCombatPlugin.inst();

    public TagSmokeBombTask(Player player) {
        this.player = player;
        runTaskLater(plugin, 0);
    }

    @Override
    public void run() {
        int radius = 5 * 5;
        for(Entity e : player.getWorld().getEntities()) {
            if(!(e instanceof Snowball)) continue;
            if(((Snowball) e).getShooter() != player) continue;
            if(e.getLocation().distanceSquared(player.getLocation()) > radius) continue;
            e.setMetadata("smoke-bomb", new FixedMetadataValue(plugin, true));
        }
    }
}
