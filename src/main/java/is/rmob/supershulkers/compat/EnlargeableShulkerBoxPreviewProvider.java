package is.rmob.supershulkers.compat;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.impl.provider.ShulkerBoxPreviewProvider;
import is.rmob.supershulkers.ShulkerUtil;
import net.minecraft.item.ItemStack;

public class EnlargeableShulkerBoxPreviewProvider extends ShulkerBoxPreviewProvider {
	@Override
	public int getInventoryMaxSize(PreviewContext ctx) {
		ItemStack stack = ctx.getStack();

		if (ShulkerUtil.isShulkerBox(stack.getItem())) {
			return ShulkerUtil.getInventorySize(stack.getEnchantments());
		}

		return super.getInventoryMaxSize(ctx);
	}

	@Override
	public int getPriority() {
		return super.getPriority() + 1;
	}
}
