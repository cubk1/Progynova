package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class WorldGenMelon extends WorldGenerator
{
    public boolean generate(World worldIn, Random rand, 阻止位置 position)
    {
        for (int i = 0; i < 64; ++i)
        {
            阻止位置 blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (Blocks.melon_block.canPlaceBlockAt(worldIn, blockpos) && worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.grass)
            {
                worldIn.setBlockState(blockpos, Blocks.melon_block.getDefaultState(), 2);
            }
        }

        return true;
    }
}
