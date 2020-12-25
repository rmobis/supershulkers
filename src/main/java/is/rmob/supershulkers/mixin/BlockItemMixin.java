package is.rmob.supershulkers.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;

import is.rmob.supershulkers.asm.ShulkerBoxEnchantmentTarget;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
	private static final Logger LOGGER = LogManager.getLogger(BlockItemMixin.class);

	public BlockItemMixin(Settings settings) {
		super(settings);
	}


	/**
	 * We override the isEnchantable method so we can hijack it and trick the game into allowing
	 * us to enchant shulker boxes.
	 *
	 * @param stack
	 * @return boolean
	 */
	@Override
	public boolean isEnchantable(ItemStack stack) {
		if (ShulkerBoxEnchantmentTarget.SHULKER_BOXES.contains(this)) {
			// While shulker boxes can't usually be stacked, we double check this
			// here to avoid conflicts with other mods
			return stack.getCount() == 1;
		}

		return super.isEnchantable(stack);
	}


	/**
	 * @return int
	 */
	@Override
	public int getEnchantability() {
		if (ShulkerBoxEnchantmentTarget.SHULKER_BOXES.contains(this)) {
			// Should probably investigate what exactly values > 1 mean, but it works
			return 1;
		}

		return super.getEnchantability();
	}
}
