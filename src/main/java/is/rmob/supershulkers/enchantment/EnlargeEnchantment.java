package is.rmob.supershulkers.enchantment;

import net.minecraft.enchantment.Enchantment;

/**
 * Increases shulker box inventory size by 1 row per level.
 *
 * Note: this is only the information about the enchantment, actual functionality is implemented through mixins.
 */
public class EnlargeEnchantment extends ShulkerBoxEnchantment {
	public EnlargeEnchantment() {
		super(Enchantment.Rarity.RARE);
	}


	@Override
	public int getMaxLevel() {
		return 3;
	}
}
