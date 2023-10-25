package net.minecraft.entity.ai;

import net.minecraft.entity.实体Creature;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsTarget extends EntityAIBase
{
    private 实体Creature theEntity;
    private 实体LivingBase targetEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double speed;
    private float maxTargetDistance;

    public EntityAIMoveTowardsTarget(实体Creature creature, double speedIn, float targetMaxDistance)
    {
        this.theEntity = creature;
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        this.targetEntity = this.theEntity.getAttackTarget();

        if (this.targetEntity == null)
        {
            return false;
        }
        else if (this.targetEntity.getDistanceSqToEntity(this.theEntity) > (double)(this.maxTargetDistance * this.maxTargetDistance))
        {
            return false;
        }
        else
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(this.targetEntity.X坐标, this.targetEntity.Y坐标, this.targetEntity.Z坐标));

            if (vec3 == null)
            {
                return false;
            }
            else
            {
                this.movePosX = vec3.xCoord;
                this.movePosY = vec3.yCoord;
                this.movePosZ = vec3.zCoord;
                return true;
            }
        }
    }

    public boolean continueExecuting()
    {
        return !this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSqToEntity(this.theEntity) < (double)(this.maxTargetDistance * this.maxTargetDistance);
    }

    public void resetTask()
    {
        this.targetEntity = null;
    }

    public void startExecuting()
    {
        this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
    }
}
