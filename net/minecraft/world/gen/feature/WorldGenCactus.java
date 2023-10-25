package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator
{
    public boolean generate(World worldIn, Random rand, 阻止位置 position)
    {
        for (int i = 0; i < 10; ++i)
        {
            阻止位置 blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos))
            {
                int j = 1 + rand.nextInt(rand.nextInt(3) + 1);

                for (int k = 0; k < j; ++k)
                {
                    if (Blocks.cactus.canBlockStay(worldIn, blockpos))
                    {
                        worldIn.setBlockState(blockpos.up(k), Blocks.cactus.getDefaultState(), 2);
                    }
                }
            }
        }

        return true;
    }
}
