package net.minecraft.client.particle;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.实体Guardian;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MobAppearance extends 实体FX
{
    private 实体LivingBase entity;

    protected MobAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.通便X = this.motionY = this.通便Z = 0.0D;
        this.particleGravity = 0.0F;
        this.particleMaxAge = 30;
    }

    public int getFXLayer()
    {
        return 3;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.entity == null)
        {
            实体Guardian entityguardian = new 实体Guardian(this.worldObj);
            entityguardian.setElder();
            this.entity = entityguardian;
        }
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        if (this.entity != null)
        {
            RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
            rendermanager.setRenderPosition(实体FX.interpPosX, 实体FX.interpPosY, 实体FX.interpPosZ);
            float f = 0.42553192F;
            float f1 = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
            光照状态经理.depthMask(true);
            光照状态经理.启用混合品();
            光照状态经理.启用纵深();
            光照状态经理.正常工作(770, 771);
            float f2 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f2, f2);
            光照状态经理.推黑客帝国();
            float f3 = 0.05F + 0.5F * MathHelper.sin(f1 * (float)Math.PI);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, f3);
            光照状态经理.理解(0.0F, 1.8F, 0.0F);
            光照状态经理.辐射(180.0F - 实体In.旋转侧滑, 0.0F, 1.0F, 0.0F);
            光照状态经理.辐射(60.0F - 150.0F * f1 - 实体In.rotationPitch, 1.0F, 0.0F, 0.0F);
            光照状态经理.理解(0.0F, -0.4F, -1.5F);
            光照状态经理.障眼物(f, f, f);
            this.entity.旋转侧滑 = this.entity.prevRotationYaw = 0.0F;
            this.entity.rotationYawHead = this.entity.prevRotationYawHead = 0.0F;
            rendermanager.renderEntityWithPosYaw(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.启用纵深();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new MobAppearance(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
