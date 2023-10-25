package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.实体;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemLead;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block
{
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockFence(Material materialIn)
    {
        this(materialIn, materialIn.getMaterialMapColor());
    }

    public BlockFence(Material p_i46395_1_, MapColor p_i46395_2_)
    {
        super(p_i46395_1_, p_i46395_2_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void addCollisionBoxesToList(World worldIn, 阻止位置 pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, 实体 colliding实体)
    {
        boolean flag = this.canConnectTo(worldIn, pos.north());
        boolean flag1 = this.canConnectTo(worldIn, pos.south());
        boolean flag2 = this.canConnectTo(worldIn, pos.west());
        boolean flag3 = this.canConnectTo(worldIn, pos.east());
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.375F;
        float f3 = 0.625F;

        if (flag)
        {
            f2 = 0.0F;
        }

        if (flag1)
        {
            f3 = 1.0F;
        }

        if (flag || flag1)
        {
            this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, colliding实体);
        }

        f2 = 0.375F;
        f3 = 0.625F;

        if (flag2)
        {
            f = 0.0F;
        }

        if (flag3)
        {
            f1 = 1.0F;
        }

        if (flag2 || flag3 || !flag && !flag1)
        {
            this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, colliding实体);
        }

        if (flag)
        {
            f2 = 0.0F;
        }

        if (flag1)
        {
            f3 = 1.0F;
        }

        this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, 阻止位置 pos)
    {
        boolean flag = this.canConnectTo(worldIn, pos.north());
        boolean flag1 = this.canConnectTo(worldIn, pos.south());
        boolean flag2 = this.canConnectTo(worldIn, pos.west());
        boolean flag3 = this.canConnectTo(worldIn, pos.east());
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.375F;
        float f3 = 0.625F;

        if (flag)
        {
            f2 = 0.0F;
        }

        if (flag1)
        {
            f3 = 1.0F;
        }

        if (flag2)
        {
            f = 0.0F;
        }

        if (flag3)
        {
            f1 = 1.0F;
        }

        this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, 阻止位置 pos)
    {
        return false;
    }

    public boolean canConnectTo(IBlockAccess worldIn, 阻止位置 pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();
        return block == Blocks.barrier ? false : ((!(block instanceof BlockFence) || block.blockMaterial != this.blockMaterial) && !(block instanceof BlockFenceGate) ? (block.blockMaterial.isOpaque() && block.isFullCube() ? block.blockMaterial != Material.gourd : false) : true);
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, 阻止位置 pos, EnumFacing side)
    {
        return true;
    }

    public boolean onBlockActivated(World worldIn, 阻止位置 pos, IBlockState state, 实体Player playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return worldIn.isRemote ? true : ItemLead.attachToFence(playerIn, worldIn, pos);
    }

    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, 阻止位置 pos)
    {
        return state.withProperty(NORTH, Boolean.valueOf(this.canConnectTo(worldIn, pos.north()))).withProperty(EAST, Boolean.valueOf(this.canConnectTo(worldIn, pos.east()))).withProperty(SOUTH, Boolean.valueOf(this.canConnectTo(worldIn, pos.south()))).withProperty(WEST, Boolean.valueOf(this.canConnectTo(worldIn, pos.west())));
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {NORTH, EAST, WEST, SOUTH});
    }
}
