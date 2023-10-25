package net.minecraft.command;

import net.minecraft.entity.实体;
import net.minecraft.entity.player.实体Player;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class CommandEntityData extends CommandBase
{
    public String getCommandName()
    {
        return "entitydata";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.entitydata.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        else
        {
            实体 实体 = getEntity(sender, args[0]);

            if (实体 instanceof 实体Player)
            {
                throw new CommandException("commands.entitydata.noPlayers", new Object[] {实体.getDisplayName()});
            }
            else
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                实体.writeToNBT(nbttagcompound);
                NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();
                NBTTagCompound nbttagcompound2;

                try
                {
                    nbttagcompound2 = JsonToNBT.getTagFromJson(getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.entitydata.tagError", new Object[] {nbtexception.getMessage()});
                }

                nbttagcompound2.removeTag("UUIDMost");
                nbttagcompound2.removeTag("UUIDLeast");
                nbttagcompound.merge(nbttagcompound2);

                if (nbttagcompound.equals(nbttagcompound1))
                {
                    throw new CommandException("commands.entitydata.failed", new Object[] {nbttagcompound.toString()});
                }
                else
                {
                    实体.readFromNBT(nbttagcompound);
                    notifyOperators(sender, this, "commands.entitydata.success", new Object[] {nbttagcompound.toString()});
                }
            }
        }
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
