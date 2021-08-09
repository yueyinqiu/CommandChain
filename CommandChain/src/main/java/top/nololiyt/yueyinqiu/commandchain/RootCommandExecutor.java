package top.nololiyt.yueyinqiu.commandchain;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import top.nololiyt.yueyinqiu.commandchain.commands.RootRouter;

import java.util.List;

public class RootCommandExecutor implements CommandExecutor, TabCompleter
{
    // private CommandChainPlugin plugin;
    
    RootCommandExecutor(CommandChainPlugin plugin)
    {
        // this.plugin = plugin;
        this.router = new RootRouter(plugin);
    }
    
    private RootRouter router;
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {
        router.routeCommand(commandSender, args);
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command,
                                      String label, String[] args)
    {
        return router.doTabComplete(commandSender, args);
    }
}