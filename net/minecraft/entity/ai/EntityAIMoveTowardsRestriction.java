package net.minecraft.entity.ai;

import net.minecraft.entity.实体Creature;
import net.minecraft.util.阻止位置;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsRestriction extends EntityAIBase
{
    private 实体Creature theEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double movementSpeed;

    public EntityAIMoveTowardsRestriction(实体Creature creatureIn, double speedIn)
    {
        this.theEntity = creatureIn;
        this.movementSpeed = speedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        if (this.theEntity.isWithinHomeDistanceCurrentPosition())
        {
            return false;
        }
        else
        {
            阻止位置 blockpos = this.theEntity.getHomePosition();
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ()));

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
        return !this.theEntity.getNavigator().noPath();
    }

    public void startExecuting()
    {
        this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}
