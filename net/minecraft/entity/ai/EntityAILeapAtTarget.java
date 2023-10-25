package net.minecraft.entity.ai;

import net.minecraft.entity.实体Living;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.MathHelper;

public class EntityAILeapAtTarget extends EntityAIBase
{
    实体Living leaper;
    实体LivingBase leapTarget;
    float leapMotionY;

    public EntityAILeapAtTarget(实体Living leapingEntity, float leapMotionYIn)
    {
        this.leaper = leapingEntity;
        this.leapMotionY = leapMotionYIn;
        this.setMutexBits(5);
    }

    public boolean shouldExecute()
    {
        this.leapTarget = this.leaper.getAttackTarget();

        if (this.leapTarget == null)
        {
            return false;
        }
        else
        {
            double d0 = this.leaper.getDistanceSqToEntity(this.leapTarget);
            return d0 >= 4.0D && d0 <= 16.0D ? (!this.leaper.onGround ? false : this.leaper.getRNG().nextInt(5) == 0) : false;
        }
    }

    public boolean continueExecuting()
    {
        return !this.leaper.onGround;
    }

    public void startExecuting()
    {
        double d0 = this.leapTarget.X坐标 - this.leaper.X坐标;
        double d1 = this.leapTarget.Z坐标 - this.leaper.Z坐标;
        float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        this.leaper.通便X += d0 / (double)f * 0.5D * 0.800000011920929D + this.leaper.通便X * 0.20000000298023224D;
        this.leaper.通便Z += d1 / (double)f * 0.5D * 0.800000011920929D + this.leaper.通便Z * 0.20000000298023224D;
        this.leaper.motionY = (double)this.leapMotionY;
    }
}
