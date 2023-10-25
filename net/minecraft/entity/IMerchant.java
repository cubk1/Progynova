package net.minecraft.entity;

import net.minecraft.entity.player.实体Player;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public interface IMerchant
{
    void setCustomer(实体Player p_70932_1_);

    实体Player getCustomer();

    MerchantRecipeList getRecipes(实体Player p_70934_1_);

    void setRecipes(MerchantRecipeList recipeList);

    void useRecipe(MerchantRecipe recipe);

    void verifySellingItem(ItemStack stack);

    IChatComponent getDisplayName();
}
