package is.rmob.supershulkers.mixin;

import is.rmob.supershulkers.ShulkerUtil;
import is.rmob.supershulkers.duck.CustomEnchantmentHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;


@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin extends Block {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerBoxBlockMixin.class);

	public ShulkerBoxBlockMixin(Settings settings) { super(settings); }

	@Shadow
	public static ItemStack getItemStack(DyeColor c) { return null; }

	@Shadow
	public abstract DyeColor getColor();


	/**
	 * Modifies the dropped stack (usually a single shulker box item) to add back the enchantments. I believe this only
	 * applies to survival.
	 */
	@Inject(
		method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;",
		at = @At("RETURN")
	)
	public void onGetDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci) {
		for (ItemStack stack : ci.getReturnValue()) {
			if (ShulkerUtil.isShulkerBox(stack.getItem())) {
				BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);

				if (blockEntity instanceof ShulkerBoxBlockEntity) {
					ShulkerUtil.rebuildStackEnchantments(stack, (ShulkerBoxBlockEntity)blockEntity);
				}
			}
		}
	}


	/**
	 * Modifies the hardcoded number of slots that the tooltip checks for when building the list of items inside the
	 * shulker box. Without this, no items after the 27th slot would show on the tooltip and it could display as empty
	 * when in reality full of goodies.
	 */
	@Environment(EnvType.CLIENT)
	@ModifyConstant(
		method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/BlockView;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V",
		constant = @Constant(intValue = 27)
	)
	public int modifyTooltipSlots(int tooltipSlots) {
		// TODO: un-hardcode
		return ShulkerUtil.getInventorySize(3);
	}


	/**
	 * Pretty much the original method, with very few additions that modify the dropped stack (usually a single shulker
	 * box item) to add back the enchantments and also makes the shulker box drop when it is empty but has enchantments
	 * (should probably reevaluate that). I believe this only applies to creative.
	 *
	 * TODO: find a more elegant way to do this, possibly without overwriting the whole method
	 *
	 * @author rmobis
	 */
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
				ShulkerUtil.rebuildStackEnchantments(itemStack, shulkerBoxBlockEntity);

				if (!newBeTag.isEmpty()) {
					//noinspection ConstantConditions
					itemStack.putSubTag("BlockEntityTag", newBeTag);
				}

				if (shulkerBoxBlockEntity.hasCustomName()) {
					//noinspection ConstantConditions
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
