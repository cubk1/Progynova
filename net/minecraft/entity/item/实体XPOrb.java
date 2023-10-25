package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.实体;
import net.minecraft.entity.player.实体Player;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体XPOrb extends 实体
{
    public int xpColor;
    public int xpOrbAge;
    public int delayBeforeCanPickup;
    private int xpOrbHealth = 5;
    private int xpValue;
    private 实体Player closestPlayer;
    private int xpTargetColor;

    public 实体XPOrb(World worldIn, double x, double y, double z, int expValue)
    {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
        this.旋转侧滑 = (float)(Math.random() * 360.0D);
        this.通便X = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F);
        this.通便Z = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.xpValue = expValue;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public 实体XPOrb(World worldIn)
    {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
    }

    public int getBrightnessForRender(float partialTicks)
    {
        float f = 0.5F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(partialTicks);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.delayBeforeCanPickup > 0)
        {
            --this.delayBeforeCanPickup;
        }

        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        this.motionY -= 0.029999999329447746D;

        if (this.worldObj.getBlockState(new 阻止位置(this)).getBlock().getMaterial() == Material.lava)
        {
            this.motionY = 0.20000000298023224D;
            this.通便X = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.通便Z = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.pushOutOfBlocks(this.X坐标, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.Z坐标);
        double d0 = 8.0D;

        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100)
        {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0)
            {
                this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
            }

            this.xpTargetColor = this.xpColor;
        }

        if (this.closestPlayer != null && this.closestPlayer.isSpectator())
        {
            this.closestPlayer = null;
        }

        if (this.closestPlayer != null)
        {
            double d1 = (this.closestPlayer.X坐标 - this.X坐标) / d0;
            double d2 = (this.closestPlayer.Y坐标 + (double)this.closestPlayer.getEyeHeight() - this.Y坐标) / d0;
            double d3 = (this.closestPlayer.Z坐标 - this.Z坐标) / d0;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D)
            {
                d5 = d5 * d5;
                this.通便X += d1 / d4 * d5 * 0.1D;
                this.motionY += d2 / d4 * d5 * 0.1D;
                this.通便Z += d3 / d4 * d5 * 0.1D;
            }
        }

        this.moveEntity(this.通便X, this.motionY, this.通便Z);
        float f = 0.98F;

        if (this.onGround)
        {
            f = this.worldObj.getBlockState(new 阻止位置(MathHelper.floor_double(this.X坐标), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.Z坐标))).getBlock().slipperiness * 0.98F;
        }

        this.通便X *= (double)f;
        this.motionY *= 0.9800000190734863D;
        this.通便Z *= (double)f;

        if (this.onGround)
        {
            this.motionY *= -0.8999999761581421D;
        }

        ++this.xpColor;
        ++this.xpOrbAge;

        if (this.xpOrbAge >= 6000)
        {
            this.setDead();
        }
    }

    public boolean handleWaterMovement()
    {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this);
    }

    protected void dealFireDamage(int amount)
    {
        this.attackEntityFrom(DamageSource.inFire, (float)amount);
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            this.setBeenAttacked();
            this.xpOrbHealth = (int)((float)this.xpOrbHealth - amount);

            if (this.xpOrbHealth <= 0)
            {
                this.setDead();
            }

            return false;
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("Health", (short)((byte)this.xpOrbHealth));
        tagCompound.setShort("Age", (short)this.xpOrbAge);
        tagCompound.setShort("Value", (short)this.xpValue);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.xpOrbHealth = tagCompund.getShort("Health") & 255;
        this.xpOrbAge = tagCompund.getShort("Age");
        this.xpValue = tagCompund.getShort("Value");
    }

    public void onCollideWithPlayer(实体Player entityIn)
    {
        if (!this.worldObj.isRemote)
        {
            if (this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0)
            {
                entityIn.xpCooldown = 2;
                this.worldObj.playSoundAtEntity(entityIn, "random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
                entityIn.onItemPickup(this, 1);
                entityIn.addExperience(this.xpValue);
                this.setDead();
            }
        }
    }

    public int getXpValue()
    {
        return this.xpValue;
    }

    public int getTextureByXP()
    {
        return this.xpValue >= 2477 ? 10 : (this.xpValue >= 1237 ? 9 : (this.xpValue >= 617 ? 8 : (this.xpValue >= 307 ? 7 : (this.xpValue >= 149 ? 6 : (this.xpValue >= 73 ? 5 : (this.xpValue >= 37 ? 4 : (this.xpValue >= 17 ? 3 : (this.xpValue >= 7 ? 2 : (this.xpValue >= 3 ? 1 : 0)))))))));
    }

    public static int getXPSplit(int expValue)
    {
        return expValue >= 2477 ? 2477 : (expValue >= 1237 ? 1237 : (expValue >= 617 ? 617 : (expValue >= 307 ? 307 : (expValue >= 149 ? 149 : (expValue >= 73 ? 73 : (expValue >= 37 ? 37 : (expValue >= 17 ? 17 : (expValue >= 7 ? 7 : (expValue >= 3 ? 3 : 1)))))))));
    }

    public boolean canAttackWithItem()
    {
        return false;
    }
}
