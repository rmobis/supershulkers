package is.rmob.supershulkers.asm;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class EarlyRiser implements Runnable {

	/**
	 * We add a new entry to the EnchantmentTarget enum (through the intermediary name), using our
	 * ShulkerBoxEnchantmentTarget class, which is where the actual functionality is implemented.
	 */
	@Override
	public void run() {
		MappingResolver mapResolver = FabricLoader.getInstance().getMappingResolver();

		final String enchTargetClass = mapResolver.mapClassName("intermediary", "net.minecraft.class_1886");

		EnumAdder enchTargetAdder = ClassTinkerers.enumBuilder(enchTargetClass, new Class[0]);
		enchTargetAdder.addEnumSubclass("SHULKER_BOX", "is.rmob.supershulkers.asm.ShulkerBoxEnchantmentTarget");
		enchTargetAdder.build();
	}
}
