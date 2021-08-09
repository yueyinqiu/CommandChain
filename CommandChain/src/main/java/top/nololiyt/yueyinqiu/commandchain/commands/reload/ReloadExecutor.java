package top.nololiyt.yueyinqiu.commandchain.commands.reload;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.commands.Executor;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.StringPair;

import java.util.ArrayList;
import java.util.List;

public abstract class ReloadExecutor extends Executor
{
    void run(CommandChainPlugin plugin, DotDividedStringBuilder messageKey, CommandSender commandSender)
    {
        run(plugin,
                new DotDividedStringBuilder(messageKey).append(messageKey()),
                commandSender, null);
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
    protected boolean run(
            CommandChainPlugin plugin,
            DotDividedStringBuilder messageKey,
            CommandSender commandSender,
            List<String> args)
    {
        reload(plugin);
        messageKey.append("completed");
    
        StringPair[] pairs = new StringPair[]{
                StringPair.senderName(commandSender.getName())
        };
    
        plugin.getMessagesManager().sendMessage(
                commandSender, messageKey, pairs);
        return true;
    }
    
    protected abstract void reload(CommandChainPlugin plugin);
}