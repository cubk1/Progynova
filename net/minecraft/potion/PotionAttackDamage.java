package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.图像位置;

public class PotionAttackDamage extends Potion
{
    protected PotionAttackDamage(int potionID, 图像位置 location, boolean badEffect, int potionColor)
    {
        super(potionID, location, badEffect, potionColor);
    }

    public double getAttributeModifierAmount(int p_111183_1_, AttributeModifier modifier)
    {
        return this.id == Potion.weakness.id ? (double)(-0.5F * (float)(p_111183_1_ + 1)) : 1.3D * (double)(p_111183_1_ + 1);
    }
}
