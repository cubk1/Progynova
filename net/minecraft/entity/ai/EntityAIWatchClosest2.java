package net.minecraft.entity.ai;

import net.minecraft.entity.实体;
import net.minecraft.entity.实体Living;

public class EntityAIWatchClosest2 extends EntityAIWatchClosest
{
    public EntityAIWatchClosest2(实体Living entitylivingIn, Class <? extends 实体> watchTargetClass, float maxDistance, float chanceIn)
    {
        super(entitylivingIn, watchTargetClass, maxDistance, chanceIn);
        this.setMutexBits(3);
    }
}
