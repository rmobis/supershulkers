package is.rmob.supershulkers.mixin;

import static is.rmob.supershulkers.asm.ShulkerBoxEnchantmentTarget.isShulkerBox;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

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

	private void rebuildStackEnchantments(ItemStack stack, ShulkerBoxBlockEntity sbEntity) {
		ListTag enchantmentTags = ((CustomEnchantmentHolder) sbEntity).getEnchantments();

		for (int i = 0; i < enchantmentTags.size(); ++i) {
			CompoundTag ench = enchantmentTags.getCompound(i);

			String enchId = ench.getString("id");
			int enchLvl = ench.getShort("lvl");

			stack.addEnchantment(Registry.ENCHANTMENT.get(new Identifier(enchId)), enchLvl);

			LOGGER.debug("Rebuilt enchantment {} (lvl {}) onto {}", enchId, enchLvl, stack);
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

	@Overwrite
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ShulkerBoxBlockEntity) {
			ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;

			// added line
			ListTag enchantmentTags = ((CustomEnchantmentHolder) shulkerBoxBlockEntity).getEnchantments();

			// we modify this if statement so that it triggers when the shulker box has enchantments too
			if (!world.isClient && player.isCreative() && (!shulkerBoxBlockEntity.isEmpty() || !enchantmentTags.isEmpty())) {
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