package is.enchantments;

import net.minecraft.enchantment.Enchantment;

public class EnlargeEnchantment extends ShulkerBoxEnchantment {
	public EnlargeEnchantment() {
		super(Enchantment.Rarity.RARE);
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
