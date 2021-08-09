package top.nololiyt.yueyinqiu.commandchain.commands.version;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.commands.Executor;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.MessagesSender;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.StringPair;

import java.util.ArrayList;
import java.util.List;


public class CurrentExecutor extends Executor
{
    protected final static String layerName = "current";
    
    @Override
    public String permissionName()
    {
        return null;
    }
    
    @Override
    public String messageKey()
    {
        return layerName;
    }
    
    @Override
    public List<String> getTabComplete(CommandChainPlugin plugin, CommandSender sender, int ordinal)
    {
        // switch (ordinal)
        // {
        //     default:
        return new ArrayList<>();
        // }
    }
    
    @Override
    protected boolean run(CommandChainPlugin plugin,
                          DotDividedStringBuilder messageKey, CommandSender commandSender,
                          List<String> args)
    {
        MessagesSender messagesSender = new MessagesSender(plugin.getMessagesManager(),
                commandSender, new StringPair[]{
                StringPair.senderName(commandSender.getName()),
                StringPair.version(plugin.getVersionManager().getCurrentVersion().toString())
        });
        messagesSender.send(messageKey.append("message"));
        return true;
    }
}