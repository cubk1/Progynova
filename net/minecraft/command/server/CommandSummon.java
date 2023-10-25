package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.实体;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.实体Living;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.effect.实体LightningBolt;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandSummon extends CommandBase
{
    public String getCommandName()
    {
        return "summon";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.summon.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        }
        else
        {
            String s = args[0];
            阻止位置 blockpos = sender.getPosition();
            Vec3 vec3 = sender.getPositionVector();
            double d0 = vec3.xCoord;
            double d1 = vec3.yCoord;
            double d2 = vec3.zCoord;

            if (args.length >= 4)
            {
                d0 = parseDouble(d0, args[1], true);
                d1 = parseDouble(d1, args[2], false);
                d2 = parseDouble(d2, args[3], true);
                blockpos = new 阻止位置(d0, d1, d2);
            }

            World world = sender.getEntityWorld();

            if (!world.isBlockLoaded(blockpos))
            {
                throw new CommandException("commands.summon.outOfWorld", new Object[0]);
            }
            else if ("LightningBolt".equals(s))
            {
                world.addWeatherEffect(new 实体LightningBolt(world, d0, d1, d2));
                notifyOperators(sender, this, "commands.summon.success", new Object[0]);
            }
            else
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (args.length >= 5)
                {
                    IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 4);

                    try
                    {
                        nbttagcompound = JsonToNBT.getTagFromJson(ichatcomponent.getUnformattedText());
                        flag = true;
                    }
                    catch (NBTException nbtexception)
                    {
                        throw new CommandException("commands.summon.tagError", new Object[] {nbtexception.getMessage()});
                    }
                }

                nbttagcompound.setString("id", s);
                实体 实体2;

                try
                {
                    实体2 = EntityList.createEntityFromNBT(nbttagcompound, world);
                }
                catch (RuntimeException var19)
                {
                    throw new CommandException("commands.summon.failed", new Object[0]);
                }

                if (实体2 == null)
                {
                    throw new CommandException("commands.summon.failed", new Object[0]);
                }
                else
                {
                    实体2.setLocationAndAngles(d0, d1, d2, 实体2.旋转侧滑, 实体2.rotationPitch);

                    if (!flag && 实体2 instanceof 实体Living)
                    {
                        ((实体Living) 实体2).onInitialSpawn(world.getDifficultyForLocation(new 阻止位置(实体2)), (IEntityLivingData)null);
                    }

                    world.spawnEntityInWorld(实体2);
                    实体 实体 = 实体2;

                    for (NBTTagCompound nbttagcompound1 = nbttagcompound; 实体 != null && nbttagcompound1.hasKey("Riding", 10); nbttagcompound1 = nbttagcompound1.getCompoundTag("Riding"))
                    {
                        实体 实体1 = EntityList.createEntityFromNBT(nbttagcompound1.getCompoundTag("Riding"), world);

                        if (实体1 != null)
                        {
                            实体1.setLocationAndAngles(d0, d1, d2, 实体1.旋转侧滑, 实体1.rotationPitch);
                            world.spawnEntityInWorld(实体1);
                            实体.mountEntity(实体1);
                        }

                        实体 = 实体1;
                    }

                    notifyOperators(sender, this, "commands.summon.success", new Object[0]);
                }
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, 阻止位置 pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList()) : (args.length > 1 && args.length <= 4 ? func_175771_a(args, 1, pos) : null);
    }
}
