package lobakkang.openlcd;

import lobakkang.openlcd.init.ModBlocks;
import lobakkang.openlcd.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = main.MODID)
public class RegistrationHandler {

	@SubscribeEvent
	public static void registerItems(Register<Item> event) {
		final Item[] itemBlocks = {
				new ItemBlock(ModBlocks.LCD_BLOCK).setRegistryName(ModBlocks.LCD_BLOCK.getRegistryName())
		};
		
		event.getRegistry().registerAll(itemBlocks);
	}

	@SubscribeEvent
	public static void registerBlocks(Register<Block> event) {
		final Block[] blocks = {
				RegistryUtil.setBlockName(new Block(Material.GLASS), "LCD").setCreativeTab(main.TUTORIAL_TAB)
		};

		event.getRegistry().registerAll(blocks);
	}

}