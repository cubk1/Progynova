package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class 实体Throwable extends 实体 implements IProjectile
{
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    protected boolean inGround;
    public int throwableShake;
    private 实体LivingBase thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;

    public 实体Throwable(World worldIn)
    {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
    }

    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0))
        {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    public 实体Throwable(World worldIn, 实体LivingBase throwerIn)
    {
        super(worldIn);
        this.thrower = throwerIn;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(throwerIn.X坐标, throwerIn.Y坐标 + (double)throwerIn.getEyeHeight(), throwerIn.Z坐标, throwerIn.旋转侧滑, throwerIn.rotationPitch);
        this.X坐标 -= (double)(MathHelper.cos(this.旋转侧滑 / 180.0F * (float)Math.PI) * 0.16F);
        this.Y坐标 -= 0.10000000149011612D;
        this.Z坐标 -= (double)(MathHelper.sin(this.旋转侧滑 / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
        float f = 0.4F;
        this.通便X = (double)(-MathHelper.sin(this.旋转侧滑 / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.通便Z = (double)(MathHelper.cos(this.旋转侧滑 / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (double)(-MathHelper.sin((this.rotationPitch + this.getInaccuracy()) / 180.0F * (float)Math.PI) * f);
        this.setThrowableHeading(this.通便X, this.motionY, this.通便Z, this.getVelocity(), 1.0F);
    }

    public 实体Throwable(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.ticksInGround = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
    }

    protected float getVelocity()
    {
        return 1.5F;
    }

    protected float getInaccuracy()
    {
        return 0.0F;
    }

    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt_double(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
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
        }
    }

    public void onUpdate()
    {
        this.lastTickPosX = this.X坐标;
        this.lastTickPosY = this.Y坐标;
        this.lastTickPosZ = this.Z坐标;
        super.onUpdate();

        if (this.throwableShake > 0)
        {
            --this.throwableShake;
        }

        if (this.inGround)
        {
            if (this.worldObj.getBlockState(new 阻止位置(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200)
                {
                    this.setDead();
                }

                return;
            }

            this.inGround = false;
            this.通便X *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.通便Z *= (double)(this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        }
        else
        {
            ++this.ticksInAir;
        }

        Vec3 vec3 = new Vec3(this.X坐标, this.Y坐标, this.Z坐标);
        Vec3 vec31 = new Vec3(this.X坐标 + this.通便X, this.Y坐标 + this.motionY, this.Z坐标 + this.通便Z);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = new Vec3(this.X坐标, this.Y坐标, this.Z坐标);
        vec31 = new Vec3(this.X坐标 + this.通便X, this.Y坐标 + this.motionY, this.Z坐标 + this.通便Z);

        if (movingobjectposition != null)
        {
            vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isRemote)
        {
            实体 实体 = null;
            List<实体> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.通便X, this.motionY, this.通便Z).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            实体LivingBase entitylivingbase = this.getThrower();

            for (int j = 0; j < list.size(); ++j)
            {
                实体 实体1 = (实体)list.get(j);

                if (实体1.canBeCollidedWith() && (实体1 != entitylivingbase || this.ticksInAir >= 5))
                {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = 实体1.getEntityBoundingBox().expand((double)f, (double)f, (double)f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec3.squareDistanceTo(movingobjectposition1.hitVec);

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
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.portal)
            {
                this.setPortal(movingobjectposition.getBlockPos());
            }
            else
            {
                this.onImpact(movingobjectposition);
            }
        }

        this.X坐标 += this.通便X;
        this.Y坐标 += this.motionY;
        this.Z坐标 += this.通便Z;
        float f1 = MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z);
        this.旋转侧滑 = (float)(MathHelper.atan2(this.通便X, this.通便Z) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
        float f2 = 0.99F;
        float f3 = this.getGravityVelocity();

        if (this.isInWater())
        {
            for (int i = 0; i < 4; ++i)
            {
                float f4 = 0.25F;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.X坐标 - this.通便X * (double)f4, this.Y坐标 - this.motionY * (double)f4, this.Z坐标 - this.通便Z * (double)f4, this.通便X, this.motionY, this.通便Z, new int[0]);
            }

            f2 = 0.8F;
        }

        this.通便X *= (double)f2;
        this.motionY *= (double)f2;
        this.通便Z *= (double)f2;
        this.motionY -= (double)f3;
        this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
    }

    protected float getGravityVelocity()
    {
        return 0.03F;
    }

    protected abstract void onImpact(MovingObjectPosition p_70184_1_);

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        图像位置 resourcelocation = (图像位置)Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("shake", (byte)this.throwableShake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));

        if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower instanceof 实体Player)
        {
            this.throwerName = this.thrower.getName();
        }

        tagCompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");

        if (tagCompund.hasKey("inTile", 8))
        {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else
        {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
        }

        this.throwableShake = tagCompund.getByte("shake") & 255;
        this.inGround = tagCompund.getByte("inGround") == 1;
        this.thrower = null;
        this.throwerName = tagCompund.getString("ownerName");

        if (this.throwerName != null && this.throwerName.length() == 0)
        {
            this.throwerName = null;
        }

        this.thrower = this.getThrower();
    }

    public 实体LivingBase getThrower()
    {
        if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0)
        {
            this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);

            if (this.thrower == null && this.worldObj instanceof WorldServer)
            {
                try
                {
                    实体 实体 = ((WorldServer)this.worldObj).getEntityFromUuid(UUID.fromString(this.throwerName));

                    if (实体 instanceof 实体LivingBase)
                    {
                        this.thrower = (实体LivingBase) 实体;
                    }
                }
                catch (Throwable var2)
                {
                    this.thrower = null;
                }
            }
        }

        return this.thrower;
    }
}
