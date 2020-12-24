package is.rmob.supershulkers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import is.rmob.supershulkers.enchantments.EnlargeEnchantment;
import is.rmob.supershulkers.enchantments.RestockEnchantment;
import is.rmob.supershulkers.enchantments.VacuumEnchantment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SuperShulkers implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger(SuperShulkers.class);

	@Override
	public void onInitialize() {
		this.registerEnchantments();
		this.registerScarpetApp();
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

	private void registerScarpetApp() {
		BundledModule superShulkersApp = BundledModule.fromPath("assets/supershulkers/scripts/", "supershulkers", false);
		CarpetScriptServer.registerBuiltInScript(superShulkersApp);

		LOGGER.info("Registered scarpet app {}", superShulkersApp);
	}
}
