package net.minecraft.entity.ai;

import net.minecraft.entity.passive.实体Horse;
import net.minecraft.entity.实体;
import net.minecraft.entity.player.实体Player;
import net.minecraft.util.Vec3;

public class EntityAIRunAroundLikeCrazy extends EntityAIBase
{
    private 实体Horse horseHost;
    private double speed;
    private double targetX;
    private double targetY;
    private double targetZ;

    public EntityAIRunAroundLikeCrazy(实体Horse horse, double speedIn)
    {
        this.horseHost = horse;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        if (!this.horseHost.isTame() && this.horseHost.riddenBy实体 != null)
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.horseHost, 5, 4);

            if (vec3 == null)
            {
                return false;
            }
            else
            {
                this.targetX = vec3.xCoord;
                this.targetY = vec3.yCoord;
                this.targetZ = vec3.zCoord;
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public void startExecuting()
    {
        this.horseHost.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
    }

    public boolean continueExecuting()
    {
        return !this.horseHost.getNavigator().noPath() && this.horseHost.riddenBy实体 != null;
    }

    public void updateTask()
    {
        if (this.horseHost.getRNG().nextInt(50) == 0)
        {
            if (this.horseHost.riddenBy实体 instanceof 实体Player)
            {
                int i = this.horseHost.getTemper();
                int j = this.horseHost.getMaxTemper();

                if (j > 0 && this.horseHost.getRNG().nextInt(j) < i)
                {
                    this.horseHost.setTamedBy((实体Player)this.horseHost.riddenBy实体);
                    this.horseHost.worldObj.setEntityState(this.horseHost, (byte)7);
                    return;
                }

                this.horseHost.increaseTemper(5);
            }

            this.horseHost.riddenBy实体.mountEntity((实体)null);
            this.horseHost.riddenBy实体 = null;
            this.horseHost.makeHorseRearWithSound();
            this.horseHost.worldObj.setEntityState(this.horseHost, (byte)6);
        }
    }
}
