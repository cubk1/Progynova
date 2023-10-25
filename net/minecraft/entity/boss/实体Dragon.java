package net.minecraft.entity.boss;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.item.实体XPOrb;
import net.minecraft.entity.实体;
import net.minecraft.entity.item.实体EnderCrystal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class 实体Dragon extends 实体Living implements IBossDisplayData, IEntityMultiPart, IMob
{
    public double targetX;
    public double targetY;
    public double targetZ;
    public double[][] ringBuffer = new double[64][3];
    public int ringBufferIndex = -1;
    public 实体DragonPart[] dragonPartArray;
    public 实体DragonPart dragonPartHead;
    public 实体DragonPart dragonPartBody;
    public 实体DragonPart dragonPartTail1;
    public 实体DragonPart dragonPartTail2;
    public 实体DragonPart dragonPartTail3;
    public 实体DragonPart dragonPartWing1;
    public 实体DragonPart dragonPartWing2;
    public float prevAnimTime;
    public float animTime;
    public boolean forceNewTarget;
    public boolean slowed;
    private 实体 target;
    public int deathTicks;
    public 实体EnderCrystal healingEnderCrystal;

    public 实体Dragon(World worldIn)
    {
        super(worldIn);
        this.dragonPartArray = new 实体DragonPart[] {this.dragonPartHead = new 实体DragonPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new 实体DragonPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new 实体DragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new 实体DragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new 实体DragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new 实体DragonPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new 实体DragonPart(this, "wing", 4.0F, 4.0F)};
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0F, 8.0F);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.targetY = 100.0D;
        this.ignoreFrustumCheck = true;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
    {
        if (this.getHealth() <= 0.0F)
        {
            p_70974_2_ = 0.0F;
        }

        p_70974_2_ = 1.0F - p_70974_2_;
        int i = this.ringBufferIndex - p_70974_1_ * 1 & 63;
        int j = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d1 = MathHelper.wrapAngleTo180_double(this.ringBuffer[j][0] - d0);
        adouble[0] = d0 + d1 * (double)p_70974_2_;
        d0 = this.ringBuffer[i][1];
        d1 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d1 * (double)p_70974_2_;
        adouble[2] = this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * (double)p_70974_2_;
        return adouble;
    }

    public void onLivingUpdate()
    {
        if (this.worldObj.isRemote)
        {
            float f = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
            float f1 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

            if (f1 <= -0.3F && f >= -0.3F && !this.isSilent())
            {
                this.worldObj.playSound(this.X坐标, this.Y坐标, this.Z坐标, "mob.enderdragon.wings", 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
            }
        }

        this.prevAnimTime = this.animTime;

        if (this.getHealth() <= 0.0F)
        {
            float f11 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f13 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f14 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.X坐标 + (double)f11, this.Y坐标 + 2.0D + (double)f13, this.Z坐标 + (double)f14, 0.0D, 0.0D, 0.0D, new int[0]);
        }
        else
        {
            this.updateDragonEnderCrystal();
            float f10 = 0.2F / (MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z) * 10.0F + 1.0F);
            f10 = f10 * (float)Math.pow(2.0D, this.motionY);

            if (this.slowed)
            {
                this.animTime += f10 * 0.5F;
            }
            else
            {
                this.animTime += f10;
            }

            this.旋转侧滑 = MathHelper.wrapAngleTo180_float(this.旋转侧滑);

            if (this.isAIDisabled())
            {
                this.animTime = 0.5F;
            }
            else
            {
                if (this.ringBufferIndex < 0)
                {
                    for (int i = 0; i < this.ringBuffer.length; ++i)
                    {
                        this.ringBuffer[i][0] = (double)this.旋转侧滑;
                        this.ringBuffer[i][1] = this.Y坐标;
                    }
                }

                if (++this.ringBufferIndex == this.ringBuffer.length)
                {
                    this.ringBufferIndex = 0;
                }

                this.ringBuffer[this.ringBufferIndex][0] = (double)this.旋转侧滑;
                this.ringBuffer[this.ringBufferIndex][1] = this.Y坐标;

                if (this.worldObj.isRemote)
                {
                    if (this.newPosRotationIncrements > 0)
                    {
                        double d10 = this.X坐标 + (this.newPosX - this.X坐标) / (double)this.newPosRotationIncrements;
                        double d0 = this.Y坐标 + (this.newPosY - this.Y坐标) / (double)this.newPosRotationIncrements;
                        double d1 = this.Z坐标 + (this.newPosZ - this.Z坐标) / (double)this.newPosRotationIncrements;
                        double d2 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.旋转侧滑);
                        this.旋转侧滑 = (float)((double)this.旋转侧滑 + d2 / (double)this.newPosRotationIncrements);
                        this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                        --this.newPosRotationIncrements;
                        this.setPosition(d10, d0, d1);
                        this.setRotation(this.旋转侧滑, this.rotationPitch);
                    }
                }
                else
                {
                    double d11 = this.targetX - this.X坐标;
                    double d12 = this.targetY - this.Y坐标;
                    double d13 = this.targetZ - this.Z坐标;
                    double d14 = d11 * d11 + d12 * d12 + d13 * d13;

                    if (this.target != null)
                    {
                        this.targetX = this.target.X坐标;
                        this.targetZ = this.target.Z坐标;
                        double d3 = this.targetX - this.X坐标;
                        double d5 = this.targetZ - this.Z坐标;
                        double d7 = Math.sqrt(d3 * d3 + d5 * d5);
                        double d8 = 0.4000000059604645D + d7 / 80.0D - 1.0D;

                        if (d8 > 10.0D)
                        {
                            d8 = 10.0D;
                        }

                        this.targetY = this.target.getEntityBoundingBox().minY + d8;
                    }
                    else
                    {
                        this.targetX += this.rand.nextGaussian() * 2.0D;
                        this.targetZ += this.rand.nextGaussian() * 2.0D;
                    }

                    if (this.forceNewTarget || d14 < 100.0D || d14 > 22500.0D || this.isCollidedHorizontally || this.isCollidedVertically)
                    {
                        this.setNewTarget();
                    }

                    d12 = d12 / (double)MathHelper.sqrt_double(d11 * d11 + d13 * d13);
                    float f17 = 0.6F;
                    d12 = MathHelper.clamp_double(d12, (double)(-f17), (double)f17);
                    this.motionY += d12 * 0.10000000149011612D;
                    this.旋转侧滑 = MathHelper.wrapAngleTo180_float(this.旋转侧滑);
                    double d4 = 180.0D - MathHelper.atan2(d11, d13) * 180.0D / Math.PI;
                    double d6 = MathHelper.wrapAngleTo180_double(d4 - (double)this.旋转侧滑);

                    if (d6 > 50.0D)
                    {
                        d6 = 50.0D;
                    }

                    if (d6 < -50.0D)
                    {
                        d6 = -50.0D;
                    }

                    Vec3 vec3 = (new Vec3(this.targetX - this.X坐标, this.targetY - this.Y坐标, this.targetZ - this.Z坐标)).normalize();
                    double d15 = (double)(-MathHelper.cos(this.旋转侧滑 * (float)Math.PI / 180.0F));
                    Vec3 vec31 = (new Vec3((double)MathHelper.sin(this.旋转侧滑 * (float)Math.PI / 180.0F), this.motionY, d15)).normalize();
                    float f5 = ((float)vec31.dotProduct(vec3) + 0.5F) / 1.5F;

                    if (f5 < 0.0F)
                    {
                        f5 = 0.0F;
                    }

                    this.randomYawVelocity *= 0.8F;
                    float f6 = MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z) * 1.0F + 1.0F;
                    double d9 = Math.sqrt(this.通便X * this.通便X + this.通便Z * this.通便Z) * 1.0D + 1.0D;

                    if (d9 > 40.0D)
                    {
                        d9 = 40.0D;
                    }

                    this.randomYawVelocity = (float)((double)this.randomYawVelocity + d6 * (0.699999988079071D / d9 / (double)f6));
                    this.旋转侧滑 += this.randomYawVelocity * 0.1F;
                    float f7 = (float)(2.0D / (d9 + 1.0D));
                    float f8 = 0.06F;
                    this.moveFlying(0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));

                    if (this.slowed)
                    {
                        this.moveEntity(this.通便X * 0.800000011920929D, this.motionY * 0.800000011920929D, this.通便Z * 0.800000011920929D);
                    }
                    else
                    {
                        this.moveEntity(this.通便X, this.motionY, this.通便Z);
                    }

                    Vec3 vec32 = (new Vec3(this.通便X, this.motionY, this.通便Z)).normalize();
                    float f9 = ((float)vec32.dotProduct(vec31) + 1.0F) / 2.0F;
                    f9 = 0.8F + 0.15F * f9;
                    this.通便X *= (double)f9;
                    this.通便Z *= (double)f9;
                    this.motionY *= 0.9100000262260437D;
                }

                this.renderYawOffset = this.旋转侧滑;
                this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
                this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
                this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
                this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
                this.dragonPartBody.height = 3.0F;
                this.dragonPartBody.width = 5.0F;
                this.dragonPartWing1.height = 2.0F;
                this.dragonPartWing1.width = 4.0F;
                this.dragonPartWing2.height = 3.0F;
                this.dragonPartWing2.width = 4.0F;
                float f12 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * (float)Math.PI;
                float f2 = MathHelper.cos(f12);
                float f15 = -MathHelper.sin(f12);
                float f3 = this.旋转侧滑 * (float)Math.PI / 180.0F;
                float f16 = MathHelper.sin(f3);
                float f4 = MathHelper.cos(f3);
                this.dragonPartBody.onUpdate();
                this.dragonPartBody.setLocationAndAngles(this.X坐标 + (double)(f16 * 0.5F), this.Y坐标, this.Z坐标 - (double)(f4 * 0.5F), 0.0F, 0.0F);
                this.dragonPartWing1.onUpdate();
                this.dragonPartWing1.setLocationAndAngles(this.X坐标 + (double)(f4 * 4.5F), this.Y坐标 + 2.0D, this.Z坐标 + (double)(f16 * 4.5F), 0.0F, 0.0F);
                this.dragonPartWing2.onUpdate();
                this.dragonPartWing2.setLocationAndAngles(this.X坐标 - (double)(f4 * 4.5F), this.Y坐标 + 2.0D, this.Z坐标 - (double)(f16 * 4.5F), 0.0F, 0.0F);

                if (!this.worldObj.isRemote && this.hurtTime == 0)
                {
                    this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                    this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                    this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D)));
                }

                double[] adouble1 = this.getMovementOffsets(5, 1.0F);
                double[] adouble = this.getMovementOffsets(0, 1.0F);
                float f18 = MathHelper.sin(this.旋转侧滑 * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
                float f19 = MathHelper.cos(this.旋转侧滑 * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
                this.dragonPartHead.onUpdate();
                this.dragonPartHead.setLocationAndAngles(this.X坐标 + (double)(f18 * 5.5F * f2), this.Y坐标 + (adouble[1] - adouble1[1]) * 1.0D + (double)(f15 * 5.5F), this.Z坐标 - (double)(f19 * 5.5F * f2), 0.0F, 0.0F);

                for (int j = 0; j < 3; ++j)
                {
                    实体DragonPart entitydragonpart = null;

                    if (j == 0)
                    {
                        entitydragonpart = this.dragonPartTail1;
                    }

                    if (j == 1)
                    {
                        entitydragonpart = this.dragonPartTail2;
                    }

                    if (j == 2)
                    {
                        entitydragonpart = this.dragonPartTail3;
                    }

                    double[] adouble2 = this.getMovementOffsets(12 + j * 2, 1.0F);
                    float f20 = this.旋转侧滑 * (float)Math.PI / 180.0F + this.simplifyAngle(adouble2[0] - adouble1[0]) * (float)Math.PI / 180.0F * 1.0F;
                    float f21 = MathHelper.sin(f20);
                    float f22 = MathHelper.cos(f20);
                    float f23 = 1.5F;
                    float f24 = (float)(j + 1) * 2.0F;
                    entitydragonpart.onUpdate();
                    entitydragonpart.setLocationAndAngles(this.X坐标 - (double)((f16 * f23 + f21 * f24) * f2), this.Y坐标 + (adouble2[1] - adouble1[1]) * 1.0D - (double)((f24 + f23) * f15) + 1.5D, this.Z坐标 + (double)((f4 * f23 + f22 * f24) * f2), 0.0F, 0.0F);
                }

                if (!this.worldObj.isRemote)
                {
                    this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
                }
            }
        }
    }

    private void updateDragonEnderCrystal()
    {
        if (this.healingEnderCrystal != null)
        {
            if (this.healingEnderCrystal.isDead)
            {
                if (!this.worldObj.isRemote)
                {
                    this.attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource((Explosion)null), 10.0F);
                }

                this.healingEnderCrystal = null;
            }
            else if (this.已存在的刻度 % 10 == 0 && this.getHealth() < this.getMaxHealth())
            {
                this.setHealth(this.getHealth() + 1.0F);
            }
        }

        if (this.rand.nextInt(10) == 0)
        {
            float f = 32.0F;
            List<实体EnderCrystal> list = this.worldObj.<实体EnderCrystal>getEntitiesWithinAABB(实体EnderCrystal.class, this.getEntityBoundingBox().expand((double)f, (double)f, (double)f));
            实体EnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;

            for (实体EnderCrystal entityendercrystal1 : list)
            {
                double d1 = entityendercrystal1.getDistanceSqToEntity(this);

                if (d1 < d0)
                {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.healingEnderCrystal = entityendercrystal;
        }
    }

    private void collideWithEntities(List<实体> p_70970_1_)
    {
        double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0D;
        double d1 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0D;

        for (实体 实体 : p_70970_1_)
        {
            if (实体 instanceof 实体LivingBase)
            {
                double d2 = 实体.X坐标 - d0;
                double d3 = 实体.Z坐标 - d1;
                double d4 = d2 * d2 + d3 * d3;
                实体.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
            }
        }
    }

    private void attackEntitiesInList(List<实体> p_70971_1_)
    {
        for (int i = 0; i < p_70971_1_.size(); ++i)
        {
            实体 实体 = (实体)p_70971_1_.get(i);

            if (实体 instanceof 实体LivingBase)
            {
                实体.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
                this.applyEnchantments(this, 实体);
            }
        }
    }

    private void setNewTarget()
    {
        this.forceNewTarget = false;
        List<实体Player> list = Lists.newArrayList(this.worldObj.playerEntities);
        Iterator<实体Player> iterator = list.iterator();

        while (iterator.hasNext())
        {
            if (((实体Player)iterator.next()).isSpectator())
            {
                iterator.remove();
            }
        }

        if (this.rand.nextInt(2) == 0 && !list.isEmpty())
        {
            this.target = (实体)list.get(this.rand.nextInt(list.size()));
        }
        else
        {
            while (true)
            {
                this.targetX = 0.0D;
                this.targetY = (double)(70.0F + this.rand.nextFloat() * 50.0F);
                this.targetZ = 0.0D;
                this.targetX += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                this.targetZ += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                double d0 = this.X坐标 - this.targetX;
                double d1 = this.Y坐标 - this.targetY;
                double d2 = this.Z坐标 - this.targetZ;
                boolean flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;

                if (flag)
                {
                    break;
                }
            }

            this.target = null;
        }
    }

    private float simplifyAngle(double p_70973_1_)
    {
        return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
    }

    private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
    {
        int i = MathHelper.floor_double(p_70972_1_.minX);
        int j = MathHelper.floor_double(p_70972_1_.minY);
        int k = MathHelper.floor_double(p_70972_1_.minZ);
        int l = MathHelper.floor_double(p_70972_1_.maxX);
        int i1 = MathHelper.floor_double(p_70972_1_.maxY);
        int j1 = MathHelper.floor_double(p_70972_1_.maxZ);
        boolean flag = false;
        boolean flag1 = false;

        for (int k1 = i; k1 <= l; ++k1)
        {
            for (int l1 = j; l1 <= i1; ++l1)
            {
                for (int i2 = k; i2 <= j1; ++i2)
                {
                    阻止位置 blockpos = new 阻止位置(k1, l1, i2);
                    Block block = this.worldObj.getBlockState(blockpos).getBlock();

                    if (block.getMaterial() != Material.air)
                    {
                        if (block != Blocks.barrier && block != Blocks.obsidian && block != Blocks.end_stone && block != Blocks.bedrock && block != Blocks.command_block && this.worldObj.getGameRules().getBoolean("mobGriefing"))
                        {
                            flag1 = this.worldObj.setBlockToAir(blockpos) || flag1;
                        }
                        else
                        {
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag1)
        {
            double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * (double)this.rand.nextFloat();
            double d1 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * (double)this.rand.nextFloat();
            double d2 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * (double)this.rand.nextFloat();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        return flag;
    }

    public boolean attackEntityFromPart(实体DragonPart dragonPart, DamageSource source, float p_70965_3_)
    {
        if (dragonPart != this.dragonPartHead)
        {
            p_70965_3_ = p_70965_3_ / 4.0F + 1.0F;
        }

        float f = this.旋转侧滑 * (float)Math.PI / 180.0F;
        float f1 = MathHelper.sin(f);
        float f2 = MathHelper.cos(f);
        this.targetX = this.X坐标 + (double)(f1 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
        this.targetY = this.Y坐标 + (double)(this.rand.nextFloat() * 3.0F) + 1.0D;
        this.targetZ = this.Z坐标 - (double)(f2 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
        this.target = null;

        if (source.getEntity() instanceof 实体Player || source.isExplosion())
        {
            this.attackDragonFrom(source, p_70965_3_);
        }

        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
        {
            this.attackDragonFrom(source, amount);
        }

        return false;
    }

    protected boolean attackDragonFrom(DamageSource source, float amount)
    {
        return super.attackEntityFrom(source, amount);
    }

    public void onKillCommand()
    {
        this.setDead();
    }

    protected void onDeathUpdate()
    {
        ++this.deathTicks;

        if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.X坐标 + (double)f, this.Y坐标 + 2.0D + (double)f1, this.Z坐标 + (double)f2, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        boolean flag = this.worldObj.getGameRules().getBoolean("doMobLoot");

        if (!this.worldObj.isRemote)
        {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && flag)
            {
                int i = 1000;

                while (i > 0)
                {
                    int k = 实体XPOrb.getXPSplit(i);
                    i -= k;
                    this.worldObj.spawnEntityInWorld(new 实体XPOrb(this.worldObj, this.X坐标, this.Y坐标, this.Z坐标, k));
                }
            }

            if (this.deathTicks == 1)
            {
                this.worldObj.playBroadcastSound(1018, new 阻止位置(this), 0);
            }
        }

        this.moveEntity(0.0D, 0.10000000149011612D, 0.0D);
        this.renderYawOffset = this.旋转侧滑 += 20.0F;

        if (this.deathTicks == 200 && !this.worldObj.isRemote)
        {
            if (flag)
            {
                int j = 2000;

                while (j > 0)
                {
                    int l = 实体XPOrb.getXPSplit(j);
                    j -= l;
                    this.worldObj.spawnEntityInWorld(new 实体XPOrb(this.worldObj, this.X坐标, this.Y坐标, this.Z坐标, l));
                }
            }

            this.generatePortal(new 阻止位置(this.X坐标, 64.0D, this.Z坐标));
            this.setDead();
        }
    }

    private void generatePortal(阻止位置 pos)
    {
        int i = 4;
        double d0 = 12.25D;
        double d1 = 6.25D;

        for (int j = -1; j <= 32; ++j)
        {
            for (int k = -4; k <= 4; ++k)
            {
                for (int l = -4; l <= 4; ++l)
                {
                    double d2 = (double)(k * k + l * l);

                    if (d2 <= 12.25D)
                    {
                        阻止位置 blockpos = pos.add(k, j, l);

                        if (j < 0)
                        {
                            if (d2 <= 6.25D)
                            {
                                this.worldObj.setBlockState(blockpos, Blocks.bedrock.getDefaultState());
                            }
                        }
                        else if (j > 0)
                        {
                            this.worldObj.setBlockState(blockpos, Blocks.air.getDefaultState());
                        }
                        else if (d2 > 6.25D)
                        {
                            this.worldObj.setBlockState(blockpos, Blocks.bedrock.getDefaultState());
                        }
                        else
                        {
                            this.worldObj.setBlockState(blockpos, Blocks.end_portal.getDefaultState());
                        }
                    }
                }
            }
        }

        this.worldObj.setBlockState(pos, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(pos.up(), Blocks.bedrock.getDefaultState());
        阻止位置 blockpos1 = pos.up(2);
        this.worldObj.setBlockState(blockpos1, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(blockpos1.west(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST));
        this.worldObj.setBlockState(blockpos1.east(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST));
        this.worldObj.setBlockState(blockpos1.north(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
        this.worldObj.setBlockState(blockpos1.south(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
        this.worldObj.setBlockState(pos.up(3), Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(pos.up(4), Blocks.dragon_egg.getDefaultState());
    }

    protected void despawnEntity()
    {
    }

    public 实体[] getParts()
    {
        return this.dragonPartArray;
    }

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public World getWorld()
    {
        return this.worldObj;
    }

    protected String getLivingSound()
    {
        return "mob.enderdragon.growl";
    }

    protected String getHurtSound()
    {
        return "mob.enderdragon.hit";
    }

    protected float getSoundVolume()
    {
        return 5.0F;
    }
}
