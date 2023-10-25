package net.minecraft.entity.ai;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.实体Animal;
import net.minecraft.entity.实体Ageable;
import net.minecraft.entity.item.实体XPOrb;
import net.minecraft.entity.passive.实体Cow;
import net.minecraft.entity.player.实体Player;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityAIMate extends EntityAIBase
{
    private 实体Animal theAnimal;
    World theWorld;
    private 实体Animal targetMate;
    int spawnBabyDelay;
    double moveSpeed;

    public EntityAIMate(实体Animal animal, double speedIn)
    {
        this.theAnimal = animal;
        this.theWorld = animal.worldObj;
        this.moveSpeed = speedIn;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        if (!this.theAnimal.isInLove())
        {
            return false;
        }
        else
        {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    public boolean continueExecuting()
    {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    public void resetTask()
    {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    public void updateTask()
    {
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float)this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D)
        {
            this.spawnBaby();
        }
    }

    private 实体Animal getNearbyMate()
    {
        float f = 8.0F;
        List<实体Animal> list = this.theWorld.<实体Animal>getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expand((double)f, (double)f, (double)f));
        double d0 = Double.MAX_VALUE;
        实体Animal entityanimal = null;

        for (实体Animal entityanimal1 : list)
        {
            if (this.theAnimal.canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity(entityanimal1) < d0)
            {
                entityanimal = entityanimal1;
                d0 = this.theAnimal.getDistanceSqToEntity(entityanimal1);
            }
        }

        return entityanimal;
    }

    private void spawnBaby()
    {
        实体Ageable entityageable = this.theAnimal.createChild(this.targetMate);

        if (entityageable != null)
        {
            实体Player entityplayer = this.theAnimal.getPlayerInLove();

            if (entityplayer == null && this.targetMate.getPlayerInLove() != null)
            {
                entityplayer = this.targetMate.getPlayerInLove();
            }

            if (entityplayer != null)
            {
                entityplayer.triggerAchievement(StatList.animalsBredStat);

                if (this.theAnimal instanceof 实体Cow)
                {
                    entityplayer.triggerAchievement(AchievementList.breedCow);
                }
            }

            this.theAnimal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.theAnimal.X坐标, this.theAnimal.Y坐标, this.theAnimal.Z坐标, 0.0F, 0.0F);
            this.theWorld.spawnEntityInWorld(entityageable);
            Random random = this.theAnimal.getRNG();

            for (int i = 0; i < 7; ++i)
            {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
                double d4 = 0.5D + random.nextDouble() * (double)this.theAnimal.height;
                double d5 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.X坐标 + d3, this.theAnimal.Y坐标 + d4, this.theAnimal.Z坐标 + d5, d0, d1, d2, new int[0]);
            }

            if (this.theWorld.getGameRules().getBoolean("doMobLoot"))
            {
                this.theWorld.spawnEntityInWorld(new 实体XPOrb(this.theWorld, this.theAnimal.X坐标, this.theAnimal.Y坐标, this.theAnimal.Z坐标, random.nextInt(7) + 1));
            }
        }
    }
}
