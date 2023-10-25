package net.minecraft.potion;

import net.minecraft.util.图像位置;

public class PotionHealth extends Potion
{
    public PotionHealth(int potionID, 图像位置 location, boolean badEffect, int potionColor)
    {
        super(potionID, location, badEffect, potionColor);
    }

    public boolean isInstant()
    {
        return true;
    }

    public boolean isReady(int p_76397_1_, int p_76397_2_)
    {
        return p_76397_1_ >= 1;
    }
}
