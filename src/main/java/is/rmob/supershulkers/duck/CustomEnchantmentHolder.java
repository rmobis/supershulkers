package is.rmob.supershulkers.duck;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.ListTag;

import java.util.Map;

/**
 * We'll apply this interface to our BlockEntity mixin so that it can quack as something that has enchantments
 */
public interface CustomEnchantmentHolder {
	public Map<Enchantment, Integer> getEnchantments();

	public void setEnchantments(Map<Enchantment, Integer> enchMap);

	public void setEnchantments(ListTag enchTag);

	public ListTag getEnchantmentTag();
}
