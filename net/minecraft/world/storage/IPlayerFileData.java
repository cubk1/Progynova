package net.minecraft.world.storage;

import net.minecraft.entity.player.实体Player;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData
{
    void writePlayerData(实体Player player);

    NBTTagCompound readPlayerData(实体Player player);

    String[] getAvailablePlayerDat();
}
