package net.minecraft.inventory;

import java.util.Random;

import net.minecraft.entity.item.实体Item;
import net.minecraft.entity.实体;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class InventoryHelper
{
    private static final Random RANDOM = new Random();

    public static void dropInventoryItems(World worldIn, 阻止位置 pos, IInventory inventory)
    {
        dropInventoryItems(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), inventory);
    }

    public static void dropInventoryItems(World worldIn, 实体 实体At, IInventory inventory)
    {
        dropInventoryItems(worldIn, 实体At.X坐标, 实体At.Y坐标, 实体At.Z坐标, inventory);
    }

    private static void dropInventoryItems(World worldIn, double x, double y, double z, IInventory inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (itemstack != null)
            {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }

    private static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
    {
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (stack.stackSize > 0)
        {
            int i = RANDOM.nextInt(21) + 10;

            if (i > stack.stackSize)
            {
                i = stack.stackSize;
            }

            stack.stackSize -= i;
            实体Item entityitem = new 实体Item(worldIn, x + (double)f, y + (double)f1, z + (double)f2, new ItemStack(stack.getItem(), i, stack.getMetadata()));

            if (stack.hasTagCompound())
            {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.通便X = RANDOM.nextGaussian() * (double)f3;
            entityitem.motionY = RANDOM.nextGaussian() * (double)f3 + 0.20000000298023224D;
            entityitem.通便Z = RANDOM.nextGaussian() * (double)f3;
            worldIn.spawnEntityInWorld(entityitem);
        }
    }
}
