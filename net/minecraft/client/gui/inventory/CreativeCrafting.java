package net.minecraft.client.gui.inventory;

import java.util.List;

import net.minecraft.client.我的手艺;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class CreativeCrafting implements ICrafting
{
    private final 我的手艺 mc;

    public CreativeCrafting(我的手艺 mc)
    {
        this.mc = mc;
    }

    public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList)
    {
    }

    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
    {
        this.mc.玩家控制者.sendSlotPacket(stack, slotInd);
    }

    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue)
    {
    }

    public void sendAllWindowProperties(Container p_175173_1_, IInventory p_175173_2_)
    {
    }
}
