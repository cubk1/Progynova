package net.minecraft.entity;

import net.minecraft.entity.boss.实体DragonPart;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPart
{
    World getWorld();

    boolean attackEntityFromPart(实体DragonPart dragonPart, DamageSource source, float p_70965_3_);
}
