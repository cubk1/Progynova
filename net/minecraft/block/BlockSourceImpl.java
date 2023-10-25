package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class BlockSourceImpl implements IBlockSource
{
    private final World worldObj;
    private final 阻止位置 pos;

    public BlockSourceImpl(World worldIn, 阻止位置 posIn)
    {
        this.worldObj = worldIn;
        this.pos = posIn;
    }

    public World getWorld()
    {
        return this.worldObj;
    }

    public double getX()
    {
        return (double)this.pos.getX() + 0.5D;
    }

    public double getY()
    {
        return (double)this.pos.getY() + 0.5D;
    }

    public double getZ()
    {
        return (double)this.pos.getZ() + 0.5D;
    }

    public 阻止位置 getBlockPos()
    {
        return this.pos;
    }

    public int getBlockMetadata()
    {
        IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
        return iblockstate.getBlock().getMetaFromState(iblockstate);
    }

    public <T extends TileEntity> T getBlockTileEntity()
    {
        return (T)this.worldObj.getTileEntity(this.pos);
    }
}
