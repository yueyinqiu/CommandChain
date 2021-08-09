package top.nololiyt.yueyinqiu.commandchain.commands;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.StringPair;

import java.util.ArrayList;
import java.util.List;


public abstract class Executor implements CommandLayer
{
    protected abstract boolean run(CommandChainPlugin plugin,
                                   DotDividedStringBuilder messageKey,
                                   CommandSender commandSender,
                                   List<String> args);
    
    public void execute(CommandChainPlugin plugin,
                        DotDividedStringBuilder permission,
                        DotDividedStringBuilder messageKey,
                        CommandSender commandSender,
                        List<String> args)
    {
        if (permissionName() != null)
        {
            permission.append(permissionName());
            if (!commandSender.hasPermission(permission.toString()))
            {
                return;
            }
        }
        DotDividedStringBuilder messageKeyCopy = new DotDividedStringBuilder(messageKey);
        
        if (messageKey() != null)
            messageKey.append(messageKey());
    
        if (!run(plugin, messageKey, commandSender, args))
        {
            sendHelp(messageKeyCopy, plugin, commandSender);
        }
    }
    
    /**
     *
     * @param plugin
     * @param commandSender
     * @param ordinal 0 means the first param, 1 means the second param .....
     * @return
     */
    public abstract List<String> getTabComplete(CommandChainPlugin plugin, CommandSender commandSender, int ordinal);
    
    @Override
    public List<String> tabComplete(CommandChainPlugin plugin,
                                    DotDividedStringBuilder permission,
                                    CommandSender commandSender, List<String> args)
    {
        if (permissionName() != null)
        {
            permission.append(permissionName());
            if (!commandSender.hasPermission(permission.toString()))
            {
                return null;
            }
        }
    
        List<String> result = new ArrayList<>();
        String last = args.get(args.size() - 1);
        for (String str : getTabComplete(plugin, commandSender, args.size() - 1))
        {
            if (str.startsWith(last))
                result.add(str);
        }
        return result;
    }
    
    protected boolean sendHelp(DotDividedStringBuilder messageKey,
                               CommandChainPlugin plugin, CommandSender commandSender)
    {
        messageKey.append("help");
        StringPair[] pairs = new StringPair[]{
                StringPair.senderName(commandSender.getName())
        };
    
        plugin.getMessagesManager().sendMessage(commandSender, messageKey, pairs);
        return true;
    }
}
