package net.minecraft.item;

import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemSoup extends ItemFood
{
    public ItemSoup(int healAmount)
    {
        super(healAmount, false);
        this.setMaxStackSize(1);
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, 实体Player playerIn)
    {
        super.onItemUseFinish(stack, worldIn, playerIn);
        return new ItemStack(Items.bowl);
    }
}
