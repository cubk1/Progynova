package net.minecraft.entity.item;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class 实体MinecartChest extends 实体MinecartContainer
{
    public 实体MinecartChest(World worldIn)
    {
        super(worldIn);
    }

    public 实体MinecartChest(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
        {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.chest), 1, 0.0F);
        }
    }

    public int getSizeInventory()
    {
        return 27;
    }

    public 实体Minecart.EnumMinecartType getMinecartType()
    {
        return 实体Minecart.EnumMinecartType.CHEST;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.chest.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH);
    }

    public int getDefaultDisplayTileOffset()
    {
        return 8;
    }

    public String getGuiID()
    {
        return "minecraft:chest";
    }

    public Container createContainer(InventoryPlayer playerInventory, 实体Player playerIn)
    {
        return new ContainerChest(playerInventory, this, playerIn);
    }
}
