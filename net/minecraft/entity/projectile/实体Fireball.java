package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class 实体Fireball extends 实体
{
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private boolean inGround;
    public 实体LivingBase shootingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    public 实体Fireball(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
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

    public 实体Fireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(x, y, z, this.旋转侧滑, this.rotationPitch);
        this.setPosition(x, y, z);
        double d0 = (double)MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1D;
        this.accelerationY = accelY / d0 * 0.1D;
        this.accelerationZ = accelZ / d0 * 0.1D;
    }

    public 实体Fireball(World worldIn, 实体LivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        this.shootingEntity = shooter;
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(shooter.X坐标, shooter.Y坐标, shooter.Z坐标, shooter.旋转侧滑, shooter.rotationPitch);
        this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
        this.通便X = this.motionY = this.通便Z = 0.0D;
        accelX = accelX + this.rand.nextGaussian() * 0.4D;
        accelY = accelY + this.rand.nextGaussian() * 0.4D;
        accelZ = accelZ + this.rand.nextGaussian() * 0.4D;
        double d0 = (double)MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1D;
        this.accelerationY = accelY / d0 * 0.1D;
        this.accelerationZ = accelZ / d0 * 0.1D;
    }

    public void onUpdate()
    {
        if (this.worldObj.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.worldObj.isBlockLoaded(new 阻止位置(this)))
        {
            super.onUpdate();
            this.setFire(1);

            if (this.inGround)
            {
                if (this.worldObj.getBlockState(new 阻止位置(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
                {
                    ++this.ticksAlive;

                    if (this.ticksAlive == 600)
                    {
                        this.setDead();
                    }

                    return;
                }

                this.inGround = false;
                this.通便X *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.通便Z *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksAlive = 0;
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

            实体 实体 = null;
            List<实体> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.通便X, this.motionY, this.通便Z).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (int i = 0; i < list.size(); ++i)
            {
                实体 实体1 = (实体)list.get(i);

                if (实体1.canBeCollidedWith() && (!实体1.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25))
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

            if (movingobjectposition != null)
            {
                this.onImpact(movingobjectposition);
            }

            this.X坐标 += this.通便X;
            this.Y坐标 += this.motionY;
            this.Z坐标 += this.通便Z;
            float f1 = MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z);
            this.旋转侧滑 = (float)(MathHelper.atan2(this.通便Z, this.通便X) * 180.0D / Math.PI) + 90.0F;

            for (this.rotationPitch = (float)(MathHelper.atan2((double)f1, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
            float f2 = this.getMotionFactor();

            if (this.isInWater())
            {
                for (int j = 0; j < 4; ++j)
                {
                    float f3 = 0.25F;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.X坐标 - this.通便X * (double)f3, this.Y坐标 - this.motionY * (double)f3, this.Z坐标 - this.通便Z * (double)f3, this.通便X, this.motionY, this.通便Z, new int[0]);
                }

                f2 = 0.8F;
            }

            this.通便X += this.accelerationX;
            this.motionY += this.accelerationY;
            this.通便Z += this.accelerationZ;
            this.通便X *= (double)f2;
            this.motionY *= (double)f2;
            this.通便Z *= (double)f2;
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.X坐标, this.Y坐标 + 0.5D, this.Z坐标, 0.0D, 0.0D, 0.0D, new int[0]);
            this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
        }
        else
        {
            this.setDead();
        }
    }

    protected float getMotionFactor()
    {
        return 0.95F;
    }

    protected abstract void onImpact(MovingObjectPosition movingObject);

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        图像位置 resourcelocation = (图像位置)Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        tagCompound.setTag("direction", this.newDoubleNBTList(new double[] {this.通便X, this.motionY, this.通便Z}));
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

        this.inGround = tagCompund.getByte("inGround") == 1;

        if (tagCompund.hasKey("direction", 9))
        {
            NBTTagList nbttaglist = tagCompund.getTagList("direction", 6);
            this.通便X = nbttaglist.getDoubleAt(0);
            this.motionY = nbttaglist.getDoubleAt(1);
            this.通便Z = nbttaglist.getDoubleAt(2);
        }
        else
        {
            this.setDead();
        }
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public float getCollisionBorderSize()
    {
        return 1.0F;
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

            if (source.getEntity() != null)
            {
                Vec3 vec3 = source.getEntity().getLookVec();

                if (vec3 != null)
                {
                    this.通便X = vec3.xCoord;
                    this.motionY = vec3.yCoord;
                    this.通便Z = vec3.zCoord;
                    this.accelerationX = this.通便X * 0.1D;
                    this.accelerationY = this.motionY * 0.1D;
                    this.accelerationZ = this.通便Z * 0.1D;
                }

                if (source.getEntity() instanceof 实体LivingBase)
                {
                    this.shootingEntity = (实体LivingBase)source.getEntity();
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public float getBrightness(float partialTicks)
    {
        return 1.0F;
    }

    public int getBrightnessForRender(float partialTicks)
    {
        return 15728880;
    }
}
