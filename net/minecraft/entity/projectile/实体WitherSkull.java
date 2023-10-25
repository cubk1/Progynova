package net.minecraft.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.boss.实体Wither;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class 实体WitherSkull extends 实体Fireball
{
    public 实体WitherSkull(World worldIn)
    {
        super(worldIn);
        this.setSize(0.3125F, 0.3125F);
    }

    public 实体WitherSkull(World worldIn, 实体LivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    protected float getMotionFactor()
    {
        return this.isInvulnerable() ? 0.73F : super.getMotionFactor();
    }

    public 实体WitherSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    public boolean isBurning()
    {
        return false;
    }

    public float getExplosionResistance(Explosion explosionIn, World worldIn, 阻止位置 pos, IBlockState blockStateIn)
    {
        float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
        Block block = blockStateIn.getBlock();

        if (this.isInvulnerable() && 实体Wither.canDestroyBlock(block))
        {
            f = Math.min(0.8F, f);
        }

        return f;
    }

    protected void onImpact(MovingObjectPosition movingObject)
    {
        if (!this.worldObj.isRemote)
        {
            if (movingObject.实体Hit != null)
            {
                if (this.shootingEntity != null)
                {
                    if (movingObject.实体Hit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F))
                    {
                        if (!movingObject.实体Hit.isEntityAlive())
                        {
                            this.shootingEntity.heal(5.0F);
                        }
                        else
                        {
                            this.applyEnchantments(this.shootingEntity, movingObject.实体Hit);
                        }
                    }
                }
                else
                {
                    movingObject.实体Hit.attackEntityFrom(DamageSource.magic, 5.0F);
                }

                if (movingObject.实体Hit instanceof 实体LivingBase)
                {
                    int i = 0;

                    if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL)
                    {
                        i = 10;
                    }
                    else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
                    {
                        i = 40;
                    }

                    if (i > 0)
                    {
                        ((实体LivingBase)movingObject.实体Hit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * i, 1));
                    }
                }
            }

            this.worldObj.newExplosion(this, this.X坐标, this.Y坐标, this.Z坐标, 1.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
            this.setDead();
        }
    }

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
    }

    public boolean isInvulnerable()
    {
        return this.dataWatcher.getWatchableObjectByte(10) == 1;
    }

    public void setInvulnerable(boolean invulnerable)
    {
        this.dataWatcher.updateObject(10, Byte.valueOf((byte)(invulnerable ? 1 : 0)));
    }
}
