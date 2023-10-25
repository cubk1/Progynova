package net.minecraft.dispenser;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.阻止位置;

public interface IBlockSource extends ILocatableSource
{
    double getX();

    double getY();

    double getZ();

    阻止位置 getBlockPos();

    int getBlockMetadata();

    <T extends TileEntity> T getBlockTileEntity();
}
