package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.optifine.gui.鬼ButtonOF;
import net.optifine.gui.鬼ScreenCapeOF;

public class 鬼CustomizeSkin extends 鬼Screen
{
    private final 鬼Screen parentScreen;
    private String title;

    public 鬼CustomizeSkin(鬼Screen parentScreenIn)
    {
        this.parentScreen = parentScreenIn;
    }

    public void initGui()
    {
        int i = 0;
        this.title = I18n.format("options.skinCustomisation.title", new Object[0]);

        for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values())
        {
            this.buttonList.add(new 鬼CustomizeSkin.ButtonPart(enumplayermodelparts.getPartId(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, enumplayermodelparts));
            ++i;
        }

        if (i % 2 == 1)
        {
            ++i;
        }

        this.buttonList.add(new 鬼ButtonOF(210, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format("of.options.skinCustomisation.ofCape", new Object[0])));
        i = i + 2;
        this.buttonList.add(new 鬼Button(200, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format("gui.done", new Object[0])));
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 210)
            {
                this.mc.displayGuiScreen(new 鬼ScreenCapeOF(this));
            }

            if (button.id == 200)
            {
                this.mc.游戏一窝.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button instanceof 鬼CustomizeSkin.ButtonPart)
            {
                EnumPlayerModelParts enumplayermodelparts = ((鬼CustomizeSkin.ButtonPart)button).playerModelParts;
                this.mc.游戏一窝.switchModelPartEnabled(enumplayermodelparts);
                button.displayString = this.func_175358_a(enumplayermodelparts);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String func_175358_a(EnumPlayerModelParts playerModelParts)
    {
        String s;

        if (this.mc.游戏一窝.getModelParts().contains(playerModelParts))
        {
            s = I18n.format("options.on", new Object[0]);
        }
        else
        {
            s = I18n.format("options.off", new Object[0]);
        }

        return playerModelParts.func_179326_d().getFormattedText() + ": " + s;
    }

    class ButtonPart extends 鬼Button
    {
        private final EnumPlayerModelParts playerModelParts;

        private ButtonPart(int p_i45514_2_, int p_i45514_3_, int p_i45514_4_, int p_i45514_5_, int p_i45514_6_, EnumPlayerModelParts playerModelParts)
        {
            super(p_i45514_2_, p_i45514_3_, p_i45514_4_, p_i45514_5_, p_i45514_6_, 鬼CustomizeSkin.this.func_175358_a(playerModelParts));
            this.playerModelParts = playerModelParts;
        }
    }
}
