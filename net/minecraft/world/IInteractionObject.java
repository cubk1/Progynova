package net.minecraft.world;

import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public interface IInteractionObject extends IWorldNameable
{
    Container createContainer(InventoryPlayer playerInventory, 实体Player playerIn);

    String getGuiID();
}
