package is.rmob.supershulkers.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import is.rmob.supershulkers.ducks.CustomEnchantmentHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ShulkerBoxBlock.class)
abstract class ShulkerBoxBlockMixin extends Block {
	
	public ShulkerBoxBlockMixin(Settings settings) {
		super(settings);
	}
	
	@Shadow
	public DyeColor getColor() { return DyeColor.BLACK; }
	
	@Shadow
	public static ItemStack getItemStack(DyeColor c) { return null; }

	@Inject(
		method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;",
		at = @At("HEAD")
	)
	public void onGetDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci) {
		System.out.println("onGetDroppedStacks");
	}


	@Overwrite
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ShulkerBoxBlockEntity) {
			ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
			ListTag enchantmentTags = ((CustomEnchantmentHolder) shulkerBoxBlockEntity).getEnchantments();
			if (!world.isClient && player.isCreative() && (!shulkerBoxBlockEntity.isEmpty() || !enchantmentTags.isEmpty())) {
				ItemStack itemStack = getItemStack(this.getColor());
				CompoundTag newBeTag = shulkerBoxBlockEntity.serializeInventory(new CompoundTag());

				System.out.println("enchantmentTags");
				System.out.println(enchantmentTags);
				if (!enchantmentTags.isEmpty()) {
					newBeTag.put("Enchantments", enchantmentTags);
					itemStack.putSubTag("Enchantments", enchantmentTags);
				}

				if (!newBeTag.isEmpty()) {
					itemStack.putSubTag("BlockEntityTag", newBeTag);
				}
				System.out.println("itemStack tags");
				System.out.println(itemStack.getTag());
				
				if (shulkerBoxBlockEntity.hasCustomName()) {
					itemStack.setCustomName(shulkerBoxBlockEntity.getCustomName());
				}
				
				ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemStack);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
			} else {
				shulkerBoxBlockEntity.checkLootInteraction(player);
			}
		}
		
		super.onBreak(world, pos, state, player);
	}
	
	// @Inject(
	// 	method= "onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V",
	// 	at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/entity/ShulkerBoxBlockEntity;serializeInventory(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"),
	// 	locals = LocalCapture.CAPTURE_FAILEXCEPTION
	// )
	// public void onOnBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci, BlockEntity _, ShulkerBoxBlockEntity sbEntity, ItemStack itemStack, CompoundTag newBeTag) {
	// 	ListTag enchantmentTags = ((CustomEnchantmentHolder) sbEntity).getEnchantments();
	// 	if (!enchantmentTags.isEmpty()) {
	// 		newBeTag.put("Enchantments", enchantmentTags);
	// 		itemStack.putSubTag("Enchantments", enchantmentTags);

	// 		System.out.println("enchantmentTags");
	// 		System.out.println(enchantmentTags);
	// 	}
	// }
}