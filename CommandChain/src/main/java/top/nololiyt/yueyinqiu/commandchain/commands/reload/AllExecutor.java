package top.nololiyt.yueyinqiu.commandchain.commands.reload;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.commands.Executor;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class AllExecutor extends Executor
{
    protected final static String layerName = "all";
    
    @Override
    public String permissionName()
    {
        return null;
    }
    
    @Override
    public String messageKey()
    {
        return null;
    }
    
    @Override
    public List<String> getTabComplete(CommandChainPlugin plugin, CommandSender sender, int ordinal)
    {
        // switch (ordinal)
        // {
        //    default:
        return new ArrayList<>();
        // }
    }
    
    @Override
    protected boolean run(CommandChainPlugin plugin,
                          DotDividedStringBuilder messageKey, CommandSender commandSender,
                          List<String> args)
    {
        ReloadExecutor[] executors = new ReloadExecutor[]{
                new ConfigExecutor(), new ChainsExecutor(), new MessagesExecutor()
        };
        
        for (ReloadExecutor executor : executors)
        {
            executor.run(plugin, messageKey, commandSender);
        }
        return true;
    }
}