package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.阻止位置;

public class S05PacketSpawnPosition implements Packet<INetHandlerPlayClient>
{
    private 阻止位置 spawn阻止位置;

    public S05PacketSpawnPosition()
    {
    }

    public S05PacketSpawnPosition(阻止位置 spawn阻止位置In)
    {
        this.spawn阻止位置 = spawn阻止位置In;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.spawn阻止位置 = buf.readBlockPos();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.spawn阻止位置);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnPosition(this);
    }

    public 阻止位置 getSpawnPos()
    {
        return this.spawn阻止位置;
    }
}
