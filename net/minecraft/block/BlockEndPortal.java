package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.实体;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndPortal extends BlockContainer
{
    protected BlockEndPortal(Material materialIn)
    {
        super(materialIn);
        this.setLightLevel(1.0F);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityEndPortal();
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, 阻止位置 pos)
    {
        float f = 0.0625F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, 阻止位置 pos, EnumFacing side)
    {
        return side == EnumFacing.DOWN ? super.shouldSideBeRendered(worldIn, pos, side) : false;
    }

    public void addCollisionBoxesToList(World worldIn, 阻止位置 pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, 实体 colliding实体)
    {
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void onEntityCollidedWithBlock(World worldIn, 阻止位置 pos, IBlockState state, 实体 实体In)
    {
        if (实体In.riding实体 == null && 实体In.riddenBy实体 == null && !worldIn.isRemote)
        {
            实体In.travelToDimension(1);
        }
    }

    public void randomDisplayTick(World worldIn, 阻止位置 pos, IBlockState state, Random rand)
    {
        double d0 = (double)((float)pos.getX() + rand.nextFloat());
        double d1 = (double)((float)pos.getY() + 0.8F);
        double d2 = (double)((float)pos.getZ() + rand.nextFloat());
        double d3 = 0.0D;
        double d4 = 0.0D;
        double d5 = 0.0D;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
    }

    public Item getItem(World worldIn, 阻止位置 pos)
    {
        return null;
    }

    public MapColor getMapColor(IBlockState state)
    {
        return MapColor.blackColor;
    }
}
