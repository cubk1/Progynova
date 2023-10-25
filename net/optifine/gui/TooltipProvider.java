package net.optifine.gui;

import java.awt.Rectangle;
import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.gui.鬼Screen;

public interface TooltipProvider
{
    Rectangle getTooltipBounds(鬼Screen var1, int var2, int var3);

    String[] getTooltipLines(鬼Button var1, int var2);

    boolean isRenderBorder();
}
