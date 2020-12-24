package is.rmob.supershulkers.mixin;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import is.rmob.supershulkers.duck.CustomEnchantmentHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.registry.Registry;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin implements CustomEnchantmentHolder {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerBoxBlockEntityMixin.class);

	@Unique
	private Map<Enchantment, Integer> enchantmentMap = new HashMap<Enchantment, Integer>();

	@Override
	public Map<Enchantment, Integer> getEnchantments() {
		return enchantmentMap;
	}

	@Override
	public ListTag getEnchantmentTag() {
		ListTag enchantmentTag = new ListTag();

		for (Map.Entry<Enchantment, Integer> ench : this.enchantmentMap.entrySet()) {
			CompoundTag enchTag = new CompoundTag();

			Enchantment enchRef = ench.getKey();
			short enchLvl = (short)((byte)((int)ench.getValue()));

			enchTag.putString("id", String.valueOf(Registry.ENCHANTMENT.getId(enchRef)));
			enchTag.putShort("lvl", enchLvl);

			enchantmentTag.add(enchTag);
		}

		return enchantmentTag;
	}

	@Inject(
		method = "fromTag(Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/CompoundTag;)V",
		at = @At("RETURN")
	)
	public void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
		if (tag.contains("Enchantments", 9)) {
			this.enchantmentMap = EnchantmentHelper.fromTag(tag.getList("Enchantments", 10));

			LOGGER.info("Recovered enchantment map {} from NBT", this.getEnchantments());
		}
	}

	@Inject(
		method = "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
		at = @At("RETURN")
	)
	public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		if (!this.getEnchantments().isEmpty()) {
			tag.put("Enchantments", this.getEnchantmentTag());

			LOGGER.info("Output enchantment data {} into NBT", tag.get("Enchantments"));
		}
	}

}
