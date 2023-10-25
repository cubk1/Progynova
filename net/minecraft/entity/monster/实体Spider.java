package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.阻止位置;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class 实体Spider extends 实体Mob
{
    public 实体Spider(World worldIn)
    {
        super(worldIn);
        this.setSize(1.4F, 0.9F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new 实体Spider.AISpiderAttack(this, 实体Player.class));
        this.tasks.addTask(4, new 实体Spider.AISpiderAttack(this, 实体IronGolem.class));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, 实体Player.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new 实体Spider.AISpiderTarget(this, 实体Player.class));
        this.targetTasks.addTask(3, new 实体Spider.AISpiderTarget(this, 实体IronGolem.class));
    }

    public double getMountedYOffset()
    {
        return (double)(this.height * 0.5F);
    }

    protected PathNavigate getNewNavigator(World worldIn)
    {
        return new PathNavigateClimber(this, worldIn);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
    }

    protected String getLivingSound()
    {
        return "mob.spider.say";
    }

    protected String getHurtSound()
    {
        return "mob.spider.say";
    }

    protected String getDeathSound()
    {
        return "mob.spider.death";
    }

    protected void playStepSound(阻止位置 pos, Block blockIn)
    {
        this.playSound("mob.spider.step", 0.15F, 1.0F);
    }

    protected Item getDropItem()
    {
        return Items.string;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        super.dropFewItems(wasRecentlyHit, lootingModifier);

        if (wasRecentlyHit && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + lootingModifier) > 0))
        {
            this.dropItem(Items.spider_eye, 1);
        }
    }

    public boolean isOnLadder()
    {
        return this.isBesideClimbableBlock();
    }

    public void setInWeb()
    {
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn)
    {
        return potioneffectIn.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(potioneffectIn);
    }

    public boolean isBesideClimbableBlock()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean p_70839_1_)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70839_1_)
        {
            b0 = (byte)(b0 | 1);
        }
        else
        {
            b0 = (byte)(b0 & -2);
        }

        this.dataWatcher.updateObject(16, Byte.valueOf(b0));
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (this.worldObj.rand.nextInt(100) == 0)
        {
            实体Skeleton entityskeleton = new 实体Skeleton(this.worldObj);
            entityskeleton.setLocationAndAngles(this.X坐标, this.Y坐标, this.Z坐标, this.旋转侧滑, 0.0F);
            entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
            this.worldObj.spawnEntityInWorld(entityskeleton);
            entityskeleton.mountEntity(this);
        }

        if (livingdata == null)
        {
            livingdata = new 实体Spider.GroupData();

            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
            {
                ((实体Spider.GroupData)livingdata).func_111104_a(this.worldObj.rand);
            }
        }

        if (livingdata instanceof 实体Spider.GroupData)
        {
            int i = ((实体Spider.GroupData)livingdata).potionEffectId;

            if (i > 0 && Potion.potionTypes[i] != null)
            {
                this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
            }
        }

        return livingdata;
    }

    public float getEyeHeight()
    {
        return 0.65F;
    }

    static class AISpiderAttack extends EntityAIAttackOnCollide
    {
        public AISpiderAttack(实体Spider spider, Class <? extends 实体> targetClass)
        {
            super(spider, targetClass, 1.0D, true);
        }

        public boolean continueExecuting()
        {
            float f = this.attacker.getBrightness(1.0F);

            if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0)
            {
                this.attacker.setAttackTarget((实体LivingBase)null);
                return false;
            }
            else
            {
                return super.continueExecuting();
            }
        }

        protected double func_179512_a(实体LivingBase attackTarget)
        {
            return (double)(4.0F + attackTarget.width);
        }
    }

    static class AISpiderTarget<T extends 实体LivingBase> extends EntityAINearestAttackableTarget
    {
        public AISpiderTarget(实体Spider spider, Class<T> classTarget)
        {
            super(spider, classTarget, true);
        }

        public boolean shouldExecute()
        {
            float f = this.taskOwner.getBrightness(1.0F);
            return f >= 0.5F ? false : super.shouldExecute();
        }
    }

    public static class GroupData implements IEntityLivingData
    {
        public int potionEffectId;

        public void func_111104_a(Random rand)
        {
            int i = rand.nextInt(5);

            if (i <= 1)
            {
                this.potionEffectId = Potion.moveSpeed.id;
            }
            else if (i <= 2)
            {
                this.potionEffectId = Potion.damageBoost.id;
            }
            else if (i <= 3)
            {
                this.potionEffectId = Potion.regeneration.id;
            }
            else if (i <= 4)
            {
                this.potionEffectId = Potion.invisibility.id;
            }
        }
    }
}
