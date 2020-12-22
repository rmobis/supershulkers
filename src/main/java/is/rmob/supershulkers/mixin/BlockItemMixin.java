package is.rmob.supershulkers.mixin;

import org.spongepowered.asm.mixin.Mixin;

import is.rmob.supershulkers.asm.ShulkerBoxEnchantmentTarget;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
	public BlockItemMixin(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		if (ShulkerBoxEnchantmentTarget.SHULKER_BOXES.contains(this)) {
			// While shulker boxes can't usually be stacked, we double check this
			// here to avoid conflicts with other mods
			return stack.getCount() == 1;
		}

		return super.isEnchantable(stack);
	}

	@Override
	public int getEnchantability() {
		if (ShulkerBoxEnchantmentTarget.SHULKER_BOXES.contains(this)) {
			// Should probably investigate what exactly values > 1 mean, but it works
			return 1;
		}

		return super.getEnchantability();
	}
}
