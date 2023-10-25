package net.minecraft.entity.ai;

import net.minecraft.entity.实体;
import net.minecraft.entity.实体Creature;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class EntityAIAttackOnCollide extends EntityAIBase
{
    World worldObj;
    protected 实体Creature attacker;
    int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    PathEntity entityPathEntity;
    Class <? extends 实体> classTarget;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;

    public EntityAIAttackOnCollide(实体Creature creature, Class <? extends 实体> targetClass, double speedIn, boolean useLongMemory)
    {
        this(creature, speedIn, useLongMemory);
        this.classTarget = targetClass;
    }

    public EntityAIAttackOnCollide(实体Creature creature, double speedIn, boolean useLongMemory)
    {
        this.attacker = creature;
        this.worldObj = creature.worldObj;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        实体LivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass()))
        {
            return false;
        }
        else
        {
            this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
            return this.entityPathEntity != null;
        }
    }

    public boolean continueExecuting()
    {
        实体LivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (!this.longMemory ? !this.attacker.getNavigator().noPath() : this.attacker.isWithinHomeDistanceFromPosition(new 阻止位置(entitylivingbase))));
    }

    public void startExecuting()
    {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.delayCounter = 0;
    }

    public void resetTask()
    {
        this.attacker.getNavigator().clearPathEntity();
    }

    public void updateTask()
    {
        实体LivingBase entitylivingbase = this.attacker.getAttackTarget();
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        double d0 = this.attacker.getDistanceSq(entitylivingbase.X坐标, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.Z坐标);
        double d1 = this.func_179512_a(entitylivingbase);
        --this.delayCounter;

        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F))
        {
            this.targetX = entitylivingbase.X坐标;
            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
            this.targetZ = entitylivingbase.Z坐标;
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);

            if (d0 > 1024.0D)
            {
                this.delayCounter += 10;
            }
            else if (d0 > 256.0D)
            {
                this.delayCounter += 5;
            }

            if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget))
            {
                this.delayCounter += 15;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);

        if (d0 <= d1 && this.attackTick <= 0)
        {
            this.attackTick = 20;

            if (this.attacker.getHeldItem() != null)
            {
                this.attacker.swingItem();
            }

            this.attacker.attackEntityAsMob(entitylivingbase);
        }
    }

    protected double func_179512_a(实体LivingBase attackTarget)
    {
        return (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F + attackTarget.width);
    }
}
