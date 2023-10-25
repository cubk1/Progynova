package net.minecraft.realms;

import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.gui.鬼ButtonRealmsProxy;
import net.minecraft.client.我的手艺;
import net.minecraft.util.图像位置;

public class RealmsButton
{
    protected static final 图像位置 WIDGETS_LOCATION = new 图像位置("textures/gui/widgets.png");
    private 鬼ButtonRealmsProxy proxy;

    public RealmsButton(int buttonId, int x, int y, String text)
    {
        this.proxy = new 鬼ButtonRealmsProxy(this, buttonId, x, y, text);
    }

    public RealmsButton(int buttonId, int x, int y, int widthIn, int heightIn, String text)
    {
        this.proxy = new 鬼ButtonRealmsProxy(this, buttonId, x, y, text, widthIn, heightIn);
    }

    public 鬼Button getProxy()
    {
        return this.proxy;
    }

    public int id()
    {
        return this.proxy.getId();
    }

    public boolean active()
    {
        return this.proxy.getEnabled();
    }

    public void active(boolean p_active_1_)
    {
        this.proxy.setEnabled(p_active_1_);
    }

    public void msg(String p_msg_1_)
    {
        this.proxy.setText(p_msg_1_);
    }

    public int getWidth()
    {
        return this.proxy.getButtonWidth();
    }

    public int getHeight()
    {
        return this.proxy.getHeight();
    }

    public int y()
    {
        return this.proxy.getPositionY();
    }

    public void render(int p_render_1_, int p_render_2_)
    {
        this.proxy.drawButton(我的手艺.得到我的手艺(), p_render_1_, p_render_2_);
    }

    public void clicked(int p_clicked_1_, int p_clicked_2_)
    {
    }

    public void released(int p_released_1_, int p_released_2_)
    {
    }

    public void blit(int p_blit_1_, int p_blit_2_, int p_blit_3_, int p_blit_4_, int p_blit_5_, int p_blit_6_)
    {
        this.proxy.drawTexturedModalRect(p_blit_1_, p_blit_2_, p_blit_3_, p_blit_4_, p_blit_5_, p_blit_6_);
    }

    public void renderBg(int p_renderBg_1_, int p_renderBg_2_)
    {
    }

    public int getYImage(boolean p_getYImage_1_)
    {
        return this.proxy.func_154312_c(p_getYImage_1_);
    }
}
