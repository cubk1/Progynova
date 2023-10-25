package net.minecraft.client.gui;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.resources.I18n;

public class 鬼Slider extends 鬼Button
{
    private float sliderPosition = 1.0F;
    public boolean isMouseDown;
    private String name;
    private final float min;
    private final float max;
    private final GuiPageButtonList.GuiResponder responder;
    private 鬼Slider.FormatHelper formatHelper;

    public 鬼Slider(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, String name, float min, float max, float defaultValue, 鬼Slider.FormatHelper formatter)
    {
        super(idIn, x, y, 150, 20, "");
        this.name = name;
        this.min = min;
        this.max = max;
        this.sliderPosition = (defaultValue - min) / (max - min);
        this.formatHelper = formatter;
        this.responder = guiResponder;
        this.displayString = this.getDisplayString();
    }

    public float func_175220_c()
    {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    public void func_175218_a(float p_175218_1_, boolean p_175218_2_)
    {
        this.sliderPosition = (p_175218_1_ - this.min) / (this.max - this.min);
        this.displayString = this.getDisplayString();

        if (p_175218_2_)
        {
            this.responder.onTick(this.id, this.func_175220_c());
        }
    }

    public float func_175217_d()
    {
        return this.sliderPosition;
    }

    private String getDisplayString()
    {
        return this.formatHelper == null ? I18n.format(this.name, new Object[0]) + ": " + this.func_175220_c() : this.formatHelper.getText(this.id, I18n.format(this.name, new Object[0]), this.func_175220_c());
    }

    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    protected void mouseDragged(我的手艺 mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.isMouseDown)
            {
                this.sliderPosition = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);

                if (this.sliderPosition < 0.0F)
                {
                    this.sliderPosition = 0.0F;
                }

                if (this.sliderPosition > 1.0F)
                {
                    this.sliderPosition = 1.0F;
                }

                this.displayString = this.getDisplayString();
                this.responder.onTick(this.id, this.func_175220_c());
            }

            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public void func_175219_a(float p_175219_1_)
    {
        this.sliderPosition = p_175219_1_;
        this.displayString = this.getDisplayString();
        this.responder.onTick(this.id, this.func_175220_c());
    }

    public boolean mousePressed(我的手艺 mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            this.sliderPosition = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);

            if (this.sliderPosition < 0.0F)
            {
                this.sliderPosition = 0.0F;
            }

            if (this.sliderPosition > 1.0F)
            {
                this.sliderPosition = 1.0F;
            }

            this.displayString = this.getDisplayString();
            this.responder.onTick(this.id, this.func_175220_c());
            this.isMouseDown = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void mouseReleased(int mouseX, int mouseY)
    {
        this.isMouseDown = false;
    }

    public interface FormatHelper
    {
        String getText(int id, String name, float value);
    }
}
