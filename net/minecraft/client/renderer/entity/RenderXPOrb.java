package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;
import net.optifine.CustomColors;

public class RenderXPOrb extends Render<EntityXPOrb>
{
    private static final 图像位置 experienceOrbTextures = new 图像位置("textures/entity/experience_orb.png");

    public RenderXPOrb(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x, (float)y, (float)z);
        this.bindEntityTexture(entity);
        int i = entity.getTextureByXP();
        float f = (float)(i % 4 * 16 + 0) / 64.0F;
        float f1 = (float)(i % 4 * 16 + 16) / 64.0F;
        float f2 = (float)(i / 4 * 16 + 0) / 64.0F;
        float f3 = (float)(i / 4 * 16 + 16) / 64.0F;
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        int j = entity.getBrightnessForRender(partialTicks);
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        float f7 = 255.0F;
        float f8 = ((float)entity.xpColor + partialTicks) / 2.0F;

        if (Config.isCustomColors())
        {
            f8 = CustomColors.getXpOrbTimer(f8);
        }

        l = (int)((MathHelper.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
        int i1 = 255;
        int j1 = (int)((MathHelper.sin(f8 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
        光照状态经理.辐射(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float f9 = 0.3F;
        光照状态经理.障眼物(0.3F, 0.3F, 0.3F);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        int k1 = l;
        int l1 = 255;
        int i2 = j1;

        if (Config.isCustomColors())
        {
            int j2 = CustomColors.getXpOrbColor(f8);

            if (j2 >= 0)
            {
                k1 = j2 >> 16 & 255;
                l1 = j2 >> 8 & 255;
                i2 = j2 >> 0 & 255;
            }
        }

        worldrenderer.pos((double)(0.0F - f5), (double)(0.0F - f6), 0.0D).tex((double)f, (double)f3).color(k1, l1, i2, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos((double)(f4 - f5), (double)(0.0F - f6), 0.0D).tex((double)f1, (double)f3).color(k1, l1, i2, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos((double)(f4 - f5), (double)(1.0F - f6), 0.0D).tex((double)f1, (double)f2).color(k1, l1, i2, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos((double)(0.0F - f5), (double)(1.0F - f6), 0.0D).tex((double)f, (double)f2).color(k1, l1, i2, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();
        光照状态经理.禁用混合品();
        光照状态经理.disableRescaleNormal();
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(EntityXPOrb entity)
    {
        return experienceOrbTextures;
    }
}
