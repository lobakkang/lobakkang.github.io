package lobakkang.openlcd;

import li.cil.oc.api.CreativeTab;
import lobakkang.openlcd.init.ModBlocks;
import lobakkang.openlcd.tile.LCDRenderer;
import lobakkang.openlcd.tile.TileLCD;
import lobakkang.openlcd.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
				RegistryUtil.setBlockName(ModBlocks.LCD_BLOCK, "lcd").setCreativeTab(CreativeTab.instance)
		};

		event.getRegistry().registerAll(blocks);
		GameRegistry.registerTileEntity(TileLCD.class, new ResourceLocation(main.MODID + ":LCD"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileLCD.class, new LCDRenderer());
	}

}