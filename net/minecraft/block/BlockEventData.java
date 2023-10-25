package net.minecraft.block;

import net.minecraft.util.阻止位置;

public class BlockEventData
{
    private 阻止位置 position;
    private Block blockType;
    private int eventID;
    private int eventParameter;

    public BlockEventData(阻止位置 pos, Block blockType, int eventId, int p_i45756_4_)
    {
        this.position = pos;
        this.eventID = eventId;
        this.eventParameter = p_i45756_4_;
        this.blockType = blockType;
    }

    public 阻止位置 getPosition()
    {
        return this.position;
    }

    public int getEventID()
    {
        return this.eventID;
    }

    public int getEventParameter()
    {
        return this.eventParameter;
    }

    public Block getBlock()
    {
        return this.blockType;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof BlockEventData))
        {
            return false;
        }
        else
        {
            BlockEventData blockeventdata = (BlockEventData)p_equals_1_;
            return this.position.equals(blockeventdata.position) && this.eventID == blockeventdata.eventID && this.eventParameter == blockeventdata.eventParameter && this.blockType == blockeventdata.blockType;
        }
    }

    public String toString()
    {
        return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
    }
}
