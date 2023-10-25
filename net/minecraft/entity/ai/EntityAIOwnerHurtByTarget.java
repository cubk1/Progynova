package net.minecraft.entity.ai;

import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.passive.实体Tameable;

public class EntityAIOwnerHurtByTarget extends EntityAITarget
{
    实体Tameable theDefendingTameable;
    实体LivingBase theOwnerAttacker;
    private int field_142051_e;

    public EntityAIOwnerHurtByTarget(实体Tameable theDefendingTameableIn)
    {
        super(theDefendingTameableIn, false);
        this.theDefendingTameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        if (!this.theDefendingTameable.isTamed())
        {
            return false;
        }
        else
        {
            实体LivingBase entitylivingbase = this.theDefendingTameable.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.theOwnerAttacker = entitylivingbase.getAITarget();
                int i = entitylivingbase.getRevengeTimer();
                return i != this.field_142051_e && this.isSuitableTarget(this.theOwnerAttacker, false) && this.theDefendingTameable.shouldAttackEntity(this.theOwnerAttacker, entitylivingbase);
            }
        }
    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        实体LivingBase entitylivingbase = this.theDefendingTameable.getOwner();

        if (entitylivingbase != null)
        {
            this.field_142051_e = entitylivingbase.getRevengeTimer();
        }

        super.startExecuting();
    }
}
