package net.minecraft.client.renderer.entity;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderFish extends Render<EntityFishHook>
{
    private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

    public RenderFish(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x, (float)y, (float)z);
        光照状态经理.enableRescaleNormal();
        光照状态经理.障眼物(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(entity);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 1;
        int j = 2;
        float f = 0.0625F;
        float f1 = 0.125F;
        float f2 = 0.125F;
        float f3 = 0.1875F;
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.5F;
        光照状态经理.辐射(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        worldrenderer.pos(-0.5D, -0.5D, 0.0D).tex(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(0.5D, -0.5D, 0.0D).tex(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(0.5D, 0.5D, 0.0D).tex(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(-0.5D, 0.5D, 0.0D).tex(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();
        光照状态经理.disableRescaleNormal();
        光照状态经理.流行音乐黑客帝国();

        if (entity.angler != null)
        {
            float f7 = entity.angler.getSwingProgress(partialTicks);
            float f8 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI);
            Vec3 vec3 = new Vec3(-0.36D, 0.03D, 0.35D);
            vec3 = vec3.rotatePitch(-(entity.angler.prevRotationPitch + (entity.angler.rotationPitch - entity.angler.prevRotationPitch) * partialTicks) * (float)Math.PI / 180.0F);
            vec3 = vec3.rotateYaw(-(entity.angler.prevRotationYaw + (entity.angler.旋转侧滑 - entity.angler.prevRotationYaw) * partialTicks) * (float)Math.PI / 180.0F);
            vec3 = vec3.rotateYaw(f8 * 0.5F);
            vec3 = vec3.rotatePitch(-f8 * 0.7F);
            double d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * (double)partialTicks + vec3.xCoord;
            double d1 = entity.angler.prevPosY + (entity.angler.posY - entity.angler.prevPosY) * (double)partialTicks + vec3.yCoord;
            double d2 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * (double)partialTicks + vec3.zCoord;
            double d3 = (double)entity.angler.getEyeHeight();

            if (this.renderManager.options != null && this.renderManager.options.thirdPersonView > 0 || entity.angler != 我的手艺.得到我的手艺().宇轩游玩者)
            {
                float f9 = (entity.angler.prevRenderYawOffset + (entity.angler.renderYawOffset - entity.angler.prevRenderYawOffset) * partialTicks) * (float)Math.PI / 180.0F;
                double d4 = (double)MathHelper.sin(f9);
                double d6 = (double)MathHelper.cos(f9);
                double d8 = 0.35D;
                double d10 = 0.8D;
                d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * (double)partialTicks - d6 * 0.35D - d4 * 0.8D;
                d1 = entity.angler.prevPosY + d3 + (entity.angler.posY - entity.angler.prevPosY) * (double)partialTicks - 0.45D;
                d2 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * (double)partialTicks - d4 * 0.35D + d6 * 0.8D;
                d3 = entity.angler.正在下蹲() ? -0.1875D : 0.0D;
            }

            double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
            double d5 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + 0.25D;
            double d7 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
            double d9 = (double)((float)(d0 - d13));
            double d11 = (double)((float)(d1 - d5)) + d3;
            double d12 = (double)((float)(d2 - d7));
            光照状态经理.禁用手感();
            光照状态经理.disableLighting();
            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            int k = 16;

            for (int l = 0; l <= 16; ++l)
            {
                float f10 = (float)l / 16.0F;
                worldrenderer.pos(x + d9 * (double)f10, y + d11 * (double)(f10 * f10 + f10) * 0.5D + 0.25D, z + d12 * (double)f10).color(0, 0, 0, 255).endVertex();
            }

            tessellator.draw();
            光照状态经理.enableLighting();
            光照状态经理.启用手感();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    protected ResourceLocation getEntityTexture(EntityFishHook entity)
    {
        return FISH_PARTICLES;
    }
}
