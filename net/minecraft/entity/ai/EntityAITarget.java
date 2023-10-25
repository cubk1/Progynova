package net.minecraft.entity.ai;

import net.minecraft.entity.*;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public abstract class EntityAITarget extends EntityAIBase
{
    protected final 实体Creature taskOwner;
    protected boolean shouldCheckSight;
    private boolean nearbyOnly;
    private int targetSearchStatus;
    private int targetSearchDelay;
    private int targetUnseenTicks;

    public EntityAITarget(实体Creature creature, boolean checkSight)
    {
        this(creature, checkSight, false);
    }

    public EntityAITarget(实体Creature creature, boolean checkSight, boolean onlyNearby)
    {
        this.taskOwner = creature;
        this.shouldCheckSight = checkSight;
        this.nearbyOnly = onlyNearby;
    }

    public boolean continueExecuting()
    {
        实体LivingBase entitylivingbase = this.taskOwner.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else
        {
            Team team = this.taskOwner.getTeam();
            Team team1 = entitylivingbase.getTeam();

            if (team != null && team1 == team)
            {
                return false;
            }
            else
            {
                double d0 = this.getTargetDistance();

                if (this.taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0)
                {
                    return false;
                }
                else
                {
                    if (this.shouldCheckSight)
                    {
                        if (this.taskOwner.getEntitySenses().canSee(entitylivingbase))
                        {
                            this.targetUnseenTicks = 0;
                        }
                        else if (++this.targetUnseenTicks > 60)
                        {
                            return false;
                        }
                    }

                    return !(entitylivingbase instanceof 实体Player) || !((实体Player)entitylivingbase).capabilities.disableDamage;
                }
            }
        }
    }

    protected double getTargetDistance()
    {
        IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }

    public void startExecuting()
    {
        this.targetSearchStatus = 0;
        this.targetSearchDelay = 0;
        this.targetUnseenTicks = 0;
    }

    public void resetTask()
    {
        this.taskOwner.setAttackTarget((实体LivingBase)null);
    }

    public static boolean isSuitableTarget(实体Living attacker, 实体LivingBase target, boolean includeInvincibles, boolean checkSight)
    {
        if (target == null)
        {
            return false;
        }
        else if (target == attacker)
        {
            return false;
        }
        else if (!target.isEntityAlive())
        {
            return false;
        }
        else if (!attacker.canAttackClass(target.getClass()))
        {
            return false;
        }
        else
        {
            Team team = attacker.getTeam();
            Team team1 = target.getTeam();

            if (team != null && team1 == team)
            {
                return false;
            }
            else
            {
                if (attacker instanceof IEntityOwnable && StringUtils.isNotEmpty(((IEntityOwnable)attacker).getOwnerId()))
                {
                    if (target instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId().equals(((IEntityOwnable)target).getOwnerId()))
                    {
                        return false;
                    }

                    if (target == ((IEntityOwnable)attacker).getOwner())
                    {
                        return false;
                    }
                }
                else if (target instanceof 实体Player && !includeInvincibles && ((实体Player)target).capabilities.disableDamage)
                {
                    return false;
                }

                return !checkSight || attacker.getEntitySenses().canSee(target);
            }
        }
    }

    protected boolean isSuitableTarget(实体LivingBase target, boolean includeInvincibles)
    {
        if (!isSuitableTarget(this.taskOwner, target, includeInvincibles, this.shouldCheckSight))
        {
            return false;
        }
        else if (!this.taskOwner.isWithinHomeDistanceFromPosition(new 阻止位置(target)))
        {
            return false;
        }
        else
        {
            if (this.nearbyOnly)
            {
                if (--this.targetSearchDelay <= 0)
                {
                    this.targetSearchStatus = 0;
                }

                if (this.targetSearchStatus == 0)
                {
                    this.targetSearchStatus = this.canEasilyReach(target) ? 1 : 2;
                }

                if (this.targetSearchStatus == 2)
                {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean canEasilyReach(实体LivingBase target)
    {
        this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
        PathEntity pathentity = this.taskOwner.getNavigator().getPathToEntityLiving(target);

        if (pathentity == null)
        {
            return false;
        }
        else
        {
            PathPoint pathpoint = pathentity.getFinalPathPoint();

            if (pathpoint == null)
            {
                return false;
            }
            else
            {
                int i = pathpoint.xCoord - MathHelper.floor_double(target.X坐标);
                int j = pathpoint.zCoord - MathHelper.floor_double(target.Z坐标);
                return (double)(i * i + j * j) <= 2.25D;
            }
        }
    }
}
