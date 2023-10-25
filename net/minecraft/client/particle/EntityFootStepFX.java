package net.minecraft.client.particle;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.图像位置;
import net.minecraft.world.World;

public class EntityFootStepFX extends EntityFX
{
    private static final 图像位置 FOOTPRINT_TEXTURE = new 图像位置("textures/particle/footprint.png");
    private int footstepAge;
    private int footstepMaxAge;
    private TextureManager currentFootSteps;

    protected EntityFootStepFX(TextureManager currentFootStepsIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.currentFootSteps = currentFootStepsIn;
        this.通便X = this.motionY = this.通便Z = 0.0D;
        this.footstepMaxAge = 200;
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.footstepAge + partialTicks) / (float)this.footstepMaxAge;
        f = f * f;
        float f1 = 2.0F - f * 2.0F;

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        f1 = f1 * 0.2F;
        光照状态经理.disableLighting();
        float f2 = 0.125F;
        float f3 = (float)(this.posX - interpPosX);
        float f4 = (float)(this.posY - interpPosY);
        float f5 = (float)(this.posZ - interpPosZ);
        float f6 = this.worldObj.getLightBrightness(new BlockPos(this));
        this.currentFootSteps.绑定手感(FOOTPRINT_TEXTURE);
        光照状态经理.启用混合品();
        光照状态经理.正常工作(770, 771);
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRendererIn.pos((double)(f3 - 0.125F), (double)f4, (double)(f5 + 0.125F)).tex(0.0D, 1.0D).color(f6, f6, f6, f1).endVertex();
        worldRendererIn.pos((double)(f3 + 0.125F), (double)f4, (double)(f5 + 0.125F)).tex(1.0D, 1.0D).color(f6, f6, f6, f1).endVertex();
        worldRendererIn.pos((double)(f3 + 0.125F), (double)f4, (double)(f5 - 0.125F)).tex(1.0D, 0.0D).color(f6, f6, f6, f1).endVertex();
        worldRendererIn.pos((double)(f3 - 0.125F), (double)f4, (double)(f5 - 0.125F)).tex(0.0D, 0.0D).color(f6, f6, f6, f1).endVertex();
        Tessellator.getInstance().draw();
        光照状态经理.禁用混合品();
        光照状态经理.enableLighting();
    }

    public void onUpdate()
    {
        ++this.footstepAge;

        if (this.footstepAge == this.footstepMaxAge)
        {
            this.setDead();
        }
    }

    public int getFXLayer()
    {
        return 3;
    }

    public static class Factory implements IParticleFactory
    {
        public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new EntityFootStepFX(我的手艺.得到我的手艺().得到手感经理(), worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
