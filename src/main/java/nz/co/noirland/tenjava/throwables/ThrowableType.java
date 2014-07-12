package nz.co.noirland.tenjava.throwables;

import org.bukkit.inventory.ItemStack;

public enum ThrowableType {

    SMOKE_BOMB,
    FLASH_BANG;

    public static ThrowableType getType(ItemStack item) {
        if(new SmokeBomb().isSimilar(item)) return SMOKE_BOMB;
        if(new FlashBang().isSimilar(item)) return FLASH_BANG;
        return null;
    }

    public static boolean isThrowable(ItemStack item) {
        return getType(item) != null;
    }

}
