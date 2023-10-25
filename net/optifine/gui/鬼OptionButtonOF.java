package net.optifine.gui;

import net.minecraft.client.gui.鬼OptionButton;
import net.minecraft.client.settings.GameSettings;

public class 鬼OptionButtonOF extends 鬼OptionButton implements IOptionControl
{
    private GameSettings.Options option = null;

    public 鬼OptionButtonOF(int id, int x, int y, GameSettings.Options option, String text)
    {
        super(id, x, y, option, text);
        this.option = option;
    }

    public GameSettings.Options getOption()
    {
        return this.option;
    }
}
