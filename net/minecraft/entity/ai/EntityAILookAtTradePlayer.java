package net.minecraft.entity.ai;

import net.minecraft.entity.passive.实体Villager;
import net.minecraft.entity.player.实体Player;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest
{
    private final 实体Villager theMerchant;

    public EntityAILookAtTradePlayer(实体Villager theMerchantIn)
    {
        super(theMerchantIn, 实体Player.class, 8.0F);
        this.theMerchant = theMerchantIn;
    }

    public boolean shouldExecute()
    {
        if (this.theMerchant.isTrading())
        {
            this.closest实体 = this.theMerchant.getCustomer();
            return true;
        }
        else
        {
            return false;
        }
    }
}
