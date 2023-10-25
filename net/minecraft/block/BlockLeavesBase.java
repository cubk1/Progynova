package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesBase extends Block
{
    protected boolean fancyGraphics;

    protected BlockLeavesBase(Material materialIn, boolean fancyGraphics)
    {
        super(materialIn);
        this.fancyGraphics = fancyGraphics;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, 阻止位置 pos, EnumFacing side)
    {
        return !this.fancyGraphics && worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }
}
