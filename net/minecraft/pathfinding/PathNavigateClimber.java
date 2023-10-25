package net.minecraft.pathfinding;

import net.minecraft.entity.实体;
import net.minecraft.entity.实体Living;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber extends PathNavigateGround
{
    private 阻止位置 targetPosition;

    public PathNavigateClimber(实体Living entityLivingIn, World worldIn)
    {
        super(entityLivingIn, worldIn);
    }

    public PathEntity getPathToPos(阻止位置 pos)
    {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }

    public PathEntity getPathToEntityLiving(实体 实体In)
    {
        this.targetPosition = new 阻止位置(实体In);
        return super.getPathToEntityLiving(实体In);
    }

    public boolean tryMoveToEntityLiving(实体 实体In, double speedIn)
    {
        PathEntity pathentity = this.getPathToEntityLiving(实体In);

        if (pathentity != null)
        {
            return this.setPath(pathentity, speedIn);
        }
        else
        {
            this.targetPosition = new 阻止位置(实体In);
            this.speed = speedIn;
            return true;
        }
    }

    public void onUpdateNavigation()
    {
        if (!this.noPath())
        {
            super.onUpdateNavigation();
        }
        else
        {
            if (this.targetPosition != null)
            {
                double d0 = (double)(this.theEntity.width * this.theEntity.width);

                if (this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d0 && (this.theEntity.Y坐标 <= (double)this.targetPosition.getY() || this.theEntity.getDistanceSqToCenter(new 阻止位置(this.targetPosition.getX(), MathHelper.floor_double(this.theEntity.Y坐标), this.targetPosition.getZ())) >= d0))
                {
                    this.theEntity.getMoveHelper().setMoveTo((double)this.targetPosition.getX(), (double)this.targetPosition.getY(), (double)this.targetPosition.getZ(), this.speed);
                }
                else
                {
                    this.targetPosition = null;
                }
            }
        }
    }
}
