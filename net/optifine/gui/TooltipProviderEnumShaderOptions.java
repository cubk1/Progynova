package net.optifine.gui;

import java.awt.Rectangle;
import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.gui.鬼Screen;
import net.optifine.shaders.config.EnumShaderOption;
import net.optifine.shaders.gui.鬼ButtonDownloadShaders;
import net.optifine.shaders.gui.鬼ButtonEnumShaderOption;

public class TooltipProviderEnumShaderOptions implements TooltipProvider
{
    public Rectangle getTooltipBounds(鬼Screen guiScreen, int x, int y)
    {
        int i = guiScreen.width - 450;
        int j = 35;

        if (i < 10)
        {
            i = 10;
        }

        if (y <= j + 94)
        {
            j += 100;
        }

        int k = i + 150 + 150;
        int l = j + 84 + 10;
        return new Rectangle(i, j, k - i, l - j);
    }

    public boolean isRenderBorder()
    {
        return true;
    }

    public String[] getTooltipLines(鬼Button btn, int width)
    {
        if (btn instanceof 鬼ButtonDownloadShaders)
        {
            return TooltipProviderOptions.getTooltipLines("of.options.shaders.DOWNLOAD");
        }
        else if (!(btn instanceof 鬼ButtonEnumShaderOption))
        {
            return null;
        }
        else
        {
            鬼ButtonEnumShaderOption guibuttonenumshaderoption = (鬼ButtonEnumShaderOption)btn;
            EnumShaderOption enumshaderoption = guibuttonenumshaderoption.getEnumShaderOption();
            String[] astring = this.getTooltipLines(enumshaderoption);
            return astring;
        }
    }

    private String[] getTooltipLines(EnumShaderOption option)
    {
        return TooltipProviderOptions.getTooltipLines(option.getResourceKey());
    }
}
