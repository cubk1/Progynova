package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.图像位置;

public class EnchantmentDigging extends Enchantment
{
    protected EnchantmentDigging(int enchID, 图像位置 enchName, int enchWeight)
    {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.DIGGER);
        this.setName("digging");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel()
    {
        return 5;
    }

    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() == Items.shears ? true : super.canApply(stack);
    }
}
