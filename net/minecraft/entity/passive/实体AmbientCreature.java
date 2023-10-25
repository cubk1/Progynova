package net.minecraft.entity.passive;

import net.minecraft.entity.实体Living;
import net.minecraft.entity.player.实体Player;
import net.minecraft.world.World;

public abstract class 实体AmbientCreature extends 实体Living implements IAnimals
{
    public 实体AmbientCreature(World worldIn)
    {
        super(worldIn);
    }

    public boolean allowLeashing()
    {
        return false;
    }

    protected boolean interact(实体Player player)
    {
        return false;
    }
}
