package net.minecraft.client.gui;

import net.minecraft.client.我的手艺;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry
{
    private final 鬼Multiplayer field_148292_c;
    protected final 我的手艺 mc;
    protected final LanServerDetector.LanServer field_148291_b;
    private long field_148290_d = 0L;

    protected ServerListEntryLanDetected(鬼Multiplayer p_i45046_1_, LanServerDetector.LanServer p_i45046_2_)
    {
        this.field_148292_c = p_i45046_1_;
        this.field_148291_b = p_i45046_2_;
        this.mc = 我的手艺.得到我的手艺();
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
        this.mc.字体渲染员.drawString(I18n.format("lanServer.title", new Object[0]), x + 32 + 3, y + 1, 16777215);
        this.mc.字体渲染员.drawString(this.field_148291_b.getServerMotd(), x + 32 + 3, y + 12, 8421504);

        if (this.mc.游戏一窝.hideServerAddress)
        {
            this.mc.字体渲染员.drawString(I18n.format("selectServer.hiddenAddress", new Object[0]), x + 32 + 3, y + 12 + 11, 3158064);
        }
        else
        {
            this.mc.字体渲染员.drawString(this.field_148291_b.getServerIpPort(), x + 32 + 3, y + 12 + 11, 3158064);
        }
    }

    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        this.field_148292_c.selectServer(slotIndex);

        if (我的手艺.getSystemTime() - this.field_148290_d < 250L)
        {
            this.field_148292_c.connectToSelected();
        }

        this.field_148290_d = 我的手艺.getSystemTime();
        return false;
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
    {
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }

    public LanServerDetector.LanServer getLanServer()
    {
        return this.field_148291_b;
    }
}
