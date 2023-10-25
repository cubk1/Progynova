package net.minecraft.item;

import net.minecraft.entity.player.实体Player;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class ItemMapBase extends Item
{
    public boolean isMap()
    {
        return true;
    }

    public Packet createMapDataPacket(ItemStack stack, World worldIn, 实体Player player)
    {
        return null;
    }
}
