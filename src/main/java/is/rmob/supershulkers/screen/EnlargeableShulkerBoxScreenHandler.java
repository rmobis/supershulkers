package is.rmob.supershulkers.screen;

import is.rmob.supershulkers.SuperShulkers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import net.minecraft.screen.slot.Slot;

import java.util.Map;

public class EnlargeableShulkerBoxScreenHandler extends GenericContainerScreenHandler {
	public static EnlargeableShulkerBoxScreenHandler createFromEnchantments(int syncId, PlayerInventory playerInventory, Inventory inventory, Map<Enchantment, Integer> enchMap) {
		int enlargeLevel = enchMap.getOrDefault(SuperShulkers.ENLARGE_ENCHANTMENT, 0);
		int rows = Math.min(enlargeLevel, 3) + 3;

		ScreenHandlerType<GenericContainerScreenHandler> hType;
		switch (rows) {
			case 6:
				hType = ScreenHandlerType.GENERIC_9X6;
				break;

			case 5:
				hType = ScreenHandlerType.GENERIC_9X5;
				break;

			case 4:
				hType = ScreenHandlerType.GENERIC_9X4;
				break;

			case 3:
			default:
				hType = ScreenHandlerType.GENERIC_9X3;
				break;
		}

		return new EnlargeableShulkerBoxScreenHandler(hType, syncId, playerInventory, inventory, rows);
	}

	public EnlargeableShulkerBoxScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, int rows) {
		this(type, syncId, playerInventory, new SimpleInventory(rows * 9), rows);
	}

	public EnlargeableShulkerBoxScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
		super(type, syncId, playerInventory, inventory, rows);

		this.convertSlots();
	}

	private void convertSlots() {
		for	(int i = 0; i < this.getRows() * 9; i++) {
			this.slots.set(i, convertSlot(this.getSlot(i), i));
		}
	}

	private ShulkerBoxSlot convertSlot(Slot oldSlot, int i) {
		ShulkerBoxSlot newSlot = new ShulkerBoxSlot(oldSlot.inventory, i, oldSlot.x, oldSlot.y);
		newSlot.id = oldSlot.id;

		return newSlot;
	}
}
