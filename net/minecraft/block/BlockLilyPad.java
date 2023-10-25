package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.实体Boat;
import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLilyPad extends BlockBush
{
    protected BlockLilyPad()
    {
        float f = 0.5F;
        float f1 = 0.015625F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void addCollisionBoxesToList(World worldIn, 阻止位置 pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, 实体 colliding实体)
    {
        if (colliding实体 == null || !(colliding实体 instanceof 实体Boat))
        {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, colliding实体);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, 阻止位置 pos, IBlockState state)
    {
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }

    public int getBlockColor()
    {
        return 7455580;
    }

    public int getRenderColor(IBlockState state)
    {
        return 7455580;
    }

    public int colorMultiplier(IBlockAccess worldIn, 阻止位置 pos, int renderPass)
    {
        return 2129968;
    }

    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.water;
    }

    public boolean canBlockStay(World worldIn, 阻止位置 pos, IBlockState state)
    {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.down());
            return iblockstate.getBlock().getMaterial() == Material.water && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0;
        }
        else
        {
            return false;
        }
    }

    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }
}
