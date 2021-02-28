package lobakkang.openlcd.tile;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import li.cil.oc.util.RenderState;
import lobakkang.openlcd.blocks.LCD_Block;

public class LCDRenderer extends TileEntitySpecialRenderer<TileLCD> {
    public TileLCD screen = null;

    @Override
    public void render(TileLCD t, double x, double y, double z, float partialTicks, int destroyStage, float a) {
        if (t.isEnabled) {
            this.screen = t;
            t.orientation_callibrate();
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xFF, 0xFF);
            RenderState.disableEntityLighting();

            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(GL11.GL_BACK);

            if (screen.facing == EnumFacing.NORTH) {
                GL11.glTranslatef((float) x, (float) y, (float) z - (float) 0.001);
            } else if (screen.facing == EnumFacing.SOUTH) {
                GL11.glTranslatef((float) x, (float) y, (float) z + (float) 1.001);
            } else if (screen.facing == EnumFacing.WEST) {
                GL11.glTranslatef((float) x - (float) 0.001, (float) y, (float) z + (float) 1.001);
                GL11.glRotatef(90, (float) 0, (float) 1, (float) 0);
            } else if (screen.facing == EnumFacing.EAST) {
                GL11.glTranslatef((float) x + (float) 1.001, (float) y, (float) z + (float) 1.001);
                GL11.glRotatef(90, (float) 0, (float) 1, (float) 0);
            } else {
                GL11.glTranslatef((float) x, (float) y, (float) z - (float) 0.001);
            }

            // GL11.glTranslatef((float) x, (float) y, (float) z - (float) 0.001);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDepthMask(false);
            t.draw();
            RenderState.enableEntityLighting();
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    // 所以老师讲的correction要怎样做
}
