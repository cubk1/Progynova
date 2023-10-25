package net.minecraft.command;

import net.minecraft.entity.player.实体Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase
{
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(sender);
    }

    public String getCommandName()
    {
        return "seed";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.seed.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        World world = (World)(sender instanceof 实体Player ? ((实体Player)sender).worldObj : MinecraftServer.getServer().worldServerForDimension(0));
        sender.增添聊天讯息(new ChatComponentTranslation("commands.seed.success", new Object[] {Long.valueOf(world.getSeed())}));
    }
}
