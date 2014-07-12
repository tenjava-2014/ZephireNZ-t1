package nz.co.noirland.tenjava.throwables;

import nz.co.noirland.tenjava.BukCombatPlugin;
import nz.co.noirland.tenjava.Util;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class SmokeBombEffect extends BukkitRunnable {

    private int count = 10;
    private Location loc;

    public SmokeBombEffect(Location loc) {
        this.loc = loc;
        runTaskTimer(BukCombatPlugin.inst(), 0, 10L);
    }

    @Override
    public void run() {
        if(count-- <= 0) {
            this.cancel();
            return;
        }
        Util.smokeEffect(loc);
    }
}
