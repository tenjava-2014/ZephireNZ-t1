package nz.co.noirland.tenjava;

import nz.co.noirland.tenjava.flashbang.FlashBang;
import nz.co.noirland.tenjava.smokebomb.SmokeBomb;
import org.bukkit.inventory.ItemStack;

public enum ThrowableType {

    SMOKE_BOMB,
    FLASH_BANG;

    public static ThrowableType getType(ItemStack item) {
        if(item.isSimilar(new SmokeBomb())) return SMOKE_BOMB;
        if(item.isSimilar(new FlashBang())) return FLASH_BANG;
        return null;
    }

    public static boolean isThrowable(ItemStack item) {
        return getType(item) != null;
    }

}
