package net.minecraft.command;

import java.util.List;
import java.util.Map;
import net.minecraft.util.阻止位置;

public interface ICommandManager
{
    int executeCommand(ICommandSender sender, String rawCommand);

    List<String> getTabCompletionOptions(ICommandSender sender, String input, 阻止位置 pos);

    List<ICommand> getPossibleCommands(ICommandSender sender);

    Map<String, ICommand> getCommands();
}
