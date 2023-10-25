package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.实体LeashKnot;
import net.minecraft.entity.实体Living;
import net.minecraft.entity.player.实体Player;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemLead extends Item
{
    public ItemLead()
    {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public boolean onItemUse(ItemStack stack, 实体Player playerIn, World worldIn, 阻止位置 pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block instanceof BlockFence)
        {
            if (worldIn.isRemote)
            {
                return true;
            }
            else
            {
                attachToFence(playerIn, worldIn, pos);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public static boolean attachToFence(实体Player player, World worldIn, 阻止位置 fence)
    {
        实体LeashKnot entityleashknot = 实体LeashKnot.getKnotForPosition(worldIn, fence);
        boolean flag = false;
        double d0 = 7.0D;
        int i = fence.getX();
        int j = fence.getY();
        int k = fence.getZ();

        for (实体Living entityliving : worldIn.getEntitiesWithinAABB(实体Living.class, new AxisAlignedBB((double)i - d0, (double)j - d0, (double)k - d0, (double)i + d0, (double)j + d0, (double)k + d0)))
        {
            if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == player)
            {
                if (entityleashknot == null)
                {
                    entityleashknot = 实体LeashKnot.createKnot(worldIn, fence);
                }

                entityliving.setLeashedToEntity(entityleashknot, true);
                flag = true;
            }
        }

        return flag;
    }
}
