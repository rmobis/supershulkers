package is.rmob.supershulkers;

import is.enchantments.ShulkerEnchantment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SuperShulkers implements ModInitializer {
	@Override
	public void onInitialize() {
		System.out.println("Registering test shulker enchantment");

		Registry.register(
			Registry.ENCHANTMENT,
			new Identifier("supershulkers", "test"),
			new ShulkerEnchantment()
		);
	}
}
