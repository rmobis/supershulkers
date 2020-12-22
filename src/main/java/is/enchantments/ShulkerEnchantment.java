package is.enchantments;

import com.chocohead.mm.api.ClassTinkerers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ShulkerEnchantment extends Enchantment {
	private static final EnchantmentTarget SHULKER_TARGET = ClassTinkerers.getEnum(EnchantmentTarget.class, "SHULKER_BOX");

	public ShulkerEnchantment() {
		super(Enchantment.Rarity.COMMON, SHULKER_TARGET, new EquipmentSlot[]{});
	}

	@Override
	public int getMinPower(int level) {
		return (level - 1) * 10;
	}

	@Override
	public int getMaxPower(int level) {
		return 999;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
