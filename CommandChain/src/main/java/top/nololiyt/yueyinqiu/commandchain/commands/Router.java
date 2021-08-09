package top.nololiyt.yueyinqiu.commandchain.commands;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.StringPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Router implements CommandLayer
{
    protected abstract Map<String, CommandLayer> nextLayers();
    
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
        if (messageKey() != null)
            messageKey.append(messageKey());
    
        if (args.size() == 0)
        {
            sendHelp(messageKey, plugin, commandSender);
            return;
        }
        
        CommandLayer nextLayer = nextLayers().get(args.get(0));
        if (nextLayer == null)
        {
            sendHelp(messageKey, plugin, commandSender);
            return;
        }
        args.remove(0);
        nextLayer.execute(
                plugin,
                permission,
                messageKey,
                commandSender,
                args
        );
    }
    
    
    private void sendHelp(DotDividedStringBuilder messageKey,
                          CommandChainPlugin plugin, CommandSender commandSender)
    {
        messageKey.append("help");
        StringPair[] pairs = new StringPair[]{
                StringPair.senderName(commandSender.getName())
        };
        
        plugin.getMessagesManager().sendMessage(
                commandSender, messageKey, pairs);
    }
    
    @Override
    public List<String> tabComplete(CommandChainPlugin plugin,
                                    DotDividedStringBuilder permission,
                                    CommandSender commandSender,
                                    List<String> args)
    {
        if (permissionName() != null)
        {
            permission.append(permissionName());
            if (!commandSender.hasPermission(permission.toString()))
            {
                return new ArrayList<>();
            }
        }
        
        if (args.size() == 1)
        {
            List<String> result = new ArrayList<>();
            for (Map.Entry<String, CommandLayer> entry : nextLayers().entrySet())
            {
                String key = entry.getKey();
                if (!key.startsWith(args.get(0)))
                {
                    continue;
                }
                String newNode = entry.getValue().permissionName();
                if (newNode == null)
                {
                    result.add(entry.getKey());
                    continue;
                }
    
                DotDividedStringBuilder permissionCopy = new DotDividedStringBuilder(permission);
                permissionCopy.append(newNode);
                if (commandSender.hasPermission(permissionCopy.toString()))
                {
                    result.add(key);
                }
            }
            return result;
        }
    
        CommandLayer nextLayer = nextLayers().get(args.get(0));
        if (nextLayer != null)
        {
            args.remove(0);
            return nextLayer.tabComplete(
                    plugin,
                    permission,
                    commandSender,
                    args);
        }
    
        return new ArrayList<>();
    }
}