package net.optifine.shaders.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.gui.鬼Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
import net.optifine.Lang;
import net.optifine.gui.鬼ScreenOF;
import net.optifine.gui.TooltipManager;
import net.optifine.gui.TooltipProviderShaderOptions;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.ShaderOption;
import net.optifine.shaders.config.ShaderOptionProfile;
import net.optifine.shaders.config.ShaderOptionScreen;

public class 鬼ShaderOptions extends 鬼ScreenOF
{
    private 鬼Screen prevScreen;
    protected String title;
    private GameSettings settings;
    private TooltipManager tooltipManager;
    private String screenName;
    private String screenText;
    private boolean changed;
    public static final String OPTION_PROFILE = "<profile>";
    public static final String OPTION_EMPTY = "<empty>";
    public static final String OPTION_REST = "*";

    public 鬼ShaderOptions(鬼Screen guiscreen, GameSettings gamesettings)
    {
        this.tooltipManager = new TooltipManager(this, new TooltipProviderShaderOptions());
        this.screenName = null;
        this.screenText = null;
        this.changed = false;
        this.title = "Shader Options";
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }

    public 鬼ShaderOptions(鬼Screen guiscreen, GameSettings gamesettings, String screenName)
    {
        this(guiscreen, gamesettings);
        this.screenName = screenName;

        if (screenName != null)
        {
            this.screenText = Shaders.translate("screen." + screenName, screenName);
        }
    }

    public void initGui()
    {
        this.title = I18n.format("of.options.shaderOptionsTitle", new Object[0]);
        int i = 100;
        int j = 0;
        int k = 30;
        int l = 20;
        int i1 = 120;
        int j1 = 20;
        int k1 = Shaders.getShaderPackColumns(this.screenName, 2);
        ShaderOption[] ashaderoption = Shaders.getShaderPackOptions(this.screenName);

        if (ashaderoption != null)
        {
            int l1 = MathHelper.ceiling_double_int((double)ashaderoption.length / 9.0D);

            if (k1 < l1)
            {
                k1 = l1;
            }

            for (int i2 = 0; i2 < ashaderoption.length; ++i2)
            {
                ShaderOption shaderoption = ashaderoption[i2];

                if (shaderoption != null && shaderoption.isVisible())
                {
                    int j2 = i2 % k1;
                    int k2 = i2 / k1;
                    int l2 = Math.min(this.width / k1, 200);
                    j = (this.width - l2 * k1) / 2;
                    int i3 = j2 * l2 + 5 + j;
                    int j3 = k + k2 * l;
                    int k3 = l2 - 10;
                    String s = getButtonText(shaderoption, k3);
                    鬼ButtonShaderOption guibuttonshaderoption;

                    if (Shaders.isShaderPackOptionSlider(shaderoption.getName()))
                    {
                        guibuttonshaderoption = new 鬼SliderShaderOption(i + i2, i3, j3, k3, j1, shaderoption, s);
                    }
                    else
                    {
                        guibuttonshaderoption = new 鬼ButtonShaderOption(i + i2, i3, j3, k3, j1, shaderoption, s);
                    }

                    guibuttonshaderoption.enabled = shaderoption.isEnabled();
                    this.buttonList.add(guibuttonshaderoption);
                }
            }
        }

        this.buttonList.add(new 鬼Button(201, this.width / 2 - i1 - 20, this.height / 6 + 168 + 11, i1, j1, I18n.format("controls.reset", new Object[0])));
        this.buttonList.add(new 鬼Button(200, this.width / 2 + 20, this.height / 6 + 168 + 11, i1, j1, I18n.format("gui.done", new Object[0])));
    }

    public static String getButtonText(ShaderOption so, int btnWidth)
    {
        String s = so.getNameText();

        if (so instanceof ShaderOptionScreen)
        {
            ShaderOptionScreen shaderoptionscreen = (ShaderOptionScreen)so;
            return s + "...";
        }
        else
        {
            FontRenderer fontrenderer = Config.getMinecraft().字体渲染员;

            for (int i = fontrenderer.getStringWidth(": " + Lang.getOff()) + 5; fontrenderer.getStringWidth(s) + i >= btnWidth && s.length() > 0; s = s.substring(0, s.length() - 1))
            {
                ;
            }

            String s1 = so.isChanged() ? so.getValueColor(so.getValue()) : "";
            String s2 = so.getValueText(so.getValue());
            return s + ": " + s1 + s2;
        }
    }

    protected void actionPerformed(鬼Button guibutton)
    {
        if (guibutton.enabled)
        {
            if (guibutton.id < 200 && guibutton instanceof 鬼ButtonShaderOption)
            {
                鬼ButtonShaderOption guibuttonshaderoption = (鬼ButtonShaderOption)guibutton;
                ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();

                if (shaderoption instanceof ShaderOptionScreen)
                {
                    String s = shaderoption.getName();
                    鬼ShaderOptions guishaderoptions = new 鬼ShaderOptions(this, this.settings, s);
                    this.mc.displayGuiScreen(guishaderoptions);
                    return;
                }

                if (isShiftKeyDown())
                {
                    shaderoption.resetValue();
                }
                else if (guibuttonshaderoption.isSwitchable())
                {
                    shaderoption.nextValue();
                }

                this.updateAllButtons();
                this.changed = true;
            }

            if (guibutton.id == 201)
            {
                ShaderOption[] ashaderoption = Shaders.getChangedOptions(Shaders.getShaderPackOptions());

                for (int i = 0; i < ashaderoption.length; ++i)
                {
                    ShaderOption shaderoption1 = ashaderoption[i];
                    shaderoption1.resetValue();
                    this.changed = true;
                }

                this.updateAllButtons();
            }

            if (guibutton.id == 200)
            {
                if (this.changed)
                {
                    Shaders.saveShaderPackOptions();
                    this.changed = false;
                    Shaders.uninit();
                }

                this.mc.displayGuiScreen(this.prevScreen);
            }
        }
    }

    protected void actionPerformedRightClick(鬼Button btn)
    {
        if (btn instanceof 鬼ButtonShaderOption)
        {
            鬼ButtonShaderOption guibuttonshaderoption = (鬼ButtonShaderOption)btn;
            ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();

            if (isShiftKeyDown())
            {
                shaderoption.resetValue();
            }
            else if (guibuttonshaderoption.isSwitchable())
            {
                shaderoption.prevValue();
            }

            this.updateAllButtons();
            this.changed = true;
        }
    }

    public void onGuiClosed()
    {
        super.onGuiClosed();

        if (this.changed)
        {
            Shaders.saveShaderPackOptions();
            this.changed = false;
            Shaders.uninit();
        }
    }

    private void updateAllButtons()
    {
        for (鬼Button guibutton : this.buttonList)
        {
            if (guibutton instanceof 鬼ButtonShaderOption)
            {
                鬼ButtonShaderOption guibuttonshaderoption = (鬼ButtonShaderOption)guibutton;
                ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();

                if (shaderoption instanceof ShaderOptionProfile)
                {
                    ShaderOptionProfile shaderoptionprofile = (ShaderOptionProfile)shaderoption;
                    shaderoptionprofile.updateProfile();
                }

                guibuttonshaderoption.displayString = getButtonText(shaderoption, guibuttonshaderoption.getButtonWidth());
                guibuttonshaderoption.valueChanged();
            }
        }
    }

    public void drawScreen(int x, int y, float f)
    {
        this.drawDefaultBackground();

        if (this.screenText != null)
        {
            this.drawCenteredString(this.fontRendererObj, this.screenText, this.width / 2, 15, 16777215);
        }
        else
        {
            this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
        }

        super.drawScreen(x, y, f);
        this.tooltipManager.drawTooltips(x, y, this.buttonList);
    }
}
