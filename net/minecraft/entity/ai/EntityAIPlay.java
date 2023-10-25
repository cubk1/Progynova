package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.passive.实体Villager;
import net.minecraft.util.Vec3;

public class EntityAIPlay extends EntityAIBase
{
    private 实体Villager villagerObj;
    private 实体LivingBase targetVillager;
    private double speed;
    private int playTime;

    public EntityAIPlay(实体Villager villagerObjIn, double speedIn)
    {
        this.villagerObj = villagerObjIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        if (this.villagerObj.getGrowingAge() >= 0)
        {
            return false;
        }
        else if (this.villagerObj.getRNG().nextInt(400) != 0)
        {
            return false;
        }
        else
        {
            List<实体Villager> list = this.villagerObj.worldObj.<实体Villager>getEntitiesWithinAABB(实体Villager.class, this.villagerObj.getEntityBoundingBox().expand(6.0D, 3.0D, 6.0D));
            double d0 = Double.MAX_VALUE;

            for (实体Villager entityvillager : list)
            {
                if (entityvillager != this.villagerObj && !entityvillager.isPlaying() && entityvillager.getGrowingAge() < 0)
                {
                    double d1 = entityvillager.getDistanceSqToEntity(this.villagerObj);

                    if (d1 <= d0)
                    {
                        d0 = d1;
                        this.targetVillager = entityvillager;
                    }
                }
            }

            if (this.targetVillager == null)
            {
                Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);

                if (vec3 == null)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean continueExecuting()
    {
        return this.playTime > 0;
    }

    public void startExecuting()
    {
        if (this.targetVillager != null)
        {
            this.villagerObj.setPlaying(true);
        }

        this.playTime = 1000;
    }

    public void resetTask()
    {
        this.villagerObj.setPlaying(false);
        this.targetVillager = null;
    }

    public void updateTask()
    {
        --this.playTime;

        if (this.targetVillager != null)
        {
            if (this.villagerObj.getDistanceSqToEntity(this.targetVillager) > 4.0D)
            {
                this.villagerObj.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
            }
        }
        else if (this.villagerObj.getNavigator().noPath())
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);

            if (vec3 == null)
            {
                return;
            }

            this.villagerObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
        }
    }
}
