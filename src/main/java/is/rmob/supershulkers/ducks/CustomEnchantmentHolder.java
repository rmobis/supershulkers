package is.rmob.supershulkers.ducks;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.ListTag;

public interface CustomEnchantmentHolder {
	public Map<Enchantment, Integer> getEnchantments();

	public ListTag getEnchantmentTag();
}
