package is.rmob.supershulkers.mixin;

import is.rmob.supershulkers.ShulkerUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
	private static final Logger LOGGER = LogManager.getLogger(BlockItemMixin.class);

	public BlockItemMixin(Settings settings) {
		super(settings);
	}


	/**
	 * We override the isEnchantable method so we can hijack it and trick the game into allowing us to enchant shulker
	 * boxes through the enchanting table.
	 */
	@Override
	public boolean isEnchantable(ItemStack stack) {
		LOGGER.trace("hijacking isEnchantable ({})", stack);

		if (ShulkerUtil.isShulkerBox(stack.getItem())) {

			// While shulker boxes can't usually be stacked, we double check this
			// here to avoid conflicts with other mods
			return stack.getCount() == 1;
		}

		return super.isEnchantable(stack);
	}


	/**
	 * We override the getEnchantability method so we can hijack it and trick the game into allowing us to enchant
	 * shulker boxes through the enchanting table.
	 */
	@Override
	public int getEnchantability() {
		LOGGER.trace("hijacking getEnchantability");

		if (ShulkerUtil.isShulkerBox(this)) {

			// Should probably investigate what exactly values > 1 mean, but it works
			return 1;
		}

		return super.getEnchantability();
	}
}
