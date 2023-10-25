package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class 实体GiantZombie extends 实体Mob
{
    public 实体GiantZombie(World worldIn)
    {
        super(worldIn);
        this.setSize(this.width * 6.0F, this.height * 6.0F);
    }

    public float getEyeHeight()
    {
        return 10.440001F;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(50.0D);
    }

    public float getBlockPathWeight(阻止位置 pos)
    {
        return this.worldObj.getLightBrightness(pos) - 0.5F;
    }
}
