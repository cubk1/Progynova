package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.*;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase
{
    private static final Logger LOGGER = LogManager.getLogger();
    private 实体Living entityLiving;
    private final Predicate<实体> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private 实体LivingBase entityTarget;

    public EntityAIFindEntityNearestPlayer(实体Living entityLivingIn)
    {
        this.entityLiving = entityLivingIn;

        if (entityLivingIn instanceof 实体Creature)
        {
            LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.predicate = new Predicate<实体>()
        {
            public boolean apply(实体 p_apply_1_)
            {
                if (!(p_apply_1_ instanceof 实体Player))
                {
                    return false;
                }
                else if (((实体Player)p_apply_1_).capabilities.disableDamage)
                {
                    return false;
                }
                else
                {
                    double d0 = EntityAIFindEntityNearestPlayer.this.maxTargetRange();

                    if (p_apply_1_.正在下蹲())
                    {
                        d0 *= 0.800000011920929D;
                    }

                    if (p_apply_1_.isInvisible())
                    {
                        float f = ((实体Player)p_apply_1_).getArmorVisibility();

                        if (f < 0.1F)
                        {
                            f = 0.1F;
                        }

                        d0 *= (double)(0.7F * f);
                    }

                    return (double)p_apply_1_.getDistanceToEntity(EntityAIFindEntityNearestPlayer.this.entityLiving) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.entityLiving, (实体LivingBase)p_apply_1_, false, true);
                }
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(entityLivingIn);
    }

    public boolean shouldExecute()
    {
        double d0 = this.maxTargetRange();
        List<实体Player> list = this.entityLiving.worldObj.<实体Player>getEntitiesWithinAABB(实体Player.class, this.entityLiving.getEntityBoundingBox().expand(d0, 4.0D, d0), this.predicate);
        Collections.sort(list, this.sorter);

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            this.entityTarget = (实体LivingBase)list.get(0);
            return true;
        }
    }

    public boolean continueExecuting()
    {
        实体LivingBase entitylivingbase = this.entityLiving.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else if (entitylivingbase instanceof 实体Player && ((实体Player)entitylivingbase).capabilities.disableDamage)
        {
            return false;
        }
        else
        {
            Team team = this.entityLiving.getTeam();
            Team team1 = entitylivingbase.getTeam();

            if (team != null && team1 == team)
            {
                return false;
            }
            else
            {
                double d0 = this.maxTargetRange();
                return this.entityLiving.getDistanceSqToEntity(entitylivingbase) > d0 * d0 ? false : !(entitylivingbase instanceof 实体PlayerMP) || !((实体PlayerMP)entitylivingbase).theItemInWorldManager.isCreative();
            }
        }
    }

    public void startExecuting()
    {
        this.entityLiving.setAttackTarget(this.entityTarget);
        super.startExecuting();
    }

    public void resetTask()
    {
        this.entityLiving.setAttackTarget((实体LivingBase)null);
        super.startExecuting();
    }

    protected double maxTargetRange()
    {
        IAttributeInstance iattributeinstance = this.entityLiving.getEntityAttribute(SharedMonsterAttributes.followRange);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}
