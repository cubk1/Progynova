package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.实体;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockSlime extends BlockBreakable
{
    public BlockSlime()
    {
        super(Material.clay, false, MapColor.grassColor);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.slipperiness = 0.8F;
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    public void onFallenUpon(World worldIn, 阻止位置 pos, 实体 实体In, float fallDistance)
    {
        if (实体In.正在下蹲())
        {
            super.onFallenUpon(worldIn, pos, 实体In, fallDistance);
        }
        else
        {
            实体In.fall(fallDistance, 0.0F);
        }
    }

    public void onLanded(World worldIn, 实体 实体In)
    {
        if (实体In.正在下蹲())
        {
            super.onLanded(worldIn, 实体In);
        }
        else if (实体In.motionY < 0.0D)
        {
            实体In.motionY = -实体In.motionY;
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, 阻止位置 pos, 实体 实体In)
    {
        if (Math.abs(实体In.motionY) < 0.1D && !实体In.正在下蹲())
        {
            double d0 = 0.4D + Math.abs(实体In.motionY) * 0.2D;
            实体In.通便X *= d0;
            实体In.通便Z *= d0;
        }

        super.onEntityCollidedWithBlock(worldIn, pos, 实体In);
    }
}
