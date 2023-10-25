package net.minecraft.entity.monster;

import net.minecraft.entity.实体Creature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.world.World;

public abstract class 实体Golem extends 实体Creature implements IAnimals
{
    public 实体Golem(World worldIn)
    {
        super(worldIn);
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected String getLivingSound()
    {
        return "none";
    }

    protected String getHurtSound()
    {
        return "none";
    }

    protected String getDeathSound()
    {
        return "none";
    }

    public int getTalkInterval()
    {
        return 120;
    }

    protected boolean canDespawn()
    {
        return false;
    }
}
