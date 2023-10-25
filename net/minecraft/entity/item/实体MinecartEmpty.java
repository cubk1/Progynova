package net.minecraft.entity.item;

import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.world.World;

public class 实体MinecartEmpty extends 实体Minecart
{
    public 实体MinecartEmpty(World worldIn)
    {
        super(worldIn);
    }

    public 实体MinecartEmpty(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public boolean interactFirst(实体Player playerIn)
    {
        if (this.riddenBy实体 != null && this.riddenBy实体 instanceof 实体Player && this.riddenBy实体 != playerIn)
        {
            return true;
        }
        else if (this.riddenBy实体 != null && this.riddenBy实体 != playerIn)
        {
            return false;
        }
        else
        {
            if (!this.worldObj.isRemote)
            {
                playerIn.mountEntity(this);
            }

            return true;
        }
    }

    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
        if (receivingPower)
        {
            if (this.riddenBy实体 != null)
            {
                this.riddenBy实体.mountEntity((实体)null);
            }

            if (this.getRollingAmplitude() == 0)
            {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setDamage(50.0F);
                this.setBeenAttacked();
            }
        }
    }

    public 实体Minecart.EnumMinecartType getMinecartType()
    {
        return 实体Minecart.EnumMinecartType.RIDEABLE;
    }
}
