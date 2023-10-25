package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class ScreenChatOptions extends 鬼Screen
{
    private static final GameSettings.Options[] field_146399_a = new GameSettings.Options[] {GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO};
    private final 鬼Screen parentScreen;
    private final GameSettings game_settings;
    private String field_146401_i;

    public ScreenChatOptions(鬼Screen parentScreenIn, GameSettings gameSettingsIn)
    {
        this.parentScreen = parentScreenIn;
        this.game_settings = gameSettingsIn;
    }

    public void initGui()
    {
        int i = 0;
        this.field_146401_i = I18n.format("options.chat.title", new Object[0]);

        for (GameSettings.Options gamesettings$options : field_146399_a)
        {
            if (gamesettings$options.getEnumFloat())
            {
                this.buttonList.add(new 鬼OptionSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), gamesettings$options));
            }
            else
            {
                this.buttonList.add(new 鬼OptionButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), gamesettings$options, this.game_settings.getKeyBinding(gamesettings$options)));
            }

            ++i;
        }

        this.buttonList.add(new 鬼Button(200, this.width / 2 - 100, this.height / 6 + 120, I18n.format("gui.done", new Object[0])));
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id < 100 && button instanceof 鬼OptionButton)
            {
                this.game_settings.setOptionValue(((鬼OptionButton)button).returnEnumOptions(), 1);
                button.displayString = this.game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }

            if (button.id == 200)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146401_i, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
