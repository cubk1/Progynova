package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.stream.鬼StreamOptions;
import net.minecraft.client.gui.stream.鬼StreamUnavailable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.IStream;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.EnumDifficulty;

public class 鬼Options extends 鬼Screen implements GuiYesNoCallback
{
    private static final GameSettings.Options[] field_146440_f = new GameSettings.Options[] {GameSettings.Options.FOV};
    private final 鬼Screen field_146441_g;
    private final GameSettings game_settings_1;
    private 鬼Button field_175357_i;
    private 鬼LockIconButton field_175356_r;
    protected String field_146442_a = "Options";

    public 鬼Options(鬼Screen p_i1046_1_, GameSettings p_i1046_2_)
    {
        this.field_146441_g = p_i1046_1_;
        this.game_settings_1 = p_i1046_2_;
    }

    public void initGui()
    {
        int i = 0;
        this.field_146442_a = I18n.format("options.title", new Object[0]);

        for (GameSettings.Options gamesettings$options : field_146440_f)
        {
            if (gamesettings$options.getEnumFloat())
            {
                this.buttonList.add(new 鬼OptionSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options));
            }
            else
            {
                鬼OptionButton guioptionbutton = new 鬼OptionButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options, this.game_settings_1.getKeyBinding(gamesettings$options));
                this.buttonList.add(guioptionbutton);
            }

            ++i;
        }

        if (this.mc.宇轩の世界 != null)
        {
            EnumDifficulty enumdifficulty = this.mc.宇轩の世界.getDifficulty();
            this.field_175357_i = new 鬼Button(108, this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), 150, 20, this.func_175355_a(enumdifficulty));
            this.buttonList.add(this.field_175357_i);

            if (this.mc.isSingleplayer() && !this.mc.宇轩の世界.getWorldInfo().isHardcoreModeEnabled())
            {
                this.field_175357_i.setWidth(this.field_175357_i.getButtonWidth() - 20);
                this.field_175356_r = new 鬼LockIconButton(109, this.field_175357_i.xPosition + this.field_175357_i.getButtonWidth(), this.field_175357_i.yPosition);
                this.buttonList.add(this.field_175356_r);
                this.field_175356_r.func_175229_b(this.mc.宇轩の世界.getWorldInfo().isDifficultyLocked());
                this.field_175356_r.enabled = !this.field_175356_r.func_175230_c();
                this.field_175357_i.enabled = !this.field_175356_r.func_175230_c();
            }
            else
            {
                this.field_175357_i.enabled = false;
            }
        }
        else
        {
            鬼OptionButton guioptionbutton1 = new 鬼OptionButton(GameSettings.Options.REALMS_NOTIFICATIONS.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), GameSettings.Options.REALMS_NOTIFICATIONS, this.game_settings_1.getKeyBinding(GameSettings.Options.REALMS_NOTIFICATIONS));
            this.buttonList.add(guioptionbutton1);
        }

        this.buttonList.add(new 鬼Button(110, this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation", new Object[0])));
        this.buttonList.add(new 鬼Button(8675309, this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, "Super Secret Settings...")
        {
            public void playPressSound(SoundHandler soundHandlerIn)
            {
                SoundEventAccessorComposite soundeventaccessorcomposite = soundHandlerIn.getRandomSoundFromCategories(new SoundCategory[] {SoundCategory.ANIMALS, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER});

                if (soundeventaccessorcomposite != null)
                {
                    soundHandlerIn.playSound(PositionedSoundRecord.create(soundeventaccessorcomposite.getSoundEventLocation(), 0.5F));
                }
            }
        });
        this.buttonList.add(new 鬼Button(106, this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, I18n.format("options.sounds", new Object[0])));
        this.buttonList.add(new 鬼Button(107, this.width / 2 + 5, this.height / 6 + 72 - 6, 150, 20, I18n.format("options.stream", new Object[0])));
        this.buttonList.add(new 鬼Button(101, this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20, I18n.format("options.video", new Object[0])));
        this.buttonList.add(new 鬼Button(100, this.width / 2 + 5, this.height / 6 + 96 - 6, 150, 20, I18n.format("options.controls", new Object[0])));
        this.buttonList.add(new 鬼Button(102, this.width / 2 - 155, this.height / 6 + 120 - 6, 150, 20, I18n.format("options.language", new Object[0])));
        this.buttonList.add(new 鬼Button(103, this.width / 2 + 5, this.height / 6 + 120 - 6, 150, 20, I18n.format("options.chat.title", new Object[0])));
        this.buttonList.add(new 鬼Button(105, this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20, I18n.format("options.resourcepack", new Object[0])));
        this.buttonList.add(new 鬼Button(104, this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20, I18n.format("options.snooper.view", new Object[0])));
        this.buttonList.add(new 鬼Button(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done", new Object[0])));
    }

    public String func_175355_a(EnumDifficulty p_175355_1_)
    {
        IChatComponent ichatcomponent = new 交流组分文本("");
        ichatcomponent.appendSibling(new ChatComponentTranslation("options.difficulty", new Object[0]));
        ichatcomponent.appendText(": ");
        ichatcomponent.appendSibling(new ChatComponentTranslation(p_175355_1_.getDifficultyResourceKey(), new Object[0]));
        return ichatcomponent.getFormattedText();
    }

    public void confirmClicked(boolean result, int id)
    {
        this.mc.displayGuiScreen(this);

        if (id == 109 && result && this.mc.宇轩の世界 != null)
        {
            this.mc.宇轩の世界.getWorldInfo().setDifficultyLocked(true);
            this.field_175356_r.func_175229_b(true);
            this.field_175356_r.enabled = false;
            this.field_175357_i.enabled = false;
        }
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id < 100 && button instanceof 鬼OptionButton)
            {
                GameSettings.Options gamesettings$options = ((鬼OptionButton)button).returnEnumOptions();
                this.game_settings_1.setOptionValue(gamesettings$options, 1);
                button.displayString = this.game_settings_1.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }

            if (button.id == 108)
            {
                this.mc.宇轩の世界.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(this.mc.宇轩の世界.getDifficulty().getDifficultyId() + 1));
                this.field_175357_i.displayString = this.func_175355_a(this.mc.宇轩の世界.getDifficulty());
            }

            if (button.id == 109)
            {
                this.mc.displayGuiScreen(new 鬼YesNo(this, (new ChatComponentTranslation("difficulty.lock.title", new Object[0])).getFormattedText(), (new ChatComponentTranslation("difficulty.lock.question", new Object[] {new ChatComponentTranslation(this.mc.宇轩の世界.getWorldInfo().getDifficulty().getDifficultyResourceKey(), new Object[0])})).getFormattedText(), 109));
            }

            if (button.id == 110)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(new 鬼CustomizeSkin(this));
            }

            if (button.id == 8675309)
            {
                this.mc.entityRenderer.activateNextShader();
            }

            if (button.id == 101)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(new 鬼VideoSettings(this, this.game_settings_1));
            }

            if (button.id == 100)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(new 鬼Controls(this, this.game_settings_1));
            }

            if (button.id == 102)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(new 鬼Language(this, this.game_settings_1, this.mc.getLanguageManager()));
            }

            if (button.id == 103)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(new ScreenChatOptions(this, this.game_settings_1));
            }

            if (button.id == 104)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(new 鬼Snooper(this, this.game_settings_1));
            }

            if (button.id == 200)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(this.field_146441_g);
            }

            if (button.id == 105)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(new 鬼ScreenResourcePacks(this));
            }

            if (button.id == 106)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(new 鬼ScreenOptionsSounds(this, this.game_settings_1));
            }

            if (button.id == 107)
            {
                this.mc.游戏一窝.saveOptions();
                IStream istream = this.mc.getTwitchStream();

                if (istream.func_152936_l() && istream.func_152928_D())
                {
                    this.mc.displayGuiScreen(new 鬼StreamOptions(this, this.game_settings_1));
                }
                else
                {
                    鬼StreamUnavailable.func_152321_a(this);
                }
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146442_a, this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
