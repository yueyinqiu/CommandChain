package top.nololiyt.yueyinqiu.commandchain.commands.version;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.VersionManager;
import top.nololiyt.yueyinqiu.commandchain.commands.Executor;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.LatestVersion;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.MessagesSender;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.StringPair;

import java.util.ArrayList;
import java.util.List;


public class LatestExecutor extends Executor
{
    protected final static String layerName = "latest";
    
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
    public List<String> getTabComplete(CommandChainPlugin plugin,CommandSender sender,int ordinal)
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
                StringPair.senderName(commandSender.getName())
        });
        VersionManager versionManager = plugin.getVersionManager();
        if (!versionManager.isUpdateCheckerEnabled())
        {
            messagesSender.send(messageKey.append("checker-not-enabled"));
            return true;
        }
        LatestVersion latestVersion = versionManager.getLatestVersion();
        if (latestVersion == null)
        {
            messagesSender.send(messageKey.append("failed-to-check"));
            return true;
        }
        messagesSender.setArgs(new StringPair[]{
                StringPair.senderName(commandSender.getName()),
                StringPair.version(latestVersion.getVersion().toString()),
                StringPair.time(latestVersion.getCheckTime().toString())
        });
        messagesSender.send(messageKey.append("message"));
        return true;
    }
}