package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.实体;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class BlockSoulSand extends Block
{
    public BlockSoulSand()
    {
        super(Material.sand, MapColor.brownColor);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, 阻止位置 pos, IBlockState state)
    {
        float f = 0.125F;
        return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)((float)(pos.getY() + 1) - f), (double)(pos.getZ() + 1));
    }

    public void onEntityCollidedWithBlock(World worldIn, 阻止位置 pos, IBlockState state, 实体 实体In)
    {
        实体In.通便X *= 0.4D;
        实体In.通便Z *= 0.4D;
    }
}
