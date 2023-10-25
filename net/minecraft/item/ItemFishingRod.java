package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.projectile.实体FishHook;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemFishingRod extends Item
{
    public ItemFishingRod()
    {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
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
        if (playerIn.fishEntity != null)
        {
            int i = playerIn.fishEntity.handleHookRetraction();
            itemStackIn.damageItem(i, playerIn);
            playerIn.swingItem();
        }
        else
        {
            worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote)
            {
                worldIn.spawnEntityInWorld(new 实体FishHook(worldIn, playerIn));
            }

            playerIn.swingItem();
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }

        return itemStackIn;
    }

    public boolean isItemTool(ItemStack stack)
    {
        return super.isItemTool(stack);
    }

    public int getItemEnchantability()
    {
        return 1;
    }
}
