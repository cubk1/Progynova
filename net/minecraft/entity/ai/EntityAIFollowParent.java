package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.passive.实体Animal;

public class EntityAIFollowParent extends EntityAIBase
{
    实体Animal childAnimal;
    实体Animal parentAnimal;
    double moveSpeed;
    private int delayCounter;

    public EntityAIFollowParent(实体Animal animal, double speed)
    {
        this.childAnimal = animal;
        this.moveSpeed = speed;
    }

    public boolean shouldExecute()
    {
        if (this.childAnimal.getGrowingAge() >= 0)
        {
            return false;
        }
        else
        {
            List<实体Animal> list = this.childAnimal.worldObj.<实体Animal>getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().expand(8.0D, 4.0D, 8.0D));
            实体Animal entityanimal = null;
            double d0 = Double.MAX_VALUE;

            for (实体Animal entityanimal1 : list)
            {
                if (entityanimal1.getGrowingAge() >= 0)
                {
                    double d1 = this.childAnimal.getDistanceSqToEntity(entityanimal1);

                    if (d1 <= d0)
                    {
                        d0 = d1;
                        entityanimal = entityanimal1;
                    }
                }
            }

            if (entityanimal == null)
            {
                return false;
            }
            else if (d0 < 9.0D)
            {
                return false;
            }
            else
            {
                this.parentAnimal = entityanimal;
                return true;
            }
        }
    }

    public boolean continueExecuting()
    {
        if (this.childAnimal.getGrowingAge() >= 0)
        {
            return false;
        }
        else if (!this.parentAnimal.isEntityAlive())
        {
            return false;
        }
        else
        {
            double d0 = this.childAnimal.getDistanceSqToEntity(this.parentAnimal);
            return d0 >= 9.0D && d0 <= 256.0D;
        }
    }

    public void startExecuting()
    {
        this.delayCounter = 0;
    }

    public void resetTask()
    {
        this.parentAnimal = null;
    }

    public void updateTask()
    {
        if (--this.delayCounter <= 0)
        {
            this.delayCounter = 10;
            this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
        }
    }
}
