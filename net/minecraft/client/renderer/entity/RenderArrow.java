package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;
import org.lwjgl.opengl.GL11;

public class RenderArrow extends Render<EntityArrow>
{
    private static final 图像位置 arrowTextures = new 图像位置("textures/entity/arrow.png");

    public RenderArrow(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.bindEntityTexture(entity);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x, (float)y, (float)z);
        光照状态经理.辐射(entity.prevRotationYaw + (entity.旋转侧滑 - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = (float)(0 + i * 10) / 32.0F;
        float f3 = (float)(5 + i * 10) / 32.0F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = (float)(5 + i * 10) / 32.0F;
        float f7 = (float)(10 + i * 10) / 32.0F;
        float f8 = 0.05625F;
        光照状态经理.enableRescaleNormal();
        float f9 = (float)entity.arrowShake - partialTicks;

        if (f9 > 0.0F)
        {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            光照状态经理.辐射(f10, 0.0F, 0.0F, 1.0F);
        }

        光照状态经理.辐射(45.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.障眼物(f8, f8, f8);
        光照状态经理.理解(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f8, 0.0F, 0.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex((double)f4, (double)f6).endVertex();
        worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex((double)f5, (double)f6).endVertex();
        worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex((double)f5, (double)f7).endVertex();
        worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex((double)f4, (double)f7).endVertex();
        tessellator.draw();
        GL11.glNormal3f(-f8, 0.0F, 0.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex((double)f4, (double)f6).endVertex();
        worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex((double)f5, (double)f6).endVertex();
        worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex((double)f5, (double)f7).endVertex();
        worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex((double)f4, (double)f7).endVertex();
        tessellator.draw();

        for (int j = 0; j < 4; ++j)
        {
            光照状态经理.辐射(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f8);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-8.0D, -2.0D, 0.0D).tex((double)f, (double)f2).endVertex();
            worldrenderer.pos(8.0D, -2.0D, 0.0D).tex((double)f1, (double)f2).endVertex();
            worldrenderer.pos(8.0D, 2.0D, 0.0D).tex((double)f1, (double)f3).endVertex();
            worldrenderer.pos(-8.0D, 2.0D, 0.0D).tex((double)f, (double)f3).endVertex();
            tessellator.draw();
        }

        光照状态经理.disableRescaleNormal();
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(EntityArrow entity)
    {
        return arrowTextures;
    }
}
