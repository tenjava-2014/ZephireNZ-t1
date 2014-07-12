package nz.co.noirland.tenjava.flashbang;

import nz.co.noirland.tenjava.BukCombatPlugin;
import nz.co.noirland.tenjava.Util;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlashBangEffect {

    public FlashBangEffect(Location bang) {
        BukCombatPlugin.inst().smokeEffect(bang);
        for(Entity e : Util.entitiesInRadius(bang, 7)) {
            if(!(e instanceof LivingEntity)) continue;
            LivingEntity le = (LivingEntity) e;
            le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10, 10, true));
            le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, 10, true));
        }
    }
}
