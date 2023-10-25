package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.实体ExpBottle;
import net.minecraft.entity.player.实体Player;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemExpBottle extends Item
{
    public ItemExpBottle()
    {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, 实体Player playerIn)
    {
        if (!playerIn.capabilities.isCreativeMode)
        {
            --itemStackIn.stackSize;
        }

        worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            worldIn.spawnEntityInWorld(new 实体ExpBottle(worldIn, playerIn));
        }

        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}
