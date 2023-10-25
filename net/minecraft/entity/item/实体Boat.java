package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体Boat extends 实体
{
    private boolean isBoatEmpty;
    private double speedMultiplier;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public 实体Boat(World worldIn)
    {
        super(worldIn);
        this.isBoatEmpty = true;
        this.speedMultiplier = 0.07D;
        this.preventEntitySpawning = true;
        this.setSize(1.5F, 0.6F);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0F));
    }

    public AxisAlignedBB getCollisionBox(实体 实体In)
    {
        return 实体In.getEntityBoundingBox();
    }

    public AxisAlignedBB getCollisionBoundingBox()
    {
        return this.getEntityBoundingBox();
    }

    public boolean canBePushed()
    {
        return true;
    }

    public 实体Boat(World worldIn, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_)
    {
        this(worldIn);
        this.setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
        this.通便X = 0.0D;
        this.motionY = 0.0D;
        this.通便Z = 0.0D;
        this.prevPosX = p_i1705_2_;
        this.prevPosY = p_i1705_4_;
        this.prevPosZ = p_i1705_6_;
    }

    public double getMountedYOffset()
    {
        return -0.3D;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.riddenBy实体 != null && this.riddenBy实体 == source.getEntity() && source instanceof EntityDamageSourceIndirect)
            {
                return false;
            }
            else
            {
                this.setForwardDirection(-this.getForwardDirection());
                this.setTimeSinceHit(10);
                this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
                this.setBeenAttacked();
                boolean flag = source.getEntity() instanceof 实体Player && ((实体Player)source.getEntity()).capabilities.isCreativeMode;

                if (flag || this.getDamageTaken() > 40.0F)
                {
                    if (this.riddenBy实体 != null)
                    {
                        this.riddenBy实体.mountEntity(this);
                    }

                    if (!flag && this.worldObj.getGameRules().getBoolean("doEntityDrops"))
                    {
                        this.dropItemWithOffset(Items.boat, 1, 0.0F);
                    }

                    this.setDead();
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void performHurtAnimation()
    {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0F);
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
        if (p_180426_10_ && this.riddenBy实体 != null)
        {
            this.prevPosX = this.X坐标 = x;
            this.prevPosY = this.Y坐标 = y;
            this.prevPosZ = this.Z坐标 = z;
            this.旋转侧滑 = yaw;
            this.rotationPitch = pitch;
            this.boatPosRotationIncrements = 0;
            this.setPosition(x, y, z);
            this.通便X = this.velocityX = 0.0D;
            this.motionY = this.velocityY = 0.0D;
            this.通便Z = this.velocityZ = 0.0D;
        }
        else
        {
            if (this.isBoatEmpty)
            {
                this.boatPosRotationIncrements = posRotationIncrements + 5;
            }
            else
            {
                double d0 = x - this.X坐标;
                double d1 = y - this.Y坐标;
                double d2 = z - this.Z坐标;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 <= 1.0D)
                {
                    return;
                }

                this.boatPosRotationIncrements = 3;
            }

            this.boatX = x;
            this.boatY = y;
            this.boatZ = z;
            this.boatYaw = (double)yaw;
            this.boatPitch = (double)pitch;
            this.通便X = this.velocityX;
            this.motionY = this.velocityY;
            this.通便Z = this.velocityZ;
        }
    }

    public void setVelocity(double x, double y, double z)
    {
        this.velocityX = this.通便X = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.通便Z = z;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.getTimeSinceHit() > 0)
        {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F)
        {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        int i = 5;
        double d0 = 0.0D;

        for (int j = 0; j < i; ++j)
        {
            double d1 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(j + 0) / (double)i - 0.125D;
            double d3 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(j + 1) / (double)i - 0.125D;
            AxisAlignedBB axisalignedbb = new AxisAlignedBB(this.getEntityBoundingBox().minX, d1, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, d3, this.getEntityBoundingBox().maxZ);

            if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water))
            {
                d0 += 1.0D / (double)i;
            }
        }

        double d9 = Math.sqrt(this.通便X * this.通便X + this.通便Z * this.通便Z);

        if (d9 > 0.2975D)
        {
            double d2 = Math.cos((double)this.旋转侧滑 * Math.PI / 180.0D);
            double d4 = Math.sin((double)this.旋转侧滑 * Math.PI / 180.0D);

            for (int k = 0; (double)k < 1.0D + d9 * 60.0D; ++k)
            {
                double d5 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
                double d6 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;

                if (this.rand.nextBoolean())
                {
                    double d7 = this.X坐标 - d2 * d5 * 0.8D + d4 * d6;
                    double d8 = this.Z坐标 - d4 * d5 * 0.8D - d2 * d6;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d7, this.Y坐标 - 0.125D, d8, this.通便X, this.motionY, this.通便Z, new int[0]);
                }
                else
                {
                    double d24 = this.X坐标 + d2 + d4 * d5 * 0.7D;
                    double d25 = this.Z坐标 + d4 - d2 * d5 * 0.7D;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d24, this.Y坐标 - 0.125D, d25, this.通便X, this.motionY, this.通便Z, new int[0]);
                }
            }
        }

        if (this.worldObj.isRemote && this.isBoatEmpty)
        {
            if (this.boatPosRotationIncrements > 0)
            {
                double d12 = this.X坐标 + (this.boatX - this.X坐标) / (double)this.boatPosRotationIncrements;
                double d16 = this.Y坐标 + (this.boatY - this.Y坐标) / (double)this.boatPosRotationIncrements;
                double d19 = this.Z坐标 + (this.boatZ - this.Z坐标) / (double)this.boatPosRotationIncrements;
                double d22 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double)this.旋转侧滑);
                this.旋转侧滑 = (float)((double)this.旋转侧滑 + d22 / (double)this.boatPosRotationIncrements);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.boatPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(d12, d16, d19);
                this.setRotation(this.旋转侧滑, this.rotationPitch);
            }
            else
            {
                double d13 = this.X坐标 + this.通便X;
                double d17 = this.Y坐标 + this.motionY;
                double d20 = this.Z坐标 + this.通便Z;
                this.setPosition(d13, d17, d20);

                if (this.onGround)
                {
                    this.通便X *= 0.5D;
                    this.motionY *= 0.5D;
                    this.通便Z *= 0.5D;
                }

                this.通便X *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.通便Z *= 0.9900000095367432D;
            }
        }
        else
        {
            if (d0 < 1.0D)
            {
                double d10 = d0 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * d10;
            }
            else
            {
                if (this.motionY < 0.0D)
                {
                    this.motionY /= 2.0D;
                }

                this.motionY += 0.007000000216066837D;
            }

            if (this.riddenBy实体 instanceof 实体LivingBase)
            {
                实体LivingBase entitylivingbase = (实体LivingBase)this.riddenBy实体;
                float f = this.riddenBy实体.旋转侧滑 + -entitylivingbase.moveStrafing * 90.0F;
                this.通便X += -Math.sin((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
                this.通便Z += Math.cos((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
            }

            double d11 = Math.sqrt(this.通便X * this.通便X + this.通便Z * this.通便Z);

            if (d11 > 0.35D)
            {
                double d14 = 0.35D / d11;
                this.通便X *= d14;
                this.通便Z *= d14;
                d11 = 0.35D;
            }

            if (d11 > d9 && this.speedMultiplier < 0.35D)
            {
                this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;

                if (this.speedMultiplier > 0.35D)
                {
                    this.speedMultiplier = 0.35D;
                }
            }
            else
            {
                this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;

                if (this.speedMultiplier < 0.07D)
                {
                    this.speedMultiplier = 0.07D;
                }
            }

            for (int i1 = 0; i1 < 4; ++i1)
            {
                int l1 = MathHelper.floor_double(this.X坐标 + ((double)(i1 % 2) - 0.5D) * 0.8D);
                int i2 = MathHelper.floor_double(this.Z坐标 + ((double)(i1 / 2) - 0.5D) * 0.8D);

                for (int j2 = 0; j2 < 2; ++j2)
                {
                    int l = MathHelper.floor_double(this.Y坐标) + j2;
                    阻止位置 blockpos = new 阻止位置(l1, l, i2);
                    Block block = this.worldObj.getBlockState(blockpos).getBlock();

                    if (block == Blocks.snow_layer)
                    {
                        this.worldObj.setBlockToAir(blockpos);
                        this.isCollidedHorizontally = false;
                    }
                    else if (block == Blocks.waterlily)
                    {
                        this.worldObj.destroyBlock(blockpos, true);
                        this.isCollidedHorizontally = false;
                    }
                }
            }

            if (this.onGround)
            {
                this.通便X *= 0.5D;
                this.motionY *= 0.5D;
                this.通便Z *= 0.5D;
            }

            this.moveEntity(this.通便X, this.motionY, this.通便Z);

            if (this.isCollidedHorizontally && d9 > 0.2975D)
            {
                if (!this.worldObj.isRemote && !this.isDead)
                {
                    this.setDead();

                    if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
                    {
                        for (int j1 = 0; j1 < 3; ++j1)
                        {
                            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
                        }

                        for (int k1 = 0; k1 < 2; ++k1)
                        {
                            this.dropItemWithOffset(Items.stick, 1, 0.0F);
                        }
                    }
                }
            }
            else
            {
                this.通便X *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.通便Z *= 0.9900000095367432D;
            }

            this.rotationPitch = 0.0F;
            double d15 = (double)this.旋转侧滑;
            double d18 = this.prevPosX - this.X坐标;
            double d21 = this.prevPosZ - this.Z坐标;

            if (d18 * d18 + d21 * d21 > 0.001D)
            {
                d15 = (double)((float)(MathHelper.atan2(d21, d18) * 180.0D / Math.PI));
            }

            double d23 = MathHelper.wrapAngleTo180_double(d15 - (double)this.旋转侧滑);

            if (d23 > 20.0D)
            {
                d23 = 20.0D;
            }

            if (d23 < -20.0D)
            {
                d23 = -20.0D;
            }

            this.旋转侧滑 = (float)((double)this.旋转侧滑 + d23);
            this.setRotation(this.旋转侧滑, this.rotationPitch);

            if (!this.worldObj.isRemote)
            {
                List<实体> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

                if (list != null && !list.isEmpty())
                {
                    for (int k2 = 0; k2 < list.size(); ++k2)
                    {
                        实体 实体 = (实体)list.get(k2);

                        if (实体 != this.riddenBy实体 && 实体.canBePushed() && 实体 instanceof 实体Boat)
                        {
                            实体.applyEntityCollision(this);
                        }
                    }
                }

                if (this.riddenBy实体 != null && this.riddenBy实体.isDead)
                {
                    this.riddenBy实体 = null;
                }
            }
        }
    }

    public void updateRiderPosition()
    {
        if (this.riddenBy实体 != null)
        {
            double d0 = Math.cos((double)this.旋转侧滑 * Math.PI / 180.0D) * 0.4D;
            double d1 = Math.sin((double)this.旋转侧滑 * Math.PI / 180.0D) * 0.4D;
            this.riddenBy实体.setPosition(this.X坐标 + d0, this.Y坐标 + this.getMountedYOffset() + this.riddenBy实体.getYOffset(), this.Z坐标 + d1);
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    }

    public boolean interactFirst(实体Player playerIn)
    {
        if (this.riddenBy实体 != null && this.riddenBy实体 instanceof 实体Player && this.riddenBy实体 != playerIn)
        {
            return true;
        }
        else
        {
            if (!this.worldObj.isRemote)
            {
                playerIn.mountEntity(this);
            }

            return true;
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, 阻止位置 pos)
    {
        if (onGroundIn)
        {
            if (this.fallDistance > 3.0F)
            {
                this.fall(this.fallDistance, 1.0F);

                if (!this.worldObj.isRemote && !this.isDead)
                {
                    this.setDead();

                    if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
                    {
                        for (int i = 0; i < 3; ++i)
                        {
                            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
                        }

                        for (int j = 0; j < 2; ++j)
                        {
                            this.dropItemWithOffset(Items.stick, 1, 0.0F);
                        }
                    }
                }

                this.fallDistance = 0.0F;
            }
        }
        else if (this.worldObj.getBlockState((new 阻止位置(this)).down()).getBlock().getMaterial() != Material.water && y < 0.0D)
        {
            this.fallDistance = (float)((double)this.fallDistance - y);
        }
    }

    public void setDamageTaken(float p_70266_1_)
    {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
    }

    public float getDamageTaken()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public void setTimeSinceHit(int p_70265_1_)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
    }

    public int getTimeSinceHit()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setForwardDirection(int p_70269_1_)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
    }

    public int getForwardDirection()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void setIsBoatEmpty(boolean p_70270_1_)
    {
        this.isBoatEmpty = p_70270_1_;
    }
}
