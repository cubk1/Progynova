package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.实体Living;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.阻止位置;

public abstract class EntityAIDoorInteract extends EntityAIBase
{
    protected 实体Living theEntity;
    protected 阻止位置 doorPosition = 阻止位置.ORIGIN;
    protected BlockDoor doorBlock;
    boolean hasStoppedDoorInteraction;
    float entityPositionX;
    float entityPositionZ;

    public EntityAIDoorInteract(实体Living entityIn)
    {
        this.theEntity = entityIn;

        if (!(entityIn.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    public boolean shouldExecute()
    {
        if (!this.theEntity.isCollidedHorizontally)
        {
            return false;
        }
        else
        {
            PathNavigateGround pathnavigateground = (PathNavigateGround)this.theEntity.getNavigator();
            PathEntity pathentity = pathnavigateground.getPath();

            if (pathentity != null && !pathentity.isFinished() && pathnavigateground.getEnterDoors())
            {
                for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); ++i)
                {
                    PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
                    this.doorPosition = new 阻止位置(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);

                    if (this.theEntity.getDistanceSq((double)this.doorPosition.getX(), this.theEntity.Y坐标, (double)this.doorPosition.getZ()) <= 2.25D)
                    {
                        this.doorBlock = this.getBlockDoor(this.doorPosition);

                        if (this.doorBlock != null)
                        {
                            return true;
                        }
                    }
                }

                this.doorPosition = (new 阻止位置(this.theEntity)).up();
                this.doorBlock = this.getBlockDoor(this.doorPosition);
                return this.doorBlock != null;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean continueExecuting()
    {
        return !this.hasStoppedDoorInteraction;
    }

    public void startExecuting()
    {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float)((double)((float)this.doorPosition.getX() + 0.5F) - this.theEntity.X坐标);
        this.entityPositionZ = (float)((double)((float)this.doorPosition.getZ() + 0.5F) - this.theEntity.Z坐标);
    }

    public void updateTask()
    {
        float f = (float)((double)((float)this.doorPosition.getX() + 0.5F) - this.theEntity.X坐标);
        float f1 = (float)((double)((float)this.doorPosition.getZ() + 0.5F) - this.theEntity.Z坐标);
        float f2 = this.entityPositionX * f + this.entityPositionZ * f1;

        if (f2 < 0.0F)
        {
            this.hasStoppedDoorInteraction = true;
        }
    }

    private BlockDoor getBlockDoor(阻止位置 pos)
    {
        Block block = this.theEntity.worldObj.getBlockState(pos).getBlock();
        return block instanceof BlockDoor && block.getMaterial() == Material.wood ? (BlockDoor)block : null;
    }
}
