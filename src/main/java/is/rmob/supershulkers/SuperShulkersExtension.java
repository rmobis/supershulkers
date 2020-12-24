package is.rmob.supershulkers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import net.minecraft.server.MinecraftServer;

public class SuperShulkersExtension implements CarpetExtension {
	private static final Logger LOGGER = LogManager.getLogger(SuperShulkersExtension.class);

	private final static String SCARPET_APP = "supershulkers";

	private BundledModule superShulkersApp;

	public SuperShulkersExtension() {
		this.registerScarpetApp();
	}

	private void registerScarpetApp() {
		this.superShulkersApp = BundledModule.fromPath("assets/supershulkers/scripts/", SuperShulkersExtension.SCARPET_APP, false);
		CarpetScriptServer.registerBuiltInScript(this.superShulkersApp);

		LOGGER.info("Registered scarpet app {}", this.superShulkersApp);
	}

	@Override
	public void onServerLoadedWorlds(MinecraftServer server) {
		CarpetServer.scriptServer.addScriptHost(server.getCommandSource(), this.superShulkersApp.getName(), null, true, false, false);

		LOGGER.info("Loaded scarpet app {}", this.superShulkersApp);
	}
}
