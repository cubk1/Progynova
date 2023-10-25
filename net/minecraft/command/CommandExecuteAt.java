package net.minecraft.command;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.实体;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.阻止位置;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandExecuteAt extends CommandBase
{
    public String getCommandName()
    {
        return "execute";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.execute.usage";
    }

    public void processCommand(final ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 5)
        {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        }
        else
        {
            final 实体 实体 = getEntity(sender, args[0], 实体.class);
            final double d0 = parseDouble(实体.X坐标, args[1], false);
            final double d1 = parseDouble(实体.Y坐标, args[2], false);
            final double d2 = parseDouble(实体.Z坐标, args[3], false);
            final 阻止位置 blockpos = new 阻止位置(d0, d1, d2);
            int i = 4;

            if ("detect".equals(args[4]) && args.length > 10)
            {
                World world = 实体.getEntityWorld();
                double d3 = parseDouble(d0, args[5], false);
                double d4 = parseDouble(d1, args[6], false);
                double d5 = parseDouble(d2, args[7], false);
                Block block = getBlockByText(sender, args[8]);
                int k = parseInt(args[9], -1, 15);
                阻止位置 blockpos1 = new 阻止位置(d3, d4, d5);
                IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getBlock() != block || k >= 0 && iblockstate.getBlock().getMetaFromState(iblockstate) != k)
                {
                    throw new CommandException("commands.execute.failed", new Object[] {"detect", 实体.getName()});
                }

                i = 10;
            }

            String s = buildString(args, i);
            ICommandSender icommandsender = new ICommandSender()
            {
                public String getName()
                {
                    return 实体.getName();
                }
                public IChatComponent getDisplayName()
                {
                    return 实体.getDisplayName();
                }
                public void 增添聊天讯息(IChatComponent component)
                {
                    sender.增添聊天讯息(component);
                }
                public boolean canCommandSenderUseCommand(int permLevel, String commandName)
                {
                    return sender.canCommandSenderUseCommand(permLevel, commandName);
                }
                public 阻止位置 getPosition()
                {
                    return blockpos;
                }
                public Vec3 getPositionVector()
                {
                    return new Vec3(d0, d1, d2);
                }
                public World getEntityWorld()
                {
                    return 实体.worldObj;
                }
                public 实体 getCommandSenderEntity()
                {
                    return 实体;
                }
                public boolean sendCommandFeedback()
                {
                    MinecraftServer minecraftserver = MinecraftServer.getServer();
                    return minecraftserver == null || minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
                }
                public void setCommandStat(CommandResultStats.Type type, int amount)
                {
                    实体.setCommandStat(type, amount);
                }
            };
            ICommandManager icommandmanager = MinecraftServer.getServer().getCommandManager();

            try
            {
                int j = icommandmanager.executeCommand(icommandsender, s);

                if (j < 1)
                {
                    throw new CommandException("commands.execute.allInvocationsFailed", new Object[] {s});
                }
            }
            catch (Throwable var23)
            {
                throw new CommandException("commands.execute.failed", new Object[] {s, 实体.getName()});
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, 阻止位置 pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : (args.length > 1 && args.length <= 4 ? func_175771_a(args, 1, pos) : (args.length > 5 && args.length <= 8 && "detect".equals(args[4]) ? func_175771_a(args, 5, pos) : (args.length == 9 && "detect".equals(args[4]) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null)));
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
