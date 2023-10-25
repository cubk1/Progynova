package net.minecraft.enchantment;

import net.minecraft.util.图像位置;

public class EnchantmentFireAspect extends Enchantment
{
    protected EnchantmentFireAspect(int enchID, 图像位置 enchName, int enchWeight)
    {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.WEAPON);
        this.setName("fire");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 10 + 20 * (enchantmentLevel - 1);
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel()
    {
        return 2;
    }
}
