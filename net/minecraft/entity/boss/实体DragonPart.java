package net.minecraft.entity.boss;

import net.minecraft.entity.实体;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class 实体DragonPart extends 实体
{
    public final IEntityMultiPart entityDragonObj;
    public final String partName;

    public 实体DragonPart(IEntityMultiPart parent, String partName, float base, float sizeHeight)
    {
        super(parent.getWorld());
        this.setSize(base, sizeHeight);
        this.entityDragonObj = parent;
        this.partName = partName;
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

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return this.isEntityInvulnerable(source) ? false : this.entityDragonObj.attackEntityFromPart(this, source, amount);
    }

    public boolean isEntityEqual(实体 实体In)
    {
        return this == 实体In || this.entityDragonObj == 实体In;
    }
}
