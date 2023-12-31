package net.minecraft.client.particle;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.实体;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体FX extends 实体
{
    protected int particleTextureIndexX;
    protected int particleTextureIndexY;
    protected float particleTextureJitterX;
    protected float particleTextureJitterY;
    protected int particleAge;
    protected int particleMaxAge;
    protected float particleScale;
    protected float particleGravity;
    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    protected float particleAlpha;
    protected TextureAtlasSprite particleIcon;
    public static double interpPosX;
    public static double interpPosY;
    public static double interpPosZ;

    protected 实体FX(World worldIn, double posXIn, double posYIn, double posZIn)
    {
        super(worldIn);
        this.particleAlpha = 1.0F;
        this.setSize(0.2F, 0.2F);
        this.setPosition(posXIn, posYIn, posZIn);
        this.lastTickPosX = this.prevPosX = posXIn;
        this.lastTickPosY = this.prevPosY = posYIn;
        this.lastTickPosZ = this.prevPosZ = posZIn;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
        this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
        this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
        this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
        this.particleAge = 0;
    }

    public 实体FX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.通便X = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.通便Z = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
        float f1 = MathHelper.sqrt_double(this.通便X * this.通便X + this.motionY * this.motionY + this.通便Z * this.通便Z);
        this.通便X = this.通便X / (double)f1 * (double)f * 0.4000000059604645D;
        this.motionY = this.motionY / (double)f1 * (double)f * 0.4000000059604645D + 0.10000000149011612D;
        this.通便Z = this.通便Z / (double)f1 * (double)f * 0.4000000059604645D;
    }

    public 实体FX multiplyVelocity(float multiplier)
    {
        this.通便X *= (double)multiplier;
        this.motionY = (this.motionY - 0.10000000149011612D) * (double)multiplier + 0.10000000149011612D;
        this.通便Z *= (double)multiplier;
        return this;
    }

    public 实体FX multipleParticleScaleBy(float scale)
    {
        this.setSize(0.2F * scale, 0.2F * scale);
        this.particleScale *= scale;
        return this;
    }

    public void setRBGColorF(float particleRedIn, float particleGreenIn, float particleBlueIn)
    {
        this.particleRed = particleRedIn;
        this.particleGreen = particleGreenIn;
        this.particleBlue = particleBlueIn;
    }

    public void setAlphaF(float alpha)
    {
        if (this.particleAlpha == 1.0F && alpha < 1.0F)
        {
            我的手艺.得到我的手艺().effectRenderer.moveToAlphaLayer(this);
        }
        else if (this.particleAlpha < 1.0F && alpha == 1.0F)
        {
            我的手艺.得到我的手艺().effectRenderer.moveToNoAlphaLayer(this);
        }

        this.particleAlpha = alpha;
    }

    public float getRedColorF()
    {
        return this.particleRed;
    }

    public float getGreenColorF()
    {
        return this.particleGreen;
    }

    public float getBlueColorF()
    {
        return this.particleBlue;
    }

    public float getAlpha()
    {
        return this.particleAlpha;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
    }

    public void onUpdate()
    {
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.motionY -= 0.04D * (double)this.particleGravity;
        this.moveEntity(this.通便X, this.motionY, this.通便Z);
        this.通便X *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.通便Z *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.通便X *= 0.699999988079071D;
            this.通便Z *= 0.699999988079071D;
        }
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = (float)this.particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float)this.particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleIcon != null)
        {
            f = this.particleIcon.getMinU();
            f1 = this.particleIcon.getMaxU();
            f2 = this.particleIcon.getMinV();
            f3 = this.particleIcon.getMaxV();
        }

        float f5 = (float)(this.prevPosX + (this.X坐标 - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.Y坐标 - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.Z坐标 - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        worldRendererIn.pos((double)(f5 - rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 - rotationYZ * f4 - rotationXZ * f4)).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 - rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 - rotationYZ * f4 + rotationXZ * f4)).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 + rotationYZ * f4 + rotationXZ * f4)).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 + rotationYZ * f4 - rotationXZ * f4)).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }

    public int getFXLayer()
    {
        return 0;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    }

    public void setParticleIcon(TextureAtlasSprite icon)
    {
        int i = this.getFXLayer();

        if (i == 1)
        {
            this.particleIcon = icon;
        }
        else
        {
            throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
        }
    }

    public void setParticleTextureIndex(int particleTextureIndex)
    {
        if (this.getFXLayer() != 0)
        {
            throw new RuntimeException("Invalid call to Particle.setMiscTex");
        }
        else
        {
            this.particleTextureIndexX = particleTextureIndex % 16;
            this.particleTextureIndexY = particleTextureIndex / 16;
        }
    }

    public void nextTextureIndexX()
    {
        ++this.particleTextureIndexX;
    }

    public boolean canAttackWithItem()
    {
        return false;
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + ", Pos (" + this.X坐标 + "," + this.Y坐标 + "," + this.Z坐标 + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
    }
}
