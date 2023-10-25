package net.minecraft.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.图像位置;
import net.optifine.shaders.ShadersRender;

public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal>
{
    private static final 图像位置 END_SKY_TEXTURE = new 图像位置("textures/environment/end_sky.png");
    private static final 图像位置 END_PORTAL_TEXTURE = new 图像位置("textures/entity/end_portal.png");
    private static final Random field_147527_e = new Random(31100L);
    FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);

    public void renderTileEntityAt(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (!Config.isShaders() || !ShadersRender.renderEndPortal(te, x, y, z, partialTicks, destroyStage, 0.75F))
        {
            float f = (float)this.rendererDispatcher.entityX;
            float f1 = (float)this.rendererDispatcher.entityY;
            float f2 = (float)this.rendererDispatcher.entityZ;
            光照状态经理.disableLighting();
            field_147527_e.setSeed(31100L);
            float f3 = 0.75F;

            for (int i = 0; i < 16; ++i)
            {
                光照状态经理.推黑客帝国();
                float f4 = (float)(16 - i);
                float f5 = 0.0625F;
                float f6 = 1.0F / (f4 + 1.0F);

                if (i == 0)
                {
                    this.bindTexture(END_SKY_TEXTURE);
                    f6 = 0.1F;
                    f4 = 65.0F;
                    f5 = 0.125F;
                    光照状态经理.启用混合品();
                    光照状态经理.正常工作(770, 771);
                }

                if (i >= 1)
                {
                    this.bindTexture(END_PORTAL_TEXTURE);
                }

                if (i == 1)
                {
                    光照状态经理.启用混合品();
                    光照状态经理.正常工作(1, 1);
                    f5 = 0.5F;
                }

                float f7 = (float)(-(y + (double)f3));
                float f8 = f7 + (float)ActiveRenderInfo.getPosition().yCoord;
                float f9 = f7 + f4 + (float)ActiveRenderInfo.getPosition().yCoord;
                float f10 = f8 / f9;
                f10 = (float)(y + (double)f3) + f10;
                光照状态经理.理解(f, f10, f2);
                光照状态经理.texGen(光照状态经理.TexGen.S, 9217);
                光照状态经理.texGen(光照状态经理.TexGen.T, 9217);
                光照状态经理.texGen(光照状态经理.TexGen.R, 9217);
                光照状态经理.texGen(光照状态经理.TexGen.Q, 9216);
                光照状态经理.texGen(光照状态经理.TexGen.S, 9473, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
                光照状态经理.texGen(光照状态经理.TexGen.T, 9473, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
                光照状态经理.texGen(光照状态经理.TexGen.R, 9473, this.func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
                光照状态经理.texGen(光照状态经理.TexGen.Q, 9474, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
                光照状态经理.enableTexGenCoord(光照状态经理.TexGen.S);
                光照状态经理.enableTexGenCoord(光照状态经理.TexGen.T);
                光照状态经理.enableTexGenCoord(光照状态经理.TexGen.R);
                光照状态经理.enableTexGenCoord(光照状态经理.TexGen.Q);
                光照状态经理.流行音乐黑客帝国();
                光照状态经理.matrixMode(5890);
                光照状态经理.推黑客帝国();
                光照状态经理.loadIdentity();
                光照状态经理.理解(0.0F, (float)(我的手艺.getSystemTime() % 700000L) / 700000.0F, 0.0F);
                光照状态经理.障眼物(f5, f5, f5);
                光照状态经理.理解(0.5F, 0.5F, 0.0F);
                光照状态经理.辐射((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
                光照状态经理.理解(-0.5F, -0.5F, 0.0F);
                光照状态经理.理解(-f, -f2, -f1);
                f8 = f7 + (float)ActiveRenderInfo.getPosition().yCoord;
                光照状态经理.理解((float)ActiveRenderInfo.getPosition().xCoord * f4 / f8, (float)ActiveRenderInfo.getPosition().zCoord * f4 / f8, -f1);
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                float f11 = (field_147527_e.nextFloat() * 0.5F + 0.1F) * f6;
                float f12 = (field_147527_e.nextFloat() * 0.5F + 0.4F) * f6;
                float f13 = (field_147527_e.nextFloat() * 0.5F + 0.5F) * f6;

                if (i == 0)
                {
                    f11 = f12 = f13 = 1.0F * f6;
                }

                worldrenderer.pos(x, y + (double)f3, z).color(f11, f12, f13, 1.0F).endVertex();
                worldrenderer.pos(x, y + (double)f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
                worldrenderer.pos(x + 1.0D, y + (double)f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
                worldrenderer.pos(x + 1.0D, y + (double)f3, z).color(f11, f12, f13, 1.0F).endVertex();
                tessellator.draw();
                光照状态经理.流行音乐黑客帝国();
                光照状态经理.matrixMode(5888);
                this.bindTexture(END_SKY_TEXTURE);
            }

            光照状态经理.禁用混合品();
            光照状态经理.disableTexGenCoord(光照状态经理.TexGen.S);
            光照状态经理.disableTexGenCoord(光照状态经理.TexGen.T);
            光照状态经理.disableTexGenCoord(光照状态经理.TexGen.R);
            光照状态经理.disableTexGenCoord(光照状态经理.TexGen.Q);
            光照状态经理.enableLighting();
        }
    }

    private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
    {
        this.field_147528_b.clear();
        this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }
}
