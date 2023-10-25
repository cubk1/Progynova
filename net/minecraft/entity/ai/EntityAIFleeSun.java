package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.实体Creature;
import net.minecraft.util.阻止位置;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIFleeSun extends EntityAIBase
{
    private 实体Creature theCreature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private double movementSpeed;
    private World theWorld;

    public EntityAIFleeSun(实体Creature theCreatureIn, double movementSpeedIn)
    {
        this.theCreature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.theWorld = theCreatureIn.worldObj;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        if (!this.theWorld.isDaytime())
        {
            return false;
        }
        else if (!this.theCreature.isBurning())
        {
            return false;
        }
        else if (!this.theWorld.canSeeSky(new 阻止位置(this.theCreature.X坐标, this.theCreature.getEntityBoundingBox().minY, this.theCreature.Z坐标)))
        {
            return false;
        }
        else
        {
            Vec3 vec3 = this.findPossibleShelter();

            if (vec3 == null)
            {
                return false;
            }
            else
            {
                this.shelterX = vec3.xCoord;
                this.shelterY = vec3.yCoord;
                this.shelterZ = vec3.zCoord;
                return true;
            }
        }
    }

    public boolean continueExecuting()
    {
        return !this.theCreature.getNavigator().noPath();
    }

    public void startExecuting()
    {
        this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    private Vec3 findPossibleShelter()
    {
        Random random = this.theCreature.getRNG();
        阻止位置 blockpos = new 阻止位置(this.theCreature.X坐标, this.theCreature.getEntityBoundingBox().minY, this.theCreature.Z坐标);

        for (int i = 0; i < 10; ++i)
        {
            阻止位置 blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (!this.theWorld.canSeeSky(blockpos1) && this.theCreature.getBlockPathWeight(blockpos1) < 0.0F)
            {
                return new Vec3((double)blockpos1.getX(), (double)blockpos1.getY(), (double)blockpos1.getZ());
            }
        }

        return null;
    }
}
