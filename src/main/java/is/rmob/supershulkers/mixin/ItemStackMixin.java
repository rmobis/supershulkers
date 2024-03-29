package is.rmob.supershulkers.mixin;

import is.rmob.supershulkers.ShulkerUtil;
import is.rmob.supershulkers.duck.EnchantmentPersistent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements EnchantmentPersistent {
	private static final Logger LOGGER = LogManager.getLogger(ItemStackMixin.class);

	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract NbtCompound getSubNbt(String key);

	@Shadow
	public abstract void setSubNbt(String key, NbtElement tag);

	@Shadow
	public abstract NbtList getEnchantments();


	/**
	 * We inject after the return of addEnchantment so we can persist enchantment info into BET.
	 */
	@Inject(method= "addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V", at=@At("RETURN"))
	public void addEnchantment(Enchantment enchantment, int level, CallbackInfo ci) {
		LOGGER.trace("hijacking addEnchantment ({}, {})", enchantment, level);

		if (ShulkerUtil.isShulkerBox(this.getItem())) {
			this.persistEnchantmentsIntoBET();
		}
	}


	/**
	 * Copies the enchantment tags into the BlockEntityTag. We can then retrieve this information when the block is broken.
	 */
	@Override
	public void persistEnchantmentsIntoBET() {
		NbtCompound beTag = getSubNbt("BlockEntityTag");

		if (beTag == null) {
			beTag = new NbtCompound();
		}

		beTag.put("Enchantments", this.getEnchantments());
		this.setSubNbt("BlockEntityTag", beTag);

		LOGGER.debug("Persisted enchantment data {} into BET {}", this.getEnchantments(), beTag);
	}
}
