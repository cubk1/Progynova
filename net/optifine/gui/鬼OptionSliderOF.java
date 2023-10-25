package net.optifine.gui;

import net.minecraft.client.gui.鬼OptionSlider;
import net.minecraft.client.settings.GameSettings;

public class 鬼OptionSliderOF extends 鬼OptionSlider implements IOptionControl
{
    private GameSettings.Options option = null;

    public 鬼OptionSliderOF(int id, int x, int y, GameSettings.Options option)
    {
        super(id, x, y, option);
        this.option = option;
    }

    public GameSettings.Options getOption()
    {
        return this.option;
    }
}
