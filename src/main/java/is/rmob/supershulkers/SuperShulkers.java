package is.rmob.supershulkers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import carpet.CarpetServer;
import is.rmob.supershulkers.enchantment.EnlargeEnchantment;
import is.rmob.supershulkers.enchantment.RestockEnchantment;
import is.rmob.supershulkers.enchantment.VacuumEnchantment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SuperShulkers implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger(SuperShulkers.class);

	private SuperShulkersExtension carpetExtension;

	@Override
	public void onInitialize() {
		this.registerEnchantments();
		this.registerCarpetExtension();
	}

	private void registerEnchantments() {
		EnlargeEnchantment ENLARGE_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new Identifier("supershulkers", "enlarge"),
			new EnlargeEnchantment()
		);

		LOGGER.info("Registered enchantment {}", ENLARGE_ENCHANTMENT);

		RestockEnchantment RESTOCK_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new Identifier("supershulkers", "restock"),
			new RestockEnchantment()
		);

		LOGGER.info("Registered enchantment {}", RESTOCK_ENCHANTMENT);

		VacuumEnchantment VACUUM_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new Identifier("supershulkers", "vacuum"),
			new VacuumEnchantment()
		);

		LOGGER.info("Registered enchantment {}", VACUUM_ENCHANTMENT);
	}

	private void registerCarpetExtension() {
		this.carpetExtension = new SuperShulkersExtension();
		CarpetServer.manageExtension(this.carpetExtension);

		LOGGER.info("Registered scarpet extension {}", this.carpetExtension);
	}
}
