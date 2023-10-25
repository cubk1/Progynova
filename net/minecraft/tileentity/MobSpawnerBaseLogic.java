package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.entity.*;
import net.minecraft.entity.item.实体Minecart;
import net.minecraft.entity.实体;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public abstract class MobSpawnerBaseLogic
{
    private int spawnDelay = 20;
    private String mobID = "Pig";
    private final List<MobSpawnerBaseLogic.WeightedRandomMinecart> minecartToSpawn = Lists.<MobSpawnerBaseLogic.WeightedRandomMinecart>newArrayList();
    private MobSpawnerBaseLogic.WeightedRandomMinecart randomEntity;
    private double mobRotation;
    private double prevMobRotation;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;
    private 实体 cached实体;
    private int maxNearbyEntities = 6;
    private int activatingRangeFromPlayer = 16;
    private int spawnRange = 4;

    private String getEntityNameToSpawn()
    {
        if (this.getRandomEntity() == null)
        {
            if (this.mobID != null && this.mobID.equals("Minecart"))
            {
                this.mobID = "MinecartRideable";
            }

            return this.mobID;
        }
        else
        {
            return this.getRandomEntity().entityType;
        }
    }

    public void setEntityName(String name)
    {
        this.mobID = name;
    }

    private boolean isActivated()
    {
        阻止位置 blockpos = this.getSpawnerPosition();
        return this.getSpawnerWorld().isAnyPlayerWithinRangeAt((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, (double)this.activatingRangeFromPlayer);
    }

    public void updateSpawner()
    {
        if (this.isActivated())
        {
            阻止位置 blockpos = this.getSpawnerPosition();

            if (this.getSpawnerWorld().isRemote)
            {
                double d3 = (double)((float)blockpos.getX() + this.getSpawnerWorld().rand.nextFloat());
                double d4 = (double)((float)blockpos.getY() + this.getSpawnerWorld().rand.nextFloat());
                double d5 = (double)((float)blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat());
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                }

                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
            }
            else
            {
                if (this.spawnDelay == -1)
                {
                    this.resetTimer();
                }

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i)
                {
                    实体 实体 = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());

                    if (实体 == null)
                    {
                        return;
                    }

                    int j = this.getSpawnerWorld().getEntitiesWithinAABB(实体.getClass(), (new AxisAlignedBB((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), (double)(blockpos.getX() + 1), (double)(blockpos.getY() + 1), (double)(blockpos.getZ() + 1))).expand((double)this.spawnRange, (double)this.spawnRange, (double)this.spawnRange)).size();

                    if (j >= this.maxNearbyEntities)
                    {
                        this.resetTimer();
                        return;
                    }

                    double d0 = (double)blockpos.getX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    double d1 = (double)(blockpos.getY() + this.getSpawnerWorld().rand.nextInt(3) - 1);
                    double d2 = (double)blockpos.getZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    实体Living entityliving = 实体 instanceof 实体Living ? (实体Living) 实体 : null;
                    实体.setLocationAndAngles(d0, d1, d2, this.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

                    if (entityliving == null || entityliving.getCanSpawnHere() && entityliving.isNotColliding())
                    {
                        this.spawnNewEntity(实体, true);
                        this.getSpawnerWorld().playAuxSFX(2004, blockpos, 0);

                        if (entityliving != null)
                        {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }

                if (flag)
                {
                    this.resetTimer();
                }
            }
        }
    }

    private 实体 spawnNewEntity(实体 实体In, boolean spawn)
    {
        if (this.getRandomEntity() != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            实体In.writeToNBTOptional(nbttagcompound);

            for (String s : this.getRandomEntity().nbtData.getKeySet())
            {
                NBTBase nbtbase = this.getRandomEntity().nbtData.getTag(s);
                nbttagcompound.setTag(s, nbtbase.copy());
            }

            实体In.readFromNBT(nbttagcompound);

            if (实体In.worldObj != null && spawn)
            {
                实体In.worldObj.spawnEntityInWorld(实体In);
            }

            NBTTagCompound nbttagcompound2;

            for (实体 实体 = 实体In; nbttagcompound.hasKey("Riding", 10); nbttagcompound = nbttagcompound2)
            {
                nbttagcompound2 = nbttagcompound.getCompoundTag("Riding");
                实体 实体1 = EntityList.createEntityByName(nbttagcompound2.getString("id"), 实体In.worldObj);

                if (实体1 != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    实体1.writeToNBTOptional(nbttagcompound1);

                    for (String s1 : nbttagcompound2.getKeySet())
                    {
                        NBTBase nbtbase1 = nbttagcompound2.getTag(s1);
                        nbttagcompound1.setTag(s1, nbtbase1.copy());
                    }

                    实体1.readFromNBT(nbttagcompound1);
                    实体1.setLocationAndAngles(实体.X坐标, 实体.Y坐标, 实体.Z坐标, 实体.旋转侧滑, 实体.rotationPitch);

                    if (实体In.worldObj != null && spawn)
                    {
                        实体In.worldObj.spawnEntityInWorld(实体1);
                    }

                    实体.mountEntity(实体1);
                }

                实体 = 实体1;
            }
        }
        else if (实体In instanceof 实体LivingBase && 实体In.worldObj != null && spawn)
        {
            if (实体In instanceof 实体Living)
            {
                ((实体Living) 实体In).onInitialSpawn(实体In.worldObj.getDifficultyForLocation(new 阻止位置(实体In)), (IEntityLivingData)null);
            }

            实体In.worldObj.spawnEntityInWorld(实体In);
        }

        return 实体In;
    }

    private void resetTimer()
    {
        if (this.maxSpawnDelay <= this.minSpawnDelay)
        {
            this.spawnDelay = this.minSpawnDelay;
        }
        else
        {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
        }

        if (this.minecartToSpawn.size() > 0)
        {
            this.setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart)WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.minecartToSpawn));
        }

        this.func_98267_a(1);
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        this.mobID = nbt.getString("EntityId");
        this.spawnDelay = nbt.getShort("Delay");
        this.minecartToSpawn.clear();

        if (nbt.hasKey("SpawnPotentials", 9))
        {
            NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                this.minecartToSpawn.add(new MobSpawnerBaseLogic.WeightedRandomMinecart(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (nbt.hasKey("SpawnData", 10))
        {
            this.setRandomEntity(new MobSpawnerBaseLogic.WeightedRandomMinecart(nbt.getCompoundTag("SpawnData"), this.mobID));
        }
        else
        {
            this.setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart)null);
        }

        if (nbt.hasKey("MinSpawnDelay", 99))
        {
            this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            this.spawnCount = nbt.getShort("SpawnCount");
        }

        if (nbt.hasKey("MaxNearbyEntities", 99))
        {
            this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
        }

        if (nbt.hasKey("SpawnRange", 99))
        {
            this.spawnRange = nbt.getShort("SpawnRange");
        }

        if (this.getSpawnerWorld() != null)
        {
            this.cached实体 = null;
        }
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        String s = this.getEntityNameToSpawn();

        if (!StringUtils.isNullOrEmpty(s))
        {
            nbt.setString("EntityId", s);
            nbt.setShort("Delay", (short)this.spawnDelay);
            nbt.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
            nbt.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
            nbt.setShort("SpawnCount", (short)this.spawnCount);
            nbt.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
            nbt.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
            nbt.setShort("SpawnRange", (short)this.spawnRange);

            if (this.getRandomEntity() != null)
            {
                nbt.setTag("SpawnData", this.getRandomEntity().nbtData.copy());
            }

            if (this.getRandomEntity() != null || this.minecartToSpawn.size() > 0)
            {
                NBTTagList nbttaglist = new NBTTagList();

                if (this.minecartToSpawn.size() > 0)
                {
                    for (MobSpawnerBaseLogic.WeightedRandomMinecart mobspawnerbaselogic$weightedrandomminecart : this.minecartToSpawn)
                    {
                        nbttaglist.appendTag(mobspawnerbaselogic$weightedrandomminecart.toNBT());
                    }
                }
                else
                {
                    nbttaglist.appendTag(this.getRandomEntity().toNBT());
                }

                nbt.setTag("SpawnPotentials", nbttaglist);
            }
        }
    }

    public 实体 func_180612_a(World worldIn)
    {
        if (this.cached实体 == null)
        {
            实体 实体 = EntityList.createEntityByName(this.getEntityNameToSpawn(), worldIn);

            if (实体 != null)
            {
                实体 = this.spawnNewEntity(实体, false);
                this.cached实体 = 实体;
            }
        }

        return this.cached实体;
    }

    public boolean setDelayToMin(int delay)
    {
        if (delay == 1 && this.getSpawnerWorld().isRemote)
        {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        else
        {
            return false;
        }
    }

    private MobSpawnerBaseLogic.WeightedRandomMinecart getRandomEntity()
    {
        return this.randomEntity;
    }

    public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_)
    {
        this.randomEntity = p_98277_1_;
    }

    public abstract void func_98267_a(int id);

    public abstract World getSpawnerWorld();

    public abstract 阻止位置 getSpawnerPosition();

    public double getMobRotation()
    {
        return this.mobRotation;
    }

    public double getPrevMobRotation()
    {
        return this.prevMobRotation;
    }

    public class WeightedRandomMinecart extends WeightedRandom.Item
    {
        private final NBTTagCompound nbtData;
        private final String entityType;

        public WeightedRandomMinecart(NBTTagCompound tagCompound)
        {
            this(tagCompound.getCompoundTag("Properties"), tagCompound.getString("Type"), tagCompound.getInteger("Weight"));
        }

        public WeightedRandomMinecart(NBTTagCompound tagCompound, String type)
        {
            this(tagCompound, type, 1);
        }

        private WeightedRandomMinecart(NBTTagCompound tagCompound, String type, int weight)
        {
            super(weight);

            if (type.equals("Minecart"))
            {
                if (tagCompound != null)
                {
                    type = 实体Minecart.EnumMinecartType.byNetworkID(tagCompound.getInteger("Type")).getName();
                }
                else
                {
                    type = "MinecartRideable";
                }
            }

            this.nbtData = tagCompound;
            this.entityType = type;
        }

        public NBTTagCompound toNBT()
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("Properties", this.nbtData);
            nbttagcompound.setString("Type", this.entityType);
            nbttagcompound.setInteger("Weight", this.itemWeight);
            return nbttagcompound;
        }
    }
}
