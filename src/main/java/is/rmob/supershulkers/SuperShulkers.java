package is.rmob.supershulkers;

import is.rmob.supershulkers.compat.SuperShulkersCarpetExtension;
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

	public static final String MODID = "supershulkers";

	public static EnlargeEnchantment ENLARGE_ENCHANTMENT;
	public static RestockEnchantment RESTOCK_ENCHANTMENT;
	public static VacuumEnchantment VACUUM_ENCHANTMENT;


	@Override
	public void onInitialize() {
		this.registerEnchantments();
		this.registerCarpetExtension();
	}


	private void registerEnchantments() {
		SuperShulkers.ENLARGE_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new Identifier(MODID, "enlarge"),
			new EnlargeEnchantment()
		);

		LOGGER.info("Registered enchantment {}", ENLARGE_ENCHANTMENT);

		SuperShulkers.RESTOCK_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new Identifier(MODID, "restock"),
			new RestockEnchantment()
		);

		LOGGER.info("Registered enchantment {}", RESTOCK_ENCHANTMENT);

		SuperShulkers.VACUUM_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new Identifier(MODID, "vacuum"),
			new VacuumEnchantment()
		);

		LOGGER.info("Registered enchantment {}", VACUUM_ENCHANTMENT);
	}


	private void registerCarpetExtension() {
		SuperShulkersCarpetExtension carpetExtension = new SuperShulkersCarpetExtension();
		CarpetServer.manageExtension(carpetExtension);

		LOGGER.info("Registered scarpet extension {}", carpetExtension);
	}
}
