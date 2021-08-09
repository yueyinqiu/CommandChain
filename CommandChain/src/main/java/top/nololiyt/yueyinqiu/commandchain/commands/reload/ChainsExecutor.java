package top.nololiyt.yueyinqiu.commandchain.commands.reload;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.RunnableTU;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.StringPair;

import java.util.List;

public class ChainsExecutor extends ReloadExecutor
{
    protected final static String layerName = "chains";
    
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
    protected boolean run(
            CommandChainPlugin plugin,
            DotDividedStringBuilder messageKey,
            CommandSender commandSender,
            List<String> args)
    {
        DotDividedStringBuilder successMessageKey =
                new DotDividedStringBuilder(messageKey).append("completed");
        {
            messageKey.append("invalid-line");
            StringPair[] pairs = new StringPair[]{
                    StringPair.senderName(commandSender.getName()),
                    null, null
            };
    
            plugin.getAllChainsManager().reload(new RunnableTU<String, String>()
            {
                @Override
                public void run(String s, String s2)
                {
                    pairs[1] = StringPair.chainName(s);
                    pairs[2] = StringPair.chainLine(s2);
                    plugin.getMessagesManager().sendMessage(
                            commandSender, messageKey, pairs);
                }
            });
        }
    
        {
            StringPair[] pairs = new StringPair[]{
                    StringPair.senderName(commandSender.getName())
            };
        
            plugin.getMessagesManager().sendMessage(
                    commandSender, successMessageKey, pairs);
            return true;
        }
    }
    
    @Override
    public void reload(CommandChainPlugin plugin)
    {
    }
}