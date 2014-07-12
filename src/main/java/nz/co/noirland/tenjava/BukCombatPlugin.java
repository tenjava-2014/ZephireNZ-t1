package nz.co.noirland.tenjava;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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

}
