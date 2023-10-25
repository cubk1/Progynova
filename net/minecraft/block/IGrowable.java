package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public interface IGrowable
{
    boolean canGrow(World worldIn, 阻止位置 pos, IBlockState state, boolean isClient);

    boolean canUseBonemeal(World worldIn, Random rand, 阻止位置 pos, IBlockState state);

    void grow(World worldIn, Random rand, 阻止位置 pos, IBlockState state);
}
