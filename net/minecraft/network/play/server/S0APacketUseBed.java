package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.实体Player;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class S0APacketUseBed implements Packet<INetHandlerPlayClient>
{
    private int playerID;
    private 阻止位置 bedPos;

    public S0APacketUseBed()
    {
    }

    public S0APacketUseBed(实体Player player, 阻止位置 bedPosIn)
    {
        this.playerID = player.getEntityId();
        this.bedPos = bedPosIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.playerID = buf.readVarIntFromBuffer();
        this.bedPos = buf.readBlockPos();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.playerID);
        buf.writeBlockPos(this.bedPos);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUseBed(this);
    }

    public 实体Player getPlayer(World worldIn)
    {
        return (实体Player)worldIn.getEntityByID(this.playerID);
    }

    public 阻止位置 getBedPosition()
    {
        return this.bedPos;
    }
}
