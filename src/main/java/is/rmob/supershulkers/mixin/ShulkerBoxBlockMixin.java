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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;


@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin extends Block {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerBoxBlockMixin.class);

	public ShulkerBoxBlockMixin(Settings settings) { super(settings); }

	@Shadow
	public static ItemStack getItemStack(DyeColor c) { return null; }

	@Shadow
	public abstract DyeColor getColor();


	/**prob
	 * Modifies the dropped stack (usually a single shulker box item) to add back the enchantments. I believe this only
	 * applies to survival.
	 */
	@Inject(
		method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;",
		at = @At("RETURN")
	)
	public void onGetDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci) {
		LOGGER.trace("hijacking onGetDroppedStacks ({}, {})", state, builder);

		for (ItemStack stack : ci.getReturnValue()) {
			if (ShulkerUtil.isShulkerBox(stack.getItem())) {
				BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);

				if (blockEntity instanceof CustomEnchantmentHolder) {
					CustomEnchantmentHolder enchHolder = (CustomEnchantmentHolder) blockEntity;

					EnchantmentHelper.set(enchHolder.getEnchantments(), stack);
				}
			}
		}
	}


	/**
	 * Modifies the dropped stack (usually a single shulker box item) to add back the enchantments. I believe this only
	 * applies to creative.
	 */
	@Inject(
		method = "onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void rebuildEnchantmentsOnBreak(
		World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci,
		BlockEntity __, ShulkerBoxBlockEntity ___, ItemStack stack // local captures
	) {
		LOGGER.trace("hijacking onBreak ({}, {}, {}, {})", world, pos, state, player);

		BlockEntity sbEntity = world.getBlockEntity(pos);
		if (sbEntity instanceof CustomEnchantmentHolder) {
			CustomEnchantmentHolder enchHolder = (CustomEnchantmentHolder) sbEntity;

			EnchantmentHelper.set(enchHolder.getEnchantments(), stack);
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
	public int modifyTooltipSlots(int tooltipSlots, ItemStack stack) {
		LOGGER.trace("hijacking appendTooltip ({})", tooltipSlots);

		return ShulkerUtil.getInventorySize(stack.getEnchantments());
	}
}
