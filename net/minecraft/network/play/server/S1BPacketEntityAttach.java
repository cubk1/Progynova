package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.实体;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1BPacketEntityAttach implements Packet<INetHandlerPlayClient>
{
    private int leash;
    private int entityId;
    private int vehicleEntityId;

    public S1BPacketEntityAttach()
    {
    }

    public S1BPacketEntityAttach(int leashIn, 实体 实体In, 实体 vehicle)
    {
        this.leash = leashIn;
        this.entityId = 实体In.getEntityId();
        this.vehicleEntityId = vehicle != null ? vehicle.getEntityId() : -1;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readInt();
        this.vehicleEntityId = buf.readInt();
        this.leash = buf.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.entityId);
        buf.writeInt(this.vehicleEntityId);
        buf.writeByte(this.leash);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityAttach(this);
    }

    public int getLeash()
    {
        return this.leash;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public int getVehicleEntityId()
    {
        return this.vehicleEntityId;
    }
}
