package is.rmob.supershulkers.enchantment;

import net.minecraft.enchantment.Enchantment;

/**
 * Allows shulker boxes to absorb items directly into its inventory when the player picks them up.
 *
 * Note: this is only the information about the enchantment, actual functionality is implemented through the
 * superhulkers.sc scarpet script, which was mostly written by @gnembon.
 */
public class VacuumEnchantment extends ShulkerBoxEnchantment {
	public VacuumEnchantment() {
		super(Enchantment.Rarity.VERY_RARE);
	}
}
