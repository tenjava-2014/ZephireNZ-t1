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
        for(Entity e : player.getNearbyEntities(2,2,2)) {
            if(!(e instanceof Snowball)) continue;
            e.setMetadata("smoke-bomb", new FixedMetadataValue(plugin, true));
        }
    }
}
