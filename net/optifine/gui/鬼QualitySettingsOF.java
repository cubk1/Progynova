package net.optifine.gui;

import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.gui.鬼OptionButton;
import net.minecraft.client.gui.鬼Screen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class 鬼QualitySettingsOF extends 鬼Screen
{
    private 鬼Screen prevScreen;
    protected String title;
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions = new GameSettings.Options[] {GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.MIPMAP_TYPE, GameSettings.Options.AF_LEVEL, GameSettings.Options.AA_LEVEL, GameSettings.Options.CLEAR_WATER, GameSettings.Options.RANDOM_ENTITIES, GameSettings.Options.BETTER_GRASS, GameSettings.Options.BETTER_SNOW, GameSettings.Options.CUSTOM_FONTS, GameSettings.Options.CUSTOM_COLORS, GameSettings.Options.CONNECTED_TEXTURES, GameSettings.Options.NATURAL_TEXTURES, GameSettings.Options.CUSTOM_SKY, GameSettings.Options.CUSTOM_ITEMS, GameSettings.Options.CUSTOM_ENTITY_MODELS, GameSettings.Options.CUSTOM_GUIS, GameSettings.Options.EMISSIVE_TEXTURES};
    private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());

    public 鬼QualitySettingsOF(鬼Screen guiscreen, GameSettings gamesettings)
    {
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }

    public void initGui()
    {
        this.title = I18n.format("of.options.qualityTitle", new Object[0]);
        this.buttonList.clear();

        for (int i = 0; i < enumOptions.length; ++i)
        {
            GameSettings.Options gamesettings$options = enumOptions[i];
            int j = this.width / 2 - 155 + i % 2 * 160;
            int k = this.height / 6 + 21 * (i / 2) - 12;

            if (!gamesettings$options.getEnumFloat())
            {
                this.buttonList.add(new 鬼OptionButtonOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
            }
            else
            {
                this.buttonList.add(new 鬼OptionSliderOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
            }
        }

        this.buttonList.add(new 鬼Button(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
    }

    protected void actionPerformed(鬼Button guibutton)
    {
        if (guibutton.enabled)
        {
            if (guibutton.id < 200 && guibutton instanceof 鬼OptionButton)
            {
                this.settings.setOptionValue(((鬼OptionButton)guibutton).returnEnumOptions(), 1);
                guibutton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(guibutton.id));
            }

            if (guibutton.id == 200)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(this.prevScreen);
            }

            if (guibutton.id != GameSettings.Options.AA_LEVEL.ordinal())
            {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                this.setWorldAndResolution(this.mc, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
            }
        }
    }

    public void drawScreen(int x, int y, float f)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
        super.drawScreen(x, y, f);
        this.tooltipManager.drawTooltips(x, y, this.buttonList);
    }
}
