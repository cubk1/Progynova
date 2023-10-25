package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.passive.实体Tameable;
import net.minecraft.entity.实体LivingBase;

public class EntityAITargetNonTamed<T extends 实体LivingBase> extends EntityAINearestAttackableTarget
{
    private 实体Tameable theTameable;

    public EntityAITargetNonTamed(实体Tameable entityIn, Class<T> classTarget, boolean checkSight, Predicate <? super T > targetSelector)
    {
        super(entityIn, classTarget, 10, checkSight, false, targetSelector);
        this.theTameable = entityIn;
    }

    public boolean shouldExecute()
    {
        return !this.theTameable.isTamed() && super.shouldExecute();
    }
}
