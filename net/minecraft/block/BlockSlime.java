package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
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

    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (entityIn.正在下蹲())
        {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
        else
        {
            entityIn.fall(fallDistance, 0.0F);
        }
    }

    public void onLanded(World worldIn, Entity entityIn)
    {
        if (entityIn.正在下蹲())
        {
            super.onLanded(worldIn, entityIn);
        }
        else if (entityIn.motionY < 0.0D)
        {
            entityIn.motionY = -entityIn.motionY;
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (Math.abs(entityIn.motionY) < 0.1D && !entityIn.正在下蹲())
        {
            double d0 = 0.4D + Math.abs(entityIn.motionY) * 0.2D;
            entityIn.通便X *= d0;
            entityIn.通便Z *= d0;
        }

        super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }
}
