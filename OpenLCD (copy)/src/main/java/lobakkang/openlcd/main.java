package lobakkang.openlcd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.handler.codec.MessageToByteEncoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import li.cil.oc.api.CreativeTab;
import lobakkang.openlcd.network.MessageHandlerOnServer;
import lobakkang.openlcd.network.MessageToServer;
import lobakkang.openlcd.tile.TileLCD;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = main.MODID, name = main.NAME, version = main.VERSION, acceptedMinecraftVersions = main.MC_VERSION)
public class main {

	public static final String MODID = "openlcd";
	public static final String NAME = "OpenLCD";
	public static final String VERSION = "1.0.0";
	public static final String MC_VERSION = "[1.12.2]";
	public static final Logger LOGGER = LogManager.getLogger(main.MODID);
	public static BufferedImage no_sig_img;
	public static BufferedImage blank_img;
	public static BufferedImage alphabet_img;

	public static SimpleNetworkWrapper data_channel;
	public static final byte MESSAGE_ID = 35;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		data_channel = NetworkRegistry.INSTANCE.newSimpleChannel("openlcd_channel");
		data_channel.registerMessage(MessageHandlerOnServer.class, MessageToServer.class, MESSAGE_ID, Side.SERVER);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// ModRecipes.initSmelting();
		LOGGER.info(main.NAME + " says hi!");
		try {
			URL img = main.class.getClassLoader().getResource("no_signal.png");
			no_sig_img = ImageIO.read(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			URL img = main.class.getClassLoader().getResource("blank.png");
			blank_img = ImageIO.read(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			URL img = main.class.getClassLoader().getResource("alphabet.png");
			alphabet_img = ImageIO.read(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}