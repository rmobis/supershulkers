package is.rmob.supershulkers.asm;

import is.rmob.supershulkers.ShulkerUtil;
import is.rmob.supershulkers.mixin.EnchantmentTargetMixin;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Using Fabric-ASM we will inject this class as a new EnchantmentTarget enum.
 */
public class ShulkerBoxEnchantmentTarget extends EnchantmentTargetMixin {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerBoxEnchantmentTarget.class);


	@Override
	public boolean isAcceptableItem(Item item) {
		LOGGER.trace("hijacking isAcceptableItem ({})", item);

		return ShulkerUtil.isShulkerBox(item);
	}
}
