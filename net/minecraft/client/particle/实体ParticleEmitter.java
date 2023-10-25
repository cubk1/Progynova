package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.实体;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class 实体ParticleEmitter extends 实体FX
{
    private 实体 attached实体;
    private int age;
    private int lifetime;
    private EnumParticleTypes particleTypes;

    public 实体ParticleEmitter(World worldIn, 实体 p_i46279_2_, EnumParticleTypes particleTypesIn)
    {
        super(worldIn, p_i46279_2_.X坐标, p_i46279_2_.getEntityBoundingBox().minY + (double)(p_i46279_2_.height / 2.0F), p_i46279_2_.Z坐标, p_i46279_2_.通便X, p_i46279_2_.motionY, p_i46279_2_.通便Z);
        this.attached实体 = p_i46279_2_;
        this.lifetime = 3;
        this.particleTypes = particleTypesIn;
        this.onUpdate();
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
    }

    public void onUpdate()
    {
        for (int i = 0; i < 16; ++i)
        {
            double d0 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);

            if (d0 * d0 + d1 * d1 + d2 * d2 <= 1.0D)
            {
                double d3 = this.attached实体.X坐标 + d0 * (double)this.attached实体.width / 4.0D;
                double d4 = this.attached实体.getEntityBoundingBox().minY + (double)(this.attached实体.height / 2.0F) + d1 * (double)this.attached实体.height / 4.0D;
                double d5 = this.attached实体.Z坐标 + d2 * (double)this.attached实体.width / 4.0D;
                this.worldObj.spawnParticle(this.particleTypes, false, d3, d4, d5, d0, d1 + 0.2D, d2, new int[0]);
            }
        }

        ++this.age;

        if (this.age >= this.lifetime)
        {
            this.setDead();
        }
    }

    public int getFXLayer()
    {
        return 3;
    }
}
