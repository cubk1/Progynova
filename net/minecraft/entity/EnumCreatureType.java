package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.实体AmbientCreature;
import net.minecraft.entity.passive.实体Animal;
import net.minecraft.entity.passive.实体WaterMob;
import net.minecraft.entity.passive.IAnimals;

public enum EnumCreatureType
{
    MONSTER(IMob.class, 70, Material.air, false, false),
    CREATURE(实体Animal.class, 10, Material.air, true, true),
    AMBIENT(实体AmbientCreature.class, 15, Material.air, true, false),
    WATER_CREATURE(实体WaterMob.class, 5, Material.water, true, false);

    private final Class <? extends IAnimals > creatureClass;
    private final int maxNumberOfCreature;
    private final Material creatureMaterial;
    private final boolean isPeacefulCreature;
    private final boolean isAnimal;

    private EnumCreatureType(Class <? extends IAnimals > creatureClassIn, int maxNumberOfCreatureIn, Material creatureMaterialIn, boolean isPeacefulCreatureIn, boolean isAnimalIn)
    {
        this.creatureClass = creatureClassIn;
        this.maxNumberOfCreature = maxNumberOfCreatureIn;
        this.creatureMaterial = creatureMaterialIn;
        this.isPeacefulCreature = isPeacefulCreatureIn;
        this.isAnimal = isAnimalIn;
    }

    public Class <? extends IAnimals > getCreatureClass()
    {
        return this.creatureClass;
    }

    public int getMaxNumberOfCreature()
    {
        return this.maxNumberOfCreature;
    }

    public boolean getPeacefulCreature()
    {
        return this.isPeacefulCreature;
    }

    public boolean getAnimal()
    {
        return this.isAnimal;
    }
}
