package net.optifine.shaders.gui;

import net.minecraft.client.gui.鬼Button;
import net.optifine.shaders.config.ShaderOption;

public class 鬼ButtonShaderOption extends 鬼Button
{
    private ShaderOption shaderOption = null;

    public 鬼ButtonShaderOption(int buttonId, int x, int y, int widthIn, int heightIn, ShaderOption shaderOption, String text)
    {
        super(buttonId, x, y, widthIn, heightIn, text);
        this.shaderOption = shaderOption;
    }

    public ShaderOption getShaderOption()
    {
        return this.shaderOption;
    }

    public void valueChanged()
    {
    }

    public boolean isSwitchable()
    {
        return true;
    }
}
