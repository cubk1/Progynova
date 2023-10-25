package net.minecraft.network.rcon;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.实体;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.阻止位置;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender
{
    private static final RConConsoleSource instance = new RConConsoleSource();
    private StringBuffer buffer = new StringBuffer();

    public String getName()
    {
        return "Rcon";
    }

    public IChatComponent getDisplayName()
    {
        return new 交流组分文本(this.getName());
    }

    public void 增添聊天讯息(IChatComponent component)
    {
        this.buffer.append(component.getUnformattedText());
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    public 阻止位置 getPosition()
    {
        return new 阻止位置(0, 0, 0);
    }

    public Vec3 getPositionVector()
    {
        return new Vec3(0.0D, 0.0D, 0.0D);
    }

    public World getEntityWorld()
    {
        return MinecraftServer.getServer().getEntityWorld();
    }

    public 实体 getCommandSenderEntity()
    {
        return null;
    }

    public boolean sendCommandFeedback()
    {
        return true;
    }

    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
    }
}
