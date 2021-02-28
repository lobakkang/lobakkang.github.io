package lobakkang.openlcd.tile;

import java.nio.ByteBuffer;

import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import lobakkang.openlcd.main;
import lobakkang.openlcd.blocks.LCD_Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.lang.Character;

public class TileLCD extends TileEntityEnvironment {
    public static final double EnergyCostPerTick = 0.5;
    public boolean isEnabled;
    public boolean hasEnergy;

    public ByteBuffer buffer;
    public EnumFacing facing = EnumFacing.NORTH;
    public int width = 640;
    public int height = 480;
    public int ScreenTextureID = 0;
    public boolean refresh_texture = false;
    public boolean reload_texture = false;
    public boolean is_gui_open = false;
    // byte[] ArrayData;

    public TileLCD() {
        isEnabled = false;
        node = Network.newNode(this, Visibility.Network).withConnector().withComponent("LCD").create();
        facing = EnumFacing.UP;

        reload_tex();
    }

    @SideOnly(Side.CLIENT)
    public void reload_tex() {
        buffer = BufferUtils.createByteBuffer(width * height * 4);
        Color c;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                c = new Color(main.no_sig_img.getRGB(x, y), true);
                buffer.put((byte) c.getRed()); // Red component
                buffer.put((byte) c.getGreen()); // Green component
                buffer.put((byte) c.getBlue()); // Blue component
                buffer.put((byte) c.getAlpha()); // Alpha component. Only for RGBA
            }
        }

        buffer.flip();
    }

    @SideOnly(Side.CLIENT)
    public int create_texture() {
        // GL11.glDeleteTextures(ScreenTextureID);
        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        // Setup wrap mode
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        // Setup texture scaling filtering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Send texel data to OpenGL
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                buffer);
        return textureID;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compoundNBT = super.getUpdateTag();
        compoundNBT.setBoolean("isEnabled", isEnabled);
        compoundNBT.setBoolean("refresh_texture", refresh_texture);
        compoundNBT.setBoolean("reload_texture", reload_texture);
        compoundNBT.setInteger("texID", ScreenTextureID);
        compoundNBT.setInteger("width", width);
        compoundNBT.setInteger("height", height);

        if (refresh_texture) {
            byte[] arr = new byte[buffer.remaining()];
            buffer.get(arr);
            buffer.clear();
            buffer.put(arr);
            buffer.flip();
            compoundNBT.setByteArray("buffer", arr);
        }

        refresh_texture = false;
        return compoundNBT;
    }

    public void sendUpdates() {
        world.notifyBlockUpdate(pos, world.getBlockState(getPos()), world.getBlockState(getPos()),
                Constants.BlockFlags.SEND_TO_CLIENTS);
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        isEnabled = tag.getBoolean("isEnabled");
        ScreenTextureID = tag.getInteger("texID");
        width = tag.getInteger("width");
        height = tag.getInteger("height");

        if (tag.getBoolean("reload_texture")) {
            reload_tex();
        }

        if (tag.getBoolean("refresh_texture")) {
            System.out.println("refresh texture");
            buffer = BufferUtils.createByteBuffer(width * height * 4);

            buffer.put(tag.getByteArray("buffer"));
            buffer.flip();

            ScreenTextureID = create_texture();
        }
    }

    @Callback
    public Object[] isOn(Context context, Arguments args) {
        return new Object[] { isEnabled };
    }

    @Callback
    public Object[] turnOn(Context context, Arguments args) {
        isEnabled = true;
        reload_texture = true;
        sendUpdates();
        return new Object[] { true };
    }

    @Callback
    public Object[] getVramSize(Context context, Arguments args) {
        return new Object[] { width * height * 4 };
    }

    @Callback
    public Object[] turnOff(Context context, Arguments args) {
        isEnabled = false;
        sendUpdates();
        return new Object[] { true };
    }

    @Callback
    public Object[] getResolution(Context context, Arguments args) {
        return new Object[] { width, height };
    }

    @Callback
    public Object[] isKeyDown(Context context, Arguments args) {
        if (!is_gui_open) {
            return new Object[] { false };
        }
        return new Object[] { Keyboard.isKeyDown(args.checkInteger(0)) };
    }

    private void draw_char(char alphabet, int x, int y, int scale, int r, int g, int b) {
        int target = (java.lang.Character.toLowerCase(alphabet) - 'a');

        for (int dy = 0; dy < 5 * scale; dy++) {
            for (int dx = 0; dx < 5 * scale; dx++) {
                if (new Color(main.alphabet_img.getRGB((dx) / scale, (dy) / scale + (target * 5))).getBlue() == 0) {
                    buffer.put((((dy + y) * width) + (dx + x)) * 4 + 0, (byte) r);
                    buffer.put((((dy + y) * width) + (dx + x)) * 4 + 1, (byte) g);
                    buffer.put((((dy + y) * width) + (dx + x)) * 4 + 2, (byte) b);
                    buffer.put((((dy + y) * width) + (dx + x)) * 4 + 3, (byte) 255);
                } else {
                }
            }
        }
    }

    @Callback
    public Object[] setResolution(Context context, Arguments args) {
        width = args.checkInteger(0);
        height = args.checkInteger(1);

        buffer = BufferUtils.createByteBuffer(width * height * 4);
        // ArrayData = new byte[width * height * 4];
        Color c;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buffer.put((byte) 0); // Red component
                buffer.put((byte) 0); // Green component
                buffer.put((byte) 0); // Blue component
                buffer.put((byte) 255); // Alpha component. Only for RGBA
            }
        }

        buffer.flip();

        refresh_texture = true;
        sendUpdates();

        return new Object[] { width, height };
    }

    @Callback
    public Object[] flush(Context context, Arguments args) {
        refresh_texture = true;
        sendUpdates();
        return new Object[] { true };
    }

    @Callback
    public Object[] fill(Context context, Arguments args) {
        int x = args.checkInteger(0);
        int y = args.checkInteger(1);
        int w = args.checkInteger(2);
        int h = args.checkInteger(3);
        int r = args.checkInteger(4);
        int g = args.checkInteger(5);
        int b = args.checkInteger(6);

        for (int dy = 0; dy < h; dy++) {
            for (int dx = 0; dx < w; dx++) {
                Color c = new Color(r, g, b);
                buffer.put((((dy + y) * width) + (dx + x)) * 4 + 0, (byte) c.getRed());
                buffer.put((((dy + y) * width) + (dx + x)) * 4 + 1, (byte) c.getGreen());
                buffer.put((((dy + y) * width) + (dx + x)) * 4 + 2, (byte) c.getBlue());
                buffer.put((((dy + y) * width) + (dx + x)) * 4 + 3, (byte) c.getAlpha());
            }
        }

        //refresh_texture = true;
        //sendUpdates();

        return new Object[] { true };
    }

    @Callback
    public Object[] copy(Context context, Arguments args) {
        int x = args.checkInteger(0);
        int y = args.checkInteger(1);
        int w = args.checkInteger(2);
        int h = args.checkInteger(3);

        byte[] data = args.checkByteArray(4);
        if (data.length != (w * h * 3)) {
            return new Object[] { false };
        }

        for (int dy = 0; dy < h; dy++) {
            for (int dx = 0; dx < w; dx++) {
                Color c = new Color(data[((y * width) + x) * 3 + 0], data[((y * width) + x) * 3 + 1],
                        data[((y * width) + x) * 3 + 2]);
                buffer.put((((dy + y) * width) + (dx + x)) * 4 + 0, (byte) c.getRed());
                buffer.put((((dy + y) * width) + (dx + x)) * 4 + 1, (byte) c.getGreen());
                buffer.put((((dy + y) * width) + (dx + x)) * 4 + 2, (byte) c.getBlue());
                buffer.put((((dy + y) * width) + (dx + x)) * 4 + 3, (byte) c.getAlpha());
            }
        }
        //refresh_texture = true;
        //sendUpdates();

        return new Object[] { true };
    }

    @Callback
    public Object[] pixel(Context context, Arguments args) {
        int x = args.checkInteger(0);
        int y = args.checkInteger(1);
        int r = args.checkInteger(2);
        int g = args.checkInteger(3);
        int b = args.checkInteger(4);

        Color c = new Color(r, g, b);
        buffer.put((((y) * width) + (x)) * 4 + 0, (byte) c.getRed());
        buffer.put((((y) * width) + (x)) * 4 + 1, (byte) c.getGreen());
        buffer.put((((y) * width) + (x)) * 4 + 2, (byte) c.getBlue());
        buffer.put((((y) * width) + (x)) * 4 + 3, (byte) c.getAlpha());

        //refresh_texture = true;
        //sendUpdates();

        return new Object[] { true };
    }

    @Callback
    public Object[] alphabet(Context context, Arguments args) {
        char alphabet = args.checkString(0).charAt(0);
        int x = args.checkInteger(1);
        int y = args.checkInteger(2);
        int scale = args.checkInteger(3);
        int r = args.checkInteger(4);
        int g = args.checkInteger(5);
        int b = args.checkInteger(6);

        draw_char(alphabet, x, y, scale, r, g, b);

        //refresh_texture = true;
        //sendUpdates();

        return new Object[] { true };
    }

    @Callback
    public Object[] alphabets(Context context, Arguments args) {
        String alphabet = args.checkString(0);
        int x = args.checkInteger(1);
        int y = args.checkInteger(2);
        int scale = args.checkInteger(3);
        int r = args.checkInteger(4);
        int g = args.checkInteger(5);
        int b = args.checkInteger(6);

        for (int i = 0; i < alphabet.length(); i++) {
            draw_char(alphabet.toCharArray()[i], x + (i * scale * 6), y, scale, r, g, b);
            System.out.println(i * scale * 6);
        }

        //refresh_texture = true;
        //sendUpdates();

        return new Object[] { true };
    }

    @SideOnly(Side.CLIENT)
    public void orientation_callibrate() {
        if (facing == EnumFacing.UP) {
            facing = this.getWorld().getBlockState(getPos()).getValue(LCD_Block.FACING);
        }
    }

    @SideOnly(Side.CLIENT)
    public void draw() {
        if (ScreenTextureID == 0) {
            ScreenTextureID = create_texture();
        }

        float border = (float) 0.15;

        float x_min = border;
        float x_max = (float) 1.0 - border;
        float y_min = border;
        float y_max = (float) 1.0 - border;

        if (width > height) {
            float ratio = ((float) ((float) width - (float) height) / (float) width) / (float) 2.0;
            y_min = y_min + ratio;
            y_max = y_max - ratio;
        } else if (width < height) {
            float ratio = ((float) ((float) height - (float) width) / (float) height) / (float) 2.0;
            x_min = x_min + ratio;
            x_max = x_max - ratio;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ScreenTextureID);
        GL11.glBegin(GL11.GL_QUADS);

        if (facing == EnumFacing.NORTH || facing == EnumFacing.WEST) {
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex3f(x_min, y_min, 0);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex3f(x_min, y_max, 0);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3f(x_max, y_max, 0);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex3f(x_max, y_min, 0);
        } else {
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex3f(x_min, y_min, 0);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3f(x_min, y_max, 0);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex3f(x_max, y_max, 0);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex3f(x_max, y_min, 0);
        }
        GL11.glEnd();
    }
}
