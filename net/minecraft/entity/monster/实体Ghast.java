package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.实体Flying;
import net.minecraft.entity.projectile.实体LargeFireball;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class 实体Ghast extends 实体Flying implements IMob
{
    private int explosionStrength = 1;

    public 实体Ghast(World worldIn)
    {
        super(worldIn);
        this.setSize(4.0F, 4.0F);
        this.isImmuneToFire = true;
        this.experienceValue = 5;
        this.moveHelper = new 实体Ghast.GhastMoveHelper(this);
        this.tasks.addTask(5, new 实体Ghast.AIRandomFly(this));
        this.tasks.addTask(7, new 实体Ghast.AILookAround(this));
        this.tasks.addTask(7, new 实体Ghast.AIFireballAttack(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }

    public boolean isAttacking()
    {
        return this.dataWatcher.getWatchableObjectByte(16) != 0;
    }

    public void setAttacking(boolean attacking)
    {
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)(attacking ? 1 : 0)));
    }

    public int getFireballStrength()
    {
        return this.explosionStrength;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            this.setDead();
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else if ("fireball".equals(source.getDamageType()) && source.getEntity() instanceof 实体Player)
        {
            super.attackEntityFrom(source, 1000.0F);
            ((实体Player)source.getEntity()).triggerAchievement(AchievementList.ghast);
            return true;
        }
        else
        {
            return super.attackEntityFrom(source, amount);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
    }

    protected String getLivingSound()
    {
        return "mob.ghast.moan";
    }

    protected String getHurtSound()
    {
        return "mob.ghast.scream";
    }

    protected String getDeathSound()
    {
        return "mob.ghast.death";
    }

    protected Item getDropItem()
    {
        return Items.gunpowder;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        int i = this.rand.nextInt(2) + this.rand.nextInt(1 + lootingModifier);

        for (int j = 0; j < i; ++j)
        {
            this.dropItem(Items.ghast_tear, 1);
        }

        i = this.rand.nextInt(3) + this.rand.nextInt(1 + lootingModifier);

        for (int k = 0; k < i; ++k)
        {
            this.dropItem(Items.gunpowder, 1);
        }
    }

    protected float getSoundVolume()
    {
        return 10.0F;
    }

    public boolean getCanSpawnHere()
    {
        return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionStrength);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("ExplosionPower", 99))
        {
            this.explosionStrength = tagCompund.getInteger("ExplosionPower");
        }
    }

    public float getEyeHeight()
    {
        return 2.6F;
    }

    static class AIFireballAttack extends EntityAIBase
    {
        private 实体Ghast parentEntity;
        public int attackTimer;

        public AIFireballAttack(实体Ghast ghast)
        {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute()
        {
            return this.parentEntity.getAttackTarget() != null;
        }

        public void startExecuting()
        {
            this.attackTimer = 0;
        }

        public void resetTask()
        {
            this.parentEntity.setAttacking(false);
        }

        public void updateTask()
        {
            实体LivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double d0 = 64.0D;

            if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < d0 * d0 && this.parentEntity.canEntityBeSeen(entitylivingbase))
            {
                World world = this.parentEntity.worldObj;
                ++this.attackTimer;

                if (this.attackTimer == 10)
                {
                    world.playAuxSFXAtEntity((实体Player)null, 1007, new 阻止位置(this.parentEntity), 0);
                }

                if (this.attackTimer == 20)
                {
                    double d1 = 4.0D;
                    Vec3 vec3 = this.parentEntity.getLook(1.0F);
                    double d2 = entitylivingbase.X坐标 - (this.parentEntity.X坐标 + vec3.xCoord * d1);
                    double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + this.parentEntity.Y坐标 + (double)(this.parentEntity.height / 2.0F));
                    double d4 = entitylivingbase.Z坐标 - (this.parentEntity.Z坐标 + vec3.zCoord * d1);
                    world.playAuxSFXAtEntity((实体Player)null, 1008, new 阻止位置(this.parentEntity), 0);
                    实体LargeFireball entitylargefireball = new 实体LargeFireball(world, this.parentEntity, d2, d3, d4);
                    entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
                    entitylargefireball.X坐标 = this.parentEntity.X坐标 + vec3.xCoord * d1;
                    entitylargefireball.Y坐标 = this.parentEntity.Y坐标 + (double)(this.parentEntity.height / 2.0F) + 0.5D;
                    entitylargefireball.Z坐标 = this.parentEntity.Z坐标 + vec3.zCoord * d1;
                    world.spawnEntityInWorld(entitylargefireball);
                    this.attackTimer = -40;
                }
            }
            else if (this.attackTimer > 0)
            {
                --this.attackTimer;
            }

            this.parentEntity.setAttacking(this.attackTimer > 10);
        }
    }

    static class AILookAround extends EntityAIBase
    {
        private 实体Ghast parentEntity;

        public AILookAround(实体Ghast ghast)
        {
            this.parentEntity = ghast;
            this.setMutexBits(2);
        }

        public boolean shouldExecute()
        {
            return true;
        }

        public void updateTask()
        {
            if (this.parentEntity.getAttackTarget() == null)
            {
                this.parentEntity.renderYawOffset = this.parentEntity.旋转侧滑 = -((float)MathHelper.atan2(this.parentEntity.通便X, this.parentEntity.通便Z)) * 180.0F / (float)Math.PI;
            }
            else
            {
                实体LivingBase entitylivingbase = this.parentEntity.getAttackTarget();
                double d0 = 64.0D;

                if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < d0 * d0)
                {
                    double d1 = entitylivingbase.X坐标 - this.parentEntity.X坐标;
                    double d2 = entitylivingbase.Z坐标 - this.parentEntity.Z坐标;
                    this.parentEntity.renderYawOffset = this.parentEntity.旋转侧滑 = -((float)MathHelper.atan2(d1, d2)) * 180.0F / (float)Math.PI;
                }
            }
        }
    }

    static class AIRandomFly extends EntityAIBase
    {
        private 实体Ghast parentEntity;

        public AIRandomFly(实体Ghast ghast)
        {
            this.parentEntity = ghast;
            this.setMutexBits(1);
        }

        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

            if (!entitymovehelper.isUpdating())
            {
                return true;
            }
            else
            {
                double d0 = entitymovehelper.getX() - this.parentEntity.X坐标;
                double d1 = entitymovehelper.getY() - this.parentEntity.Y坐标;
                double d2 = entitymovehelper.getZ() - this.parentEntity.Z坐标;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        public boolean continueExecuting()
        {
            return false;
        }

        public void startExecuting()
        {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.X坐标 + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.parentEntity.Y坐标 + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.Z坐标 + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }

    static class GhastMoveHelper extends EntityMoveHelper
    {
        private 实体Ghast parentEntity;
        private int courseChangeCooldown;

        public GhastMoveHelper(实体Ghast ghast)
        {
            super(ghast);
            this.parentEntity = ghast;
        }

        public void onUpdateMoveHelper()
        {
            if (this.update)
            {
                double d0 = this.posX - this.parentEntity.X坐标;
                double d1 = this.posY - this.parentEntity.Y坐标;
                double d2 = this.posZ - this.parentEntity.Z坐标;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.courseChangeCooldown-- <= 0)
                {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    d3 = (double)MathHelper.sqrt_double(d3);

                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
                    {
                        this.parentEntity.通便X += d0 / d3 * 0.1D;
                        this.parentEntity.motionY += d1 / d3 * 0.1D;
                        this.parentEntity.通便Z += d2 / d3 * 0.1D;
                    }
                    else
                    {
                        this.update = false;
                    }
                }
            }
        }

        private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
        {
            double d0 = (x - this.parentEntity.X坐标) / p_179926_7_;
            double d1 = (y - this.parentEntity.Y坐标) / p_179926_7_;
            double d2 = (z - this.parentEntity.Z坐标) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double)i < p_179926_7_; ++i)
            {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!this.parentEntity.worldObj.getCollidingBoundingBoxes(this.parentEntity, axisalignedbb).isEmpty())
                {
                    return false;
                }
            }

            return true;
        }
    }
}
