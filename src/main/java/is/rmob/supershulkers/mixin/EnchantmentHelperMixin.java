package is.rmob.supershulkers.mixin;

import is.rmob.supershulkers.duck.EnchantmentPersistent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	private static final Logger LOGGER = LogManager.getLogger(EnchantmentHelperMixin.class);

	@Inject(
		method = "set(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V",
		at = @At(
			value = "INVOKE",
			target ="Lnet/minecraft/item/ItemStack;setSubNbt(Ljava/lang/String;Lnet/minecraft/nbt/NbtElement;)V",
			shift = At.Shift.AFTER
		)
	)
	private static void onSetEnchantments(Map<Enchantment, Integer> enchMap, ItemStack stack, CallbackInfo ci) {
		LOGGER.trace("hijacking EnchantmentHelper#set ({}, {})", enchMap, stack);

		//noinspection ConstantConditions
		((EnchantmentPersistent) (Object) stack).persistEnchantmentsIntoBET();
	}
}
