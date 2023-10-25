package net.minecraft.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.实体Minecart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class 实体MinecartMobSpawner extends 实体Minecart
{
    private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic()
    {
        public void func_98267_a(int id)
        {
            实体MinecartMobSpawner.this.worldObj.setEntityState(实体MinecartMobSpawner.this, (byte)id);
        }
        public World getSpawnerWorld()
        {
            return 实体MinecartMobSpawner.this.worldObj;
        }
        public 阻止位置 getSpawnerPosition()
        {
            return new 阻止位置(实体MinecartMobSpawner.this);
        }
    };

    public 实体MinecartMobSpawner(World worldIn)
    {
        super(worldIn);
    }

    public 实体MinecartMobSpawner(World worldIn, double p_i1726_2_, double p_i1726_4_, double p_i1726_6_)
    {
        super(worldIn, p_i1726_2_, p_i1726_4_, p_i1726_6_);
    }

    public 实体Minecart.EnumMinecartType getMinecartType()
    {
        return 实体Minecart.EnumMinecartType.SPAWNER;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.mob_spawner.getDefaultState();
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.mobSpawnerLogic.readFromNBT(tagCompund);
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        this.mobSpawnerLogic.writeToNBT(tagCompound);
    }

    public void handleStatusUpdate(byte id)
    {
        this.mobSpawnerLogic.setDelayToMin(id);
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.mobSpawnerLogic.updateSpawner();
    }

    public MobSpawnerBaseLogic func_98039_d()
    {
        return this.mobSpawnerLogic;
    }
}
