package is.rmob.supershulkers.asm;

import java.util.Arrays;
import java.util.List;

import is.rmob.supershulkers.mixin.EnchantmentTargetMixin;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class ShulkerBoxEnchantmentTarget extends EnchantmentTargetMixin {
	public static final List<Item> SHULKER_BOXES = Arrays.asList(
		Items.BLACK_SHULKER_BOX,
		Items.BLUE_SHULKER_BOX,
		Items.BROWN_SHULKER_BOX,
		Items.CYAN_SHULKER_BOX,
		Items.GRAY_SHULKER_BOX,
		Items.GREEN_SHULKER_BOX,
		Items.LIGHT_BLUE_SHULKER_BOX,
		Items.LIGHT_GRAY_SHULKER_BOX,
		Items.LIME_SHULKER_BOX,
		Items.MAGENTA_SHULKER_BOX,
		Items.ORANGE_SHULKER_BOX,
		Items.PINK_SHULKER_BOX,
		Items.PURPLE_SHULKER_BOX,
		Items.RED_SHULKER_BOX,
		Items.SHULKER_BOX,
		Items.WHITE_SHULKER_BOX,
		Items.YELLOW_SHULKER_BOX
	);

	@Override
	public boolean isAcceptableItem(Item item) {
		return SHULKER_BOXES.contains(item);
	}
}
