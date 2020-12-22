package is.rmob.supershulkers.asm;

import com.chocohead.mm.api.ClassTinkerers;

import net.fabricmc.loader.api.FabricLoader;

public class EarlyRiser implements Runnable {
	@Override
	public void run() {
		System.out.println("EARLY RISING");


		final String enchantmentTargetClass = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_1886");
		ClassTinkerers.enumBuilder(enchantmentTargetClass, new Class[0]).addEnumSubclass("SHULKER_BOX", "is.rmob.supershulkers.asm.ShulkerBoxEnchantmentTarget").build();
	}
}
