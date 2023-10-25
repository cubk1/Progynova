package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.鬼EditSign;
import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.图像位置;
import net.optifine.CustomColors;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign>
{
    private static final 图像位置 SIGN_TEXTURE = new 图像位置("textures/entity/sign.png");
    private final ModelSign model = new ModelSign();
    private static double textRenderDistanceSq = 4096.0D;

    public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        Block block = te.getBlockType();
        光照状态经理.推黑客帝国();
        float f = 0.6666667F;

        if (block == Blocks.standing_sign)
        {
            光照状态经理.理解((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
            float f1 = (float)(te.getBlockMetadata() * 360) / 16.0F;
            光照状态经理.辐射(-f1, 0.0F, 1.0F, 0.0F);
            this.model.signStick.showModel = true;
        }
        else
        {
            int k = te.getBlockMetadata();
            float f2 = 0.0F;

            if (k == 2)
            {
                f2 = 180.0F;
            }

            if (k == 4)
            {
                f2 = 90.0F;
            }

            if (k == 5)
            {
                f2 = -90.0F;
            }

            光照状态经理.理解((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
            光照状态经理.辐射(-f2, 0.0F, 1.0F, 0.0F);
            光照状态经理.理解(0.0F, -0.3125F, -0.4375F);
            this.model.signStick.showModel = false;
        }

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            光照状态经理.matrixMode(5890);
            光照状态经理.推黑客帝国();
            光照状态经理.障眼物(4.0F, 2.0F, 1.0F);
            光照状态经理.理解(0.0625F, 0.0625F, 0.0625F);
            光照状态经理.matrixMode(5888);
        }
        else
        {
            this.bindTexture(SIGN_TEXTURE);
        }

        光照状态经理.enableRescaleNormal();
        光照状态经理.推黑客帝国();
        光照状态经理.障眼物(f, -f, -f);
        this.model.renderSign();
        光照状态经理.流行音乐黑客帝国();

        if (isRenderText(te))
        {
            FontRenderer fontrenderer = this.getFontRenderer();
            float f3 = 0.015625F * f;
            光照状态经理.理解(0.0F, 0.5F * f, 0.07F * f);
            光照状态经理.障眼物(f3, -f3, f3);
            GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
            光照状态经理.depthMask(false);
            int i = 0;

            if (Config.isCustomColors())
            {
                i = CustomColors.getSignTextColor(i);
            }

            if (destroyStage < 0)
            {
                for (int j = 0; j < te.signText.length; ++j)
                {
                    if (te.signText[j] != null)
                    {
                        IChatComponent ichatcomponent = te.signText[j];
                        List<IChatComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                        String s = list != null && list.size() > 0 ? ((IChatComponent)list.get(0)).getFormattedText() : "";

                        if (j == te.lineBeingEdited)
                        {
                            s = "> " + s + " <";
                            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                        }
                        else
                        {
                            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                        }
                    }
                }
            }
        }

        光照状态经理.depthMask(true);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.流行音乐黑客帝国();

        if (destroyStage >= 0)
        {
            光照状态经理.matrixMode(5890);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.matrixMode(5888);
        }
    }

    private static boolean isRenderText(TileEntitySign p_isRenderText_0_)
    {
        if (Shaders.isShadowPass)
        {
            return false;
        }
        else if (Config.getMinecraft().currentScreen instanceof 鬼EditSign)
        {
            return true;
        }
        else
        {
            if (!Config.zoomMode && p_isRenderText_0_.lineBeingEdited < 0)
            {
                实体 实体 = Config.getMinecraft().getRenderViewEntity();
                double d0 = p_isRenderText_0_.getDistanceSq(实体.X坐标, 实体.Y坐标, 实体.Z坐标);

                if (d0 > textRenderDistanceSq)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public static void updateTextRenderDistance()
    {
        我的手艺 宇轩的世界 = Config.getMinecraft();
        double d0 = (double)Config.limit(宇轩的世界.游戏一窝.fovSetting, 1.0F, 120.0F);
        double d1 = Math.max(1.5D * (double) 宇轩的世界.displayHeight / d0, 16.0D);
        textRenderDistanceSq = d1 * d1;
    }
}
