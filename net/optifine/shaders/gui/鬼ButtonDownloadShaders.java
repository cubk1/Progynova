package net.optifine.shaders.gui;

import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.util.ResourceLocation;

public class 鬼ButtonDownloadShaders extends 鬼Button
{
    public 鬼ButtonDownloadShaders(int buttonID, int xPos, int yPos)
    {
        super(buttonID, xPos, yPos, 22, 20, "");
    }

    public void drawButton(我的手艺 mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            super.drawButton(mc, mouseX, mouseY);
            ResourceLocation resourcelocation = new ResourceLocation("optifine/textures/icons.png");
            mc.得到手感经理().绑定手感(resourcelocation);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + 3, this.yPosition + 2, 0, 0, 16, 16);
        }
    }
}
