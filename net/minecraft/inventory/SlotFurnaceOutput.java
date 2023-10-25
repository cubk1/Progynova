package net.minecraft.inventory;

import net.minecraft.entity.item.实体XPOrb;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;

public class SlotFurnaceOutput extends Slot
{
    private 实体Player thePlayer;
    private int field_75228_b;

    public SlotFurnaceOutput(实体Player player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
    }

    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    public ItemStack decrStackSize(int amount)
    {
        if (this.getHasStack())
        {
            this.field_75228_b += Math.min(amount, this.getStack().stackSize);
        }

        return super.decrStackSize(amount);
    }

    public void onPickupFromSlot(实体Player playerIn, ItemStack stack)
    {
        this.onCrafting(stack);
        super.onPickupFromSlot(playerIn, stack);
    }

    protected void onCrafting(ItemStack stack, int amount)
    {
        this.field_75228_b += amount;
        this.onCrafting(stack);
    }

    protected void onCrafting(ItemStack stack)
    {
        stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);

        if (!this.thePlayer.worldObj.isRemote)
        {
            int i = this.field_75228_b;
            float f = FurnaceRecipes.instance().getSmeltingExperience(stack);

            if (f == 0.0F)
            {
                i = 0;
            }
            else if (f < 1.0F)
            {
                int j = MathHelper.floor_float((float)i * f);

                if (j < MathHelper.ceiling_float_int((float)i * f) && Math.random() < (double)((float)i * f - (float)j))
                {
                    ++j;
                }

                i = j;
            }

            while (i > 0)
            {
                int k = 实体XPOrb.getXPSplit(i);
                i -= k;
                this.thePlayer.worldObj.spawnEntityInWorld(new 实体XPOrb(this.thePlayer.worldObj, this.thePlayer.X坐标, this.thePlayer.Y坐标 + 0.5D, this.thePlayer.Z坐标 + 0.5D, k));
            }
        }

        this.field_75228_b = 0;

        if (stack.getItem() == Items.iron_ingot)
        {
            this.thePlayer.triggerAchievement(AchievementList.acquireIron);
        }

        if (stack.getItem() == Items.cooked_fish)
        {
            this.thePlayer.triggerAchievement(AchievementList.cookFish);
        }
    }
}
