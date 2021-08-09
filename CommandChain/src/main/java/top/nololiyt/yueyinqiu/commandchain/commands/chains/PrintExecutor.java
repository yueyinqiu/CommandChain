package top.nololiyt.yueyinqiu.commandchain.commands.chains;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.commands.Executor;
import top.nololiyt.yueyinqiu.commandchain.configurationmanagers.AllChainsManager;
import top.nololiyt.yueyinqiu.commandchain.configurationmanagers.ChainManager;
import top.nololiyt.yueyinqiu.commandchain.configurationmanagers.MessagesManager;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.MessagesSender;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.RunnableT;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.StringPair;

import java.util.ArrayList;
import java.util.List;

public class PrintExecutor extends Executor
{
    protected final static String layerName = "print";
    
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
        switch (ordinal)
        {
            case 0:
                return plugin.getAllChainsManager().allChainsNameWithPrintPermission(sender);
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
        MessagesManager messagesManager = plugin.getMessagesManager();
        MessagesSender messagesSender = new MessagesSender(messagesManager,
                commandSender, new StringPair[]{
                StringPair.senderName(commandSender.getName()),
                StringPair.chainName(chainName)
        });
    
        ChainManager chainManager = plugin.getAllChainsManager().getChain(chainName);
        if (chainManager == null)
        {
            messagesSender.send(messageKey.append("no-such-chain"));
            return true;
        }
    
        args.set(0, commandSender.getName());
    
        List<String> lines = new ArrayList<>();
        chainManager.printChain(commandSender, args.toArray(new String[0]),
                new RunnableT<String>()
                {
                    @Override
                    public void run(String s)
                    {
                        lines.add(s);
                    }
                });
    
        messageKey.append("list");
        String beginning = getMessageItem(messagesManager, messageKey, "beginning");
        String separator = getMessageItem(messagesManager, messageKey, "separator");
        String ending = getMessageItem(messagesManager, messageKey, "ending");
        messagesSender.sendJointed(beginning, ending, separator, lines);
        return true;
    }
    
    private String getMessageItem(MessagesManager messagesManager,
                                  DotDividedStringBuilder messageKey, String nodeName)
    {
        return messagesManager.getItemAndShowError(
                new DotDividedStringBuilder(messageKey).append(nodeName));
    }
}