package net.minecraft.client.gui;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;

public class 鬼ButtonLanguage extends 鬼Button
{
    public 鬼ButtonLanguage(int buttonID, int xPos, int yPos)
    {
        super(buttonID, xPos, yPos, 20, 20, "");
    }

    public void drawButton(我的手艺 mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            mc.得到手感经理().绑定手感(鬼Button.buttonTextures);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = 106;

            if (flag)
            {
                i += this.height;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width, this.height);
        }
    }
}
