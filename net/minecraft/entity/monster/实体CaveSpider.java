package net.minecraft.entity.monster;

import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class 实体CaveSpider extends 实体Spider
{
    public 实体CaveSpider(World worldIn)
    {
        super(worldIn);
        this.setSize(0.7F, 0.5F);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D);
    }

    public boolean attackEntityAsMob(实体 实体In)
    {
        if (super.attackEntityAsMob(实体In))
        {
            if (实体In instanceof 实体LivingBase)
            {
                int i = 0;

                if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL)
                {
                    i = 7;
                }
                else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
                {
                    i = 15;
                }

                if (i > 0)
                {
                    ((实体LivingBase) 实体In).addPotionEffect(new PotionEffect(Potion.poison.id, i * 20, 0));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        return livingdata;
    }

    public float getEyeHeight()
    {
        return 0.45F;
    }
}
