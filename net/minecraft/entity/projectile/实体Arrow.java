package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.实体Enderman;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class 实体Arrow extends 实体 implements IProjectile
{
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData;
    private boolean inGround;
    public int canBePickedUp;
    public int arrowShake;
    public 实体 shooting实体;
    private int ticksInGround;
    private int ticksInAir;
    private double damage = 2.0D;
    private int knockbackStrength;

    public 实体Arrow(World worldIn)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public 实体Arrow(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
    }

    public 实体Arrow(World worldIn, 实体LivingBase shooter, 实体LivingBase target, float velocity, float innacuracy)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shooting实体 = shooter;

        if (shooter instanceof 实体Player)
        {
            this.canBePickedUp = 1;
        }

        this.Y坐标 = shooter.Y坐标 + (double)shooter.getEyeHeight() - 0.10000000149011612D;
        double d0 = target.X坐标 - shooter.X坐标;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - this.Y坐标;
        double d2 = target.Z坐标 - shooter.Z坐标;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f1 = (float)(-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            this.setLocationAndAngles(shooter.X坐标 + d4, this.Y坐标, shooter.Z坐标 + d5, f, f1);
            float f2 = (float)(d3 * 0.20000000298023224D);
            this.setThrowableHeading(d0, d1 + (double)f2, d2, velocity, innacuracy);
        }
    }

    public 实体Arrow(World worldIn, 实体LivingBase shooter, float velocity)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shooting实体 = shooter;

        if (shooter instanceof 实体Player)
        {
            this.canBePickedUp = 1;
        }

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(shooter.X坐标, shooter.Y坐标 + (double)shooter.getEyeHeight(), shooter.Z坐标, shooter.旋转侧滑, shooter.rotationPitch);
        this.X坐标 -= (double)(MathHelper.cos(this.旋转侧滑 / 180.0F * (float)Math.PI) * 0.16F);
        this.Y坐标 -= 0.10000000149011612D;
        this.Z坐标 -= (double)(MathHelper.sin(this.旋转侧滑 / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
        this.通便X = (double)(-MathHelper.sin(this.旋转侧滑 / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.通便Z = (double)(MathHelper.cos(this.旋转侧滑 / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.通便X, this.motionY, this.通便Z, velocity * 1.5F, 1.0F);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt_double(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        this.通便X = x;
        this.motionY = y;
        this.通便Z = z;
        float f1 = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = this.旋转侧滑 = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    public void setVelocity(double x, double y, double z)
    {
        this.通便X = x;
        this.motionY = y;
        this.通便Z = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(x * x + z * z);
            this.prevRotationYaw = this.旋转侧滑 = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.旋转侧滑;
            this.setLocationAndAngles(this.X坐标, this.Y坐标, this.Z坐标, this.旋转侧滑, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z);
            this.prevRotationYaw = this.旋转侧滑 = (float)(MathHelper.atan2(this.通便X, this.通便Z) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }

        阻止位置 blockpos = new 阻止位置(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getMaterial() != Material.air)
        {
            block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockpos, iblockstate);

            if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.X坐标, this.Y坐标, this.Z坐标)))
            {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround)
        {
            int j = block.getMetaFromState(iblockstate);

            if (block == this.inTile && j == this.inData)
            {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1200)
                {
                    this.setDead();
                }
            }
            else
            {
                this.inGround = false;
                this.通便X *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.通便Z *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else
        {
            ++this.ticksInAir;
            Vec3 vec31 = new Vec3(this.X坐标, this.Y坐标, this.Z坐标);
            Vec3 vec3 = new Vec3(this.X坐标 + this.通便X, this.Y坐标 + this.motionY, this.Z坐标 + this.通便Z);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
            vec31 = new Vec3(this.X坐标, this.Y坐标, this.Z坐标);
            vec3 = new Vec3(this.X坐标 + this.通便X, this.Y坐标 + this.motionY, this.Z坐标 + this.通便Z);

            if (movingobjectposition != null)
            {
                vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            实体 实体 = null;
            List<实体> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.通便X, this.motionY, this.通便Z).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (int i = 0; i < list.size(); ++i)
            {
                实体 实体1 = (实体)list.get(i);

                if (实体1.canBeCollidedWith() && (实体1 != this.shooting实体 || this.ticksInAir >= 5))
                {
                    float f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = 实体1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec31.squareDistanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            实体 = 实体1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (实体 != null)
            {
                movingobjectposition = new MovingObjectPosition(实体);
            }

            if (movingobjectposition != null && movingobjectposition.实体Hit != null && movingobjectposition.实体Hit instanceof 实体Player)
            {
                实体Player entityplayer = (实体Player)movingobjectposition.实体Hit;

                if (entityplayer.capabilities.disableDamage || this.shooting实体 instanceof 实体Player && !((实体Player)this.shooting实体).canAttackPlayer(entityplayer))
                {
                    movingobjectposition = null;
                }
            }

            if (movingobjectposition != null)
            {
                if (movingobjectposition.实体Hit != null)
                {
                    float f2 = MathHelper.sqrt_double(this.通便X * this.通便X + this.motionY * this.motionY + this.通便Z * this.通便Z);
                    int l = MathHelper.ceiling_double_int((double)f2 * this.damage);

                    if (this.getIsCritical())
                    {
                        l += this.rand.nextInt(l / 2 + 2);
                    }

                    DamageSource damagesource;

                    if (this.shooting实体 == null)
                    {
                        damagesource = DamageSource.causeArrowDamage(this, this);
                    }
                    else
                    {
                        damagesource = DamageSource.causeArrowDamage(this, this.shooting实体);
                    }

                    if (this.isBurning() && !(movingobjectposition.实体Hit instanceof 实体Enderman))
                    {
                        movingobjectposition.实体Hit.setFire(5);
                    }

                    if (movingobjectposition.实体Hit.attackEntityFrom(damagesource, (float)l))
                    {
                        if (movingobjectposition.实体Hit instanceof 实体LivingBase)
                        {
                            实体LivingBase entitylivingbase = (实体LivingBase)movingobjectposition.实体Hit;

                            if (!this.worldObj.isRemote)
                            {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength > 0)
                            {
                                float f7 = MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z);

                                if (f7 > 0.0F)
                                {
                                    movingobjectposition.实体Hit.addVelocity(this.通便X * (double)this.knockbackStrength * 0.6000000238418579D / (double)f7, 0.1D, this.通便Z * (double)this.knockbackStrength * 0.6000000238418579D / (double)f7);
                                }
                            }

                            if (this.shooting实体 instanceof 实体LivingBase)
                            {
                                EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shooting实体);
                                EnchantmentHelper.applyArthropodEnchantments((实体LivingBase)this.shooting实体, entitylivingbase);
                            }

                            if (this.shooting实体 != null && movingobjectposition.实体Hit != this.shooting实体 && movingobjectposition.实体Hit instanceof 实体Player && this.shooting实体 instanceof 实体PlayerMP)
                            {
                                ((实体PlayerMP)this.shooting实体).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                            }
                        }

                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        if (!(movingobjectposition.实体Hit instanceof 实体Enderman))
                        {
                            this.setDead();
                        }
                    }
                    else
                    {
                        this.通便X *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.通便Z *= -0.10000000149011612D;
                        this.旋转侧滑 += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                }
                else
                {
                    阻止位置 blockpos1 = movingobjectposition.getBlockPos();
                    this.xTile = blockpos1.getX();
                    this.yTile = blockpos1.getY();
                    this.zTile = blockpos1.getZ();
                    IBlockState iblockstate1 = this.worldObj.getBlockState(blockpos1);
                    this.inTile = iblockstate1.getBlock();
                    this.inData = this.inTile.getMetaFromState(iblockstate1);
                    this.通便X = (double)((float)(movingobjectposition.hitVec.xCoord - this.X坐标));
                    this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.Y坐标));
                    this.通便Z = (double)((float)(movingobjectposition.hitVec.zCoord - this.Z坐标));
                    float f5 = MathHelper.sqrt_double(this.通便X * this.通便X + this.motionY * this.motionY + this.通便Z * this.通便Z);
                    this.X坐标 -= this.通便X / (double)f5 * 0.05000000074505806D;
                    this.Y坐标 -= this.motionY / (double)f5 * 0.05000000074505806D;
                    this.Z坐标 -= this.通便Z / (double)f5 * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);

                    if (this.inTile.getMaterial() != Material.air)
                    {
                        this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos1, iblockstate1, this);
                    }
                }
            }

            if (this.getIsCritical())
            {
                for (int k = 0; k < 4; ++k)
                {
                    this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.X坐标 + this.通便X * (double)k / 4.0D, this.Y坐标 + this.motionY * (double)k / 4.0D, this.Z坐标 + this.通便Z * (double)k / 4.0D, -this.通便X, -this.motionY + 0.2D, -this.通便Z, new int[0]);
                }
            }

            this.X坐标 += this.通便X;
            this.Y坐标 += this.motionY;
            this.Z坐标 += this.通便Z;
            float f3 = MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z);
            this.旋转侧滑 = (float)(MathHelper.atan2(this.通便X, this.通便Z) * 180.0D / Math.PI);

            for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f3) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
            {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
            {
                this.prevRotationPitch += 360.0F;
            }

            while (this.旋转侧滑 - this.prevRotationYaw < -180.0F)
            {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.旋转侧滑 - this.prevRotationYaw >= 180.0F)
            {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.旋转侧滑 = this.prevRotationYaw + (this.旋转侧滑 - this.prevRotationYaw) * 0.2F;
            float f4 = 0.99F;
            float f6 = 0.05F;

            if (this.isInWater())
            {
                for (int i1 = 0; i1 < 4; ++i1)
                {
                    float f8 = 0.25F;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.X坐标 - this.通便X * (double)f8, this.Y坐标 - this.motionY * (double)f8, this.Z坐标 - this.通便Z * (double)f8, this.通便X, this.motionY, this.通便Z, new int[0]);
                }

                f4 = 0.6F;
            }

            if (this.isWet())
            {
                this.extinguish();
            }

            this.通便X *= (double)f4;
            this.motionY *= (double)f4;
            this.通便Z *= (double)f4;
            this.motionY -= (double)f6;
            this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
            this.doBlockCollisions();
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        tagCompound.setShort("life", (short)this.ticksInGround);
        图像位置 resourcelocation = (图像位置)Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("inData", (byte)this.inData);
        tagCompound.setByte("shake", (byte)this.arrowShake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        tagCompound.setByte("pickup", (byte)this.canBePickedUp);
        tagCompound.setDouble("damage", this.damage);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        this.ticksInGround = tagCompund.getShort("life");

        if (tagCompund.hasKey("inTile", 8))
        {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else
        {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
        }

        this.inData = tagCompund.getByte("inData") & 255;
        this.arrowShake = tagCompund.getByte("shake") & 255;
        this.inGround = tagCompund.getByte("inGround") == 1;

        if (tagCompund.hasKey("damage", 99))
        {
            this.damage = tagCompund.getDouble("damage");
        }

        if (tagCompund.hasKey("pickup", 99))
        {
            this.canBePickedUp = tagCompund.getByte("pickup");
        }
        else if (tagCompund.hasKey("player", 99))
        {
            this.canBePickedUp = tagCompund.getBoolean("player") ? 1 : 0;
        }
    }

    public void onCollideWithPlayer(实体Player entityIn)
    {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1)))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityIn.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void setDamage(double damageIn)
    {
        this.damage = damageIn;
    }

    public double getDamage()
    {
        return this.damage;
    }

    public void setKnockbackStrength(int knockbackStrengthIn)
    {
        this.knockbackStrength = knockbackStrengthIn;
    }

    public boolean canAttackWithItem()
    {
        return false;
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public void setIsCritical(boolean critical)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (critical)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    public boolean getIsCritical()
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        return (b0 & 1) != 0;
    }
}
