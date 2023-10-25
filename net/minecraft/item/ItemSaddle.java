package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.passive.实体Pig;

public class ItemSaddle extends Item
{
    public ItemSaddle()
    {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    public boolean itemInteractionForEntity(ItemStack stack, 实体Player playerIn, 实体LivingBase target)
    {
        if (target instanceof 实体Pig)
        {
            实体Pig entitypig = (实体Pig)target;

            if (!entitypig.getSaddled() && !entitypig.isChild())
            {
                entitypig.setSaddled(true);
                entitypig.worldObj.playSoundAtEntity(entitypig, "mob.horse.leather", 0.5F, 1.0F);
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean hitEntity(ItemStack stack, 实体LivingBase target, 实体LivingBase attacker)
    {
        this.itemInteractionForEntity(stack, (实体Player)null, target);
        return true;
    }
}
