package net.minecraft.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.实体LightningBolt;
import net.minecraft.entity.item.实体Item;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class 实体 implements ICommandSender
{
    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static int nextEntityID;
    private int entityId;
    public double renderDistanceWeight;
    public boolean preventEntitySpawning;
    public 实体 riddenBy实体;
    public 实体 riding实体;
    public boolean forceSpawn;
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double X坐标;
    public double Y坐标;
    public double Z坐标;
    public double 通便X;
    public double motionY;
    public double 通便Z;
    public float 旋转侧滑;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    private AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public boolean isCollided;
    public boolean velocityChanged;
    protected boolean isInWeb;
    private boolean isOutsideBorder;
    public boolean isDead;
    public float width;
    public float height;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    private int nextStepDistance;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    protected Random rand;
    public int 已存在的刻度;
    public int fireResistance;
    private int fire;
    protected boolean inWater;
    public int hurtResistantTime;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    protected boolean inPortal;
    protected int portalCounter;
    public int dimension;
    protected 阻止位置 lastPortalPos;
    protected Vec3 lastPortalVec;
    protected EnumFacing teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    private final CommandResultStats cmdResultStats;

    public int getEntityId()
    {
        return this.entityId;
    }

    public void setEntityId(int id)
    {
        this.entityId = id;
    }

    public void onKillCommand()
    {
        this.setDead();
    }

    public 实体(World worldIn)
    {
        this.entityId = nextEntityID++;
        this.renderDistanceWeight = 1.0D;
        this.boundingBox = ZERO_AABB;
        this.width = 0.6F;
        this.height = 1.8F;
        this.nextStepDistance = 1;
        this.rand = new Random();
        this.fireResistance = 1;
        this.firstUpdate = true;
        this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
        this.cmdResultStats = new CommandResultStats();
        this.worldObj = worldIn;
        this.setPosition(0.0D, 0.0D, 0.0D);

        if (worldIn != null)
        {
            this.dimension = worldIn.provider.getDimensionId();
        }

        this.dataWatcher = new DataWatcher(this);
        this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(1, Short.valueOf((short)300));
        this.dataWatcher.addObject(3, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(2, "");
        this.dataWatcher.addObject(4, Byte.valueOf((byte)0));
        this.entityInit();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher()
    {
        return this.dataWatcher;
    }

    public boolean equals(Object p_equals_1_)
    {
        return p_equals_1_ instanceof 实体 ? ((实体)p_equals_1_).entityId == this.entityId : false;
    }

    public int hashCode()
    {
        return this.entityId;
    }

    protected void preparePlayerToSpawn()
    {
        if (this.worldObj != null)
        {
            while (this.Y坐标 > 0.0D && this.Y坐标 < 256.0D)
            {
                this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);

                if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty())
                {
                    break;
                }

                ++this.Y坐标;
            }

            this.通便X = this.motionY = this.通便Z = 0.0D;
            this.rotationPitch = 0.0F;
        }
    }

    public void setDead()
    {
        this.isDead = true;
    }

    protected void setSize(float width, float height)
    {
        if (width != this.width || height != this.height)
        {
            float f = this.width;
            this.width = width;
            this.height = height;
            this.setEntityBoundingBox(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().minX + (double)this.width, this.getEntityBoundingBox().minY + (double)this.height, this.getEntityBoundingBox().minZ + (double)this.width));

            if (this.width > f && !this.firstUpdate && !this.worldObj.isRemote)
            {
                this.moveEntity((double)(f - this.width), 0.0D, (double)(f - this.width));
            }
        }
    }

    protected void setRotation(float yaw, float pitch)
    {
        this.旋转侧滑 = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }

    public void setPosition(double x, double y, double z)
    {
        this.X坐标 = x;
        this.Y坐标 = y;
        this.Z坐标 = z;
        float f = this.width / 2.0F;
        float f1 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f));
    }

    public void setAngles(float yaw, float pitch)
    {
        float f = this.rotationPitch;
        float f1 = this.旋转侧滑;
        this.旋转侧滑 = (float)((double)this.旋转侧滑 + (double)yaw * 0.15D);
        this.rotationPitch = (float)((double)this.rotationPitch - (double)pitch * 0.15D);
        this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0F, 90.0F);
        this.prevRotationPitch += this.rotationPitch - f;
        this.prevRotationYaw += this.旋转侧滑 - f1;
    }

    public void onUpdate()
    {
        this.onEntityUpdate();
    }

    public void onEntityUpdate()
    {
        this.worldObj.theProfiler.startSection("entityBaseTick");

        if (this.riding实体 != null && this.riding实体.isDead)
        {
            this.riding实体 = null;
        }

        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.X坐标;
        this.prevPosY = this.Y坐标;
        this.prevPosZ = this.Z坐标;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.旋转侧滑;

        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer)
        {
            this.worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
            int i = this.getMaxInPortalTime();

            if (this.inPortal)
            {
                if (minecraftserver.getAllowNether())
                {
                    if (this.riding实体 == null && this.portalCounter++ >= i)
                    {
                        this.portalCounter = i;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int j;

                        if (this.worldObj.provider.getDimensionId() == -1)
                        {
                            j = 0;
                        }
                        else
                        {
                            j = -1;
                        }

                        this.travelToDimension(j);
                    }

                    this.inPortal = false;
                }
            }
            else
            {
                if (this.portalCounter > 0)
                {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0)
                {
                    this.portalCounter = 0;
                }
            }

            if (this.timeUntilPortal > 0)
            {
                --this.timeUntilPortal;
            }

            this.worldObj.theProfiler.endSection();
        }

        this.spawnRunningParticles();
        this.handleWaterMovement();

        if (this.worldObj.isRemote)
        {
            this.fire = 0;
        }
        else if (this.fire > 0)
        {
            if (this.isImmuneToFire)
            {
                this.fire -= 4;

                if (this.fire < 0)
                {
                    this.fire = 0;
                }
            }
            else
            {
                if (this.fire % 20 == 0)
                {
                    this.attackEntityFrom(DamageSource.onFire, 1.0F);
                }

                --this.fire;
            }
        }

        if (this.isInLava())
        {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }

        if (this.Y坐标 < -64.0D)
        {
            this.kill();
        }

        if (!this.worldObj.isRemote)
        {
            this.setFlag(0, this.fire > 0);
        }

        this.firstUpdate = false;
        this.worldObj.theProfiler.endSection();
    }

    public int getMaxInPortalTime()
    {
        return 0;
    }

    protected void setOnFireFromLava()
    {
        if (!this.isImmuneToFire)
        {
            this.attackEntityFrom(DamageSource.lava, 4.0F);
            this.setFire(15);
        }
    }

    public void setFire(int seconds)
    {
        int i = seconds * 20;
        i = EnchantmentProtection.getFireTimeForEntity(this, i);

        if (this.fire < i)
        {
            this.fire = i;
        }
    }

    public void extinguish()
    {
        this.fire = 0;
    }

    protected void kill()
    {
        this.setDead();
    }

    public boolean isOffsetPositionInLiquid(double x, double y, double z)
    {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().offset(x, y, z);
        return this.isLiquidPresentInAABB(axisalignedbb);
    }

    private boolean isLiquidPresentInAABB(AxisAlignedBB bb)
    {
        return this.worldObj.getCollidingBoundingBoxes(this, bb).isEmpty() && !this.worldObj.isAnyLiquid(bb);
    }

    public void moveEntity(double x, double y, double z)
    {
        if (this.noClip)
        {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.resetPositionToBB();
        }
        else
        {
            this.worldObj.theProfiler.startSection("move");
            double d0 = this.X坐标;
            double d1 = this.Y坐标;
            double d2 = this.Z坐标;

            if (this.isInWeb)
            {
                this.isInWeb = false;
                x *= 0.25D;
                y *= 0.05000000074505806D;
                z *= 0.25D;
                this.通便X = 0.0D;
                this.motionY = 0.0D;
                this.通便Z = 0.0D;
            }

            double d3 = x;
            double d4 = y;
            double d5 = z;
            boolean flag = this.onGround && this.正在下蹲() && this instanceof 实体Player;

            if (flag)
            {
                double d6;

                for (d6 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty(); d3 = x)
                {
                    if (x < d6 && x >= -d6)
                    {
                        x = 0.0D;
                    }
                    else if (x > 0.0D)
                    {
                        x -= d6;
                    }
                    else
                    {
                        x += d6;
                    }
                }

                for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty(); d5 = z)
                {
                    if (z < d6 && z >= -d6)
                    {
                        z = 0.0D;
                    }
                    else if (z > 0.0D)
                    {
                        z -= d6;
                    }
                    else
                    {
                        z += d6;
                    }
                }

                for (; x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty(); d5 = z)
                {
                    if (x < d6 && x >= -d6)
                    {
                        x = 0.0D;
                    }
                    else if (x > 0.0D)
                    {
                        x -= d6;
                    }
                    else
                    {
                        x += d6;
                    }

                    d3 = x;

                    if (z < d6 && z >= -d6)
                    {
                        z = 0.0D;
                    }
                    else if (z > 0.0D)
                    {
                        z -= d6;
                    }
                    else
                    {
                        z += d6;
                    }
                }
            }

            List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

            for (AxisAlignedBB axisalignedbb1 : list1)
            {
                y = axisalignedbb1.calculateYOffset(this.getEntityBoundingBox(), y);
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
            boolean flag1 = this.onGround || d4 != y && d4 < 0.0D;

            for (AxisAlignedBB axisalignedbb2 : list1)
            {
                x = axisalignedbb2.calculateXOffset(this.getEntityBoundingBox(), x);
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb13 : list1)
            {
                z = axisalignedbb13.calculateZOffset(this.getEntityBoundingBox(), z);
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));

            if (this.stepHeight > 0.0F && flag1 && (d3 != x || d5 != z))
            {
                double d11 = x;
                double d7 = y;
                double d8 = z;
                AxisAlignedBB axisalignedbb3 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisalignedbb);
                y = (double)this.stepHeight;
                List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(d3, y, d5));
                AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
                AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0D, d5);
                double d9 = y;

                for (AxisAlignedBB axisalignedbb6 : list)
                {
                    d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
                double d15 = d3;

                for (AxisAlignedBB axisalignedbb7 : list)
                {
                    d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
                }

                axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
                double d16 = d5;

                for (AxisAlignedBB axisalignedbb8 : list)
                {
                    d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
                AxisAlignedBB axisalignedbb14 = this.getEntityBoundingBox();
                double d17 = y;

                for (AxisAlignedBB axisalignedbb9 : list)
                {
                    d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
                }

                axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
                double d18 = d3;

                for (AxisAlignedBB axisalignedbb10 : list)
                {
                    d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
                }

                axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
                double d19 = d5;

                for (AxisAlignedBB axisalignedbb11 : list)
                {
                    d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
                }

                axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
                double d20 = d15 * d15 + d16 * d16;
                double d10 = d18 * d18 + d19 * d19;

                if (d20 > d10)
                {
                    x = d15;
                    z = d16;
                    y = -d9;
                    this.setEntityBoundingBox(axisalignedbb4);
                }
                else
                {
                    x = d18;
                    z = d19;
                    y = -d17;
                    this.setEntityBoundingBox(axisalignedbb14);
                }

                for (AxisAlignedBB axisalignedbb12 : list)
                {
                    y = axisalignedbb12.calculateYOffset(this.getEntityBoundingBox(), y);
                }

                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));

                if (d11 * d11 + d8 * d8 >= x * x + z * z)
                {
                    x = d11;
                    y = d7;
                    z = d8;
                    this.setEntityBoundingBox(axisalignedbb3);
                }
            }

            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.resetPositionToBB();
            this.isCollidedHorizontally = d3 != x || d5 != z;
            this.isCollidedVertically = d4 != y;
            this.onGround = this.isCollidedVertically && d4 < 0.0D;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            int i = MathHelper.floor_double(this.X坐标);
            int j = MathHelper.floor_double(this.Y坐标 - 0.20000000298023224D);
            int k = MathHelper.floor_double(this.Z坐标);
            阻止位置 blockpos = new 阻止位置(i, j, k);
            Block block1 = this.worldObj.getBlockState(blockpos).getBlock();

            if (block1.getMaterial() == Material.air)
            {
                Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();

                if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)
                {
                    block1 = block;
                    blockpos = blockpos.down();
                }
            }

            this.updateFallState(y, this.onGround, block1, blockpos);

            if (d3 != x)
            {
                this.通便X = 0.0D;
            }

            if (d5 != z)
            {
                this.通便Z = 0.0D;
            }

            if (d4 != y)
            {
                block1.onLanded(this.worldObj, this);
            }

            if (this.canTriggerWalking() && !flag && this.riding实体 == null)
            {
                double d12 = this.X坐标 - d0;
                double d13 = this.Y坐标 - d1;
                double d14 = this.Z坐标 - d2;

                if (block1 != Blocks.ladder)
                {
                    d13 = 0.0D;
                }

                if (block1 != null && this.onGround)
                {
                    block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
                }

                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d12 * d12 + d14 * d14) * 0.6D);
                this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);

                if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && block1.getMaterial() != Material.air)
                {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;

                    if (this.isInWater())
                    {
                        float f = MathHelper.sqrt_double(this.通便X * this.通便X * 0.20000000298023224D + this.motionY * this.motionY + this.通便Z * this.通便Z * 0.20000000298023224D) * 0.35F;

                        if (f > 1.0F)
                        {
                            f = 1.0F;
                        }

                        this.playSound(this.getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    }

                    this.playStepSound(blockpos, block1);
                }
            }

            try
            {
                this.doBlockCollisions();
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            boolean flag2 = this.isWet();

            if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D)))
            {
                this.dealFireDamage(1);

                if (!flag2)
                {
                    ++this.fire;

                    if (this.fire == 0)
                    {
                        this.setFire(8);
                    }
                }
            }
            else if (this.fire <= 0)
            {
                this.fire = -this.fireResistance;
            }

            if (flag2 && this.fire > 0)
            {
                this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.fire = -this.fireResistance;
            }

            this.worldObj.theProfiler.endSection();
        }
    }

    private void resetPositionToBB()
    {
        this.X坐标 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
        this.Y坐标 = this.getEntityBoundingBox().minY;
        this.Z坐标 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
    }

    protected String getSwimSound()
    {
        return "game.neutral.swim";
    }

    protected void doBlockCollisions()
    {
        阻止位置 blockpos = new 阻止位置(this.getEntityBoundingBox().minX + 0.001D, this.getEntityBoundingBox().minY + 0.001D, this.getEntityBoundingBox().minZ + 0.001D);
        阻止位置 blockpos1 = new 阻止位置(this.getEntityBoundingBox().maxX - 0.001D, this.getEntityBoundingBox().maxY - 0.001D, this.getEntityBoundingBox().maxZ - 0.001D);

        if (this.worldObj.isAreaLoaded(blockpos, blockpos1))
        {
            for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i)
            {
                for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j)
                {
                    for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k)
                    {
                        阻止位置 blockpos2 = new 阻止位置(i, j, k);
                        IBlockState iblockstate = this.worldObj.getBlockState(blockpos2);

                        try
                        {
                            iblockstate.getBlock().onEntityCollidedWithBlock(this.worldObj, blockpos2, iblockstate, this);
                        }
                        catch (Throwable throwable)
                        {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashreportcategory, blockpos2, iblockstate);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }
    }

    protected void playStepSound(阻止位置 pos, Block blockIn)
    {
        Block.SoundType block$soundtype = blockIn.stepSound;

        if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer)
        {
            block$soundtype = Blocks.snow_layer.stepSound;
            this.playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
        }
        else if (!blockIn.getMaterial().isLiquid())
        {
            this.playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
        }
    }

    public void playSound(String name, float volume, float pitch)
    {
        if (!this.isSilent())
        {
            this.worldObj.playSoundAtEntity(this, name, volume, pitch);
        }
    }

    public boolean isSilent()
    {
        return this.dataWatcher.getWatchableObjectByte(4) == 1;
    }

    public void setSilent(boolean isSilent)
    {
        this.dataWatcher.updateObject(4, Byte.valueOf((byte)(isSilent ? 1 : 0)));
    }

    protected boolean canTriggerWalking()
    {
        return true;
    }

    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, 阻止位置 pos)
    {
        if (onGroundIn)
        {
            if (this.fallDistance > 0.0F)
            {
                if (blockIn != null)
                {
                    blockIn.onFallenUpon(this.worldObj, pos, this, this.fallDistance);
                }
                else
                {
                    this.fall(this.fallDistance, 1.0F);
                }

                this.fallDistance = 0.0F;
            }
        }
        else if (y < 0.0D)
        {
            this.fallDistance = (float)((double)this.fallDistance - y);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox()
    {
        return null;
    }

    protected void dealFireDamage(int amount)
    {
        if (!this.isImmuneToFire)
        {
            this.attackEntityFrom(DamageSource.inFire, (float)amount);
        }
    }

    public final boolean isImmuneToFire()
    {
        return this.isImmuneToFire;
    }

    public void fall(float distance, float damageMultiplier)
    {
        if (this.riddenBy实体 != null)
        {
            this.riddenBy实体.fall(distance, damageMultiplier);
        }
    }

    public boolean isWet()
    {
        return this.inWater || this.worldObj.isRainingAt(new 阻止位置(this.X坐标, this.Y坐标, this.Z坐标)) || this.worldObj.isRainingAt(new 阻止位置(this.X坐标, this.Y坐标 + (double)this.height, this.Z坐标));
    }

    public boolean isInWater()
    {
        return this.inWater;
    }

    public boolean handleWaterMovement()
    {
        if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this))
        {
            if (!this.inWater && !this.firstUpdate)
            {
                this.resetHeight();
            }

            this.fallDistance = 0.0F;
            this.inWater = true;
            this.fire = 0;
        }
        else
        {
            this.inWater = false;
        }

        return this.inWater;
    }

    protected void resetHeight()
    {
        float f = MathHelper.sqrt_double(this.通便X * this.通便X * 0.20000000298023224D + this.motionY * this.motionY + this.通便Z * this.通便Z * 0.20000000298023224D) * 0.2F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        this.playSound(this.getSplashSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
        float f1 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY);

        for (int i = 0; (float)i < 1.0F + this.width * 20.0F; ++i)
        {
            float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            float f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.X坐标 + (double)f2, (double)(f1 + 1.0F), this.Z坐标 + (double)f3, this.通便X, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.通便Z, new int[0]);
        }

        for (int j = 0; (float)j < 1.0F + this.width * 20.0F; ++j)
        {
            float f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.X坐标 + (double)f4, (double)(f1 + 1.0F), this.Z坐标 + (double)f5, this.通便X, this.motionY, this.通便Z, new int[0]);
        }
    }

    public void spawnRunningParticles()
    {
        if (this.isSprinting() && !this.isInWater())
        {
            this.createRunningParticles();
        }
    }

    protected void createRunningParticles()
    {
        int i = MathHelper.floor_double(this.X坐标);
        int j = MathHelper.floor_double(this.Y坐标 - 0.20000000298023224D);
        int k = MathHelper.floor_double(this.Z坐标);
        阻止位置 blockpos = new 阻止位置(i, j, k);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getRenderType() != -1)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.X坐标 + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.Z坐标 + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.通便X * 4.0D, 1.5D, -this.通便Z * 4.0D, new int[] {Block.getStateId(iblockstate)});
        }
    }

    protected String getSplashSound()
    {
        return "game.neutral.swim.splash";
    }

    public boolean isInsideOfMaterial(Material materialIn)
    {
        double d0 = this.Y坐标 + (double)this.getEyeHeight();
        阻止位置 blockpos = new 阻止位置(this.X坐标, d0, this.Z坐标);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getMaterial() == materialIn)
        {
            float f = BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111F;
            float f1 = (float)(blockpos.getY() + 1) - f;
            boolean flag = d0 < (double)f1;
            return !flag && this instanceof 实体Player ? false : flag;
        }
        else
        {
            return false;
        }
    }

    public boolean isInLava()
    {
        return this.worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
    }

    public void moveFlying(float strafe, float forward, float friction)
    {
        float f = strafe * strafe + forward * forward;

        if (f >= 1.0E-4F)
        {
            f = MathHelper.sqrt_float(f);

            if (f < 1.0F)
            {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1 = MathHelper.sin(this.旋转侧滑 * (float)Math.PI / 180.0F);
            float f2 = MathHelper.cos(this.旋转侧滑 * (float)Math.PI / 180.0F);
            this.通便X += (double)(strafe * f2 - forward * f1);
            this.通便Z += (double)(forward * f2 + strafe * f1);
        }
    }

    public int getBrightnessForRender(float partialTicks)
    {
        阻止位置 blockpos = new 阻止位置(this.X坐标, this.Y坐标 + (double)this.getEyeHeight(), this.Z坐标);
        return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
    }

    public float getBrightness(float partialTicks)
    {
        阻止位置 blockpos = new 阻止位置(this.X坐标, this.Y坐标 + (double)this.getEyeHeight(), this.Z坐标);
        return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getLightBrightness(blockpos) : 0.0F;
    }

    public void setWorld(World worldIn)
    {
        this.worldObj = worldIn;
    }

    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
    {
        this.prevPosX = this.X坐标 = x;
        this.prevPosY = this.Y坐标 = y;
        this.prevPosZ = this.Z坐标 = z;
        this.prevRotationYaw = this.旋转侧滑 = yaw;
        this.prevRotationPitch = this.rotationPitch = pitch;
        double d0 = (double)(this.prevRotationYaw - yaw);

        if (d0 < -180.0D)
        {
            this.prevRotationYaw += 360.0F;
        }

        if (d0 >= 180.0D)
        {
            this.prevRotationYaw -= 360.0F;
        }

        this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
        this.setRotation(yaw, pitch);
    }

    public void moveToBlockPosAndAngles(阻止位置 pos, float rotationYawIn, float rotationPitchIn)
    {
        this.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, rotationYawIn, rotationPitchIn);
    }

    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
    {
        this.lastTickPosX = this.prevPosX = this.X坐标 = x;
        this.lastTickPosY = this.prevPosY = this.Y坐标 = y;
        this.lastTickPosZ = this.prevPosZ = this.Z坐标 = z;
        this.旋转侧滑 = yaw;
        this.rotationPitch = pitch;
        this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
    }

    public float getDistanceToEntity(实体 实体In)
    {
        float f = (float)(this.X坐标 - 实体In.X坐标);
        float f1 = (float)(this.Y坐标 - 实体In.Y坐标);
        float f2 = (float)(this.Z坐标 - 实体In.Z坐标);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    public double getDistanceSq(double x, double y, double z)
    {
        double d0 = this.X坐标 - x;
        double d1 = this.Y坐标 - y;
        double d2 = this.Z坐标 - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getDistanceSq(阻止位置 pos)
    {
        return pos.distanceSq(this.X坐标, this.Y坐标, this.Z坐标);
    }

    public double getDistanceSqToCenter(阻止位置 pos)
    {
        return pos.distanceSqToCenter(this.X坐标, this.Y坐标, this.Z坐标);
    }

    public double getDistance(double x, double y, double z)
    {
        double d0 = this.X坐标 - x;
        double d1 = this.Y坐标 - y;
        double d2 = this.Z坐标 - z;
        return (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double getDistanceSqToEntity(实体 实体In)
    {
        double d0 = this.X坐标 - 实体In.X坐标;
        double d1 = this.Y坐标 - 实体In.Y坐标;
        double d2 = this.Z坐标 - 实体In.Z坐标;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void onCollideWithPlayer(实体Player entityIn)
    {
    }

    public void applyEntityCollision(实体 实体In)
    {
        if (实体In.riddenBy实体 != this && 实体In.riding实体 != this)
        {
            if (!实体In.noClip && !this.noClip)
            {
                double d0 = 实体In.X坐标 - this.X坐标;
                double d1 = 实体In.Z坐标 - this.Z坐标;
                double d2 = MathHelper.abs_max(d0, d1);

                if (d2 >= 0.009999999776482582D)
                {
                    d2 = (double)MathHelper.sqrt_double(d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D)
                    {
                        d3 = 1.0D;
                    }

                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
                    d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                    d1 = d1 * (double)(1.0F - this.entityCollisionReduction);

                    if (this.riddenBy实体 == null)
                    {
                        this.addVelocity(-d0, 0.0D, -d1);
                    }

                    if (实体In.riddenBy实体 == null)
                    {
                        实体In.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
        }
    }

    public void addVelocity(double x, double y, double z)
    {
        this.通便X += x;
        this.motionY += y;
        this.通便Z += z;
        this.isAirBorne = true;
    }

    protected void setBeenAttacked()
    {
        this.velocityChanged = true;
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
            return false;
        }
    }

    public Vec3 getLook(float partialTicks)
    {
        if (partialTicks == 1.0F)
        {
            return this.getVectorForRotation(this.rotationPitch, this.旋转侧滑);
        }
        else
        {
            float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
            float f1 = this.prevRotationYaw + (this.旋转侧滑 - this.prevRotationYaw) * partialTicks;
            return this.getVectorForRotation(f, f1);
        }
    }

    protected final Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    public Vec3 getPositionEyes(float partialTicks)
    {
        if (partialTicks == 1.0F)
        {
            return new Vec3(this.X坐标, this.Y坐标 + (double)this.getEyeHeight(), this.Z坐标);
        }
        else
        {
            double d0 = this.prevPosX + (this.X坐标 - this.prevPosX) * (double)partialTicks;
            double d1 = this.prevPosY + (this.Y坐标 - this.prevPosY) * (double)partialTicks + (double)this.getEyeHeight();
            double d2 = this.prevPosZ + (this.Z坐标 - this.prevPosZ) * (double)partialTicks;
            return new Vec3(d0, d1, d2);
        }
    }

    public MovingObjectPosition rayTrace(double blockReachDistance, float partialTicks)
    {
        Vec3 vec3 = this.getPositionEyes(partialTicks);
        Vec3 vec31 = this.getLook(partialTicks);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return this.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public boolean canBePushed()
    {
        return false;
    }

    public void addToPlayerScore(实体 实体In, int amount)
    {
    }

    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        double d0 = this.X坐标 - x;
        double d1 = this.Y坐标 - y;
        double d2 = this.Z坐标 - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        return this.isInRangeToRenderDist(d3);
    }

    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * this.renderDistanceWeight;
        return distance < d0 * d0;
    }

    public boolean writeMountToNBT(NBTTagCompound tagCompund)
    {
        String s = this.getEntityString();

        if (!this.isDead && s != null)
        {
            tagCompund.setString("id", s);
            this.writeToNBT(tagCompund);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean writeToNBTOptional(NBTTagCompound tagCompund)
    {
        String s = this.getEntityString();

        if (!this.isDead && s != null && this.riddenBy实体 == null)
        {
            tagCompund.setString("id", s);
            this.writeToNBT(tagCompund);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void writeToNBT(NBTTagCompound tagCompund)
    {
        try
        {
            tagCompund.setTag("Pos", this.newDoubleNBTList(new double[] {this.X坐标, this.Y坐标, this.Z坐标}));
            tagCompund.setTag("Motion", this.newDoubleNBTList(new double[] {this.通便X, this.motionY, this.通便Z}));
            tagCompund.setTag("Rotation", this.newFloatNBTList(new float[] {this.旋转侧滑, this.rotationPitch}));
            tagCompund.setFloat("FallDistance", this.fallDistance);
            tagCompund.setShort("Fire", (short)this.fire);
            tagCompund.setShort("Air", (short)this.getAir());
            tagCompund.setBoolean("OnGround", this.onGround);
            tagCompund.setInteger("Dimension", this.dimension);
            tagCompund.setBoolean("Invulnerable", this.invulnerable);
            tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
            tagCompund.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
            tagCompund.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());

            if (this.getCustomNameTag() != null && this.getCustomNameTag().length() > 0)
            {
                tagCompund.setString("CustomName", this.getCustomNameTag());
                tagCompund.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
            }

            this.cmdResultStats.writeStatsToNBT(tagCompund);

            if (this.isSilent())
            {
                tagCompund.setBoolean("Silent", this.isSilent());
            }

            this.writeEntityToNBT(tagCompund);

            if (this.riding实体 != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                if (this.riding实体.writeMountToNBT(nbttagcompound))
                {
                    tagCompund.setTag("Riding", nbttagcompound);
                }
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    public void readFromNBT(NBTTagCompound tagCompund)
    {
        try
        {
            NBTTagList nbttaglist = tagCompund.getTagList("Pos", 6);
            NBTTagList nbttaglist1 = tagCompund.getTagList("Motion", 6);
            NBTTagList nbttaglist2 = tagCompund.getTagList("Rotation", 5);
            this.通便X = nbttaglist1.getDoubleAt(0);
            this.motionY = nbttaglist1.getDoubleAt(1);
            this.通便Z = nbttaglist1.getDoubleAt(2);

            if (Math.abs(this.通便X) > 10.0D)
            {
                this.通便X = 0.0D;
            }

            if (Math.abs(this.motionY) > 10.0D)
            {
                this.motionY = 0.0D;
            }

            if (Math.abs(this.通便Z) > 10.0D)
            {
                this.通便Z = 0.0D;
            }

            this.prevPosX = this.lastTickPosX = this.X坐标 = nbttaglist.getDoubleAt(0);
            this.prevPosY = this.lastTickPosY = this.Y坐标 = nbttaglist.getDoubleAt(1);
            this.prevPosZ = this.lastTickPosZ = this.Z坐标 = nbttaglist.getDoubleAt(2);
            this.prevRotationYaw = this.旋转侧滑 = nbttaglist2.getFloatAt(0);
            this.prevRotationPitch = this.rotationPitch = nbttaglist2.getFloatAt(1);
            this.setRotationYawHead(this.旋转侧滑);
            this.setRenderYawOffset(this.旋转侧滑);
            this.fallDistance = tagCompund.getFloat("FallDistance");
            this.fire = tagCompund.getShort("Fire");
            this.setAir(tagCompund.getShort("Air"));
            this.onGround = tagCompund.getBoolean("OnGround");
            this.dimension = tagCompund.getInteger("Dimension");
            this.invulnerable = tagCompund.getBoolean("Invulnerable");
            this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");

            if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4))
            {
                this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
            }
            else if (tagCompund.hasKey("UUID", 8))
            {
                this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
            }

            this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
            this.setRotation(this.旋转侧滑, this.rotationPitch);

            if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
            {
                this.setCustomNameTag(tagCompund.getString("CustomName"));
            }

            this.setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
            this.cmdResultStats.readStatsFromNBT(tagCompund);
            this.setSilent(tagCompund.getBoolean("Silent"));
            this.readEntityFromNBT(tagCompund);

            if (this.shouldSetPosAfterLoading())
            {
                this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    protected boolean shouldSetPosAfterLoading()
    {
        return true;
    }

    protected final String getEntityString()
    {
        return EntityList.getEntityString(this);
    }

    protected abstract void readEntityFromNBT(NBTTagCompound tagCompund);

    protected abstract void writeEntityToNBT(NBTTagCompound tagCompound);

    public void onChunkLoad()
    {
    }

    protected NBTTagList newDoubleNBTList(double... numbers)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (double d0 : numbers)
        {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    protected NBTTagList newFloatNBTList(float... numbers)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (float f : numbers)
        {
            nbttaglist.appendTag(new NBTTagFloat(f));
        }

        return nbttaglist;
    }

    public 实体Item dropItem(Item itemIn, int size)
    {
        return this.dropItemWithOffset(itemIn, size, 0.0F);
    }

    public 实体Item dropItemWithOffset(Item itemIn, int size, float offsetY)
    {
        return this.entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
    }

    public 实体Item entityDropItem(ItemStack itemStackIn, float offsetY)
    {
        if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null)
        {
            实体Item entityitem = new 实体Item(this.worldObj, this.X坐标, this.Y坐标 + (double)offsetY, this.Z坐标, itemStackIn);
            entityitem.setDefaultPickupDelay();
            this.worldObj.spawnEntityInWorld(entityitem);
            return entityitem;
        }
        else
        {
            return null;
        }
    }

    public boolean isEntityAlive()
    {
        return !this.isDead;
    }

    public boolean isEntityInsideOpaqueBlock()
    {
        if (this.noClip)
        {
            return false;
        }
        else
        {
            阻止位置.Mutable阻止位置 blockpos$mutableblockpos = new 阻止位置.Mutable阻止位置(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            for (int i = 0; i < 8; ++i)
            {
                int j = MathHelper.floor_double(this.Y坐标 + (double)(((float)((i >> 0) % 2) - 0.5F) * 0.1F) + (double)this.getEyeHeight());
                int k = MathHelper.floor_double(this.X坐标 + (double)(((float)((i >> 1) % 2) - 0.5F) * this.width * 0.8F));
                int l = MathHelper.floor_double(this.Z坐标 + (double)(((float)((i >> 2) % 2) - 0.5F) * this.width * 0.8F));

                if (blockpos$mutableblockpos.getX() != k || blockpos$mutableblockpos.getY() != j || blockpos$mutableblockpos.getZ() != l)
                {
                    blockpos$mutableblockpos.set(k, j, l);

                    if (this.worldObj.getBlockState(blockpos$mutableblockpos).getBlock().isVisuallyOpaque())
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean interactFirst(实体Player playerIn)
    {
        return false;
    }

    public AxisAlignedBB getCollisionBox(实体 实体In)
    {
        return null;
    }

    public void updateRidden()
    {
        if (this.riding实体.isDead)
        {
            this.riding实体 = null;
        }
        else
        {
            this.通便X = 0.0D;
            this.motionY = 0.0D;
            this.通便Z = 0.0D;
            this.onUpdate();

            if (this.riding实体 != null)
            {
                this.riding实体.updateRiderPosition();
                this.entityRiderYawDelta += (double)(this.riding实体.旋转侧滑 - this.riding实体.prevRotationYaw);

                for (this.entityRiderPitchDelta += (double)(this.riding实体.rotationPitch - this.riding实体.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D)
                {
                    ;
                }

                while (this.entityRiderYawDelta < -180.0D)
                {
                    this.entityRiderYawDelta += 360.0D;
                }

                while (this.entityRiderPitchDelta >= 180.0D)
                {
                    this.entityRiderPitchDelta -= 360.0D;
                }

                while (this.entityRiderPitchDelta < -180.0D)
                {
                    this.entityRiderPitchDelta += 360.0D;
                }

                double d0 = this.entityRiderYawDelta * 0.5D;
                double d1 = this.entityRiderPitchDelta * 0.5D;
                float f = 10.0F;

                if (d0 > (double)f)
                {
                    d0 = (double)f;
                }

                if (d0 < (double)(-f))
                {
                    d0 = (double)(-f);
                }

                if (d1 > (double)f)
                {
                    d1 = (double)f;
                }

                if (d1 < (double)(-f))
                {
                    d1 = (double)(-f);
                }

                this.entityRiderYawDelta -= d0;
                this.entityRiderPitchDelta -= d1;
            }
        }
    }

    public void updateRiderPosition()
    {
        if (this.riddenBy实体 != null)
        {
            this.riddenBy实体.setPosition(this.X坐标, this.Y坐标 + this.getMountedYOffset() + this.riddenBy实体.getYOffset(), this.Z坐标);
        }
    }

    public double getYOffset()
    {
        return 0.0D;
    }

    public double getMountedYOffset()
    {
        return (double)this.height * 0.75D;
    }

    public void mountEntity(实体 实体In)
    {
        this.entityRiderPitchDelta = 0.0D;
        this.entityRiderYawDelta = 0.0D;

        if (实体In == null)
        {
            if (this.riding实体 != null)
            {
                this.setLocationAndAngles(this.riding实体.X坐标, this.riding实体.getEntityBoundingBox().minY + (double)this.riding实体.height, this.riding实体.Z坐标, this.旋转侧滑, this.rotationPitch);
                this.riding实体.riddenBy实体 = null;
            }

            this.riding实体 = null;
        }
        else
        {
            if (this.riding实体 != null)
            {
                this.riding实体.riddenBy实体 = null;
            }

            if (实体In != null)
            {
                for (实体 实体 = 实体In.riding实体; 实体 != null; 实体 = 实体.riding实体)
                {
                    if (实体 == this)
                    {
                        return;
                    }
                }
            }

            this.riding实体 = 实体In;
            实体In.riddenBy实体 = this;
        }
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
        List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));

        if (!list.isEmpty())
        {
            double d0 = 0.0D;

            for (AxisAlignedBB axisalignedbb : list)
            {
                if (axisalignedbb.maxY > d0)
                {
                    d0 = axisalignedbb.maxY;
                }
            }

            y = y + (d0 - this.getEntityBoundingBox().minY);
            this.setPosition(x, y, z);
        }
    }

    public float getCollisionBorderSize()
    {
        return 0.1F;
    }

    public Vec3 getLookVec()
    {
        return null;
    }

    public void setPortal(阻止位置 pos)
    {
        if (this.timeUntilPortal > 0)
        {
            this.timeUntilPortal = this.getPortalCooldown();
        }
        else
        {
            if (!this.worldObj.isRemote && !pos.equals(this.lastPortalPos))
            {
                this.lastPortalPos = pos;
                BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldObj, pos);
                double d0 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? (double)blockpattern$patternhelper.getPos().getZ() : (double)blockpattern$patternhelper.getPos().getX();
                double d1 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? this.Z坐标 : this.X坐标;
                d1 = Math.abs(MathHelper.func_181160_c(d1 - (double)(blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - (double)blockpattern$patternhelper.func_181118_d()));
                double d2 = MathHelper.func_181160_c(this.Y坐标 - 1.0D, (double)blockpattern$patternhelper.getPos().getY(), (double)(blockpattern$patternhelper.getPos().getY() - blockpattern$patternhelper.func_181119_e()));
                this.lastPortalVec = new Vec3(d1, d2, 0.0D);
                this.teleportDirection = blockpattern$patternhelper.getFinger();
            }

            this.inPortal = true;
        }
    }

    public int getPortalCooldown()
    {
        return 300;
    }

    public void setVelocity(double x, double y, double z)
    {
        this.通便X = x;
        this.motionY = y;
        this.通便Z = z;
    }

    public void handleStatusUpdate(byte id)
    {
    }

    public void performHurtAnimation()
    {
    }

    public ItemStack[] getInventory()
    {
        return null;
    }

    public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
    {
    }

    public boolean isBurning()
    {
        boolean flag = this.worldObj != null && this.worldObj.isRemote;
        return !this.isImmuneToFire && (this.fire > 0 || flag && this.getFlag(0));
    }

    public boolean isRiding()
    {
        return this.riding实体 != null;
    }

    public boolean 正在下蹲()
    {
        return this.getFlag(1);
    }

    public void setSneaking(boolean sneaking)
    {
        this.setFlag(1, sneaking);
    }

    public boolean isSprinting()
    {
        return this.getFlag(3);
    }

    public void 设置宇轩的疾跑状态(boolean sprinting)
    {
        this.setFlag(3, sprinting);
    }

    public boolean isInvisible()
    {
        return this.getFlag(5);
    }

    public boolean isInvisibleToPlayer(实体Player player)
    {
        return player.isSpectator() ? false : this.isInvisible();
    }

    public void setInvisible(boolean invisible)
    {
        this.setFlag(5, invisible);
    }

    public boolean isEating()
    {
        return this.getFlag(4);
    }

    public void setEating(boolean eating)
    {
        this.setFlag(4, eating);
    }

    protected boolean getFlag(int flag)
    {
        return (this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0;
    }

    protected void setFlag(int flag, boolean set)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(0);

        if (set)
        {
            this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 | 1 << flag)));
        }
        else
        {
            this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 & ~(1 << flag))));
        }
    }

    public int getAir()
    {
        return this.dataWatcher.getWatchableObjectShort(1);
    }

    public void setAir(int air)
    {
        this.dataWatcher.updateObject(1, Short.valueOf((short)air));
    }

    public void onStruckByLightning(实体LightningBolt lightningBolt)
    {
        this.attackEntityFrom(DamageSource.lightningBolt, 5.0F);
        ++this.fire;

        if (this.fire == 0)
        {
            this.setFire(8);
        }
    }

    public void onKillEntity(实体LivingBase entityLivingIn)
    {
    }

    protected boolean pushOutOfBlocks(double x, double y, double z)
    {
        阻止位置 blockpos = new 阻止位置(x, y, z);
        double d0 = x - (double)blockpos.getX();
        double d1 = y - (double)blockpos.getY();
        double d2 = z - (double)blockpos.getZ();
        List<AxisAlignedBB> list = this.worldObj.getCollisionBoxes(this.getEntityBoundingBox());

        if (list.isEmpty() && !this.worldObj.isBlockFullCube(blockpos))
        {
            return false;
        }
        else
        {
            int i = 3;
            double d3 = 9999.0D;

            if (!this.worldObj.isBlockFullCube(blockpos.west()) && d0 < d3)
            {
                d3 = d0;
                i = 0;
            }

            if (!this.worldObj.isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3)
            {
                d3 = 1.0D - d0;
                i = 1;
            }

            if (!this.worldObj.isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3)
            {
                d3 = 1.0D - d1;
                i = 3;
            }

            if (!this.worldObj.isBlockFullCube(blockpos.north()) && d2 < d3)
            {
                d3 = d2;
                i = 4;
            }

            if (!this.worldObj.isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3)
            {
                d3 = 1.0D - d2;
                i = 5;
            }

            float f = this.rand.nextFloat() * 0.2F + 0.1F;

            if (i == 0)
            {
                this.通便X = (double)(-f);
            }

            if (i == 1)
            {
                this.通便X = (double)f;
            }

            if (i == 3)
            {
                this.motionY = (double)f;
            }

            if (i == 4)
            {
                this.通便Z = (double)(-f);
            }

            if (i == 5)
            {
                this.通便Z = (double)f;
            }

            return true;
        }
    }

    public void setInWeb()
    {
        this.isInWeb = true;
        this.fallDistance = 0.0F;
    }

    public String getName()
    {
        if (this.hasCustomName())
        {
            return this.getCustomNameTag();
        }
        else
        {
            String s = EntityList.getEntityString(this);

            if (s == null)
            {
                s = "generic";
            }

            return StatCollector.translateToLocal("entity." + s + ".name");
        }
    }

    public 实体[] getParts()
    {
        return null;
    }

    public boolean isEntityEqual(实体 实体In)
    {
        return this == 实体In;
    }

    public float getRotationYawHead()
    {
        return 0.0F;
    }

    public void setRotationYawHead(float rotation)
    {
    }

    public void setRenderYawOffset(float offset)
    {
    }

    public boolean canAttackWithItem()
    {
        return true;
    }

    public boolean hitByEntity(实体 实体In)
    {
        return false;
    }

    public String toString()
    {
        return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[] {this.getClass().getSimpleName(), this.getName(), Integer.valueOf(this.entityId), this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), Double.valueOf(this.X坐标), Double.valueOf(this.Y坐标), Double.valueOf(this.Z坐标)});
    }

    public boolean isEntityInvulnerable(DamageSource source)
    {
        return this.invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer();
    }

    public void copyLocationAndAnglesFrom(实体 实体In)
    {
        this.setLocationAndAngles(实体In.X坐标, 实体In.Y坐标, 实体In.Z坐标, 实体In.旋转侧滑, 实体In.rotationPitch);
    }

    public void copyDataFromOld(实体 实体In)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        实体In.writeToNBT(nbttagcompound);
        this.readFromNBT(nbttagcompound);
        this.timeUntilPortal = 实体In.timeUntilPortal;
        this.lastPortalPos = 实体In.lastPortalPos;
        this.lastPortalVec = 实体In.lastPortalVec;
        this.teleportDirection = 实体In.teleportDirection;
    }

    public void travelToDimension(int dimensionId)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            this.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int i = this.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(i);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionId);
            this.dimension = dimensionId;

            if (i == 1 && dimensionId == 1)
            {
                worldserver1 = minecraftserver.worldServerForDimension(0);
                this.dimension = 0;
            }

            this.worldObj.removeEntity(this);
            this.isDead = false;
            this.worldObj.theProfiler.startSection("reposition");
            minecraftserver.getConfigurationManager().transferEntityToWorld(this, i, worldserver, worldserver1);
            this.worldObj.theProfiler.endStartSection("reloading");
            实体 实体 = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver1);

            if (实体 != null)
            {
                实体.copyDataFromOld(this);

                if (i == 1 && dimensionId == 1)
                {
                    阻止位置 blockpos = this.worldObj.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
                    实体.moveToBlockPosAndAngles(blockpos, 实体.旋转侧滑, 实体.rotationPitch);
                }

                worldserver1.spawnEntityInWorld(实体);
            }

            this.isDead = true;
            this.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            this.worldObj.theProfiler.endSection();
        }
    }

    public float getExplosionResistance(Explosion explosionIn, World worldIn, 阻止位置 pos, IBlockState blockStateIn)
    {
        return blockStateIn.getBlock().getExplosionResistance(this);
    }

    public boolean verifyExplosion(Explosion explosionIn, World worldIn, 阻止位置 pos, IBlockState blockStateIn, float p_174816_5_)
    {
        return true;
    }

    public int getMaxFallHeight()
    {
        return 3;
    }

    public Vec3 func_181014_aG()
    {
        return this.lastPortalVec;
    }

    public EnumFacing getTeleportDirection()
    {
        return this.teleportDirection;
    }

    public boolean doesEntityNotTriggerPressurePlate()
    {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category)
    {
        category.addCrashSectionCallable("Entity Type", new Callable<String>()
        {
            public String call() throws Exception
            {
                return EntityList.getEntityString(实体.this) + " (" + 实体.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addCrashSection("Entity ID", Integer.valueOf(this.entityId));
        category.addCrashSectionCallable("Entity Name", new Callable<String>()
        {
            public String call() throws Exception
            {
                return 实体.this.getName();
            }
        });
        category.addCrashSection("Entity\'s Exact location", String.format("%.2f, %.2f, %.2f", new Object[] {Double.valueOf(this.X坐标), Double.valueOf(this.Y坐标), Double.valueOf(this.Z坐标)}));
        category.addCrashSection("Entity\'s Block location", CrashReportCategory.getCoordinateInfo((double)MathHelper.floor_double(this.X坐标), (double)MathHelper.floor_double(this.Y坐标), (double)MathHelper.floor_double(this.Z坐标)));
        category.addCrashSection("Entity\'s Momentum", String.format("%.2f, %.2f, %.2f", new Object[] {Double.valueOf(this.通便X), Double.valueOf(this.motionY), Double.valueOf(this.通便Z)}));
        category.addCrashSectionCallable("Entity\'s Rider", new Callable<String>()
        {
            public String call() throws Exception
            {
                return 实体.this.riddenBy实体.toString();
            }
        });
        category.addCrashSectionCallable("Entity\'s Vehicle", new Callable<String>()
        {
            public String call() throws Exception
            {
                return 实体.this.riding实体.toString();
            }
        });
    }

    public boolean canRenderOnFire()
    {
        return this.isBurning();
    }

    public UUID getUniqueID()
    {
        return this.entityUniqueID;
    }

    public boolean isPushedByWater()
    {
        return true;
    }

    public IChatComponent getDisplayName()
    {
        交流组分文本 chatcomponenttext = new 交流组分文本(this.getName());
        chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
        chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
        return chatcomponenttext;
    }

    public void setCustomNameTag(String name)
    {
        this.dataWatcher.updateObject(2, name);
    }

    public String getCustomNameTag()
    {
        return this.dataWatcher.getWatchableObjectString(2);
    }

    public boolean hasCustomName()
    {
        return this.dataWatcher.getWatchableObjectString(2).length() > 0;
    }

    public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag)
    {
        this.dataWatcher.updateObject(3, Byte.valueOf((byte)(alwaysRenderNameTag ? 1 : 0)));
    }

    public boolean getAlwaysRenderNameTag()
    {
        return this.dataWatcher.getWatchableObjectByte(3) == 1;
    }

    public void setPositionAndUpdate(double x, double y, double z)
    {
        this.setLocationAndAngles(x, y, z, this.旋转侧滑, this.rotationPitch);
    }

    public boolean getAlwaysRenderNameTagForRender()
    {
        return this.getAlwaysRenderNameTag();
    }

    public void onDataWatcherUpdate(int dataID)
    {
    }

    public EnumFacing getHorizontalFacing()
    {
        return EnumFacing.getHorizontal(MathHelper.floor_double((double)(this.旋转侧滑 * 4.0F / 360.0F) + 0.5D) & 3);
    }

    protected HoverEvent getHoverEvent()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        String s = EntityList.getEntityString(this);
        nbttagcompound.setString("id", this.getUniqueID().toString());

        if (s != null)
        {
            nbttagcompound.setString("type", s);
        }

        nbttagcompound.setString("name", this.getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new 交流组分文本(nbttagcompound.toString()));
    }

    public boolean isSpectatedByPlayer(实体PlayerMP player)
    {
        return true;
    }

    public AxisAlignedBB getEntityBoundingBox()
    {
        return this.boundingBox;
    }

    public void setEntityBoundingBox(AxisAlignedBB bb)
    {
        this.boundingBox = bb;
    }

    public float getEyeHeight()
    {
        return this.height * 0.85F;
    }

    public boolean isOutsideBorder()
    {
        return this.isOutsideBorder;
    }

    public void setOutsideBorder(boolean outsideBorder)
    {
        this.isOutsideBorder = outsideBorder;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        return false;
    }

    public void 增添聊天讯息(IChatComponent component)
    {
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    public 阻止位置 getPosition()
    {
        return new 阻止位置(this.X坐标, this.Y坐标 + 0.5D, this.Z坐标);
    }

    public Vec3 getPositionVector()
    {
        return new Vec3(this.X坐标, this.Y坐标, this.Z坐标);
    }

    public World getEntityWorld()
    {
        return this.worldObj;
    }

    public 实体 getCommandSenderEntity()
    {
        return this;
    }

    public boolean sendCommandFeedback()
    {
        return false;
    }

    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
        this.cmdResultStats.setCommandStatScore(this, type, amount);
    }

    public CommandResultStats getCommandStats()
    {
        return this.cmdResultStats;
    }

    public void setCommandStats(实体 实体In)
    {
        this.cmdResultStats.addAllStats(实体In.getCommandStats());
    }

    public NBTTagCompound getNBTTagCompound()
    {
        return null;
    }

    public void clientUpdateEntityNBT(NBTTagCompound compound)
    {
    }

    public boolean interactAt(实体Player player, Vec3 targetVec3)
    {
        return false;
    }

    public boolean isImmuneToExplosions()
    {
        return false;
    }

    protected void applyEnchantments(实体LivingBase entityLivingBaseIn, 实体 实体In)
    {
        if (实体In instanceof 实体LivingBase)
        {
            EnchantmentHelper.applyThornEnchantments((实体LivingBase) 实体In, entityLivingBaseIn);
        }

        EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, 实体In);
    }
}
