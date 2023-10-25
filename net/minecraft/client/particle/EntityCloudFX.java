package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityCloudFX extends EntityFX
{
    float field_70569_a;

    protected EntityCloudFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1221_8_, double p_i1221_10_, double p_i1221_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        float f = 2.5F;
        this.通便X *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.通便Z *= 0.10000000149011612D;
        this.通便X += p_i1221_8_;
        this.motionY += p_i1221_10_;
        this.通便Z += p_i1221_12_;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * 0.30000001192092896D);
        this.particleScale *= 0.75F;
        this.particleScale *= f;
        this.field_70569_a = this.particleScale;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.3D));
        this.particleMaxAge = (int)((float)this.particleMaxAge * f);
        this.noClip = false;
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        this.particleScale = this.field_70569_a * f;
        super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.moveEntity(this.通便X, this.motionY, this.通便Z);
        this.通便X *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.通便Z *= 0.9599999785423279D;
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 2.0D);

        if (entityplayer != null && this.posY > entityplayer.getEntityBoundingBox().minY)
        {
            this.posY += (entityplayer.getEntityBoundingBox().minY - this.posY) * 0.2D;
            this.motionY += (entityplayer.motionY - this.motionY) * 0.2D;
            this.setPosition(this.posX, this.posY, this.posZ);
        }

        if (this.onGround)
        {
            this.通便X *= 0.699999988079071D;
            this.通便Z *= 0.699999988079071D;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new EntityCloudFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
