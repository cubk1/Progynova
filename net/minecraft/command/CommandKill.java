package net.minecraft.command;

import java.util.List;

import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.阻止位置;

public class CommandKill extends CommandBase
{
    public String getCommandName()
    {
        return "kill";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.kill.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 0)
        {
            实体Player entityplayer = getCommandSenderAsPlayer(sender);
            entityplayer.onKillCommand();
            notifyOperators(sender, this, "commands.kill.successful", new Object[] {entityplayer.getDisplayName()});
        }
        else
        {
            实体 实体 = getEntity(sender, args[0]);
            实体.onKillCommand();
            notifyOperators(sender, this, "commands.kill.successful", new Object[] {实体.getDisplayName()});
        }
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, 阻止位置 pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
