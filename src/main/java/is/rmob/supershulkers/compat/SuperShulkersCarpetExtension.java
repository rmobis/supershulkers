package is.rmob.supershulkers.compat;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import is.rmob.supershulkers.SuperShulkers;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SuperShulkersCarpetExtension implements CarpetExtension {
	private static final Logger LOGGER = LogManager.getLogger(SuperShulkersCarpetExtension.class);

	private final static String SCARPET_APP = "supershulkers";

	private BundledModule superShulkersApp;


	public SuperShulkersCarpetExtension() {
		this.registerScarpetApp();
	}


	private void registerScarpetApp() {
		this.superShulkersApp = BundledModule.fromPath("assets/" + SuperShulkers.MODID + "/scripts/", SCARPET_APP, false);
		CarpetScriptServer.registerBuiltInScript(this.superShulkersApp);

		LOGGER.info("Registered scarpet app {}", this.superShulkersApp);
	}


	@Override
	public void onServerLoadedWorlds(MinecraftServer server) {
		CarpetServer.scriptServer.addScriptHost(server.getCommandSource(), this.superShulkersApp.getName(), null, true, false, false, null);

		LOGGER.info("Loaded scarpet app {}", this.superShulkersApp);
	}
}
