package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.阻止位置;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class S33PacketUpdateSign implements Packet<INetHandlerPlayClient>
{
    private World world;
    private 阻止位置 阻止位置;
    private IChatComponent[] lines;

    public S33PacketUpdateSign()
    {
    }

    public S33PacketUpdateSign(World worldIn, 阻止位置 阻止位置In, IChatComponent[] linesIn)
    {
        this.world = worldIn;
        this.阻止位置 = 阻止位置In;
        this.lines = new IChatComponent[] {linesIn[0], linesIn[1], linesIn[2], linesIn[3]};
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.阻止位置 = buf.readBlockPos();
        this.lines = new IChatComponent[4];

        for (int i = 0; i < 4; ++i)
        {
            this.lines[i] = buf.readChatComponent();
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.阻止位置);

        for (int i = 0; i < 4; ++i)
        {
            buf.writeChatComponent(this.lines[i]);
        }
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateSign(this);
    }

    public 阻止位置 getPos()
    {
        return this.阻止位置;
    }

    public IChatComponent[] getLines()
    {
        return this.lines;
    }
}
