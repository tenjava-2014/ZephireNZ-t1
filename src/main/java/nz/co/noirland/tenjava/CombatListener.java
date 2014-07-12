package nz.co.noirland.tenjava;

import nz.co.noirland.tenjava.flashbang.FlashBangEffect;
import nz.co.noirland.tenjava.smokebomb.SmokeBombEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CombatListener implements Listener {

    private final HashMap<ProjectileSource, ThrowableType> thrown= new HashMap<ProjectileSource, ThrowableType>();

    PluginConfig config = PluginConfig.inst();
    BukCombatPlugin plugin = BukCombatPlugin.inst();

    /**
     * EventHandler for splash potions. This will be pushed off to various things depending on what was splashed.
     */
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {

    }

    /**
     * Smoke bomb listener. Detects when a smoke bomb has hit the ground.
     */
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Snowball)) return;
        Snowball ball = (Snowball) event.getEntity();
        List<MetadataValue> meta = ball.getMetadata("smoke-bomb");
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
     * @param event
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

    /**
     * Called when player drinks a potion.
     */
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {

    }

    /**
     * EventHandler for 'Hardened' custom enchant.
     * Listens for when a player is hit by another player.
     */
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        // Don't care about anything that aren't players
        if(event.getDamager().getType() != EntityType.PLAYER) return;

        Player attacker = (Player) event.getDamager();
        LivingEntity target = (LivingEntity) event.getEntity();

        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        ItemStack weapon = attacker.getItemInHand();
        ItemStack[] armour = target.getEquipment().getArmorContents();
        short dur = 0;
        for(ItemStack item : armour) {
            if(!item.hasItemMeta()) continue;
            if(!item.getItemMeta().hasLore()) continue;

            List<String> lore = item.getItemMeta().getLore();
            if(lore.contains(BukCombatPlugin.HARDNESS_NAME)) {
                dur += config.getHardnessDamage();
            }
        }
        //TODO: Check for non-tool weapons
        weapon.setDurability((short) (weapon.getDurability() + dur));
        attacker.playSound(target.getLocation(), Sound.ANVIL_LAND, 0.2F, (float) (1.5D + Util.rand(0.5)));
    }

    /**
     * For managing potion creation.
     */
    @EventHandler
    public void onBrew(BrewEvent event) {

    }

    /**
     * DEBUG event for giving all zombies Hardness armour,
     * TODO: REMOVE
     */
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if(event.getEntityType() != EntityType.ZOMBIE) return;
        event.getEntity().setCanPickupItems(true);
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        List<String> lore = new ArrayList<String>();
        ItemMeta meta = item.getItemMeta();
        if(meta.hasLore()) {
            lore = item.getItemMeta().getLore();
        }
        lore.add(0, BukCombatPlugin.HARDNESS_NAME);
        meta.setLore(lore);
        item.setItemMeta(meta);

        event.getEntity().getEquipment().setChestplate(item);
    }

    @EventHandler
    public void onPrepCraft(PrepareItemCraftEvent event) {
        event.getRecipe();
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.getRecipe();
    }

    /**
     * Enchantment event for 'Hardness' custom enchant.
     * When enchanting, you have a chance of getting this enchant when using 30 levels.
     */
    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        if(event.getExpLevelCost() != 30) return; // Must be 30 levels to get

        if(!EnchantmentTarget.ARMOR.includes(item.getType())) return; // Ignore things that aren't armour
        if(!Util.roll(config.getHardnessChance())) return;

        List<String> lore = new ArrayList<String>();
        ItemMeta meta = item.getItemMeta();
        if(meta.hasLore()) {
            lore = item.getItemMeta().getLore();
        }
        lore.add(0, BukCombatPlugin.HARDNESS_NAME);
        meta.setLore(lore);
        item.setItemMeta(meta);
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
