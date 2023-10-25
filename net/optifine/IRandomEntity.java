package net.optifine;

import net.minecraft.util.阻止位置;
import net.minecraft.world.biome.BiomeGenBase;

public interface IRandomEntity
{
    int getId();

    阻止位置 getSpawnPosition();

    BiomeGenBase getSpawnBiome();

    String getName();

    int getHealth();

    int getMaxHealth();
}
