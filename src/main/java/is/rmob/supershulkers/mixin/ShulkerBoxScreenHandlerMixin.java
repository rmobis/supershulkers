package is.rmob.supershulkers.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.ShulkerBoxScreenHandler;

@Mixin(ShulkerBoxScreenHandler.class)
public class ShulkerBoxScreenHandlerMixin extends ScreenHandler {
	private static final Logger LOGGER = LogManager.getLogger(ShulkerBoxScreenHandlerMixin.class);

	private static final int INVENTORY_HARDCODED_ROWS = 3;
	private static final int INVENTORY_HARDCODED_SLOTS = INVENTORY_HARDCODED_ROWS * 9;
	private static final int PLAYER_INVENTORY_OLD_OFFSET = 84;
	private static final int PLAYER_HOTBAR_OLD_OFFSET = 142;

	@Unique
	private int rows = 6;

	@Shadow
	public boolean canUse(PlayerEntity pe) { return true; }

	public ShulkerBoxScreenHandlerMixin() {
		super(ScreenHandlerType.SHULKER_BOX, 0);
	}

	// @ModifyConstant(
	// 	method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;)V",
	// 	constant = @Constant(intValue = 27)
	// )
	// private int replaceConstructedInventorySize(int invSize) {
	// 	int newInvSize = this.rows * 9;

	// 	LOGGER.info("replacing constructed inventory size {} with {}", invSize, newInvSize);

	// 	return newInvSize;
	// }

	// @Inject(
	// 	method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;)V",
	// 	at = @At("RETURN")
	// )
	// private void onEarlyConstructor(int synchId, PlayerInventory pInv, CallbackInfo ci) {
	// 	LOGGER.info("inside early ctor");
	// // }

	// @ModifyVariable(
	// 	method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;)V",
	// 	at = @At(value = "FIELD", target = "inventory:Lnet/minecraft/inventory/Inventory;", opcode = Opcodes.PUTFIELD)
	// )
	// private Inventory doThingy(Inventory inv) {
	// 	LOGGER.info(inv);
	// 	return inv;
	// // }


	// @Redirect(
	// 	method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;)V",
	// 	at = @At(value = "NEW", target = "(I)Lnet/minecraft/inventory/SimpleInventory")
	// )
	// private SimpleInventory doThingy(int invSize) {
	// 	LOGGER.info(invSize);

	// 	return new SimpleInventory(this.rows * 9);
	// }


	// @ModifyConstant(
	// 	method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
	// 	constant = @Constant(intValue = 27)
	// )
	// private int doThingy(int invSize) {
	// 	LOGGER.info(invSize);

	// 	return this.rows * 9;
	// }

	@ModifyConstant(
		method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
		constant = @Constant(intValue = INVENTORY_HARDCODED_SLOTS)
	)
	private int replaceSlotCount(int slotCount) {
		int newSlotCount = this.rows * 9;

		LOGGER.info("replacing slot count {} with {}", slotCount, newSlotCount);

		return newSlotCount;
	}

	@ModifyConstant(
		method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
		constant = @Constant(intValue = INVENTORY_HARDCODED_ROWS, ordinal = 1)
	)
	private int replaceRowCount(int rowCount) {
		LOGGER.info("replacing row count {} with {}", rowCount, this.rows);

		return this.rows;
	}

	@ModifyConstant(
		method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
		constant = {
			@Constant(intValue = PLAYER_INVENTORY_OLD_OFFSET, ordinal = 0),
			@Constant(intValue = PLAYER_HOTBAR_OLD_OFFSET, ordinal = 0)
		}
	)
	private int calculateInventoryOffset(int invOffset) {
		int extraOffset = (this.rows - 3) * 18 + 1;
		int newOffset = invOffset + extraOffset;

		LOGGER.info("replacing inventory offset {} with {}", invOffset, newOffset);

		return newOffset;
	}

}
