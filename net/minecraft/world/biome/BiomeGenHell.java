package net.minecraft.world.biome;

import net.minecraft.entity.monster.实体Ghast;
import net.minecraft.entity.monster.实体MagmaCube;
import net.minecraft.entity.monster.实体PigZombie;

public class BiomeGenHell extends BiomeGenBase
{
    public BiomeGenHell(int id)
    {
        super(id);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(实体Ghast.class, 50, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(实体PigZombie.class, 100, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(实体MagmaCube.class, 1, 4, 4));
    }
}
