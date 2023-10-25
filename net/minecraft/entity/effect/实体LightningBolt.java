package net.minecraft.entity.effect;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class 实体LightningBolt extends 实体WeatherEffect
{
    private int lightningState;
    public long boltVertex;
    private int boltLivingTime;

    public 实体LightningBolt(World worldIn, double posX, double posY, double posZ)
    {
        super(worldIn);
        this.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        阻止位置 blockpos = new 阻止位置(this);

        if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doFireTick") && (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD) && worldIn.isAreaLoaded(blockpos, 10))
        {
            if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(worldIn, blockpos))
            {
                worldIn.setBlockState(blockpos, Blocks.fire.getDefaultState());
            }

            for (int i = 0; i < 4; ++i)
            {
                阻止位置 blockpos1 = blockpos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);

                if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(worldIn, blockpos1))
                {
                    worldIn.setBlockState(blockpos1, Blocks.fire.getDefaultState());
                }
            }
        }
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.lightningState == 2)
        {
            this.worldObj.playSoundEffect(this.X坐标, this.Y坐标, this.Z坐标, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.worldObj.playSoundEffect(this.X坐标, this.Y坐标, this.Z坐标, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;

        if (this.lightningState < 0)
        {
            if (this.boltLivingTime == 0)
            {
                this.setDead();
            }
            else if (this.lightningState < -this.rand.nextInt(10))
            {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
                阻止位置 blockpos = new 阻止位置(this);

                if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("doFireTick") && this.worldObj.isAreaLoaded(blockpos, 10) && this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(this.worldObj, blockpos))
                {
                    this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
                }
            }
        }

        if (this.lightningState >= 0)
        {
            if (this.worldObj.isRemote)
            {
                this.worldObj.setLastLightningBolt(2);
            }
            else
            {
                double d0 = 3.0D;
                List<实体> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.X坐标 - d0, this.Y坐标 - d0, this.Z坐标 - d0, this.X坐标 + d0, this.Y坐标 + 6.0D + d0, this.Z坐标 + d0));

                for (int i = 0; i < list.size(); ++i)
                {
                    实体 实体 = (实体)list.get(i);
                    实体.onStruckByLightning(this);
                }
            }
        }
    }

    protected void entityInit()
    {
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    }
}
