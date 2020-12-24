package is.rmob.supershulkers;

import is.enchantments.EnlargeEnchantment;
import is.enchantments.RestockEnchantment;
import is.enchantments.VacuumEnchantment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SuperShulkers implements ModInitializer {
	@Override
	public void onInitialize() {
		Registry.register(
			Registry.ENCHANTMENT,
			new Identifier("supershulkers", "enlarge"),
			new EnlargeEnchantment()
		);

		Registry.register(
			Registry.ENCHANTMENT,
			new Identifier("supershulkers", "restock"),
			new RestockEnchantment()
		);

		Registry.register(
			Registry.ENCHANTMENT,
			new Identifier("supershulkers", "vacuum"),
			new VacuumEnchantment()
		);
	}
}
