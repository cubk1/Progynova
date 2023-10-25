package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.achievement.鬼Achievements;
import net.minecraft.client.gui.achievement.鬼Stats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;

public class 鬼IngameMenu extends 鬼Screen
{
    private int field_146445_a;
    private int field_146444_f;

    public void initGui()
    {
        this.field_146445_a = 0;
        this.buttonList.clear();
        int i = -16;
        int j = 98;
        this.buttonList.add(new 鬼Button(1, this.width / 2 - 100, this.height / 4 + 120 + i, I18n.format("menu.returnToMenu", new Object[0])));

        if (!this.mc.isIntegratedServerRunning())
        {
            ((鬼Button)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
        }

        this.buttonList.add(new 鬼Button(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame", new Object[0])));
        this.buttonList.add(new 鬼Button(0, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.options", new Object[0])));
        鬼Button guibutton;
        this.buttonList.add(guibutton = new 鬼Button(7, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
        this.buttonList.add(new 鬼Button(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements", new Object[0])));
        this.buttonList.add(new 鬼Button(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats", new Object[0])));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(new 鬼Options(this, this.mc.游戏一窝));
                break;

            case 1:
                boolean flag = this.mc.isIntegratedServerRunning();
                boolean flag1 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.宇轩の世界.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);

                if (flag)
                {
                    this.mc.displayGuiScreen(new 鬼MainMenu());
                }
                else if (flag1)
                {
                    RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms(new 鬼MainMenu());
                }
                else
                {
                    this.mc.displayGuiScreen(new 鬼Multiplayer(new 鬼MainMenu()));
                }

            case 2:
            case 3:
            default:
                break;

            case 4:
                this.mc.displayGuiScreen((鬼Screen)null);
                this.mc.setIngameFocus();
                break;

            case 5:
                this.mc.displayGuiScreen(new 鬼Achievements(this, this.mc.宇轩游玩者.getStatFileWriter()));
                break;

            case 6:
                this.mc.displayGuiScreen(new 鬼Stats(this, this.mc.宇轩游玩者.getStatFileWriter()));
                break;

            case 7:
                this.mc.displayGuiScreen(new 鬼ShareToLan(this));
        }
    }

    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_146444_f;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
