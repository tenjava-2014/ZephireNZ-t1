package nz.co.noirland.tenjava;

import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class BukCombatPlugin extends JavaPlugin {

    private static BukCombatPlugin inst;

    public static final String HARDNESS_NAME = ChatColor.GRAY + "Hardness I";

    public static BukCombatPlugin inst() {
        return inst;
    }

    @Override
    public void onEnable() {
        inst = this;
        getServer().getPluginManager().registerEvents(new CombatListener(), this);
        addRecipes();
    }

    public void addRecipes() {
        ShapelessRecipe smoke = new ShapelessRecipe(new SmokeBomb());
        smoke.addIngredient(1, Material.PAPER);
        smoke.addIngredient(3, Material.SULPHUR);
        smoke.addIngredient(2, Material.SUGAR);
        getServer().addRecipe(smoke);
    }

    public void smokeBombEffects(Location loc) {
        int r = 5;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        Packet packet = new PacketPlayOutWorldParticles("largesmoke", (float)x-r, (float)y-r, (float)z-r,
                (float)x+r, (float)y+r, (float)z+r, 4F, 100);
        for(Player p : getServer().getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

}
