package is.rmob.supershulkers.duck;

/**
 * We'll apply this interface to our ItemStack mixin so that it can quack as something that has persists enchantments
 * into the BlockEntityTag.
 */
public interface EnchantmentPersistent {
	void persistEnchantmentsIntoBET();
}
