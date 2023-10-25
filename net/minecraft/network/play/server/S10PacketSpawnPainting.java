package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.实体Painting;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;

public class S10PacketSpawnPainting implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private 阻止位置 position;
    private EnumFacing facing;
    private String title;

    public S10PacketSpawnPainting()
    {
    }

    public S10PacketSpawnPainting(实体Painting painting)
    {
        this.entityID = painting.getEntityId();
        this.position = painting.getHangingPosition();
        this.facing = painting.facingDirection;
        this.title = painting.art.title;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityID = buf.readVarIntFromBuffer();
        this.title = buf.readStringFromBuffer(实体Painting.EnumArt.field_180001_A);
        this.position = buf.readBlockPos();
        this.facing = EnumFacing.getHorizontal(buf.readUnsignedByte());
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeString(this.title);
        buf.writeBlockPos(this.position);
        buf.writeByte(this.facing.getHorizontalIndex());
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnPainting(this);
    }

    public int getEntityID()
    {
        return this.entityID;
    }

    public 阻止位置 getPosition()
    {
        return this.position;
    }

    public EnumFacing getFacing()
    {
        return this.facing;
    }

    public String getTitle()
    {
        return this.title;
    }
}
