package net.minecraft.inventory;

import net.minecraft.entity.player.实体Player;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable
{
    int getSizeInventory();

    ItemStack getStackInSlot(int index);

    ItemStack decrStackSize(int index, int count);

    ItemStack removeStackFromSlot(int index);

    void setInventorySlotContents(int index, ItemStack stack);

    int getInventoryStackLimit();

    void markDirty();

    boolean isUseableByPlayer(实体Player player);

    void openInventory(实体Player player);

    void closeInventory(实体Player player);

    boolean isItemValidForSlot(int index, ItemStack stack);

    int getField(int id);

    void setField(int id, int value);

    int getFieldCount();

    void clear();
}
