package net.minecraft.entity.ai;

import net.minecraft.entity.实体Creature;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget
{
    private boolean entityCallsForHelp;
    private int revengeTimerOld;
    private final Class[] targetClasses;

    public EntityAIHurtByTarget(实体Creature creatureIn, boolean entityCallsForHelpIn, Class... targetClassesIn)
    {
        super(creatureIn, false);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.targetClasses = targetClassesIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        int i = this.taskOwner.getRevengeTimer();
        return i != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();

        if (this.entityCallsForHelp)
        {
            double d0 = this.getTargetDistance();

            for (实体Creature entitycreature : this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.X坐标, this.taskOwner.Y坐标, this.taskOwner.Z坐标, this.taskOwner.X坐标 + 1.0D, this.taskOwner.Y坐标 + 1.0D, this.taskOwner.Z坐标 + 1.0D)).expand(d0, 10.0D, d0)))
            {
                if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(this.taskOwner.getAITarget()))
                {
                    boolean flag = false;

                    for (Class oclass : this.targetClasses)
                    {
                        if (entitycreature.getClass() == oclass)
                        {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag)
                    {
                        this.setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget());
                    }
                }
            }
        }

        super.startExecuting();
    }

    protected void setEntityAttackTarget(实体Creature creatureIn, 实体LivingBase entityLivingBaseIn)
    {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }
}
