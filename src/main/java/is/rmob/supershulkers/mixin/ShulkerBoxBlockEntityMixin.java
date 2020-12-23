package is.rmob.supershulkers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import is.rmob.supershulkers.ducks.CustomEnchantmentHolder;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin implements CustomEnchantmentHolder {
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
			System.out.println("fromTag extraction");
			System.out.println(this.enchantmentList);
		}
	}

	@Inject(
		method = "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
		at = @At("RETURN")
	)
	public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		if (!this.enchantmentList.isEmpty()) {
			tag.put("Enchantments", this.enchantmentList);
			System.out.println("toTag result");
			System.out.println(tag);
		}
	}

}
