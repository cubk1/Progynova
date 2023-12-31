package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体Ageable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class 实体Ocelot extends 实体Tameable
{
    private EntityAIAvoidEntity<实体Player> avoidEntity;
    private EntityAITempt aiTempt;

    public 实体Ocelot(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 0.7F);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, this.aiTempt = new EntityAITempt(this, 0.6D, Items.fish, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8D));
        this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
        this.tasks.addTask(8, new EntityAIOcelotAttack(this));
        this.tasks.addTask(9, new EntityAIMate(this, 0.8D));
        this.tasks.addTask(10, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, 实体Player.class, 10.0F));
        this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, 实体Chicken.class, false, (Predicate)null));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
    }

    public void updateAITasks()
    {
        if (this.getMoveHelper().isUpdating())
        {
            double d0 = this.getMoveHelper().getSpeed();

            if (d0 == 0.6D)
            {
                this.setSneaking(true);
                this.设置宇轩的疾跑状态(false);
            }
            else if (d0 == 1.33D)
            {
                this.setSneaking(false);
                this.设置宇轩的疾跑状态(true);
            }
            else
            {
                this.setSneaking(false);
                this.设置宇轩的疾跑状态(false);
            }
        }
        else
        {
            this.setSneaking(false);
            this.设置宇轩的疾跑状态(false);
        }
    }

    protected boolean canDespawn()
    {
        return !this.isTamed() && this.已存在的刻度 > 2400;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("CatType", this.getTameSkin());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setTameSkin(tagCompund.getInteger("CatType"));
    }

    protected String getLivingSound()
    {
        return this.isTamed() ? (this.isInLove() ? "mob.cat.purr" : (this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : "";
    }

    protected String getHurtSound()
    {
        return "mob.cat.hitt";
    }

    protected String getDeathSound()
    {
        return "mob.cat.hitt";
    }

    protected float getSoundVolume()
    {
        return 0.4F;
    }

    protected Item getDropItem()
    {
        return Items.leather;
    }

    public boolean attackEntityAsMob(实体 实体In)
    {
        return 实体In.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            this.aiSit.setSitting(false);
            return super.attackEntityFrom(source, amount);
        }
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
    }

    public boolean interact(实体Player player)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();

        if (this.isTamed())
        {
            if (this.isOwner(player) && !this.worldObj.isRemote && !this.isBreedingItem(itemstack))
            {
                this.aiSit.setSitting(!this.isSitting());
            }
        }
        else if (this.aiTempt.isRunning() && itemstack != null && itemstack.getItem() == Items.fish && player.getDistanceSqToEntity(this) < 9.0D)
        {
            if (!player.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            if (itemstack.stackSize <= 0)
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }

            if (!this.worldObj.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.setTamed(true);
                    this.setTameSkin(1 + this.worldObj.rand.nextInt(3));
                    this.setOwnerId(player.getUniqueID().toString());
                    this.playTameEffect(true);
                    this.aiSit.setSitting(true);
                    this.worldObj.setEntityState(this, (byte)7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.interact(player);
    }

    public 实体Ocelot createChild(实体Ageable ageable)
    {
        实体Ocelot entityocelot = new 实体Ocelot(this.worldObj);

        if (this.isTamed())
        {
            entityocelot.setOwnerId(this.getOwnerId());
            entityocelot.setTamed(true);
            entityocelot.setTameSkin(this.getTameSkin());
        }

        return entityocelot;
    }

    public boolean isBreedingItem(ItemStack stack)
    {
        return stack != null && stack.getItem() == Items.fish;
    }

    public boolean canMateWith(实体Animal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (!this.isTamed())
        {
            return false;
        }
        else if (!(otherAnimal instanceof 实体Ocelot))
        {
            return false;
        }
        else
        {
            实体Ocelot entityocelot = (实体Ocelot)otherAnimal;
            return !entityocelot.isTamed() ? false : this.isInLove() && entityocelot.isInLove();
        }
    }

    public int getTameSkin()
    {
        return this.dataWatcher.getWatchableObjectByte(18);
    }

    public void setTameSkin(int skinId)
    {
        this.dataWatcher.updateObject(18, Byte.valueOf((byte)skinId));
    }

    public boolean getCanSpawnHere()
    {
        return this.worldObj.rand.nextInt(3) != 0;
    }

    public boolean isNotColliding()
    {
        if (this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox()))
        {
            阻止位置 blockpos = new 阻止位置(this.X坐标, this.getEntityBoundingBox().minY, this.Z坐标);

            if (blockpos.getY() < this.worldObj.getSeaLevel())
            {
                return false;
            }

            Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();

            if (block == Blocks.grass || block.getMaterial() == Material.leaves)
            {
                return true;
            }
        }

        return false;
    }

    public String getName()
    {
        return this.hasCustomName() ? this.getCustomNameTag() : (this.isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getName());
    }

    public void setTamed(boolean tamed)
    {
        super.setTamed(tamed);
    }

    protected void setupTamedAI()
    {
        if (this.avoidEntity == null)
        {
            this.avoidEntity = new EntityAIAvoidEntity(this, 实体Player.class, 16.0F, 0.8D, 1.33D);
        }

        this.tasks.removeTask(this.avoidEntity);

        if (!this.isTamed())
        {
            this.tasks.addTask(4, this.avoidEntity);
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (this.worldObj.rand.nextInt(7) == 0)
        {
            for (int i = 0; i < 2; ++i)
            {
                实体Ocelot entityocelot = new 实体Ocelot(this.worldObj);
                entityocelot.setLocationAndAngles(this.X坐标, this.Y坐标, this.Z坐标, this.旋转侧滑, 0.0F);
                entityocelot.setGrowingAge(-24000);
                this.worldObj.spawnEntityInWorld(entityocelot);
            }
        }

        return livingdata;
    }
}
