package net.minecraft.world.biome;

import net.minecraft.util.阻止位置;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper
{
    private static final BiomeColorHelper.ColorResolver GRASS_COLOR = new BiomeColorHelper.ColorResolver()
    {
        public int getColorAtPos(BiomeGenBase biome, 阻止位置 blockPosition)
        {
            return biome.getGrassColorAtPos(blockPosition);
        }
    };
    private static final BiomeColorHelper.ColorResolver FOLIAGE_COLOR = new BiomeColorHelper.ColorResolver()
    {
        public int getColorAtPos(BiomeGenBase biome, 阻止位置 blockPosition)
        {
            return biome.getFoliageColorAtPos(blockPosition);
        }
    };
    private static final BiomeColorHelper.ColorResolver WATER_COLOR_MULTIPLIER = new BiomeColorHelper.ColorResolver()
    {
        public int getColorAtPos(BiomeGenBase biome, 阻止位置 blockPosition)
        {
            return biome.waterColorMultiplier;
        }
    };

    private static int getColorAtPos(IBlockAccess blockAccess, 阻止位置 pos, BiomeColorHelper.ColorResolver colorResolver)
    {
        int i = 0;
        int j = 0;
        int k = 0;

        for (阻止位置.Mutable阻止位置 blockpos$mutableblockpos : 阻止位置.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1)))
        {
            int l = colorResolver.getColorAtPos(blockAccess.getBiomeGenForCoords(blockpos$mutableblockpos), blockpos$mutableblockpos);
            i += (l & 16711680) >> 16;
            j += (l & 65280) >> 8;
            k += l & 255;
        }

        return (i / 9 & 255) << 16 | (j / 9 & 255) << 8 | k / 9 & 255;
    }

    public static int getGrassColorAtPos(IBlockAccess p_180286_0_, 阻止位置 p_180286_1_)
    {
        return getColorAtPos(p_180286_0_, p_180286_1_, GRASS_COLOR);
    }

    public static int getFoliageColorAtPos(IBlockAccess p_180287_0_, 阻止位置 p_180287_1_)
    {
        return getColorAtPos(p_180287_0_, p_180287_1_, FOLIAGE_COLOR);
    }

    public static int getWaterColorAtPos(IBlockAccess p_180288_0_, 阻止位置 p_180288_1_)
    {
        return getColorAtPos(p_180288_0_, p_180288_1_, WATER_COLOR_MULTIPLIER);
    }

    interface ColorResolver
    {
        int getColorAtPos(BiomeGenBase biome, 阻止位置 blockPosition);
    }
}
