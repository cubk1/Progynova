package net.minecraft.world;

import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.util.阻止位置;

public interface IWorldAccess
{
    void markBlockForUpdate(阻止位置 pos);

    void notifyLightSet(阻止位置 pos);

    void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2);

    void playSound(String soundName, double x, double y, double z, float volume, float pitch);

    void playSoundToNearExcept(实体Player except, String soundName, double x, double y, double z, float volume, float pitch);

    void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... parameters);

    void onEntityAdded(实体 实体In);

    void onEntityRemoved(实体 实体In);

    void playRecord(String recordName, 阻止位置 阻止位置In);

    void broadcastSound(int soundID, 阻止位置 pos, int data);

    void playAuxSFX(实体Player player, int sfxType, 阻止位置 阻止位置In, int data);

    void sendBlockBreakProgress(int breakerId, 阻止位置 pos, int progress);
}
