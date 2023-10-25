package net.minecraft.potion;

import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.util.图像位置;

public class PotionHealthBoost extends Potion
{
    public PotionHealthBoost(int potionID, 图像位置 location, boolean badEffect, int potionColor)
    {
        super(potionID, location, badEffect, potionColor);
    }

    public void removeAttributesModifiersFromEntity(实体LivingBase entityLivingBaseIn, BaseAttributeMap p_111187_2_, int amplifier)
    {
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, p_111187_2_, amplifier);

        if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth())
        {
            entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
        }
    }
}
