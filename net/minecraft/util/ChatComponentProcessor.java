package net.minecraft.util;

import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.实体;

public class ChatComponentProcessor
{
    public static IChatComponent processComponent(ICommandSender commandSender, IChatComponent component, 实体 实体In) throws CommandException
    {
        IChatComponent ichatcomponent = null;

        if (component instanceof ChatComponentScore)
        {
            ChatComponentScore chatcomponentscore = (ChatComponentScore)component;
            String s = chatcomponentscore.getName();

            if (PlayerSelector.hasArguments(s))
            {
                List<实体> list = PlayerSelector.<实体>matchEntities(commandSender, s, 实体.class);

                if (list.size() != 1)
                {
                    throw new EntityNotFoundException();
                }

                s = ((实体)list.get(0)).getName();
            }

            ichatcomponent = 实体In != null && s.equals("*") ? new ChatComponentScore(实体In.getName(), chatcomponentscore.getObjective()) : new ChatComponentScore(s, chatcomponentscore.getObjective());
            ((ChatComponentScore)ichatcomponent).setValue(chatcomponentscore.getUnformattedTextForChat());
        }
        else if (component instanceof ChatComponentSelector)
        {
            String s1 = ((ChatComponentSelector)component).getSelector();
            ichatcomponent = PlayerSelector.matchEntitiesToChatComponent(commandSender, s1);

            if (ichatcomponent == null)
            {
                ichatcomponent = new 交流组分文本("");
            }
        }
        else if (component instanceof 交流组分文本)
        {
            ichatcomponent = new 交流组分文本(((交流组分文本)component).getChatComponentText_TextValue());
        }
        else
        {
            if (!(component instanceof ChatComponentTranslation))
            {
                return component;
            }

            Object[] aobject = ((ChatComponentTranslation)component).getFormatArgs();

            for (int i = 0; i < aobject.length; ++i)
            {
                Object object = aobject[i];

                if (object instanceof IChatComponent)
                {
                    aobject[i] = processComponent(commandSender, (IChatComponent)object, 实体In);
                }
            }

            ichatcomponent = new ChatComponentTranslation(((ChatComponentTranslation)component).getKey(), aobject);
        }

        ChatStyle chatstyle = component.getChatStyle();

        if (chatstyle != null)
        {
            ichatcomponent.setChatStyle(chatstyle.createShallowCopy());
        }

        for (IChatComponent ichatcomponent1 : component.getSiblings())
        {
            ichatcomponent.appendSibling(processComponent(commandSender, ichatcomponent1, 实体In));
        }

        return ichatcomponent;
    }
}
