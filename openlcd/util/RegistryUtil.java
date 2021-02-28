package lobakkang.openlcd.util;

import lobakkang.openlcd.main;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class RegistryUtil {

	public static Item setItemName(final Item item, final String name) {
		item.setRegistryName(main.MODID, name).setTranslationKey(main.MODID + "." + name);
		return item;
	}
	
	public static Block setBlockName(final Block block, final String name) {
		block.setRegistryName(main.MODID, name).setTranslationKey(main.MODID + "." + name);
		return block;
	}
	
}
