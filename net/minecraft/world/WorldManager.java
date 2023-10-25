package net.minecraft.world;

import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.entity.实体;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.阻止位置;

public class WorldManager implements IWorldAccess
{
    private MinecraftServer mcServer;
    private WorldServer theWorldServer;

    public WorldManager(MinecraftServer mcServerIn, WorldServer worldServerIn)
    {
        this.mcServer = mcServerIn;
        this.theWorldServer = worldServerIn;
    }

    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... parameters)
    {
    }

    public void onEntityAdded(实体 实体In)
    {
        this.theWorldServer.getEntityTracker().trackEntity(实体In);
    }

    public void onEntityRemoved(实体 实体In)
    {
        this.theWorldServer.getEntityTracker().untrackEntity(实体In);
        this.theWorldServer.getScoreboard().func_181140_a(实体In);
    }

    public void playSound(String soundName, double x, double y, double z, float volume, float pitch)
    {
        this.mcServer.getConfigurationManager().sendToAllNear(x, y, z, volume > 1.0F ? (double)(16.0F * volume) : 16.0D, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }

    public void playSoundToNearExcept(实体Player except, String soundName, double x, double y, double z, float volume, float pitch)
    {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(except, x, y, z, volume > 1.0F ? (double)(16.0F * volume) : 16.0D, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }

    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
    }

    public void markBlockForUpdate(阻止位置 pos)
    {
        this.theWorldServer.getPlayerManager().markBlockForUpdate(pos);
    }

    public void notifyLightSet(阻止位置 pos)
    {
    }

    public void playRecord(String recordName, 阻止位置 阻止位置In)
    {
    }

    public void playAuxSFX(实体Player player, int sfxType, 阻止位置 阻止位置In, int data)
    {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(player, (double) 阻止位置In.getX(), (double) 阻止位置In.getY(), (double) 阻止位置In.getZ(), 64.0D, this.theWorldServer.provider.getDimensionId(), new S28PacketEffect(sfxType, 阻止位置In, data, false));
    }

    public void broadcastSound(int soundID, 阻止位置 pos, int data)
    {
        this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(soundID, pos, data, true));
    }

    public void sendBlockBreakProgress(int breakerId, 阻止位置 pos, int progress)
    {
        for (实体PlayerMP entityplayermp : this.mcServer.getConfigurationManager().getPlayerList())
        {
            if (entityplayermp != null && entityplayermp.worldObj == this.theWorldServer && entityplayermp.getEntityId() != breakerId)
            {
                double d0 = (double)pos.getX() - entityplayermp.X坐标;
                double d1 = (double)pos.getY() - entityplayermp.Y坐标;
                double d2 = (double)pos.getZ() - entityplayermp.Z坐标;

                if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D)
                {
                    entityplayermp.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(breakerId, pos, progress));
                }
            }
        }
    }
}
