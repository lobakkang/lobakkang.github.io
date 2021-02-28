package lobakkang.openlcd.init;

import lobakkang.openlcd.main;
import lobakkang.openlcd.blocks.LCD_Block;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(main.MODID)
public class ModBlocks {

	public static final Block LCD_BLOCK = new LCD_Block();
	
}
