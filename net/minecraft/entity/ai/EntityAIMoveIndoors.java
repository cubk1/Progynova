package net.minecraft.entity.ai;

import net.minecraft.entity.实体Creature;
import net.minecraft.util.阻止位置;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private 实体Creature entityObj;
    private VillageDoorInfo doorInfo;
    private int insidePosX = -1;
    private int insidePosZ = -1;

    public EntityAIMoveIndoors(实体Creature entityObjIn)
    {
        this.entityObj = entityObjIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        阻止位置 blockpos = new 阻止位置(this.entityObj);

        if ((!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining() && !this.entityObj.worldObj.getBiomeGenForCoords(blockpos).canRain()) && !this.entityObj.worldObj.provider.getHasNoSky())
        {
            if (this.entityObj.getRNG().nextInt(50) != 0)
            {
                return false;
            }
            else if (this.insidePosX != -1 && this.entityObj.getDistanceSq((double)this.insidePosX, this.entityObj.Y坐标, (double)this.insidePosZ) < 4.0D)
            {
                return false;
            }
            else
            {
                Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 14);

                if (village == null)
                {
                    return false;
                }
                else
                {
                    this.doorInfo = village.getDoorInfo(blockpos);
                    return this.doorInfo != null;
                }
            }
        }
        else
        {
            return false;
        }
    }

    public boolean continueExecuting()
    {
        return !this.entityObj.getNavigator().noPath();
    }

    public void startExecuting()
    {
        this.insidePosX = -1;
        阻止位置 blockpos = this.doorInfo.getInsideBlockPos();
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();

        if (this.entityObj.getDistanceSq(blockpos) > 256.0D)
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3((double)i + 0.5D, (double)j, (double)k + 0.5D));

            if (vec3 != null)
            {
                this.entityObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
            }
        }
        else
        {
            this.entityObj.getNavigator().tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
        }
    }

    public void resetTask()
    {
        this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;
    }
}
