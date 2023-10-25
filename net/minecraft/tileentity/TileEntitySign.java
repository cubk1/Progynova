package net.minecraft.tileentity;

import com.google.gson.JsonParseException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.event.ClickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.阻止位置;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileEntitySign extends TileEntity
{
    public final IChatComponent[] signText = new IChatComponent[] {new 交流组分文本(""), new 交流组分文本(""), new 交流组分文本(""), new 交流组分文本("")};
    public int lineBeingEdited = -1;
    private boolean isEditable = true;
    private 实体Player player;
    private final CommandResultStats stats = new CommandResultStats();

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        for (int i = 0; i < 4; ++i)
        {
            String s = IChatComponent.Serializer.componentToJson(this.signText[i]);
            compound.setString("Text" + (i + 1), s);
        }

        this.stats.writeStatsToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        this.isEditable = false;
        super.readFromNBT(compound);
        ICommandSender icommandsender = new ICommandSender()
        {
            public String getName()
            {
                return "Sign";
            }
            public IChatComponent getDisplayName()
            {
                return new 交流组分文本(this.getName());
            }
            public void 增添聊天讯息(IChatComponent component)
            {
            }
            public boolean canCommandSenderUseCommand(int permLevel, String commandName)
            {
                return true;
            }
            public 阻止位置 getPosition()
            {
                return TileEntitySign.this.pos;
            }
            public Vec3 getPositionVector()
            {
                return new Vec3((double)TileEntitySign.this.pos.getX() + 0.5D, (double)TileEntitySign.this.pos.getY() + 0.5D, (double)TileEntitySign.this.pos.getZ() + 0.5D);
            }
            public World getEntityWorld()
            {
                return TileEntitySign.this.worldObj;
            }
            public 实体 getCommandSenderEntity()
            {
                return null;
            }
            public boolean sendCommandFeedback()
            {
                return false;
            }
            public void setCommandStat(CommandResultStats.Type type, int amount)
            {
            }
        };

        for (int i = 0; i < 4; ++i)
        {
            String s = compound.getString("Text" + (i + 1));

            try
            {
                IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);

                try
                {
                    this.signText[i] = ChatComponentProcessor.processComponent(icommandsender, ichatcomponent, (实体)null);
                }
                catch (CommandException var7)
                {
                    this.signText[i] = ichatcomponent;
                }
            }
            catch (JsonParseException var8)
            {
                this.signText[i] = new 交流组分文本(s);
            }
        }

        this.stats.readStatsFromNBT(compound);
    }

    public Packet getDescriptionPacket()
    {
        IChatComponent[] aichatcomponent = new IChatComponent[4];
        System.arraycopy(this.signText, 0, aichatcomponent, 0, 4);
        return new S33PacketUpdateSign(this.worldObj, this.pos, aichatcomponent);
    }

    public boolean func_183000_F()
    {
        return true;
    }

    public boolean getIsEditable()
    {
        return this.isEditable;
    }

    public void setEditable(boolean isEditableIn)
    {
        this.isEditable = isEditableIn;

        if (!isEditableIn)
        {
            this.player = null;
        }
    }

    public void setPlayer(实体Player playerIn)
    {
        this.player = playerIn;
    }

    public 实体Player getPlayer()
    {
        return this.player;
    }

    public boolean executeCommand(final 实体Player playerIn)
    {
        ICommandSender icommandsender = new ICommandSender()
        {
            public String getName()
            {
                return playerIn.getName();
            }
            public IChatComponent getDisplayName()
            {
                return playerIn.getDisplayName();
            }
            public void 增添聊天讯息(IChatComponent component)
            {
            }
            public boolean canCommandSenderUseCommand(int permLevel, String commandName)
            {
                return permLevel <= 2;
            }
            public 阻止位置 getPosition()
            {
                return TileEntitySign.this.pos;
            }
            public Vec3 getPositionVector()
            {
                return new Vec3((double)TileEntitySign.this.pos.getX() + 0.5D, (double)TileEntitySign.this.pos.getY() + 0.5D, (double)TileEntitySign.this.pos.getZ() + 0.5D);
            }
            public World getEntityWorld()
            {
                return playerIn.getEntityWorld();
            }
            public 实体 getCommandSenderEntity()
            {
                return playerIn;
            }
            public boolean sendCommandFeedback()
            {
                return false;
            }
            public void setCommandStat(CommandResultStats.Type type, int amount)
            {
                TileEntitySign.this.stats.setCommandStatScore(this, type, amount);
            }
        };

        for (int i = 0; i < this.signText.length; ++i)
        {
            ChatStyle chatstyle = this.signText[i] == null ? null : this.signText[i].getChatStyle();

            if (chatstyle != null && chatstyle.getChatClickEvent() != null)
            {
                ClickEvent clickevent = chatstyle.getChatClickEvent();

                if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
                {
                    MinecraftServer.getServer().getCommandManager().executeCommand(icommandsender, clickevent.getValue());
                }
            }
        }

        return true;
    }

    public CommandResultStats getStats()
    {
        return this.stats;
    }
}
