package is.rmob.supershulkers.mixin;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * This only exists so Fabric-ASM can instantiate our ShulkerBoxEnchantmentTarget.
 */
@Mixin(EnchantmentTarget.class)
public abstract class EnchantmentTargetMixin {
	@Shadow
	public abstract boolean isAcceptableItem(Item item);
}
