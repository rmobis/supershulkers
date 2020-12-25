package is.rmob.supershulkers.mixin;

import static is.rmob.supershulkers.asm.ShulkerBoxEnchantmentTarget.isShulkerBox;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import is.rmob.supershulkers.duck.CustomEnchantmentHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Mixin(ShulkerBoxBlock.class)
abstract class ShulkerBoxBlockMixin extends Block {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerBoxBlockMixin.class);

	public ShulkerBoxBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	public DyeColor getColor() { return DyeColor.BLACK; }

	@Shadow
	public static ItemStack getItemStack(DyeColor c) { return null; }

	@Unique
	private void rebuildStackEnchantments(ItemStack stack, ShulkerBoxBlockEntity sbEntity) {
		Map<Enchantment, Integer> enchantmentMap = ((CustomEnchantmentHolder) sbEntity).getEnchantments();

		// we couldn't use EnchantmentHelper.set because it skips the usual addEnchantment method,
		// which is what we have hooked for additional persistence
		for (Map.Entry<Enchantment, Integer> ench : enchantmentMap.entrySet()) {
			Enchantment enchRef = ench.getKey();
			int enchLvl = ench.getValue();

			stack.addEnchantment(enchRef, enchLvl);
			LOGGER.info("Rebuilt enchantment {} (lvl {}) onto {}", enchRef, enchLvl, stack);
		}
	}

	@Inject(
		method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;",
		at = @At("RETURN")
	)
	public void onGetDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci) {
		for (ItemStack stack : ci.getReturnValue()) {
			if (isShulkerBox(stack.getItem())) {
				BlockEntity blockEntity = (BlockEntity)builder.getNullable(LootContextParameters.BLOCK_ENTITY);
				if (blockEntity instanceof ShulkerBoxBlockEntity) {
					rebuildStackEnchantments(stack, (ShulkerBoxBlockEntity)blockEntity);
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@ModifyConstant(
		method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/BlockView;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V",
		constant = @Constant(intValue = 27)
	)
	public int modifyTooltipSlots(int tooltipSlots) {
		return 54;
	}

	@Overwrite
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ShulkerBoxBlockEntity) {
			ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;

			// added line
			Map<Enchantment, Integer> enchantmentMap = ((CustomEnchantmentHolder) shulkerBoxBlockEntity).getEnchantments();

			// we modify this if statement so that it triggers when the shulker box has enchantments too
			if (!world.isClient && player.isCreative() && (!shulkerBoxBlockEntity.isEmpty() || !enchantmentMap.isEmpty())) {
				ItemStack itemStack = getItemStack(this.getColor());
				CompoundTag newBeTag = shulkerBoxBlockEntity.serializeInventory(new CompoundTag());

				// added call
				rebuildStackEnchantments(itemStack, shulkerBoxBlockEntity);

				if (!newBeTag.isEmpty()) {
					itemStack.putSubTag("BlockEntityTag", newBeTag);
				}

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
}