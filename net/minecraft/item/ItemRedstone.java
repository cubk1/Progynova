package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.实体;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Blocks;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRedstone extends Item
{
    public ItemRedstone()
    {
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public boolean onItemUse(ItemStack stack, 实体Player playerIn, World worldIn, 阻止位置 pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        阻止位置 blockpos = flag ? pos : pos.offset(side);

        if (!playerIn.canPlayerEdit(blockpos, side, stack))
        {
            return false;
        }
        else
        {
            Block block = worldIn.getBlockState(blockpos).getBlock();

            if (!worldIn.canBlockBePlaced(block, blockpos, false, side, (实体)null, stack))
            {
                return false;
            }
            else if (Blocks.redstone_wire.canPlaceBlockAt(worldIn, blockpos))
            {
                --stack.stackSize;
                worldIn.setBlockState(blockpos, Blocks.redstone_wire.getDefaultState());
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
