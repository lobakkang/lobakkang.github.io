package lobakkang.openlcd.gui;

import org.lwjgl.opengl.GL11;

import lobakkang.openlcd.main;
import lobakkang.openlcd.network.MessageToServer;
import lobakkang.openlcd.tile.TileLCD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class LCD_gui extends GuiScreen {
    TileLCD tileLCD;
    BlockPos pos;

    public LCD_gui(BlockPos pos) {
        super();

        System.out.println(pos.toString());
        tileLCD = (TileLCD) Minecraft.getMinecraft().world.getTileEntity(pos);
        this.pos = pos;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", pos.getX());
        tag.setInteger("y", pos.getY());
        tag.setInteger("z", pos.getZ());
        tag.setBoolean("is_gui_open", false);

        MessageToServer msg = new MessageToServer(tag);
        main.data_channel.sendToServer(msg);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tileLCD.ScreenTextureID);
        // System.out.printf("w= %d h= %d\n", width, height);
        // System.out.printf("texture: %d\n", tileLCD.ScreenTextureID);

        // int x_min = 0;//(int) (width * 0.1);
        // int x_max = tileLCD.width;//(int) (width * 0.9);
        // int y_min = 0;//(int) (height * 0.1);
        // int y_max = tileLCD.height;//(int) (height * 0.9);

        int x_min = (int) (width * 0.1);
        int x_max = (int) (width * 0.9);
        int y_min = (int) (height * 0.1);
        int y_max = (int) (height * 0.9);

        float target_ratio = (float) tileLCD.width / (float) tileLCD.height;
        float current_ratio = (float) (width * 0.8) / (float) (height * 0.8);

        float ratio_diff = target_ratio - current_ratio;
        if (ratio_diff > (float) 0) {
            float y_new = (height - ((float) (width * 0.8) / target_ratio)) / (float) 2.0;
            y_min = (int) y_new;
            y_max = height - (int) y_new;
        } else {
            float x_new = (width - ((float) (height * 0.8) * target_ratio)) / (float) 2.0;
            x_min = (int) x_new;
            x_max = width - (int) x_new;
        }

        // GuiScreen.drawModalRectWithCustomSizedTexture(x_min, y_min, 0, 0,
        // x_max,y_max, tileLCD.width, tileLCD.height);

        GL11.glPushMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tileLCD.ScreenTextureID);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(x_min, y_min, 0);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(x_min, y_max, 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(x_max, y_max, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(x_max, y_min, 0);
        GL11.glEnd();

        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
