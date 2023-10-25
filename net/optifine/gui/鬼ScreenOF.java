package net.optifine.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.gui.鬼Screen;
import net.minecraft.client.gui.鬼VideoSettings;

public class 鬼ScreenOF extends 鬼Screen
{
    protected void actionPerformedRightClick(鬼Button button) throws IOException
    {
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 1)
        {
            鬼Button guibutton = getSelectedButton(mouseX, mouseY, this.buttonList);

            if (guibutton != null && guibutton.enabled)
            {
                guibutton.playPressSound(this.mc.getSoundHandler());
                this.actionPerformedRightClick(guibutton);
            }
        }
    }

    public static 鬼Button getSelectedButton(int x, int y, List<鬼Button> listButtons)
    {
        for (int i = 0; i < listButtons.size(); ++i)
        {
            鬼Button guibutton = (鬼Button)listButtons.get(i);

            if (guibutton.visible)
            {
                int j = 鬼VideoSettings.getButtonWidth(guibutton);
                int k = 鬼VideoSettings.getButtonHeight(guibutton);

                if (x >= guibutton.xPosition && y >= guibutton.yPosition && x < guibutton.xPosition + j && y < guibutton.yPosition + k)
                {
                    return guibutton;
                }
            }
        }

        return null;
    }
}
