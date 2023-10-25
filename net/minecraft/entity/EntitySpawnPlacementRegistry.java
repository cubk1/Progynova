package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.HashMap;
import net.minecraft.entity.boss.实体Dragon;
import net.minecraft.entity.boss.实体Wither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.实体Ghast;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.实体Bat;

public class EntitySpawnPlacementRegistry
{
    private static final HashMap<Class, 实体Living.SpawnPlacementType> ENTITY_PLACEMENTS = Maps.<Class, 实体Living.SpawnPlacementType>newHashMap();

    public static 实体Living.SpawnPlacementType getPlacementForEntity(Class entityClass)
    {
        return (实体Living.SpawnPlacementType)ENTITY_PLACEMENTS.get(entityClass);
    }

    static
    {
        ENTITY_PLACEMENTS.put(实体Bat.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Chicken.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Cow.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Horse.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Mooshroom.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Ocelot.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Pig.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Rabbit.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Sheep.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Snowman.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Squid.class, 实体Living.SpawnPlacementType.IN_WATER);
        ENTITY_PLACEMENTS.put(实体IronGolem.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Wolf.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Villager.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Dragon.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Wither.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Blaze.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体CaveSpider.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Creeper.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Enderman.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Endermite.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Ghast.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体GiantZombie.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Guardian.class, 实体Living.SpawnPlacementType.IN_WATER);
        ENTITY_PLACEMENTS.put(实体MagmaCube.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体PigZombie.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Silverfish.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Skeleton.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Slime.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Spider.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Witch.class, 实体Living.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(实体Zombie.class, 实体Living.SpawnPlacementType.ON_GROUND);
    }
}
