package is.rmob.supershulkers;

import is.rmob.supershulkers.duck.CustomEnchantmentHolder;
import is.rmob.supershulkers.mixin.ShulkerBoxBlockMixin;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class ShulkerUtil {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerUtil.class);

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


	public static boolean isShulkerBox(Item item) {
		return SHULKER_BOXES.contains(item);
	}


	public static int getInventorySize(int enlargeLevel) {
		return getRowCount(enlargeLevel) * 9;
	}


	public static int getInventorySize(Map<Enchantment, Integer> enchMap) {
		return getRowCount(enchMap) * 9;
	}


	public static int getInventorySize(ListTag enchTag) {
		return getInventorySize(EnchantmentHelper.fromTag(enchTag));
	}


	public static int getRowCount(int enlargeLevel) {
		return 3 + enlargeLevel;
	}


	public static int getRowCount(Map<Enchantment, Integer> enchMap) {
		return getRowCount(getEnlargeLevel(enchMap));
	}


	public static int getRowCount(ListTag enchTag) {
		return getRowCount(EnchantmentHelper.fromTag(enchTag));
	}


	public static int getEnlargeLevel(Map<Enchantment, Integer> enchMap) {
		return enchMap.getOrDefault(SuperShulkers.ENLARGE_ENCHANTMENT, 0);
	}


	public static int getEnlargeLevel(ListTag enchTag) {
		return getEnlargeLevel(EnchantmentHelper.fromTag(enchTag));
	}
}
