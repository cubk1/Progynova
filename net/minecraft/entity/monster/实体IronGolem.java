package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class 实体IronGolem extends 实体Golem
{
    private int homeCheckTimer;
    Village villageObj;
    private int attackTimer;
    private int holdRoseTick;

    public 实体IronGolem(World worldIn)
    {
        super(worldIn);
        this.setSize(1.4F, 2.9F);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(5, new EntityAILookAtVillager(this));
        this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, 实体Player.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new 实体IronGolem.AINearestAttackableTargetNonCreeper(this, 实体Living.class, 10, false, true, IMob.VISIBLE_MOB_SELECTOR));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    protected void updateAITasks()
    {
        if (--this.homeCheckTimer <= 0)
        {
            this.homeCheckTimer = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new 阻止位置(this), 32);

            if (this.villageObj == null)
            {
                this.detachHome();
            }
            else
            {
                阻止位置 blockpos = this.villageObj.getCenter();
                this.setHomePosAndDistance(blockpos, (int)((float)this.villageObj.getVillageRadius() * 0.6F));
            }
        }

        super.updateAITasks();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    protected int decreaseAirSupply(int p_70682_1_)
    {
        return p_70682_1_;
    }

    protected void collideWithEntity(实体 实体In)
    {
        if (实体In instanceof IMob && !(实体In instanceof 实体Creeper) && this.getRNG().nextInt(20) == 0)
        {
            this.setAttackTarget((实体LivingBase) 实体In);
        }

        super.collideWithEntity(实体In);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }

        if (this.holdRoseTick > 0)
        {
            --this.holdRoseTick;
        }

        if (this.通便X * this.通便X + this.通便Z * this.通便Z > 2.500000277905201E-7D && this.rand.nextInt(5) == 0)
        {
            int i = MathHelper.floor_double(this.X坐标);
            int j = MathHelper.floor_double(this.Y坐标 - 0.20000000298023224D);
            int k = MathHelper.floor_double(this.Z坐标);
            IBlockState iblockstate = this.worldObj.getBlockState(new 阻止位置(i, j, k));
            Block block = iblockstate.getBlock();

            if (block.getMaterial() != Material.air)
            {
                this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.X坐标 + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.Z坐标 + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, new int[] {Block.getStateId(iblockstate)});
            }
        }
    }

    public boolean canAttackClass(Class <? extends 实体LivingBase> cls)
    {
        return this.isPlayerCreated() && 实体Player.class.isAssignableFrom(cls) ? false : (cls == 实体Creeper.class ? false : super.canAttackClass(cls));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("PlayerCreated", this.isPlayerCreated());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
    }

    public boolean attackEntityAsMob(实体 实体In)
    {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean flag = 实体In.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(15)));

        if (flag)
        {
            实体In.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, 实体In);
        }

        this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 4)
        {
            this.attackTimer = 10;
            this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        }
        else if (id == 11)
        {
            this.holdRoseTick = 400;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public Village getVillage()
    {
        return this.villageObj;
    }

    public int getAttackTimer()
    {
        return this.attackTimer;
    }

    public void setHoldingRose(boolean p_70851_1_)
    {
        this.holdRoseTick = p_70851_1_ ? 400 : 0;
        this.worldObj.setEntityState(this, (byte)11);
    }

    protected String getHurtSound()
    {
        return "mob.irongolem.hit";
    }

    protected String getDeathSound()
    {
        return "mob.irongolem.death";
    }

    protected void playStepSound(阻止位置 pos, Block blockIn)
    {
        this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        int i = this.rand.nextInt(3);

        for (int j = 0; j < i; ++j)
        {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.red_flower), 1, (float)BlockFlower.EnumFlowerType.POPPY.getMeta());
        }

        int l = 3 + this.rand.nextInt(3);

        for (int k = 0; k < l; ++k)
        {
            this.dropItem(Items.iron_ingot, 1);
        }
    }

    public int getHoldRoseTick()
    {
        return this.holdRoseTick;
    }

    public boolean isPlayerCreated()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setPlayerCreated(boolean p_70849_1_)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70849_1_)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    public void onDeath(DamageSource cause)
    {
        if (!this.isPlayerCreated() && this.attackingPlayer != null && this.villageObj != null)
        {
            this.villageObj.setReputationForPlayer(this.attackingPlayer.getName(), -5);
        }

        super.onDeath(cause);
    }

    static class AINearestAttackableTargetNonCreeper<T extends 实体LivingBase> extends EntityAINearestAttackableTarget<T>
    {
        public AINearestAttackableTargetNonCreeper(final 实体Creature creature, Class<T> classTarget, int chance, boolean p_i45858_4_, boolean p_i45858_5_, final Predicate <? super T > p_i45858_6_)
        {
            super(creature, classTarget, chance, p_i45858_4_, p_i45858_5_, p_i45858_6_);
            this.targetEntitySelector = new Predicate<T>()
            {
                public boolean apply(T p_apply_1_)
                {
                    if (p_i45858_6_ != null && !p_i45858_6_.apply(p_apply_1_))
                    {
                        return false;
                    }
                    else if (p_apply_1_ instanceof 实体Creeper)
                    {
                        return false;
                    }
                    else
                    {
                        if (p_apply_1_ instanceof 实体Player)
                        {
                            double d0 = AINearestAttackableTargetNonCreeper.this.getTargetDistance();

                            if (p_apply_1_.正在下蹲())
                            {
                                d0 *= 0.800000011920929D;
                            }

                            if (p_apply_1_.isInvisible())
                            {
                                float f = ((实体Player)p_apply_1_).getArmorVisibility();

                                if (f < 0.1F)
                                {
                                    f = 0.1F;
                                }

                                d0 *= (double)(0.7F * f);
                            }

                            if ((double)p_apply_1_.getDistanceToEntity(creature) > d0)
                            {
                                return false;
                            }
                        }

                        return AINearestAttackableTargetNonCreeper.this.isSuitableTarget(p_apply_1_, false);
                    }
                }
            };
        }
    }
}
