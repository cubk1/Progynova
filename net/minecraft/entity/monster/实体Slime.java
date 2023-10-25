package net.minecraft.entity.monster;

import net.minecraft.entity.*;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class 实体Slime extends 实体Living implements IMob
{
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public 实体Slime(World worldIn)
    {
        super(worldIn);
        this.moveHelper = new 实体Slime.SlimeMoveHelper(this);
        this.tasks.addTask(1, new 实体Slime.AISlimeFloat(this));
        this.tasks.addTask(2, new 实体Slime.AISlimeAttack(this));
        this.tasks.addTask(3, new 实体Slime.AISlimeFaceRandom(this));
        this.tasks.addTask(5, new 实体Slime.AISlimeHop(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
        this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, 实体IronGolem.class));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)1));
    }

    protected void setSlimeSize(int size)
    {
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)size));
        this.setSize(0.51000005F * (float)size, 0.51000005F * (float)size);
        this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)(size * size));
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)(0.2F + 0.1F * (float)size));
        this.setHealth(this.getMaxHealth());
        this.experienceValue = size;
    }

    public int getSlimeSize()
    {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Size", this.getSlimeSize() - 1);
        tagCompound.setBoolean("wasOnGround", this.wasOnGround);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        int i = tagCompund.getInteger("Size");

        if (i < 0)
        {
            i = 0;
        }

        this.setSlimeSize(i + 1);
        this.wasOnGround = tagCompund.getBoolean("wasOnGround");
    }

    protected EnumParticleTypes getParticleType()
    {
        return EnumParticleTypes.SLIME;
    }

    protected String getJumpSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    public void onUpdate()
    {
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0)
        {
            this.isDead = true;
        }

        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
        this.prevSquishFactor = this.squishFactor;
        super.onUpdate();

        if (this.onGround && !this.wasOnGround)
        {
            int i = this.getSlimeSize();

            for (int j = 0; j < i * 8; ++j)
            {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
                World world = this.worldObj;
                EnumParticleTypes enumparticletypes = this.getParticleType();
                double d0 = this.X坐标 + (double)f2;
                double d1 = this.Z坐标 + (double)f3;
                world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D, new int[0]);
            }

            if (this.makesSoundOnLand())
            {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.squishAmount = -0.5F;
        }
        else if (!this.onGround && this.wasOnGround)
        {
            this.squishAmount = 1.0F;
        }

        this.wasOnGround = this.onGround;
        this.alterSquishAmount();
    }

    protected void alterSquishAmount()
    {
        this.squishAmount *= 0.6F;
    }

    protected int getJumpDelay()
    {
        return this.rand.nextInt(20) + 10;
    }

    protected 实体Slime createInstance()
    {
        return new 实体Slime(this.worldObj);
    }

    public void onDataWatcherUpdate(int dataID)
    {
        if (dataID == 16)
        {
            int i = this.getSlimeSize();
            this.setSize(0.51000005F * (float)i, 0.51000005F * (float)i);
            this.旋转侧滑 = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;

            if (this.isInWater() && this.rand.nextInt(20) == 0)
            {
                this.resetHeight();
            }
        }

        super.onDataWatcherUpdate(dataID);
    }

    public void setDead()
    {
        int i = this.getSlimeSize();

        if (!this.worldObj.isRemote && i > 1 && this.getHealth() <= 0.0F)
        {
            int j = 2 + this.rand.nextInt(3);

            for (int k = 0; k < j; ++k)
            {
                float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
                float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
                实体Slime entityslime = this.createInstance();

                if (this.hasCustomName())
                {
                    entityslime.setCustomNameTag(this.getCustomNameTag());
                }

                if (this.isNoDespawnRequired())
                {
                    entityslime.enablePersistence();
                }

                entityslime.setSlimeSize(i / 2);
                entityslime.setLocationAndAngles(this.X坐标 + (double)f, this.Y坐标 + 0.5D, this.Z坐标 + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
                this.worldObj.spawnEntityInWorld(entityslime);
            }
        }

        super.setDead();
    }

    public void applyEntityCollision(实体 实体In)
    {
        super.applyEntityCollision(实体In);

        if (实体In instanceof 实体IronGolem && this.canDamagePlayer())
        {
            this.func_175451_e((实体LivingBase) 实体In);
        }
    }

    public void onCollideWithPlayer(实体Player entityIn)
    {
        if (this.canDamagePlayer())
        {
            this.func_175451_e(entityIn);
        }
    }

    protected void func_175451_e(实体LivingBase p_175451_1_)
    {
        int i = this.getSlimeSize();

        if (this.canEntityBeSeen(p_175451_1_) && this.getDistanceSqToEntity(p_175451_1_) < 0.6D * (double)i * 0.6D * (double)i && p_175451_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttackStrength()))
        {
            this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.applyEnchantments(this, p_175451_1_);
        }
    }

    public float getEyeHeight()
    {
        return 0.625F * this.height;
    }

    protected boolean canDamagePlayer()
    {
        return this.getSlimeSize() > 1;
    }

    protected int getAttackStrength()
    {
        return this.getSlimeSize();
    }

    protected String getHurtSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected String getDeathSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected Item getDropItem()
    {
        return this.getSlimeSize() == 1 ? Items.slime_ball : null;
    }

    public boolean getCanSpawnHere()
    {
        阻止位置 blockpos = new 阻止位置(MathHelper.floor_double(this.X坐标), 0, MathHelper.floor_double(this.Z坐标));
        Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);

        if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1)
        {
            return false;
        }
        else
        {
            if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
                BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);

                if (biomegenbase == BiomeGenBase.swampland && this.Y坐标 > 50.0D && this.Y坐标 < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new 阻止位置(this)) <= this.rand.nextInt(8))
                {
                    return super.getCanSpawnHere();
                }

                if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.Y坐标 < 40.0D)
                {
                    return super.getCanSpawnHere();
                }
            }

            return false;
        }
    }

    protected float getSoundVolume()
    {
        return 0.4F * (float)this.getSlimeSize();
    }

    public int getVerticalFaceSpeed()
    {
        return 0;
    }

    protected boolean makesSoundOnJump()
    {
        return this.getSlimeSize() > 0;
    }

    protected boolean makesSoundOnLand()
    {
        return this.getSlimeSize() > 2;
    }

    protected void jump()
    {
        this.motionY = 0.41999998688697815D;
        this.isAirBorne = true;
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        int i = this.rand.nextInt(3);

        if (i < 2 && this.rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty())
        {
            ++i;
        }

        int j = 1 << i;
        this.setSlimeSize(j);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    static class AISlimeAttack extends EntityAIBase
    {
        private 实体Slime slime;
        private int field_179465_b;

        public AISlimeAttack(实体Slime slimeIn)
        {
            this.slime = slimeIn;
            this.setMutexBits(2);
        }

        public boolean shouldExecute()
        {
            实体LivingBase entitylivingbase = this.slime.getAttackTarget();
            return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : !(entitylivingbase instanceof 实体Player) || !((实体Player)entitylivingbase).capabilities.disableDamage);
        }

        public void startExecuting()
        {
            this.field_179465_b = 300;
            super.startExecuting();
        }

        public boolean continueExecuting()
        {
            实体LivingBase entitylivingbase = this.slime.getAttackTarget();
            return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (entitylivingbase instanceof 实体Player && ((实体Player)entitylivingbase).capabilities.disableDamage ? false : --this.field_179465_b > 0));
        }

        public void updateTask()
        {
            this.slime.faceEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
            ((实体Slime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.slime.旋转侧滑, this.slime.canDamagePlayer());
        }
    }

    static class AISlimeFaceRandom extends EntityAIBase
    {
        private 实体Slime slime;
        private float field_179459_b;
        private int field_179460_c;

        public AISlimeFaceRandom(实体Slime slimeIn)
        {
            this.slime = slimeIn;
            this.setMutexBits(2);
        }

        public boolean shouldExecute()
        {
            return this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava());
        }

        public void updateTask()
        {
            if (--this.field_179460_c <= 0)
            {
                this.field_179460_c = 40 + this.slime.getRNG().nextInt(60);
                this.field_179459_b = (float)this.slime.getRNG().nextInt(360);
            }

            ((实体Slime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.field_179459_b, false);
        }
    }

    static class AISlimeFloat extends EntityAIBase
    {
        private 实体Slime slime;

        public AISlimeFloat(实体Slime slimeIn)
        {
            this.slime = slimeIn;
            this.setMutexBits(5);
            ((PathNavigateGround)slimeIn.getNavigator()).setCanSwim(true);
        }

        public boolean shouldExecute()
        {
            return this.slime.isInWater() || this.slime.isInLava();
        }

        public void updateTask()
        {
            if (this.slime.getRNG().nextFloat() < 0.8F)
            {
                this.slime.getJumpHelper().setJumping();
            }

            ((实体Slime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2D);
        }
    }

    static class AISlimeHop extends EntityAIBase
    {
        private 实体Slime slime;

        public AISlimeHop(实体Slime slimeIn)
        {
            this.slime = slimeIn;
            this.setMutexBits(5);
        }

        public boolean shouldExecute()
        {
            return true;
        }

        public void updateTask()
        {
            ((实体Slime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0D);
        }
    }

    static class SlimeMoveHelper extends EntityMoveHelper
    {
        private float field_179922_g;
        private int field_179924_h;
        private 实体Slime slime;
        private boolean field_179923_j;

        public SlimeMoveHelper(实体Slime slimeIn)
        {
            super(slimeIn);
            this.slime = slimeIn;
        }

        public void func_179920_a(float p_179920_1_, boolean p_179920_2_)
        {
            this.field_179922_g = p_179920_1_;
            this.field_179923_j = p_179920_2_;
        }

        public void setSpeed(double speedIn)
        {
            this.speed = speedIn;
            this.update = true;
        }

        public void onUpdateMoveHelper()
        {
            this.entity.旋转侧滑 = this.limitAngle(this.entity.旋转侧滑, this.field_179922_g, 30.0F);
            this.entity.rotationYawHead = this.entity.旋转侧滑;
            this.entity.renderYawOffset = this.entity.旋转侧滑;

            if (!this.update)
            {
                this.entity.setMoveForward(0.0F);
            }
            else
            {
                this.update = false;

                if (this.entity.onGround)
                {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));

                    if (this.field_179924_h-- <= 0)
                    {
                        this.field_179924_h = this.slime.getJumpDelay();

                        if (this.field_179923_j)
                        {
                            this.field_179924_h /= 3;
                        }

                        this.slime.getJumpHelper().setJumping();

                        if (this.slime.makesSoundOnJump())
                        {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                        }
                    }
                    else
                    {
                        this.slime.moveStrafing = this.slime.moveForward = 0.0F;
                        this.entity.setAIMoveSpeed(0.0F);
                    }
                }
                else
                {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                }
            }
        }
    }
}
