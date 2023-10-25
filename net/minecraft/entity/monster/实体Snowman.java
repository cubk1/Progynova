package net.minecraft.entity.monster;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体Living;
import net.minecraft.entity.projectile.实体Snowball;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体Snowman extends 实体Golem implements IRangedAttackMob
{
    public 实体Snowman(World worldIn)
    {
        super(worldIn);
        this.setSize(0.7F, 1.9F);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAIArrowAttack(this, 1.25D, 20, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, 实体Player.class, 6.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, 实体Living.class, 10, true, false, IMob.mobSelector));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote)
        {
            int i = MathHelper.floor_double(this.X坐标);
            int j = MathHelper.floor_double(this.Y坐标);
            int k = MathHelper.floor_double(this.Z坐标);

            if (this.isWet())
            {
                this.attackEntityFrom(DamageSource.drown, 1.0F);
            }

            if (this.worldObj.getBiomeGenForCoords(new 阻止位置(i, 0, k)).getFloatTemperature(new 阻止位置(i, j, k)) > 1.0F)
            {
                this.attackEntityFrom(DamageSource.onFire, 1.0F);
            }

            for (int l = 0; l < 4; ++l)
            {
                i = MathHelper.floor_double(this.X坐标 + (double)((float)(l % 2 * 2 - 1) * 0.25F));
                j = MathHelper.floor_double(this.Y坐标);
                k = MathHelper.floor_double(this.Z坐标 + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
                阻止位置 blockpos = new 阻止位置(i, j, k);

                if (this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air && this.worldObj.getBiomeGenForCoords(new 阻止位置(i, 0, k)).getFloatTemperature(blockpos) < 0.8F && Blocks.snow_layer.canPlaceBlockAt(this.worldObj, blockpos))
                {
                    this.worldObj.setBlockState(blockpos, Blocks.snow_layer.getDefaultState());
                }
            }
        }
    }

    protected Item getDropItem()
    {
        return Items.snowball;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        int i = this.rand.nextInt(16);

        for (int j = 0; j < i; ++j)
        {
            this.dropItem(Items.snowball, 1);
        }
    }

    public void attackEntityWithRangedAttack(实体LivingBase target, float p_82196_2_)
    {
        实体Snowball entitysnowball = new 实体Snowball(this.worldObj, this);
        double d0 = target.Y坐标 + (double)target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.X坐标 - this.X坐标;
        double d2 = d0 - entitysnowball.Y坐标;
        double d3 = target.Z坐标 - this.Z坐标;
        float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3) * 0.2F;
        entitysnowball.setThrowableHeading(d1, d2 + (double)f, d3, 1.6F, 12.0F);
        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entitysnowball);
    }

    public float getEyeHeight()
    {
        return 1.7F;
    }
}
