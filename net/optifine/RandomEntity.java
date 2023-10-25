package net.optifine;

import java.util.UUID;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体Living;
import net.minecraft.util.阻止位置;
import net.minecraft.world.biome.BiomeGenBase;

public class RandomEntity implements IRandomEntity
{
    private 实体 实体;

    public int getId()
    {
        UUID uuid = this.实体.getUniqueID();
        long i = uuid.getLeastSignificantBits();
        int j = (int)(i & 2147483647L);
        return j;
    }

    public 阻止位置 getSpawnPosition()
    {
        return this.实体.getDataWatcher().spawnPosition;
    }

    public BiomeGenBase getSpawnBiome()
    {
        return this.实体.getDataWatcher().spawnBiome;
    }

    public String getName()
    {
        return this.实体.hasCustomName() ? this.实体.getCustomNameTag() : null;
    }

    public int getHealth()
    {
        if (!(this.实体 instanceof 实体Living))
        {
            return 0;
        }
        else
        {
            实体Living entityliving = (实体Living)this.实体;
            return (int)entityliving.getHealth();
        }
    }

    public int getMaxHealth()
    {
        if (!(this.实体 instanceof 实体Living))
        {
            return 0;
        }
        else
        {
            实体Living entityliving = (实体Living)this.实体;
            return (int)entityliving.getMaxHealth();
        }
    }

    public 实体 getEntity()
    {
        return this.实体;
    }

    public void setEntity(实体 实体)
    {
        this.实体 = 实体;
    }
}
