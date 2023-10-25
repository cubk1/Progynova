package net.minecraft.entity.item;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.projectile.实体Arrow;
import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class 实体MinecartTNT extends 实体Minecart
{
    private int minecartTNTFuse = -1;

    public 实体MinecartTNT(World worldIn)
    {
        super(worldIn);
    }

    public 实体MinecartTNT(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public 实体Minecart.EnumMinecartType getMinecartType()
    {
        return 实体Minecart.EnumMinecartType.TNT;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.tnt.getDefaultState();
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.minecartTNTFuse > 0)
        {
            --this.minecartTNTFuse;
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.X坐标, this.Y坐标 + 0.5D, this.Z坐标, 0.0D, 0.0D, 0.0D, new int[0]);
        }
        else if (this.minecartTNTFuse == 0)
        {
            this.explodeCart(this.通便X * this.通便X + this.通便Z * this.通便Z);
        }

        if (this.isCollidedHorizontally)
        {
            double d0 = this.通便X * this.通便X + this.通便Z * this.通便Z;

            if (d0 >= 0.009999999776482582D)
            {
                this.explodeCart(d0);
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        实体 实体 = source.getSourceOfDamage();

        if (实体 instanceof 实体Arrow)
        {
            实体Arrow entityarrow = (实体Arrow) 实体;

            if (entityarrow.isBurning())
            {
                this.explodeCart(entityarrow.通便X * entityarrow.通便X + entityarrow.motionY * entityarrow.motionY + entityarrow.通便Z * entityarrow.通便Z);
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);
        double d0 = this.通便X * this.通便X + this.通便Z * this.通便Z;

        if (!source.isExplosion() && this.worldObj.getGameRules().getBoolean("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(Blocks.tnt, 1), 0.0F);
        }

        if (source.isFireDamage() || source.isExplosion() || d0 >= 0.009999999776482582D)
        {
            this.explodeCart(d0);
        }
    }

    protected void explodeCart(double p_94103_1_)
    {
        if (!this.worldObj.isRemote)
        {
            double d0 = Math.sqrt(p_94103_1_);

            if (d0 > 5.0D)
            {
                d0 = 5.0D;
            }

            this.worldObj.createExplosion(this, this.X坐标, this.Y坐标, this.Z坐标, (float)(4.0D + this.rand.nextDouble() * 1.5D * d0), true);
            this.setDead();
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
        if (distance >= 3.0F)
        {
            float f = distance / 10.0F;
            this.explodeCart((double)(f * f));
        }

        super.fall(distance, damageMultiplier);
    }

    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
        if (receivingPower && this.minecartTNTFuse < 0)
        {
            this.ignite();
        }
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 10)
        {
            this.ignite();
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public void ignite()
    {
        this.minecartTNTFuse = 80;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.setEntityState(this, (byte)10);

            if (!this.isSilent())
            {
                this.worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0F, 1.0F);
            }
        }
    }

    public int getFuseTicks()
    {
        return this.minecartTNTFuse;
    }

    public boolean isIgnited()
    {
        return this.minecartTNTFuse > -1;
    }

    public float getExplosionResistance(Explosion explosionIn, World worldIn, 阻止位置 pos, IBlockState blockStateIn)
    {
        return !this.isIgnited() || !BlockRailBase.isRailBlock(blockStateIn) && !BlockRailBase.isRailBlock(worldIn, pos.up()) ? super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) : 0.0F;
    }

    public boolean verifyExplosion(Explosion explosionIn, World worldIn, 阻止位置 pos, IBlockState blockStateIn, float p_174816_5_)
    {
        return !this.isIgnited() || !BlockRailBase.isRailBlock(blockStateIn) && !BlockRailBase.isRailBlock(worldIn, pos.up()) ? super.verifyExplosion(explosionIn, worldIn, pos, blockStateIn, p_174816_5_) : false;
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("TNTFuse", 99))
        {
            this.minecartTNTFuse = tagCompund.getInteger("TNTFuse");
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("TNTFuse", this.minecartTNTFuse);
    }
}
