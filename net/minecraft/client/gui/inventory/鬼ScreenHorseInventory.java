package net.minecraft.client.gui.inventory;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.passive.实体Horse;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.图像位置;

public class 鬼ScreenHorseInventory extends 鬼Container
{
    private static final 图像位置 horseGuiTextures = new 图像位置("textures/gui/container/horse.png");
    private IInventory playerInventory;
    private IInventory horseInventory;
    private 实体Horse horseEntity;
    private float mousePosx;
    private float mousePosY;

    public 鬼ScreenHorseInventory(IInventory playerInv, IInventory horseInv, 实体Horse horse)
    {
        super(new ContainerHorseInventory(playerInv, horseInv, horse, 我的手艺.得到我的手艺().宇轩游玩者));
        this.playerInventory = playerInv;
        this.horseInventory = horseInv;
        this.horseEntity = horse;
        this.allowUserInput = false;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.得到手感经理().绑定手感(horseGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (this.horseEntity.isChested())
        {
            this.drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, 90, 54);
        }

        if (this.horseEntity.canWearArmor())
        {
            this.drawTexturedModalRect(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
        }

        鬼Inventory.drawEntityOnScreen(i + 51, j + 60, 17, (float)(i + 51) - this.mousePosx, (float)(j + 75 - 50) - this.mousePosY, this.horseEntity);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.mousePosx = (float)mouseX;
        this.mousePosY = (float)mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
