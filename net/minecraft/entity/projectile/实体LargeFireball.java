package net.minecraft.entity.projectile;

import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class 实体LargeFireball extends 实体Fireball
{
    public int explosionPower = 1;

    public 实体LargeFireball(World worldIn)
    {
        super(worldIn);
    }

    public 实体LargeFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    public 实体LargeFireball(World worldIn, 实体LivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    protected void onImpact(MovingObjectPosition movingObject)
    {
        if (!this.worldObj.isRemote)
        {
            if (movingObject.实体Hit != null)
            {
                movingObject.实体Hit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
                this.applyEnchantments(this.shootingEntity, movingObject.实体Hit);
            }

            boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
            this.worldObj.newExplosion((实体)null, this.X坐标, this.Y坐标, this.Z坐标, (float)this.explosionPower, flag, flag);
            this.setDead();
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionPower);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("ExplosionPower", 99))
        {
            this.explosionPower = tagCompund.getInteger("ExplosionPower");
        }
    }
}
