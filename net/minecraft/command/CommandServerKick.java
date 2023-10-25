package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.阻止位置;

public class CommandServerKick extends CommandBase
{
    public String getCommandName()
    {
        return "kick";
    }

    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.kick.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 0 && args[0].length() > 1)
        {
            实体PlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
            String s = "Kicked by an operator.";
            boolean flag = false;

            if (entityplayermp == null)
            {
                throw new PlayerNotFoundException();
            }
            else
            {
                if (args.length >= 2)
                {
                    s = getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
                    flag = true;
                }

                entityplayermp.playerNetServerHandler.kickPlayerFromServer(s);

                if (flag)
                {
                    notifyOperators(sender, this, "commands.kick.success.reason", new Object[] {entityplayermp.getName(), s});
                }
                else
                {
                    notifyOperators(sender, this, "commands.kick.success", new Object[] {entityplayermp.getName()});
                }
            }
        }
        else
        {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, 阻止位置 pos)
    {
        return args.length >= 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
