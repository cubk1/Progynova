package net.minecraft.entity.ai;

import net.minecraft.entity.passive.实体Villager;
import net.minecraft.entity.player.实体Player;
import net.minecraft.inventory.Container;

public class EntityAITradePlayer extends EntityAIBase
{
    private 实体Villager villager;

    public EntityAITradePlayer(实体Villager villagerIn)
    {
        this.villager = villagerIn;
        this.setMutexBits(5);
    }

    public boolean shouldExecute()
    {
        if (!this.villager.isEntityAlive())
        {
            return false;
        }
        else if (this.villager.isInWater())
        {
            return false;
        }
        else if (!this.villager.onGround)
        {
            return false;
        }
        else if (this.villager.velocityChanged)
        {
            return false;
        }
        else
        {
            实体Player entityplayer = this.villager.getCustomer();
            return entityplayer == null ? false : (this.villager.getDistanceSqToEntity(entityplayer) > 16.0D ? false : entityplayer.openContainer instanceof Container);
        }
    }

    public void startExecuting()
    {
        this.villager.getNavigator().clearPathEntity();
    }

    public void resetTask()
    {
        this.villager.setCustomer((实体Player)null);
    }
}
