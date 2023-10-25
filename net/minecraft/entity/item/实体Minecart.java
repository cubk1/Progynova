package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.实体MinecartMobSpawner;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.实体MinecartCommandBlock;
import net.minecraft.entity.monster.实体IronGolem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class 实体Minecart extends 实体 implements IWorldNameable
{
    private boolean isInReverse;
    private String entityName;
    private static final int[][][] matrix = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public 实体Minecart(World worldIn)
    {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.7F);
    }

    public static 实体Minecart getMinecart(World worldIn, double x, double y, double z, 实体Minecart.EnumMinecartType type)
    {
        switch (type)
        {
            case CHEST:
                return new 实体MinecartChest(worldIn, x, y, z);

            case FURNACE:
                return new 实体MinecartFurnace(worldIn, x, y, z);

            case TNT:
                return new 实体MinecartTNT(worldIn, x, y, z);

            case SPAWNER:
                return new 实体MinecartMobSpawner(worldIn, x, y, z);

            case HOPPER:
                return new 实体MinecartHopper(worldIn, x, y, z);

            case COMMAND_BLOCK:
                return new 实体MinecartCommandBlock(worldIn, x, y, z);

            default:
                return new 实体MinecartEmpty(worldIn, x, y, z);
        }
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
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(6));
        this.dataWatcher.addObject(22, Byte.valueOf((byte)0));
    }

    public AxisAlignedBB getCollisionBox(实体 实体In)
    {
        return 实体In.canBePushed() ? 实体In.getEntityBoundingBox() : null;
    }

    public AxisAlignedBB getCollisionBoundingBox()
    {
        return null;
    }

    public boolean canBePushed()
    {
        return true;
    }

    public 实体Minecart(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        this.通便X = 0.0D;
        this.motionY = 0.0D;
        this.通便Z = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    public double getMountedYOffset()
    {
        return 0.0D;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.isEntityInvulnerable(source))
            {
                return false;
            }
            else
            {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setBeenAttacked();
                this.setDamage(this.getDamage() + amount * 10.0F);
                boolean flag = source.getEntity() instanceof 实体Player && ((实体Player)source.getEntity()).capabilities.isCreativeMode;

                if (flag || this.getDamage() > 40.0F)
                {
                    if (this.riddenBy实体 != null)
                    {
                        this.riddenBy实体.mountEntity((实体)null);
                    }

                    if (flag && !this.hasCustomName())
                    {
                        this.setDead();
                    }
                    else
                    {
                        this.killMinecart(source);
                    }
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void killMinecart(DamageSource source)
    {
        this.setDead();

        if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
        {
            ItemStack itemstack = new ItemStack(Items.minecart, 1);

            if (this.entityName != null)
            {
                itemstack.setStackDisplayName(this.entityName);
            }

            this.entityDropItem(itemstack, 0.0F);
        }
    }

    public void performHurtAnimation()
    {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void setDead()
    {
        super.setDead();
    }

    public void onUpdate()
    {
        if (this.getRollingAmplitude() > 0)
        {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }

        if (this.getDamage() > 0.0F)
        {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.Y坐标 < -64.0D)
        {
            this.kill();
        }

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

        if (this.worldObj.isRemote)
        {
            if (this.turnProgress > 0)
            {
                double d4 = this.X坐标 + (this.minecartX - this.X坐标) / (double)this.turnProgress;
                double d5 = this.Y坐标 + (this.minecartY - this.Y坐标) / (double)this.turnProgress;
                double d6 = this.Z坐标 + (this.minecartZ - this.Z坐标) / (double)this.turnProgress;
                double d1 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.旋转侧滑);
                this.旋转侧滑 = (float)((double)this.旋转侧滑 + d1 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(d4, d5, d6);
                this.setRotation(this.旋转侧滑, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
                this.setRotation(this.旋转侧滑, this.rotationPitch);
            }
        }
        else
        {
            this.prevPosX = this.X坐标;
            this.prevPosY = this.Y坐标;
            this.prevPosZ = this.Z坐标;
            this.motionY -= 0.03999999910593033D;
            int k = MathHelper.floor_double(this.X坐标);
            int l = MathHelper.floor_double(this.Y坐标);
            int i1 = MathHelper.floor_double(this.Z坐标);

            if (BlockRailBase.isRailBlock(this.worldObj, new 阻止位置(k, l - 1, i1)))
            {
                --l;
            }

            阻止位置 blockpos = new 阻止位置(k, l, i1);
            IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

            if (BlockRailBase.isRailBlock(iblockstate))
            {
                this.func_180460_a(blockpos, iblockstate);

                if (iblockstate.getBlock() == Blocks.activator_rail)
                {
                    this.onActivatorRailPass(k, l, i1, ((Boolean)iblockstate.getValue(BlockRailPowered.POWERED)).booleanValue());
                }
            }
            else
            {
                this.moveDerailedMinecart();
            }

            this.doBlockCollisions();
            this.rotationPitch = 0.0F;
            double d0 = this.prevPosX - this.X坐标;
            double d2 = this.prevPosZ - this.Z坐标;

            if (d0 * d0 + d2 * d2 > 0.001D)
            {
                this.旋转侧滑 = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI);

                if (this.isInReverse)
                {
                    this.旋转侧滑 += 180.0F;
                }
            }

            double d3 = (double)MathHelper.wrapAngleTo180_float(this.旋转侧滑 - this.prevRotationYaw);

            if (d3 < -170.0D || d3 >= 170.0D)
            {
                this.旋转侧滑 += 180.0F;
                this.isInReverse = !this.isInReverse;
            }

            this.setRotation(this.旋转侧滑, this.rotationPitch);

            for (实体 实体 : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D)))
            {
                if (实体 != this.riddenBy实体 && 实体.canBePushed() && 实体 instanceof 实体Minecart)
                {
                    实体.applyEntityCollision(this);
                }
            }

            if (this.riddenBy实体 != null && this.riddenBy实体.isDead)
            {
                if (this.riddenBy实体.riding实体 == this)
                {
                    this.riddenBy实体.riding实体 = null;
                }

                this.riddenBy实体 = null;
            }

            this.handleWaterMovement();
        }
    }

    protected double getMaximumSpeed()
    {
        return 0.4D;
    }

    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
    }

    protected void moveDerailedMinecart()
    {
        double d0 = this.getMaximumSpeed();
        this.通便X = MathHelper.clamp_double(this.通便X, -d0, d0);
        this.通便Z = MathHelper.clamp_double(this.通便Z, -d0, d0);

        if (this.onGround)
        {
            this.通便X *= 0.5D;
            this.motionY *= 0.5D;
            this.通便Z *= 0.5D;
        }

        this.moveEntity(this.通便X, this.motionY, this.通便Z);

        if (!this.onGround)
        {
            this.通便X *= 0.949999988079071D;
            this.motionY *= 0.949999988079071D;
            this.通便Z *= 0.949999988079071D;
        }
    }

    @SuppressWarnings("incomplete-switch")
    protected void func_180460_a(阻止位置 p_180460_1_, IBlockState p_180460_2_)
    {
        this.fallDistance = 0.0F;
        Vec3 vec3 = this.func_70489_a(this.X坐标, this.Y坐标, this.Z坐标);
        this.Y坐标 = (double)p_180460_1_.getY();
        boolean flag = false;
        boolean flag1 = false;
        BlockRailBase blockrailbase = (BlockRailBase)p_180460_2_.getBlock();

        if (blockrailbase == Blocks.golden_rail)
        {
            flag = ((Boolean)p_180460_2_.getValue(BlockRailPowered.POWERED)).booleanValue();
            flag1 = !flag;
        }

        double d0 = 0.0078125D;
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)p_180460_2_.getValue(blockrailbase.getShapeProperty());

        switch (blockrailbase$enumraildirection)
        {
            case ASCENDING_EAST:
                this.通便X -= 0.0078125D;
                ++this.Y坐标;
                break;

            case ASCENDING_WEST:
                this.通便X += 0.0078125D;
                ++this.Y坐标;
                break;

            case ASCENDING_NORTH:
                this.通便Z += 0.0078125D;
                ++this.Y坐标;
                break;

            case ASCENDING_SOUTH:
                this.通便Z -= 0.0078125D;
                ++this.Y坐标;
        }

        int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
        double d1 = (double)(aint[1][0] - aint[0][0]);
        double d2 = (double)(aint[1][2] - aint[0][2]);
        double d3 = Math.sqrt(d1 * d1 + d2 * d2);
        double d4 = this.通便X * d1 + this.通便Z * d2;

        if (d4 < 0.0D)
        {
            d1 = -d1;
            d2 = -d2;
        }

        double d5 = Math.sqrt(this.通便X * this.通便X + this.通便Z * this.通便Z);

        if (d5 > 2.0D)
        {
            d5 = 2.0D;
        }

        this.通便X = d5 * d1 / d3;
        this.通便Z = d5 * d2 / d3;

        if (this.riddenBy实体 instanceof 实体LivingBase)
        {
            double d6 = (double)((实体LivingBase)this.riddenBy实体).moveForward;

            if (d6 > 0.0D)
            {
                double d7 = -Math.sin((double)(this.riddenBy实体.旋转侧滑 * (float)Math.PI / 180.0F));
                double d8 = Math.cos((double)(this.riddenBy实体.旋转侧滑 * (float)Math.PI / 180.0F));
                double d9 = this.通便X * this.通便X + this.通便Z * this.通便Z;

                if (d9 < 0.01D)
                {
                    this.通便X += d7 * 0.1D;
                    this.通便Z += d8 * 0.1D;
                    flag1 = false;
                }
            }
        }

        if (flag1)
        {
            double d17 = Math.sqrt(this.通便X * this.通便X + this.通便Z * this.通便Z);

            if (d17 < 0.03D)
            {
                this.通便X *= 0.0D;
                this.motionY *= 0.0D;
                this.通便Z *= 0.0D;
            }
            else
            {
                this.通便X *= 0.5D;
                this.motionY *= 0.0D;
                this.通便Z *= 0.5D;
            }
        }

        double d18 = 0.0D;
        double d19 = (double)p_180460_1_.getX() + 0.5D + (double)aint[0][0] * 0.5D;
        double d20 = (double)p_180460_1_.getZ() + 0.5D + (double)aint[0][2] * 0.5D;
        double d21 = (double)p_180460_1_.getX() + 0.5D + (double)aint[1][0] * 0.5D;
        double d10 = (double)p_180460_1_.getZ() + 0.5D + (double)aint[1][2] * 0.5D;
        d1 = d21 - d19;
        d2 = d10 - d20;

        if (d1 == 0.0D)
        {
            this.X坐标 = (double)p_180460_1_.getX() + 0.5D;
            d18 = this.Z坐标 - (double)p_180460_1_.getZ();
        }
        else if (d2 == 0.0D)
        {
            this.Z坐标 = (double)p_180460_1_.getZ() + 0.5D;
            d18 = this.X坐标 - (double)p_180460_1_.getX();
        }
        else
        {
            double d11 = this.X坐标 - d19;
            double d12 = this.Z坐标 - d20;
            d18 = (d11 * d1 + d12 * d2) * 2.0D;
        }

        this.X坐标 = d19 + d1 * d18;
        this.Z坐标 = d20 + d2 * d18;
        this.setPosition(this.X坐标, this.Y坐标, this.Z坐标);
        double d22 = this.通便X;
        double d23 = this.通便Z;

        if (this.riddenBy实体 != null)
        {
            d22 *= 0.75D;
            d23 *= 0.75D;
        }

        double d13 = this.getMaximumSpeed();
        d22 = MathHelper.clamp_double(d22, -d13, d13);
        d23 = MathHelper.clamp_double(d23, -d13, d13);
        this.moveEntity(d22, 0.0D, d23);

        if (aint[0][1] != 0 && MathHelper.floor_double(this.X坐标) - p_180460_1_.getX() == aint[0][0] && MathHelper.floor_double(this.Z坐标) - p_180460_1_.getZ() == aint[0][2])
        {
            this.setPosition(this.X坐标, this.Y坐标 + (double)aint[0][1], this.Z坐标);
        }
        else if (aint[1][1] != 0 && MathHelper.floor_double(this.X坐标) - p_180460_1_.getX() == aint[1][0] && MathHelper.floor_double(this.Z坐标) - p_180460_1_.getZ() == aint[1][2])
        {
            this.setPosition(this.X坐标, this.Y坐标 + (double)aint[1][1], this.Z坐标);
        }

        this.applyDrag();
        Vec3 vec31 = this.func_70489_a(this.X坐标, this.Y坐标, this.Z坐标);

        if (vec31 != null && vec3 != null)
        {
            double d14 = (vec3.yCoord - vec31.yCoord) * 0.05D;
            d5 = Math.sqrt(this.通便X * this.通便X + this.通便Z * this.通便Z);

            if (d5 > 0.0D)
            {
                this.通便X = this.通便X / d5 * (d5 + d14);
                this.通便Z = this.通便Z / d5 * (d5 + d14);
            }

            this.setPosition(this.X坐标, vec31.yCoord, this.Z坐标);
        }

        int j = MathHelper.floor_double(this.X坐标);
        int i = MathHelper.floor_double(this.Z坐标);

        if (j != p_180460_1_.getX() || i != p_180460_1_.getZ())
        {
            d5 = Math.sqrt(this.通便X * this.通便X + this.通便Z * this.通便Z);
            this.通便X = d5 * (double)(j - p_180460_1_.getX());
            this.通便Z = d5 * (double)(i - p_180460_1_.getZ());
        }

        if (flag)
        {
            double d15 = Math.sqrt(this.通便X * this.通便X + this.通便Z * this.通便Z);

            if (d15 > 0.01D)
            {
                double d16 = 0.06D;
                this.通便X += this.通便X / d15 * d16;
                this.通便Z += this.通便Z / d15 * d16;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (this.worldObj.getBlockState(p_180460_1_.west()).getBlock().isNormalCube())
                {
                    this.通便X = 0.02D;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.east()).getBlock().isNormalCube())
                {
                    this.通便X = -0.02D;
                }
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (this.worldObj.getBlockState(p_180460_1_.north()).getBlock().isNormalCube())
                {
                    this.通便Z = 0.02D;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.south()).getBlock().isNormalCube())
                {
                    this.通便Z = -0.02D;
                }
            }
        }
    }

    protected void applyDrag()
    {
        if (this.riddenBy实体 != null)
        {
            this.通便X *= 0.996999979019165D;
            this.motionY *= 0.0D;
            this.通便Z *= 0.996999979019165D;
        }
        else
        {
            this.通便X *= 0.9599999785423279D;
            this.motionY *= 0.0D;
            this.通便Z *= 0.9599999785423279D;
        }
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

    public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_)
    {
        int i = MathHelper.floor_double(p_70495_1_);
        int j = MathHelper.floor_double(p_70495_3_);
        int k = MathHelper.floor_double(p_70495_5_);

        if (BlockRailBase.isRailBlock(this.worldObj, new 阻止位置(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = this.worldObj.getBlockState(new 阻止位置(i, j, k));

        if (BlockRailBase.isRailBlock(iblockstate))
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            p_70495_3_ = (double)j;

            if (blockrailbase$enumraildirection.isAscending())
            {
                p_70495_3_ = (double)(j + 1);
            }

            int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
            double d0 = (double)(aint[1][0] - aint[0][0]);
            double d1 = (double)(aint[1][2] - aint[0][2]);
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            d0 = d0 / d2;
            d1 = d1 / d2;
            p_70495_1_ = p_70495_1_ + d0 * p_70495_7_;
            p_70495_5_ = p_70495_5_ + d1 * p_70495_7_;

            if (aint[0][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[0][0] && MathHelper.floor_double(p_70495_5_) - k == aint[0][2])
            {
                p_70495_3_ += (double)aint[0][1];
            }
            else if (aint[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[1][0] && MathHelper.floor_double(p_70495_5_) - k == aint[1][2])
            {
                p_70495_3_ += (double)aint[1][1];
            }

            return this.func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
        }
        else
        {
            return null;
        }
    }

    public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_)
    {
        int i = MathHelper.floor_double(p_70489_1_);
        int j = MathHelper.floor_double(p_70489_3_);
        int k = MathHelper.floor_double(p_70489_5_);

        if (BlockRailBase.isRailBlock(this.worldObj, new 阻止位置(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = this.worldObj.getBlockState(new 阻止位置(i, j, k));

        if (BlockRailBase.isRailBlock(iblockstate))
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
            double d0 = 0.0D;
            double d1 = (double)i + 0.5D + (double)aint[0][0] * 0.5D;
            double d2 = (double)j + 0.0625D + (double)aint[0][1] * 0.5D;
            double d3 = (double)k + 0.5D + (double)aint[0][2] * 0.5D;
            double d4 = (double)i + 0.5D + (double)aint[1][0] * 0.5D;
            double d5 = (double)j + 0.0625D + (double)aint[1][1] * 0.5D;
            double d6 = (double)k + 0.5D + (double)aint[1][2] * 0.5D;
            double d7 = d4 - d1;
            double d8 = (d5 - d2) * 2.0D;
            double d9 = d6 - d3;

            if (d7 == 0.0D)
            {
                p_70489_1_ = (double)i + 0.5D;
                d0 = p_70489_5_ - (double)k;
            }
            else if (d9 == 0.0D)
            {
                p_70489_5_ = (double)k + 0.5D;
                d0 = p_70489_1_ - (double)i;
            }
            else
            {
                double d10 = p_70489_1_ - d1;
                double d11 = p_70489_5_ - d3;
                d0 = (d10 * d7 + d11 * d9) * 2.0D;
            }

            p_70489_1_ = d1 + d7 * d0;
            p_70489_3_ = d2 + d8 * d0;
            p_70489_5_ = d3 + d9 * d0;

            if (d8 < 0.0D)
            {
                ++p_70489_3_;
            }

            if (d8 > 0.0D)
            {
                p_70489_3_ += 0.5D;
            }

            return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        else
        {
            return null;
        }
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        if (tagCompund.getBoolean("CustomDisplayTile"))
        {
            int i = tagCompund.getInteger("DisplayData");

            if (tagCompund.hasKey("DisplayTile", 8))
            {
                Block block = Block.getBlockFromName(tagCompund.getString("DisplayTile"));

                if (block == null)
                {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else
                {
                    this.func_174899_a(block.getStateFromMeta(i));
                }
            }
            else
            {
                Block block1 = Block.getBlockById(tagCompund.getInteger("DisplayTile"));

                if (block1 == null)
                {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else
                {
                    this.func_174899_a(block1.getStateFromMeta(i));
                }
            }

            this.setDisplayTileOffset(tagCompund.getInteger("DisplayOffset"));
        }

        if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
        {
            this.entityName = tagCompund.getString("CustomName");
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        if (this.hasDisplayTile())
        {
            tagCompound.setBoolean("CustomDisplayTile", true);
            IBlockState iblockstate = this.getDisplayTile();
            图像位置 resourcelocation = (图像位置)Block.blockRegistry.getNameForObject(iblockstate.getBlock());
            tagCompound.setString("DisplayTile", resourcelocation == null ? "" : resourcelocation.toString());
            tagCompound.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
            tagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }

        if (this.entityName != null && this.entityName.length() > 0)
        {
            tagCompound.setString("CustomName", this.entityName);
        }
    }

    public void applyEntityCollision(实体 实体In)
    {
        if (!this.worldObj.isRemote)
        {
            if (!实体In.noClip && !this.noClip)
            {
                if (实体In != this.riddenBy实体)
                {
                    if (实体In instanceof 实体LivingBase && !(实体In instanceof 实体Player) && !(实体In instanceof 实体IronGolem) && this.getMinecartType() == 实体Minecart.EnumMinecartType.RIDEABLE && this.通便X * this.通便X + this.通便Z * this.通便Z > 0.01D && this.riddenBy实体 == null && 实体In.riding实体 == null)
                    {
                        实体In.mountEntity(this);
                    }

                    double d0 = 实体In.X坐标 - this.X坐标;
                    double d1 = 实体In.Z坐标 - this.Z坐标;
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 >= 9.999999747378752E-5D)
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
                        d0 = d0 * 0.10000000149011612D;
                        d1 = d1 * 0.10000000149011612D;
                        d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                        d1 = d1 * (double)(1.0F - this.entityCollisionReduction);
                        d0 = d0 * 0.5D;
                        d1 = d1 * 0.5D;

                        if (实体In instanceof 实体Minecart)
                        {
                            double d4 = 实体In.X坐标 - this.X坐标;
                            double d5 = 实体In.Z坐标 - this.Z坐标;
                            Vec3 vec3 = (new Vec3(d4, 0.0D, d5)).normalize();
                            Vec3 vec31 = (new Vec3((double)MathHelper.cos(this.旋转侧滑 * (float)Math.PI / 180.0F), 0.0D, (double)MathHelper.sin(this.旋转侧滑 * (float)Math.PI / 180.0F))).normalize();
                            double d6 = Math.abs(vec3.dotProduct(vec31));

                            if (d6 < 0.800000011920929D)
                            {
                                return;
                            }

                            double d7 = 实体In.通便X + this.通便X;
                            double d8 = 实体In.通便Z + this.通便Z;

                            if (((实体Minecart) 实体In).getMinecartType() == 实体Minecart.EnumMinecartType.FURNACE && this.getMinecartType() != 实体Minecart.EnumMinecartType.FURNACE)
                            {
                                this.通便X *= 0.20000000298023224D;
                                this.通便Z *= 0.20000000298023224D;
                                this.addVelocity(实体In.通便X - d0, 0.0D, 实体In.通便Z - d1);
                                实体In.通便X *= 0.949999988079071D;
                                实体In.通便Z *= 0.949999988079071D;
                            }
                            else if (((实体Minecart) 实体In).getMinecartType() != 实体Minecart.EnumMinecartType.FURNACE && this.getMinecartType() == 实体Minecart.EnumMinecartType.FURNACE)
                            {
                                实体In.通便X *= 0.20000000298023224D;
                                实体In.通便Z *= 0.20000000298023224D;
                                实体In.addVelocity(this.通便X + d0, 0.0D, this.通便Z + d1);
                                this.通便X *= 0.949999988079071D;
                                this.通便Z *= 0.949999988079071D;
                            }
                            else
                            {
                                d7 = d7 / 2.0D;
                                d8 = d8 / 2.0D;
                                this.通便X *= 0.20000000298023224D;
                                this.通便Z *= 0.20000000298023224D;
                                this.addVelocity(d7 - d0, 0.0D, d8 - d1);
                                实体In.通便X *= 0.20000000298023224D;
                                实体In.通便Z *= 0.20000000298023224D;
                                实体In.addVelocity(d7 + d0, 0.0D, d8 + d1);
                            }
                        }
                        else
                        {
                            this.addVelocity(-d0, 0.0D, -d1);
                            实体In.addVelocity(d0 / 4.0D, 0.0D, d1 / 4.0D);
                        }
                    }
                }
            }
        }
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
        this.minecartX = x;
        this.minecartY = y;
        this.minecartZ = z;
        this.minecartYaw = (double)yaw;
        this.minecartPitch = (double)pitch;
        this.turnProgress = posRotationIncrements + 2;
        this.通便X = this.velocityX;
        this.motionY = this.velocityY;
        this.通便Z = this.velocityZ;
    }

    public void setVelocity(double x, double y, double z)
    {
        this.velocityX = this.通便X = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.通便Z = z;
    }

    public void setDamage(float p_70492_1_)
    {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
    }

    public float getDamage()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public void setRollingAmplitude(int p_70497_1_)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(p_70497_1_));
    }

    public int getRollingAmplitude()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setRollingDirection(int p_70494_1_)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_70494_1_));
    }

    public int getRollingDirection()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public abstract 实体Minecart.EnumMinecartType getMinecartType();

    public IBlockState getDisplayTile()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTile() : Block.getStateById(this.getDataWatcher().getWatchableObjectInt(20));
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.air.getDefaultState();
    }

    public int getDisplayTileOffset()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : this.getDataWatcher().getWatchableObjectInt(21);
    }

    public int getDefaultDisplayTileOffset()
    {
        return 6;
    }

    public void func_174899_a(IBlockState p_174899_1_)
    {
        this.getDataWatcher().updateObject(20, Integer.valueOf(Block.getStateId(p_174899_1_)));
        this.setHasDisplayTile(true);
    }

    public void setDisplayTileOffset(int p_94086_1_)
    {
        this.getDataWatcher().updateObject(21, Integer.valueOf(p_94086_1_));
        this.setHasDisplayTile(true);
    }

    public boolean hasDisplayTile()
    {
        return this.getDataWatcher().getWatchableObjectByte(22) == 1;
    }

    public void setHasDisplayTile(boolean p_94096_1_)
    {
        this.getDataWatcher().updateObject(22, Byte.valueOf((byte)(p_94096_1_ ? 1 : 0)));
    }

    public void setCustomNameTag(String name)
    {
        this.entityName = name;
    }

    public String getName()
    {
        return this.entityName != null ? this.entityName : super.getName();
    }

    public boolean hasCustomName()
    {
        return this.entityName != null;
    }

    public String getCustomNameTag()
    {
        return this.entityName;
    }

    public IChatComponent getDisplayName()
    {
        if (this.hasCustomName())
        {
            交流组分文本 chatcomponenttext = new 交流组分文本(this.entityName);
            chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatcomponenttext;
        }
        else
        {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(this.getName(), new Object[0]);
            chatcomponenttranslation.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttranslation.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatcomponenttranslation;
        }
    }

    public static enum EnumMinecartType
    {
        RIDEABLE(0, "MinecartRideable"),
        CHEST(1, "MinecartChest"),
        FURNACE(2, "MinecartFurnace"),
        TNT(3, "MinecartTNT"),
        SPAWNER(4, "MinecartSpawner"),
        HOPPER(5, "MinecartHopper"),
        COMMAND_BLOCK(6, "MinecartCommandBlock");

        private static final Map<Integer, 实体Minecart.EnumMinecartType> ID_LOOKUP = Maps.<Integer, 实体Minecart.EnumMinecartType>newHashMap();
        private final int networkID;
        private final String name;

        private EnumMinecartType(int networkID, String name)
        {
            this.networkID = networkID;
            this.name = name;
        }

        public int getNetworkID()
        {
            return this.networkID;
        }

        public String getName()
        {
            return this.name;
        }

        public static 实体Minecart.EnumMinecartType byNetworkID(int id)
        {
            实体Minecart.EnumMinecartType entityminecart$enumminecarttype = (实体Minecart.EnumMinecartType)ID_LOOKUP.get(Integer.valueOf(id));
            return entityminecart$enumminecarttype == null ? RIDEABLE : entityminecart$enumminecarttype;
        }

        static {
            for (实体Minecart.EnumMinecartType entityminecart$enumminecarttype : values())
            {
                ID_LOOKUP.put(Integer.valueOf(entityminecart$enumminecarttype.getNetworkID()), entityminecart$enumminecarttype);
            }
        }
    }
}
