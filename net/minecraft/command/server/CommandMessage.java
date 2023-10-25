package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.实体Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.阻止位置;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.枚举聊天格式;
import net.minecraft.util.IChatComponent;

public class CommandMessage extends CommandBase
{
    public List<String> getCommandAliases()
    {
        return Arrays.<String>asList(new String[] {"w", "msg"});
    }

    public String getCommandName()
    {
        return "tell";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.message.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        else
        {
            实体Player entityplayer = getPlayer(sender, args[0]);

            if (entityplayer == sender)
            {
                throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
            }
            else
            {
                IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof 实体Player));
                ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.message.display.incoming", new Object[] {sender.getDisplayName(), ichatcomponent.createCopy()});
                ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.message.display.outgoing", new Object[] {entityplayer.getDisplayName(), ichatcomponent.createCopy()});
                chatcomponenttranslation.getChatStyle().setColor(枚举聊天格式.GRAY).setItalic(Boolean.valueOf(true));
                chatcomponenttranslation1.getChatStyle().setColor(枚举聊天格式.GRAY).setItalic(Boolean.valueOf(true));
                entityplayer.增添聊天讯息(chatcomponenttranslation);
                sender.增添聊天讯息(chatcomponenttranslation1);
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, 阻止位置 pos)
    {
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
