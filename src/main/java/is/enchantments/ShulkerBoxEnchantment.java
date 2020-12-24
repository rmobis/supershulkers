package is.enchantments;

import com.chocohead.mm.api.ClassTinkerers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public abstract class ShulkerBoxEnchantment extends Enchantment {
	// This retrives the custom enum entry we created in our early riser
	private static final EnchantmentTarget SHULKER_TARGET = ClassTinkerers.getEnum(EnchantmentTarget.class,
			"SHULKER_BOX");

	public ShulkerBoxEnchantment(Enchantment.Rarity rarity) {
		super(rarity, SHULKER_TARGET, new EquipmentSlot[] {});
	}

	@Override
	public int getMinPower(int level) {
		return (level - 1) * 10 + 1;
	}

	@Override
	public int getMaxPower(int level) {
		return Integer.MAX_VALUE;
	}
}
