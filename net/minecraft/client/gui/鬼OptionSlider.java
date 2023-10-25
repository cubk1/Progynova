package net.minecraft.client.gui;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;

public class 鬼OptionSlider extends 鬼Button
{
    private float sliderValue;
    public boolean dragging;
    private GameSettings.Options options;
    private final float field_146132_r;
    private final float field_146131_s;

    public 鬼OptionSlider(int p_i45016_1_, int p_i45016_2_, int p_i45016_3_, GameSettings.Options p_i45016_4_)
    {
        this(p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0F, 1.0F);
    }

    public 鬼OptionSlider(int p_i45017_1_, int p_i45017_2_, int p_i45017_3_, GameSettings.Options p_i45017_4_, float p_i45017_5_, float p_i45017_6_)
    {
        super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 150, 20, "");
        this.sliderValue = 1.0F;
        this.options = p_i45017_4_;
        this.field_146132_r = p_i45017_5_;
        this.field_146131_s = p_i45017_6_;
        我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();
        this.sliderValue = p_i45017_4_.normalizeValue(宇轩的世界.游戏一窝.getOptionFloatValue(p_i45017_4_));
        this.displayString = 宇轩的世界.游戏一窝.getKeyBinding(p_i45017_4_);
    }

    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    protected void mouseDragged(我的手艺 mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                float f = this.options.denormalizeValue(this.sliderValue);
                mc.游戏一窝.setOptionFloatValue(this.options, f);
                this.sliderValue = this.options.normalizeValue(f);
                this.displayString = mc.游戏一窝.getKeyBinding(this.options);
            }

            mc.得到手感经理().绑定手感(buttonTextures);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public boolean mousePressed(我的手艺 mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
            mc.游戏一窝.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
            this.displayString = mc.游戏一窝.getKeyBinding(this.options);
            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void mouseReleased(int mouseX, int mouseY)
    {
        this.dragging = false;
    }
}
