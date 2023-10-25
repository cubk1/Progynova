package net.minecraft.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;

public interface IBlockAccess
{
    TileEntity getTileEntity(阻止位置 pos);

    int getCombinedLight(阻止位置 pos, int lightValue);

    IBlockState getBlockState(阻止位置 pos);

    boolean isAirBlock(阻止位置 pos);

    BiomeGenBase getBiomeGenForCoords(阻止位置 pos);

    boolean extendedLevelsInChunkCache();

    int getStrongPower(阻止位置 pos, EnumFacing direction);

    WorldType getWorldType();
}
