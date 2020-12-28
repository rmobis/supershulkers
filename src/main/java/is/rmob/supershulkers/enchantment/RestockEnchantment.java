package is.rmob.supershulkers.enchantment;

import net.minecraft.enchantment.Enchantment;

/**
 * Allows shulker boxes to restock items from the hotbar when using, throwing, consuming etc.
 *
 * Note: this is only the information about the enchantment, actual functionality is implemented through the
 * superhulkers.sc scarpet script, which was mostly written by @gnembon.
 */
public class RestockEnchantment extends ShulkerBoxEnchantment {
	public RestockEnchantment() {
		super(Enchantment.Rarity.VERY_RARE);
	}
}
