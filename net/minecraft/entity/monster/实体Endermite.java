package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.实体Player;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class 实体Endermite extends 实体Mob
{
    private int lifetime = 0;
    private boolean playerSpawned = false;

    public 实体Endermite(World worldIn)
    {
        super(worldIn);
        this.experienceValue = 3;
        this.setSize(0.4F, 0.3F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 实体Player.class, 1.0D, false));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, 实体Player.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, 实体Player.class, true));
    }

    public float getEyeHeight()
    {
        return 0.1F;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

    protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

    protected String getDeathSound()
    {
        return "mob.silverfish.kill";
    }

    protected void playStepSound(阻止位置 pos, Block blockIn)
    {
        this.playSound("mob.silverfish.step", 0.15F, 1.0F);
    }

    protected Item getDropItem()
    {
        return null;
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.lifetime = tagCompund.getInteger("Lifetime");
        this.playerSpawned = tagCompund.getBoolean("PlayerSpawned");
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Lifetime", this.lifetime);
        tagCompound.setBoolean("PlayerSpawned", this.playerSpawned);
    }

    public void onUpdate()
    {
        this.renderYawOffset = this.旋转侧滑;
        super.onUpdate();
    }

    public boolean isSpawnedByPlayer()
    {
        return this.playerSpawned;
    }

    public void setSpawnedByPlayer(boolean spawnedByPlayer)
    {
        this.playerSpawned = spawnedByPlayer;
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.worldObj.isRemote)
        {
            for (int i = 0; i < 2; ++i)
            {
                this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.X坐标 + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.Y坐标 + this.rand.nextDouble() * (double)this.height, this.Z坐标 + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
            }
        }
        else
        {
            if (!this.isNoDespawnRequired())
            {
                ++this.lifetime;
            }

            if (this.lifetime >= 2400)
            {
                this.setDead();
            }
        }
    }

    protected boolean isValidLightLevel()
    {
        return true;
    }

    public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            实体Player entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
            return entityplayer == null;
        }
        else
        {
            return false;
        }
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }
}
