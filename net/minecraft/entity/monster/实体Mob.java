package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.实体Creature;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class 实体Mob extends 实体Creature implements IMob
{
    public 实体Mob(World worldIn)
    {
        super(worldIn);
        this.experienceValue = 5;
    }

    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
        float f = this.getBrightness(1.0F);

        if (f > 0.5F)
        {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            this.setDead();
        }
    }

    protected String getSwimSound()
    {
        return "game.hostile.swim";
    }

    protected String getSplashSound()
    {
        return "game.hostile.swim.splash";
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else if (super.attackEntityFrom(source, amount))
        {
            实体 实体 = source.getEntity();
            return this.riddenBy实体 != 实体 && this.riding实体 != 实体 ? true : true;
        }
        else
        {
            return false;
        }
    }

    protected String getHurtSound()
    {
        return "game.hostile.hurt";
    }

    protected String getDeathSound()
    {
        return "game.hostile.die";
    }

    protected String getFallSoundString(int damageValue)
    {
        return damageValue > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
    }

    public boolean attackEntityAsMob(实体 实体In)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i = 0;

        if (实体In instanceof 实体LivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItem(), ((实体LivingBase) 实体In).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = 实体In.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0)
            {
                实体In.addVelocity((double)(-MathHelper.sin(this.旋转侧滑 * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.旋转侧滑 * (float)Math.PI / 180.0F) * (float)i * 0.5F));
                this.通便X *= 0.6D;
                this.通便Z *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                实体In.setFire(j * 4);
            }

            this.applyEnchantments(this, 实体In);
        }

        return flag;
    }

    public float getBlockPathWeight(阻止位置 pos)
    {
        return 0.5F - this.worldObj.getLightBrightness(pos);
    }

    protected boolean isValidLightLevel()
    {
        阻止位置 blockpos = new 阻止位置(this.X坐标, this.getEntityBoundingBox().minY, this.Z坐标);

        if (this.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int i = this.worldObj.getLightFromNeighbors(blockpos);

            if (this.worldObj.isThundering())
            {
                int j = this.worldObj.getSkylightSubtracted();
                this.worldObj.setSkylightSubtracted(10);
                i = this.worldObj.getLightFromNeighbors(blockpos);
                this.worldObj.setSkylightSubtracted(j);
            }

            return i <= this.rand.nextInt(8);
        }
    }

    public boolean getCanSpawnHere()
    {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    }

    protected boolean canDropLoot()
    {
        return true;
    }
}
