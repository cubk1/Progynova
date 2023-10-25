package net.optifine.gui;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.*;
import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.鬼;
import net.minecraft.client.gui.鬼Screen;

public class TooltipManager
{
    private 鬼Screen guiScreen;
    private TooltipProvider tooltipProvider;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public TooltipManager(鬼Screen guiScreen, TooltipProvider tooltipProvider)
    {
        this.guiScreen = guiScreen;
        this.tooltipProvider = tooltipProvider;
    }

    public void drawTooltips(int x, int y, List buttonList)
    {
        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5)
        {
            int i = 700;

            if (System.currentTimeMillis() >= this.mouseStillTime + (long)i)
            {
                鬼Button guibutton = 鬼ScreenOF.getSelectedButton(x, y, buttonList);

                if (guibutton != null)
                {
                    Rectangle rectangle = this.tooltipProvider.getTooltipBounds(this.guiScreen, x, y);
                    String[] astring = this.tooltipProvider.getTooltipLines(guibutton, rectangle.width);

                    if (astring != null)
                    {
                        if (astring.length > 8)
                        {
                            astring = (String[])Arrays.copyOf(astring, 8);
                            astring[astring.length - 1] = astring[astring.length - 1] + " ...";
                        }

                        if (this.tooltipProvider.isRenderBorder())
                        {
                            int j = -528449408;
                            this.drawRectBorder(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, j);
                        }

                        鬼.drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, -536870912);

                        for (int l = 0; l < astring.length; ++l)
                        {
                            String s = astring[l];
                            int k = 14540253;

                            if (s.endsWith("!"))
                            {
                                k = 16719904;
                            }

                            FontRenderer fontrenderer = 我的手艺.得到我的手艺().字体渲染员;
                            fontrenderer.绘制纵梁带心理阴影(s, (float)(rectangle.x + 5), (float)(rectangle.y + 5 + l * 11), k);
                        }
                    }
                }
            }
        }
        else
        {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }

    private void drawRectBorder(int x1, int y1, int x2, int y2, int col)
    {
        鬼.drawRect(x1, y1 - 1, x2, y1, col);
        鬼.drawRect(x1, y2, x2, y2 + 1, col);
        鬼.drawRect(x1 - 1, y1, x1, y2, col);
        鬼.drawRect(x2, y1, x2 + 1, y2, col);
    }
}
