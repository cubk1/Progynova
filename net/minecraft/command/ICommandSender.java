package net.minecraft.command;

import net.minecraft.entity.实体;
import net.minecraft.util.阻止位置;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ICommandSender
{
    String getName();

    IChatComponent getDisplayName();

    void 增添聊天讯息(IChatComponent component);

    boolean canCommandSenderUseCommand(int permLevel, String commandName);

    阻止位置 getPosition();

    Vec3 getPositionVector();

    World getEntityWorld();

    实体 getCommandSenderEntity();

    boolean sendCommandFeedback();

    void setCommandStat(CommandResultStats.Type type, int amount);
}
