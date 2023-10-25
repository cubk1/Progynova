package net.minecraft.entity.ai;

import net.minecraft.entity.passive.实体Tameable;
import net.minecraft.entity.实体LivingBase;

public class EntityAIOwnerHurtTarget extends EntityAITarget
{
    实体Tameable theEntityTameable;
    实体LivingBase theTarget;
    private int field_142050_e;

    public EntityAIOwnerHurtTarget(实体Tameable theEntityTameableIn)
    {
        super(theEntityTameableIn, false);
        this.theEntityTameable = theEntityTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        if (!this.theEntityTameable.isTamed())
        {
            return false;
        }
        else
        {
            实体LivingBase entitylivingbase = this.theEntityTameable.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.theTarget = entitylivingbase.getLastAttacker();
                int i = entitylivingbase.getLastAttackerTime();
                return i != this.field_142050_e && this.isSuitableTarget(this.theTarget, false) && this.theEntityTameable.shouldAttackEntity(this.theTarget, entitylivingbase);
            }
        }
    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.theTarget);
        实体LivingBase entitylivingbase = this.theEntityTameable.getOwner();

        if (entitylivingbase != null)
        {
            this.field_142050_e = entitylivingbase.getLastAttackerTime();
        }

        super.startExecuting();
    }
}
