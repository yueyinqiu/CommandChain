package top.nololiyt.yueyinqiu.commandchain.commands;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.commands.chains.ChainsRouter;
import top.nololiyt.yueyinqiu.commandchain.commands.reload.ReloadRouter;
import top.nololiyt.yueyinqiu.commandchain.commands.version.VersionRouter;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;

import java.util.*;

public class RootRouter extends Router
{
    
    private CommandChainPlugin plugin;
    
    public RootRouter(CommandChainPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    
    public void routeCommand(CommandSender commandSender, String[] args)
    {
        DotDividedStringBuilder messagesRoot = new DotDividedStringBuilder("messages");
        DotDividedStringBuilder permissionRoot = new DotDividedStringBuilder("commandchain");
    
        List<String> argList = new LinkedList<>(Arrays.asList(args));
        execute(plugin, permissionRoot, messagesRoot, commandSender, argList);
    }
    public List<String> doTabComplete(CommandSender commandSender, String[] args)
    {
        DotDividedStringBuilder permissionRoot = new DotDividedStringBuilder("commandchain");
    
        List<String> argList = new LinkedList<>(Arrays.asList(args));
        return tabComplete(plugin, permissionRoot, commandSender, argList);
    }
    
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
    
    private Map<String, CommandLayer> commandLayers = new LinkedHashMap<String, CommandLayer>()
    {
        {
            put("chains", new ChainsRouter());
            put("reload", new ReloadRouter());
            put("version", new VersionRouter());
        }
    };
    
    @Override
    protected Map<String, CommandLayer> nextLayers()
    {
        return commandLayers;
    }
}