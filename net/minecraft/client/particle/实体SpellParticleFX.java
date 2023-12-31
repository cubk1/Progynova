package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.实体;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体SpellParticleFX extends 实体FX
{
    private static final Random RANDOM = new Random();
    private int baseSpellTextureIndex = 128;

    protected 实体SpellParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1229_8_, double p_i1229_10_, double p_i1229_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.5D - RANDOM.nextDouble(), p_i1229_10_, 0.5D - RANDOM.nextDouble());
        this.motionY *= 0.20000000298023224D;

        if (p_i1229_8_ == 0.0D && p_i1229_12_ == 0.0D)
        {
            this.通便X *= 0.10000000149011612D;
            this.通便Z *= 0.10000000149011612D;
        }

        this.particleScale *= 0.75F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.noClip = false;
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        super.renderParticle(worldRendererIn, 实体In, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
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

        this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY += 0.004D;
        this.moveEntity(this.通便X, this.motionY, this.通便Z);

        if (this.Y坐标 == this.prevPosY)
        {
            this.通便X *= 1.1D;
            this.通便Z *= 1.1D;
        }

        this.通便X *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.通便Z *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.通便X *= 0.699999988079071D;
            this.通便Z *= 0.699999988079071D;
        }
    }

    public void setBaseSpellTextureIndex(int baseSpellTextureIndexIn)
    {
        this.baseSpellTextureIndex = baseSpellTextureIndexIn;
    }

    public static class AmbientMobFactory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            实体FX entityfx = new 实体SpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            entityfx.setAlphaF(0.15F);
            entityfx.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
            return entityfx;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new 实体SpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

    public static class InstantFactory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            实体FX entityfx = new 实体SpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((实体SpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
            return entityfx;
        }
    }

    public static class MobFactory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            实体FX entityfx = new 实体SpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            entityfx.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
            return entityfx;
        }
    }

    public static class WitchFactory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            实体FX entityfx = new 实体SpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((实体SpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
            float f = worldIn.rand.nextFloat() * 0.5F + 0.35F;
            entityfx.setRBGColorF(1.0F * f, 0.0F * f, 1.0F * f);
            return entityfx;
        }
    }
}
