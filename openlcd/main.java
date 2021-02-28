package lobakkang.openlcd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import li.cil.oc.api.CreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = main.MODID, name = main.NAME, version = main.VERSION, acceptedMinecraftVersions = main.MC_VERSION)
public class main {

	public static final String MODID = "openlcd";
	public static final String NAME = "OpenLCD";
	public static final String VERSION = "1.0.0";
	public static final String MC_VERSION = "[1.12.2]";
	public static final Logger LOGGER = LogManager.getLogger(main.MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
        
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//ModRecipes.initSmelting();
		LOGGER.info(main.NAME + " says hi!");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
        
	}

}