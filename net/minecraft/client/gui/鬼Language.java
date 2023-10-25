package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import net.minecraft.client.我的手艺;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class 鬼Language extends 鬼Screen
{
    protected 鬼Screen parentScreen;
    private 鬼Language.List list;
    private final GameSettings game_settings_3;
    private final LanguageManager languageManager;
    private 鬼OptionButton forceUnicodeFontBtn;
    private 鬼OptionButton confirmSettingsBtn;

    public 鬼Language(鬼Screen screen, GameSettings gameSettingsObj, LanguageManager manager)
    {
        this.parentScreen = screen;
        this.game_settings_3 = gameSettingsObj;
        this.languageManager = manager;
    }

    public void initGui()
    {
        this.buttonList.add(this.forceUnicodeFontBtn = new 鬼OptionButton(100, this.width / 2 - 155, this.height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
        this.buttonList.add(this.confirmSettingsBtn = new 鬼OptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done", new Object[0])));
        this.list = new 鬼Language.List(this.mc);
        this.list.registerScrollButtons(7, 8);
    }

    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 5:
                    break;

                case 6:
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;

                case 100:
                    if (button instanceof 鬼OptionButton)
                    {
                        this.game_settings_3.setOptionValue(((鬼OptionButton)button).returnEnumOptions(), 1);
                        button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                        比例解析 scaledresolution = new 比例解析(this.mc);
                        int i = scaledresolution.getScaledWidth();
                        int j = scaledresolution.得到高度();
                        this.setWorldAndResolution(this.mc, i, j);
                    }

                    break;

                default:
                    this.list.actionPerformed(button);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", this.width / 2, this.height - 56, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class List extends GuiSlot
    {
        private final java.util.List<String> langCodeList = Lists.<String>newArrayList();
        private final Map<String, Language> languageMap = Maps.<String, Language>newHashMap();

        public List(我的手艺 mcIn)
        {
            super(mcIn, 鬼Language.this.width, 鬼Language.this.height, 32, 鬼Language.this.height - 65 + 4, 18);

            for (Language language : 鬼Language.this.languageManager.getLanguages())
            {
                this.languageMap.put(language.getLanguageCode(), language);
                this.langCodeList.add(language.getLanguageCode());
            }
        }

        protected int getSize()
        {
            return this.langCodeList.size();
        }

        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            Language language = (Language)this.languageMap.get(this.langCodeList.get(slotIndex));
            鬼Language.this.languageManager.setCurrentLanguage(language);
            鬼Language.this.game_settings_3.language = language.getLanguageCode();
            this.mc.refreshResources();
            鬼Language.this.fontRendererObj.setUnicodeFlag(鬼Language.this.languageManager.isCurrentLocaleUnicode() || 鬼Language.this.game_settings_3.forceUnicodeFont);
            鬼Language.this.fontRendererObj.setBidiFlag(鬼Language.this.languageManager.isCurrentLanguageBidirectional());
            鬼Language.this.confirmSettingsBtn.displayString = I18n.format("gui.done", new Object[0]);
            鬼Language.this.forceUnicodeFontBtn.displayString = 鬼Language.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
            鬼Language.this.game_settings_3.saveOptions();
        }

        protected boolean isSelected(int slotIndex)
        {
            return ((String)this.langCodeList.get(slotIndex)).equals(鬼Language.this.languageManager.getCurrentLanguage().getLanguageCode());
        }

        protected int getContentHeight()
        {
            return this.getSize() * 18;
        }

        protected void drawBackground()
        {
            鬼Language.this.drawDefaultBackground();
        }

        protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
        {
            鬼Language.this.fontRendererObj.setBidiFlag(true);
            鬼Language.this.drawCenteredString(鬼Language.this.fontRendererObj, ((Language)this.languageMap.get(this.langCodeList.get(entryID))).toString(), this.width / 2, p_180791_3_ + 1, 16777215);
            鬼Language.this.fontRendererObj.setBidiFlag(鬼Language.this.languageManager.getCurrentLanguage().isBidirectional());
        }
    }
}
