package top.nololiyt.yueyinqiu.commandchain.commands.chains;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.commands.Executor;
import top.nololiyt.yueyinqiu.commandchain.configurationmanagers.ChainManager;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExecuteExecutor extends Executor
{
    protected final static String layerName = "execute";
    
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
        switch (ordinal)
        {
            case 0:
                return plugin.getAllChainsManager().allChainsNameWithExecutePermission(sender);
            default:
                return new ArrayList<String>()
                {
                    {
                        add("arg" + ordinal);
                    }
                };
        }
    }
    
    @Override
    protected boolean run(CommandChainPlugin plugin,
                          DotDividedStringBuilder messageKey, CommandSender commandSender,
                          List<String> args)
    {
        if (args.size() < 1)
            return false;
    
        String chainName = args.get(0);
    
        MessagesSender messagesSender = new MessagesSender(plugin.getMessagesManager(),
                commandSender, new StringPair[]{
                StringPair.senderName(commandSender.getName()),
                StringPair.chainName(chainName),
                null
        });
    
        ChainManager chainManager = plugin.getAllChainsManager().getChain(chainName);
        if (chainManager == null)
        {
            messagesSender.send(messageKey.append("no-such-chain"));
            return true;
        }
    
        args.set(0, commandSender.getName());
        
        DotDividedStringBuilder messagesKeyFailed =
                new DotDividedStringBuilder(messageKey).append("failed");
        DotDividedStringBuilder messagesKeyFailedAndStopped =
                new DotDividedStringBuilder(messageKey).append("failed-and-stopped");
    
        messagesSender.send(
                new DotDividedStringBuilder(messageKey).append("started"));
    
        chainManager.executeChain(commandSender, args.toArray(new String[0]),
                new RunnableT<String>()
                {
                    @Override
                    public void run(String s)
                    {
                        messagesSender.getArgs()[2] = StringPair.chainLine(s);
                        messagesSender.send(messagesKeyFailed);
                    }
                }, new RunnableT<String>()
                {
                    @Override
                    public void run(String s)
                    {
                        messagesSender.getArgs()[2] = StringPair.chainLine(s);
                        messagesSender.send(messagesKeyFailedAndStopped);
                    }
                }, () -> {
                    messagesSender.getArgs()[2] = null;
                    messagesSender.send(messageKey.append("completed"));
                });
    
        return true;
    }
}