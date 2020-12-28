package is.rmob.supershulkers.mixin;

import is.rmob.supershulkers.ShulkerUtil;
import is.rmob.supershulkers.duck.CustomEnchantmentHolder;
import is.rmob.supershulkers.minecraft.screen.EnlargeableShulkerBoxScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin implements CustomEnchantmentHolder {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerBoxBlockEntityMixin.class);

	@Unique
	private Map<Enchantment, Integer> enchantmentMap = new HashMap<>();

	@Override
	public abstract Map<Enchantment, Integer> getEnchantments();

	@Shadow
	private DefaultedList<ItemStack> inventory;


	/**
	 * Rebuild enchantment NBT from enchantment map.
	 */
	@Override
	public ListTag getEnchantmentTag() {
		ListTag enchantmentTag = new ListTag();

		for (Map.Entry<Enchantment, Integer> ench : this.enchantmentMap.entrySet()) {
			CompoundTag enchTag = new CompoundTag();

			Enchantment enchRef = ench.getKey();
			short enchLvl = (short) ((byte) ((int) ench.getValue()));

			enchTag.putString("id", String.valueOf(Registry.ENCHANTMENT.getId(enchRef)));
			enchTag.putShort("lvl", enchLvl);

			enchantmentTag.add(enchTag);
		}

		return enchantmentTag;
	}


	/**
	 * Recovers enchantment map from BET.
	 */
	@Inject(method = "fromTag(Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
	public void recoverEnchantmentsFromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
		LOGGER.trace("hijacking fromTag ({}, {})", state, tag);

		if (tag.contains("Enchantments", 9)) {
			this.enchantmentMap = EnchantmentHelper.fromTag(tag.getList("Enchantments", 10));

			LOGGER.info("Recovered enchantment map {} from NBT", this.getEnchantments());
		}
	}


	/**
	 * Output enchantment data to BET.
	 */
	@Inject(method = "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"))
	public void putEnchantsIntoTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		LOGGER.trace("hijacking toTag ({})", tag);

		if (!this.getEnchantments().isEmpty()) {
			tag.put("Enchantments", this.getEnchantmentTag());

			LOGGER.info("Output enchantment data {} into BET", tag.get("Enchantments"));
		}
	}


	/**
	 * Very simple overwrite, only to increase the amount of slots on the final field AVAILABLE_SLOTS. Not exactly sure
	 * what this does or why it is needed, but it seems to help make things work.
	 *
	 * TODO: verify possibility of using accessors (?)
	 *
	 * @author rmobis
	 */
	@Overwrite
	public int[] getAvailableSlots(Direction side) {
		LOGGER.trace("hijacking getAvailableSlots ({})", side);

		int invSize = ShulkerUtil.getInventorySize(this.getEnchantments());

		return IntStream.range(0, invSize).toArray();
	}


	/**
	 * Recreates the inventory list with the appropriate number of elements.
	 */
	@Inject(method = "<init>(Lnet/minecraft/util/DyeColor;)V", at = @At("TAIL"))
	public void onConstructRecreateInventory(DyeColor dye, CallbackInfo ci) {
		LOGGER.trace("hijacking ShulkerBoxEntity#constructor ({})", dye);

		int invSize = ShulkerUtil.getInventorySize(this.getEnchantments());

		this.inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);

		LOGGER.info("total inventory size {}",  invSize);
	}

	/**
	 * We overwrite only so we can return our own customized ScreenHandler.
	 *
	 * TODO: convert this to a short circuit injector
	 *
	 * @author rmobis
	 */
	@Overwrite
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		LOGGER.trace("hijacking createScreenHandler ({}, {})", syncId, playerInventory);

		return EnlargeableShulkerBoxScreenHandler.createFromEnchantments(
			syncId,
			playerInventory,
			(ShulkerBoxBlockEntity) (Object) this, this.getEnchantments()
		);
	}

}
