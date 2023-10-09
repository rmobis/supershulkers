package is.rmob.supershulkers.mixin;

import is.rmob.supershulkers.ShulkerUtil;
import is.rmob.supershulkers.duck.CustomEnchantmentHolder;
import is.rmob.supershulkers.screen.EnlargeableShulkerBoxScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
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

	@Shadow
	private DefaultedList<ItemStack> inventory;


	/**
	 * Allows us to access enchantments in a map format.
	 */
	@Override
	public Map<Enchantment, Integer> getEnchantments() {
		return enchantmentMap;
	}


	@Override
	public void setEnchantments(Map<Enchantment, Integer> enchMap) {
		this.enchantmentMap = enchMap;

		updateInventorySize();
	}


	@Override
	public void setEnchantments(NbtList enchNbt) {
		setEnchantments(EnchantmentHelper.fromNbt(enchNbt));
	}

	/**
	 * Rebuild enchantment NBT from enchantment map.
	 */
	@Override
	public NbtList getEnchantmentNbt() {
		NbtList enchantmentTag = new NbtList();

		for (Map.Entry<Enchantment, Integer> ench : this.enchantmentMap.entrySet()) {
			NbtCompound enchTag = new NbtCompound();

			Enchantment enchRef = ench.getKey();
			short enchLvl = (short) ((byte) ((int) ench.getValue()));

			enchTag.putString("id", String.valueOf(Registry.ENCHANTMENT.getId(enchRef)));
			enchTag.putShort("lvl", enchLvl);

			enchantmentTag.add(enchTag);
		}

		return enchantmentTag;
	}


	@Unique
	private void updateInventorySize() {
		int newInvSize = ShulkerUtil.getInventorySize(this.getEnchantments());

		if (this.inventory.size() >= newInvSize) {
			return;
		}

		DefaultedList<ItemStack> newInv = DefaultedList.ofSize(newInvSize, ItemStack.EMPTY);

		for (int i = 0; i < this.inventory.size(); i++) {
			newInv.set(i, this.inventory.get(i));
		}

		this.inventory = newInv;
	}


	/**
	 * Recovers enchantment map from BET.
	 */
	@Inject(
		method = "readNbt(Lnet/minecraft/nbt/NbtCompound;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/ShulkerBoxBlockEntity;readInventoryNbt(Lnet/minecraft/nbt/NbtCompound;)V")
	)
	public void recoverEnchantmentsFromTag(NbtCompound tag, CallbackInfo ci) {
		LOGGER.trace("hijacking readNbt ({})", tag);

		if (tag.contains("Enchantments", 9)) {
			setEnchantments(tag.getList("Enchantments", 10));

			LOGGER.info("Recovered enchantment map {} from NBT", this.getEnchantments());
		}
	}


	/**
	 * Output enchantment data to BET.
	 */
	@Inject(method = "Lnet/minecraft/block/entity/BlockEntity;writeNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("RETURN"))
	public void putEnchantsIntoTag(NbtCompound tag, CallbackInfoReturnable<NbtCompound> cir) {
		LOGGER.trace("hijacking writeNbt ({})", tag);

		if (!this.getEnchantments().isEmpty()) {
			tag.put("Enchantments", this.getEnchantmentNbt());

			LOGGER.info("Output enchantment data {} into BET", tag.get("Enchantments"));
		}
	}


	/**
	 * Short circuit only to increase the amount of "available" slots based on enchantment.
	 */
	@Inject(method = "getAvailableSlots(Lnet/minecraft/util/math/Direction;)[I", at = @At("HEAD"), cancellable = true)
	public void getAvailableSlots(Direction side, CallbackInfoReturnable<int[]> ci) {
		LOGGER.trace("hijacking getAvailableSlots ({})", side);

		int invSize = ShulkerUtil.getInventorySize(this.getEnchantments());
		int[] availableSlots = IntStream.range(0, invSize).toArray();

		ci.setReturnValue(availableSlots);
	}


	/**
	 * Recreates the inventory list with the appropriate number of elements.
	 */
	@Inject(method = "<init>(Lnet/minecraft/util/DyeColor;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("TAIL"))
	public void onConstructRecreateInventory(DyeColor dye, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
		LOGGER.trace("hijacking ShulkerBoxEntity#constructor ({})", dye);

		int invSize = ShulkerUtil.getInventorySize(this.getEnchantments());

		this.inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);

		LOGGER.info("total inventory size {}",  invSize);
	}

	/**
	 * Short circuit to return our own customized ScreenHandler.
	 */
	@Inject(
		method = "createScreenHandler(ILnet/minecraft/entity/player/PlayerInventory;)Lnet/minecraft/screen/ScreenHandler;",
		at = @At("HEAD"),
		cancellable = true
	)
	public void provideCustomScreenHandler(int syncId, PlayerInventory playerInventory, CallbackInfoReturnable<ScreenHandler> ci) {
		LOGGER.trace("hijacking createScreenHandler ({}, {})", syncId, playerInventory);

		ScreenHandler scHandler = EnlargeableShulkerBoxScreenHandler.createFromEnchantments(
			syncId,
			playerInventory,
			(Inventory) this,
			this.getEnchantments()
		);

		ci.setReturnValue(scHandler);
	}

}
