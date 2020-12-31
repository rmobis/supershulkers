package is.rmob.supershulkers.compat;

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import is.rmob.supershulkers.ShulkerUtil;
import is.rmob.supershulkers.SuperShulkers;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ShulkerBoxTooltipExtension implements ShulkerBoxTooltipApi {
	private static final Logger LOGGER = LogManager.getLogger(SuperShulkersCarpetExtension.class);

	@Override
	public String getModId() {
		return SuperShulkers.MODID;
	}


	@Override
	public void registerProviders(Map<PreviewProvider, List<Item>> providersMap) {
		providersMap.put(new EnlargeableShulkerBoxPreviewProvider(), ShulkerUtil.SHULKER_BOXES);

		LOGGER.debug("registered shulkerboxtooltip preview provider {}", providersMap);
	}
}
