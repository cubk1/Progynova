package net.minecraft.entity.ai;

import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.monster.实体Creeper;
import net.minecraft.entity.monster.实体IronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage extends EntityAITarget
{
    实体IronGolem irongolem;
    实体LivingBase villageAgressorTarget;

    public EntityAIDefendVillage(实体IronGolem ironGolemIn)
    {
        super(ironGolemIn, false, true);
        this.irongolem = ironGolemIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        Village village = this.irongolem.getVillage();

        if (village == null)
        {
            return false;
        }
        else
        {
            this.villageAgressorTarget = village.findNearestVillageAggressor(this.irongolem);

            if (this.villageAgressorTarget instanceof 实体Creeper)
            {
                return false;
            }
            else if (!this.isSuitableTarget(this.villageAgressorTarget, false))
            {
                if (this.taskOwner.getRNG().nextInt(20) == 0)
                {
                    this.villageAgressorTarget = village.getNearestTargetPlayer(this.irongolem);
                    return this.isSuitableTarget(this.villageAgressorTarget, false);
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
    }

    public void startExecuting()
    {
        this.irongolem.setAttackTarget(this.villageAgressorTarget);
        super.startExecuting();
    }
}
