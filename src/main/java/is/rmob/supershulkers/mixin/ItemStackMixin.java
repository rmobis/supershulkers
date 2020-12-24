package is.rmob.supershulkers.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static is.rmob.supershulkers.asm.ShulkerBoxEnchantmentTarget.isShulkerBox;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	private static final Logger LOGGER = LogManager.getLogger(ItemStackMixin.class);

	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract CompoundTag getSubTag(String key);

	@Shadow
	public abstract void putSubTag(String key, Tag tag);

	@Shadow
	public abstract CompoundTag getTag();

	@Inject(
		method= "addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V",
		at=@At("RETURN")
	)
	public void addEnchantment(Enchantment enchantment, int level, CallbackInfo ci) {
		if (isShulkerBox(getItem())) {
			// We copy enchantment tags to the BlockEntityTag so that it is saved to the block
			// entity when the shulker box is placed; we can then retrieve it when it is broken
			CompoundTag beTag = getSubTag("BlockEntityTag");

			if (beTag == null) {
				beTag = new CompoundTag();
			}

			beTag.put("Enchantments", this.getTag().get("Enchantments"));
			putSubTag("BlockEntityTag", beTag);

			LOGGER.debug("Persisted enchantment data {} into BET", this.getTag().get("Enchantments"));
		}
	}
}
