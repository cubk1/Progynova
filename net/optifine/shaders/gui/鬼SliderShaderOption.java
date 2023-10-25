package net.optifine.shaders.gui;

import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.鬼Screen;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.util.MathHelper;
import net.optifine.shaders.config.ShaderOption;

public class 鬼SliderShaderOption extends 鬼ButtonShaderOption
{
    private float sliderValue = 1.0F;
    public boolean dragging;
    private ShaderOption shaderOption = null;

    public 鬼SliderShaderOption(int buttonId, int x, int y, int w, int h, ShaderOption shaderOption, String text)
    {
        super(buttonId, x, y, w, h, shaderOption, text);
        this.shaderOption = shaderOption;
        this.sliderValue = shaderOption.getIndexNormalized();
        this.displayString = 鬼ShaderOptions.getButtonText(shaderOption, this.width);
    }

    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    protected void mouseDragged(我的手艺 mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging && !鬼Screen.isShiftKeyDown())
            {
                this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                this.shaderOption.setIndexNormalized(this.sliderValue);
                this.sliderValue = this.shaderOption.getIndexNormalized();
                this.displayString = 鬼ShaderOptions.getButtonText(this.shaderOption, this.width);
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
            this.shaderOption.setIndexNormalized(this.sliderValue);
            this.displayString = 鬼ShaderOptions.getButtonText(this.shaderOption, this.width);
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

    public void valueChanged()
    {
        this.sliderValue = this.shaderOption.getIndexNormalized();
    }

    public boolean isSwitchable()
    {
        return false;
    }
}
