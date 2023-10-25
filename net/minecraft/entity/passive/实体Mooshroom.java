package net.minecraft.entity.passive;

import net.minecraft.entity.实体Ageable;
import net.minecraft.entity.item.实体Item;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class 实体Mooshroom extends 实体Cow
{
    public 实体Mooshroom(World worldIn)
    {
        super(worldIn);
        this.setSize(0.9F, 1.3F);
        this.spawnableBlock = Blocks.mycelium;
    }

    public boolean interact(实体Player player)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();

        if (itemstack != null && itemstack.getItem() == Items.bowl && this.getGrowingAge() >= 0)
        {
            if (itemstack.stackSize == 1)
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.mushroom_stew));
                return true;
            }

            if (player.inventory.addItemStackToInventory(new ItemStack(Items.mushroom_stew)) && !player.capabilities.isCreativeMode)
            {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
                return true;
            }
        }

        if (itemstack != null && itemstack.getItem() == Items.shears && this.getGrowingAge() >= 0)
        {
            this.setDead();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.X坐标, this.Y坐标 + (double)(this.height / 2.0F), this.Z坐标, 0.0D, 0.0D, 0.0D, new int[0]);

            if (!this.worldObj.isRemote)
            {
                实体Cow entitycow = new 实体Cow(this.worldObj);
                entitycow.setLocationAndAngles(this.X坐标, this.Y坐标, this.Z坐标, this.旋转侧滑, this.rotationPitch);
                entitycow.setHealth(this.getHealth());
                entitycow.renderYawOffset = this.renderYawOffset;

                if (this.hasCustomName())
                {
                    entitycow.setCustomNameTag(this.getCustomNameTag());
                }

                this.worldObj.spawnEntityInWorld(entitycow);

                for (int i = 0; i < 5; ++i)
                {
                    this.worldObj.spawnEntityInWorld(new 实体Item(this.worldObj, this.X坐标, this.Y坐标 + (double)this.height, this.Z坐标, new ItemStack(Blocks.red_mushroom)));
                }

                itemstack.damageItem(1, player);
                this.playSound("mob.sheep.shear", 1.0F, 1.0F);
            }

            return true;
        }
        else
        {
            return super.interact(player);
        }
    }

    public 实体Mooshroom createChild(实体Ageable ageable)
    {
        return new 实体Mooshroom(this.worldObj);
    }
}
