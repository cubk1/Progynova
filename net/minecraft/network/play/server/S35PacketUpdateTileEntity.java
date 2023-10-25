package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.阻止位置;

public class S35PacketUpdateTileEntity implements Packet<INetHandlerPlayClient>
{
    private 阻止位置 阻止位置;
    private int metadata;
    private NBTTagCompound nbt;

    public S35PacketUpdateTileEntity()
    {
    }

    public S35PacketUpdateTileEntity(阻止位置 阻止位置In, int metadataIn, NBTTagCompound nbtIn)
    {
        this.阻止位置 = 阻止位置In;
        this.metadata = metadataIn;
        this.nbt = nbtIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.阻止位置 = buf.readBlockPos();
        this.metadata = buf.readUnsignedByte();
        this.nbt = buf.readNBTTagCompoundFromBuffer();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.阻止位置);
        buf.writeByte((byte)this.metadata);
        buf.writeNBTTagCompoundToBuffer(this.nbt);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateTileEntity(this);
    }

    public 阻止位置 getPos()
    {
        return this.阻止位置;
    }

    public int getTileEntityType()
    {
        return this.metadata;
    }

    public NBTTagCompound getNbtCompound()
    {
        return this.nbt;
    }
}
