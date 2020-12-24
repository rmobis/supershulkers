package is.rmob.supershulkers.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import is.rmob.supershulkers.ducks.CustomEnchantmentHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin implements CustomEnchantmentHolder {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerBoxBlockEntityMixin.class);

	@Unique
	private ListTag enchantmentList = new ListTag();

	@Override
	public ListTag getEnchantments() {
		return enchantmentList;
	}

	@Inject(
		method = "fromTag(Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/CompoundTag;)V",
		at = @At("RETURN")
	)
	public void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
		if (tag.contains("Enchantments", 9)) {
			this.enchantmentList = tag.getList("Enchantments", 10);

			LOGGER.debug("Recovered enchantment data {} from NBT", this.enchantmentList);
		}
	}

	@Inject(
		method = "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
		at = @At("RETURN")
	)
	public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		if (!this.enchantmentList.isEmpty()) {
			tag.put("Enchantments", this.enchantmentList);

			LOGGER.debug("Output enchantment data {} into NBT", tag.get("Enchantments"));
		}
	}

}
