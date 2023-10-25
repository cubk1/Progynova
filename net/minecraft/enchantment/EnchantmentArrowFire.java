package net.minecraft.enchantment;

import net.minecraft.util.图像位置;

public class EnchantmentArrowFire extends Enchantment
{
    public EnchantmentArrowFire(int enchID, 图像位置 enchName, int enchWeight)
    {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.BOW);
        this.setName("arrowFire");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 20;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return 50;
    }

    public int getMaxLevel()
    {
        return 1;
    }
}
