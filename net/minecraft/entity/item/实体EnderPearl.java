package net.minecraft.entity.item;

import net.minecraft.entity.monster.实体Endermite;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.entity.projectile.实体Throwable;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class 实体EnderPearl extends 实体Throwable
{
    private 实体LivingBase field_181555_c;

    public 实体EnderPearl(World worldIn)
    {
        super(worldIn);
    }

    public 实体EnderPearl(World worldIn, 实体LivingBase p_i1783_2_)
    {
        super(worldIn, p_i1783_2_);
        this.field_181555_c = p_i1783_2_;
    }

    public 实体EnderPearl(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    protected void onImpact(MovingObjectPosition p_70184_1_)
    {
        实体LivingBase entitylivingbase = this.getThrower();

        if (p_70184_1_.实体Hit != null)
        {
            if (p_70184_1_.实体Hit == this.field_181555_c)
            {
                return;
            }

            p_70184_1_.实体Hit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 0.0F);
        }

        for (int i = 0; i < 32; ++i)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.X坐标, this.Y坐标 + this.rand.nextDouble() * 2.0D, this.Z坐标, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
        }

        if (!this.worldObj.isRemote)
        {
            if (entitylivingbase instanceof 实体PlayerMP)
            {
                实体PlayerMP entityplayermp = (实体PlayerMP)entitylivingbase;

                if (entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == this.worldObj && !entityplayermp.isPlayerSleeping())
                {
                    if (this.rand.nextFloat() < 0.05F && this.worldObj.getGameRules().getBoolean("doMobSpawning"))
                    {
                        实体Endermite entityendermite = new 实体Endermite(this.worldObj);
                        entityendermite.setSpawnedByPlayer(true);
                        entityendermite.setLocationAndAngles(entitylivingbase.X坐标, entitylivingbase.Y坐标, entitylivingbase.Z坐标, entitylivingbase.旋转侧滑, entitylivingbase.rotationPitch);
                        this.worldObj.spawnEntityInWorld(entityendermite);
                    }

                    if (entitylivingbase.isRiding())
                    {
                        entitylivingbase.mountEntity((实体)null);
                    }

                    entitylivingbase.setPositionAndUpdate(this.X坐标, this.Y坐标, this.Z坐标);
                    entitylivingbase.fallDistance = 0.0F;
                    entitylivingbase.attackEntityFrom(DamageSource.fall, 5.0F);
                }
            }
            else if (entitylivingbase != null)
            {
                entitylivingbase.setPositionAndUpdate(this.X坐标, this.Y坐标, this.Z坐标);
                entitylivingbase.fallDistance = 0.0F;
            }

            this.setDead();
        }
    }

    public void onUpdate()
    {
        实体LivingBase entitylivingbase = this.getThrower();

        if (entitylivingbase != null && entitylivingbase instanceof 实体Player && !entitylivingbase.isEntityAlive())
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();
        }
    }
}
