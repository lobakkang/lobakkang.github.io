package lobakkang.openlcd.tile;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import li.cil.oc.util.RenderState;

public class LCDRenderer extends TileEntitySpecialRenderer<TileLCD> {
    public TileLCD screen = null;

    @Override
    public void render(TileLCD t, double x, double y, double z, float partialTicks, int destroyStage, float a) {
        this.screen = t;
        RenderState.checkError(this.getClass().getName() + ".render: entering (aka: wasntme)");

        RenderState.checkError(this.getClass().getName() + ".render: checks");
        RenderState.pushAttrib();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xFF, 0xFF);
        RenderState.disableEntityLighting();
        // RenderState.makeItBlend();
        // GlStateManager.color(1, 1, 1, 1);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        draw(t);

        RenderState.disableBlend();
        RenderState.enableEntityLighting();
        GlStateManager.popMatrix();
        RenderState.popAttrib();

        RenderState.checkError(this.getClass().getName() + ".render: leaving");
    }

    public void transform() {
        switch (screen.yaw()) {
            case WEST:
                GlStateManager.rotate(-90, 0, 1, 0);
                break;
            case NORTH:
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            default:
                break;
        }

        switch (screen.pitch()) {
            case DOWN:
                GlStateManager.rotate(90, 1, 0, 0);
                break;
            case UP:
                GlStateManager.rotate(-90, 1, 0, 0);
                break;
            default:
                break;
        }

        // Fit area to screen (bottom left = bottom left).
        GlStateManager.translate(-0.5f, -0.5f, 0.5f);

        GlStateManager.translate(0, screen.height(), 0);

        // Flip text upside down.
        GlStateManager.scale(1, -1, 1);

    }

    private void draw(TileLCD t) {

    }
}
