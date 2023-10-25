package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class 鬼GameOver extends 鬼Screen implements GuiYesNoCallback
{
    private int enableButtonsTimer;
    private boolean field_146346_f = false;

    public void initGui()
    {
        this.buttonList.clear();

        if (this.mc.宇轩の世界.getWorldInfo().isHardcoreModeEnabled())
        {
            if (this.mc.isIntegratedServerRunning())
            {
                this.buttonList.add(new 鬼Button(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld", new Object[0])));
            }
            else
            {
                this.buttonList.add(new 鬼Button(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer", new Object[0])));
            }
        }
        else
        {
            this.buttonList.add(new 鬼Button(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn", new Object[0])));
            this.buttonList.add(new 鬼Button(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen", new Object[0])));

            if (this.mc.getSession() == null)
            {
                ((鬼Button)this.buttonList.get(1)).enabled = false;
            }
        }

        for (鬼Button guibutton : this.buttonList)
        {
            guibutton.enabled = false;
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        switch (button.id)
        {
            case 0:
                this.mc.宇轩游玩者.respawnPlayer();
                this.mc.displayGuiScreen((鬼Screen)null);
                break;

            case 1:
                if (this.mc.宇轩の世界.getWorldInfo().isHardcoreModeEnabled())
                {
                    this.mc.displayGuiScreen(new 鬼MainMenu());
                }
                else
                {
                    鬼YesNo guiyesno = new 鬼YesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format("deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
                    this.mc.displayGuiScreen(guiyesno);
                    guiyesno.setButtonDelay(20);
                }
        }
    }

    public void confirmClicked(boolean result, int id)
    {
        if (result)
        {
            this.mc.宇轩の世界.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient)null);
            this.mc.displayGuiScreen(new 鬼MainMenu());
        }
        else
        {
            this.mc.宇轩游玩者.respawnPlayer();
            this.mc.displayGuiScreen((鬼Screen)null);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        光照状态经理.推黑客帝国();
        光照状态经理.障眼物(2.0F, 2.0F, 2.0F);
        boolean flag = this.mc.宇轩の世界.getWorldInfo().isHardcoreModeEnabled();
        String s = flag ? I18n.format("deathScreen.title.hardcore", new Object[0]) : I18n.format("deathScreen.title", new Object[0]);
        this.drawCenteredString(this.fontRendererObj, s, this.width / 2 / 2, 30, 16777215);
        光照状态经理.流行音乐黑客帝国();

        if (flag)
        {
            this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo", new Object[0]), this.width / 2, 144, 16777215);
        }

        this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": " + EnumChatFormatting.YELLOW + this.mc.宇轩游玩者.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void updateScreen()
    {
        super.updateScreen();
        ++this.enableButtonsTimer;

        if (this.enableButtonsTimer == 20)
        {
            for (鬼Button guibutton : this.buttonList)
            {
                guibutton.enabled = true;
            }
        }
    }
}
