package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.实体Ageable;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体Chicken extends 实体Animal
{
    public float wingRotation;
    public float destPos;
    public float field_70884_g;
    public float field_70888_h;
    public float wingRotDelta = 1.0F;
    public int timeUntilNextEgg;
    public boolean chickenJockey;

    public 实体Chicken(World worldIn)
    {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.wheat_seeds, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, 实体Player.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    public float getEyeHeight()
    {
        return this.height;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.field_70888_h = this.wingRotation;
        this.field_70884_g = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp_float(this.destPos, 0.0F, 1.0F);

        if (!this.onGround && this.wingRotDelta < 1.0F)
        {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.wingRotation += this.wingRotDelta * 2.0F;

        if (!this.worldObj.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0)
        {
            this.playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Items.egg, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected String getLivingSound()
    {
        return "mob.chicken.say";
    }

    protected String getHurtSound()
    {
        return "mob.chicken.hurt";
    }

    protected String getDeathSound()
    {
        return "mob.chicken.hurt";
    }

    protected void playStepSound(阻止位置 pos, Block blockIn)
    {
        this.playSound("mob.chicken.step", 0.15F, 1.0F);
    }

    protected Item getDropItem()
    {
        return Items.feather;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        int i = this.rand.nextInt(3) + this.rand.nextInt(1 + lootingModifier);

        for (int j = 0; j < i; ++j)
        {
            this.dropItem(Items.feather, 1);
        }

        if (this.isBurning())
        {
            this.dropItem(Items.cooked_chicken, 1);
        }
        else
        {
            this.dropItem(Items.chicken, 1);
        }
    }

    public 实体Chicken createChild(实体Ageable ageable)
    {
        return new 实体Chicken(this.worldObj);
    }

    public boolean isBreedingItem(ItemStack stack)
    {
        return stack != null && stack.getItem() == Items.wheat_seeds;
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.chickenJockey = tagCompund.getBoolean("IsChickenJockey");

        if (tagCompund.hasKey("EggLayTime"))
        {
            this.timeUntilNextEgg = tagCompund.getInteger("EggLayTime");
        }
    }

    protected int getExperiencePoints(实体Player player)
    {
        return this.isChickenJockey() ? 10 : super.getExperiencePoints(player);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("IsChickenJockey", this.chickenJockey);
        tagCompound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

    protected boolean canDespawn()
    {
        return this.isChickenJockey() && this.riddenBy实体 == null;
    }

    public void updateRiderPosition()
    {
        super.updateRiderPosition();
        float f = MathHelper.sin(this.renderYawOffset * (float)Math.PI / 180.0F);
        float f1 = MathHelper.cos(this.renderYawOffset * (float)Math.PI / 180.0F);
        float f2 = 0.1F;
        float f3 = 0.0F;
        this.riddenBy实体.setPosition(this.X坐标 + (double)(f2 * f), this.Y坐标 + (double)(this.height * 0.5F) + this.riddenBy实体.getYOffset() + (double)f3, this.Z坐标 - (double)(f2 * f1));

        if (this.riddenBy实体 instanceof 实体LivingBase)
        {
            ((实体LivingBase)this.riddenBy实体).renderYawOffset = this.renderYawOffset;
        }
    }

    public boolean isChickenJockey()
    {
        return this.chickenJockey;
    }

    public void setChickenJockey(boolean jockey)
    {
        this.chickenJockey = jockey;
    }
}
