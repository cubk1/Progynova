package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.实体Tameable;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public abstract class 实体Creature extends 实体Living
{
    public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier FLEEING_SPEED_MODIFIER = (new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
    private 阻止位置 homePosition = 阻止位置.ORIGIN;
    private float maximumHomeDistance = -1.0F;
    private EntityAIBase aiBase = new EntityAIMoveTowardsRestriction(this, 1.0D);
    private boolean isMovementAITaskSet;

    public 实体Creature(World worldIn)
    {
        super(worldIn);
    }

    public float getBlockPathWeight(阻止位置 pos)
    {
        return 0.0F;
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() && this.getBlockPathWeight(new 阻止位置(this.X坐标, this.getEntityBoundingBox().minY, this.Z坐标)) >= 0.0F;
    }

    public boolean hasPath()
    {
        return !this.navigator.noPath();
    }

    public boolean isWithinHomeDistanceCurrentPosition()
    {
        return this.isWithinHomeDistanceFromPosition(new 阻止位置(this));
    }

    public boolean isWithinHomeDistanceFromPosition(阻止位置 pos)
    {
        return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(pos) < (double)(this.maximumHomeDistance * this.maximumHomeDistance);
    }

    public void setHomePosAndDistance(阻止位置 pos, int distance)
    {
        this.homePosition = pos;
        this.maximumHomeDistance = (float)distance;
    }

    public 阻止位置 getHomePosition()
    {
        return this.homePosition;
    }

    public float getMaximumHomeDistance()
    {
        return this.maximumHomeDistance;
    }

    public void detachHome()
    {
        this.maximumHomeDistance = -1.0F;
    }

    public boolean hasHome()
    {
        return this.maximumHomeDistance != -1.0F;
    }

    protected void updateLeashedState()
    {
        super.updateLeashedState();

        if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj)
        {
            实体 实体 = this.getLeashedToEntity();
            this.setHomePosAndDistance(new 阻止位置((int) 实体.X坐标, (int) 实体.Y坐标, (int) 实体.Z坐标), 5);
            float f = this.getDistanceToEntity(实体);

            if (this instanceof 实体Tameable && ((实体Tameable)this).isSitting())
            {
                if (f > 10.0F)
                {
                    this.clearLeashed(true, true);
                }

                return;
            }

            if (!this.isMovementAITaskSet)
            {
                this.tasks.addTask(2, this.aiBase);

                if (this.getNavigator() instanceof PathNavigateGround)
                {
                    ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
                }

                this.isMovementAITaskSet = true;
            }

            this.func_142017_o(f);

            if (f > 4.0F)
            {
                this.getNavigator().tryMoveToEntityLiving(实体, 1.0D);
            }

            if (f > 6.0F)
            {
                double d0 = (实体.X坐标 - this.X坐标) / (double)f;
                double d1 = (实体.Y坐标 - this.Y坐标) / (double)f;
                double d2 = (实体.Z坐标 - this.Z坐标) / (double)f;
                this.通便X += d0 * Math.abs(d0) * 0.4D;
                this.motionY += d1 * Math.abs(d1) * 0.4D;
                this.通便Z += d2 * Math.abs(d2) * 0.4D;
            }

            if (f > 10.0F)
            {
                this.clearLeashed(true, true);
            }
        }
        else if (!this.getLeashed() && this.isMovementAITaskSet)
        {
            this.isMovementAITaskSet = false;
            this.tasks.removeTask(this.aiBase);

            if (this.getNavigator() instanceof PathNavigateGround)
            {
                ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
            }

            this.detachHome();
        }
    }

    protected void func_142017_o(float p_142017_1_)
    {
    }
}
