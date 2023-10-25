package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.*;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.entity.实体Living;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.实体LivingBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase
{
    private static final Logger LOGGER = LogManager.getLogger();
    private 实体Living mob;
    private final Predicate<实体LivingBase> field_179443_c;
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;
    private 实体LivingBase target;
    private Class <? extends 实体LivingBase> field_179439_f;

    public EntityAIFindEntityNearest(实体Living mobIn, Class <? extends 实体LivingBase> p_i45884_2_)
    {
        this.mob = mobIn;
        this.field_179439_f = p_i45884_2_;

        if (mobIn instanceof 实体Creature)
        {
            LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.field_179443_c = new Predicate<实体LivingBase>()
        {
            public boolean apply(实体LivingBase p_apply_1_)
            {
                double d0 = EntityAIFindEntityNearest.this.getFollowRange();

                if (p_apply_1_.正在下蹲())
                {
                    d0 *= 0.800000011920929D;
                }

                return p_apply_1_.isInvisible() ? false : ((double)p_apply_1_.getDistanceToEntity(EntityAIFindEntityNearest.this.mob) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.mob, p_apply_1_, false, true));
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(mobIn);
    }

    public boolean shouldExecute()
    {
        double d0 = this.getFollowRange();
        List<实体LivingBase> list = this.mob.worldObj.<实体LivingBase>getEntitiesWithinAABB(this.field_179439_f, this.mob.getEntityBoundingBox().expand(d0, 4.0D, d0), this.field_179443_c);
        Collections.sort(list, this.field_179440_d);

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            this.target = (实体LivingBase)list.get(0);
            return true;
        }
    }

    public boolean continueExecuting()
    {
        实体LivingBase entitylivingbase = this.mob.getAttackTarget();

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
            double d0 = this.getFollowRange();
            return this.mob.getDistanceSqToEntity(entitylivingbase) > d0 * d0 ? false : !(entitylivingbase instanceof 实体PlayerMP) || !((实体PlayerMP)entitylivingbase).theItemInWorldManager.isCreative();
        }
    }

    public void startExecuting()
    {
        this.mob.setAttackTarget(this.target);
        super.startExecuting();
    }

    public void resetTask()
    {
        this.mob.setAttackTarget((实体LivingBase)null);
        super.startExecuting();
    }

    protected double getFollowRange()
    {
        IAttributeInstance iattributeinstance = this.mob.getEntityAttribute(SharedMonsterAttributes.followRange);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}
