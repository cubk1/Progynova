package net.minecraft.client.gui;

import net.minecraft.client.gui.inventory.鬼Container;
import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.图像位置;

public class 鬼Hopper extends 鬼Container
{
    private static final 图像位置 HOPPER_GUI_TEXTURE = new 图像位置("textures/gui/container/hopper.png");
    private IInventory playerInventory;
    private IInventory hopperInventory;

    public 鬼Hopper(InventoryPlayer playerInv, IInventory hopperInv)
    {
        super(new ContainerHopper(playerInv, hopperInv, 我的手艺.得到我的手艺().宇轩游玩者));
        this.playerInventory = playerInv;
        this.hopperInventory = hopperInv;
        this.allowUserInput = false;
        this.ySize = 133;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(this.hopperInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.得到手感经理().绑定手感(HOPPER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
