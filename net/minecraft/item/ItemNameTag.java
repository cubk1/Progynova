package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体Living;
import net.minecraft.entity.实体LivingBase;

public class ItemNameTag extends Item
{
    public ItemNameTag()
    {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public boolean itemInteractionForEntity(ItemStack stack, 实体Player playerIn, 实体LivingBase target)
    {
        if (!stack.hasDisplayName())
        {
            return false;
        }
        else if (target instanceof 实体Living)
        {
            实体Living entityliving = (实体Living)target;
            entityliving.setCustomNameTag(stack.getDisplayName());
            entityliving.enablePersistence();
            --stack.stackSize;
            return true;
        }
        else
        {
            return super.itemInteractionForEntity(stack, playerIn, target);
        }
    }
}
