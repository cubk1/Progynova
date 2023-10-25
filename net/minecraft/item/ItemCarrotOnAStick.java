package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.实体Pig;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item
{
    public ItemCarrotOnAStick()
    {
        this.setCreativeTab(CreativeTabs.tabTransport);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
    }

    public boolean isFull3D()
    {
        return true;
    }

    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, 实体Player playerIn)
    {
        if (playerIn.isRiding() && playerIn.riding实体 instanceof 实体Pig)
        {
            实体Pig entitypig = (实体Pig)playerIn.riding实体;

            if (entitypig.getAIControlledByPlayer().isControlledByPlayer() && itemStackIn.getMaxDamage() - itemStackIn.getMetadata() >= 7)
            {
                entitypig.getAIControlledByPlayer().boostSpeed();
                itemStackIn.damageItem(7, playerIn);

                if (itemStackIn.stackSize == 0)
                {
                    ItemStack itemstack = new ItemStack(Items.fishing_rod);
                    itemstack.setTagCompound(itemStackIn.getTagCompound());
                    return itemstack;
                }
            }
        }

        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}
