package top.nololiyt.yueyinqiu.commandchain.configurationmanagers;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.CommandInChains;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.RunnableT;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.RunnableTU;

import java.util.LinkedList;
import java.util.List;

public class ChainManager extends ConfigurationManager
{
    private List<CommandInChains> commands = null;
    private boolean goOnEvenFailure;
    
    private String executePermission = null;
    
    String getExecutePermission()
    {
        if (commands == null)
            readCommands();
        return executePermission;
    }
    
    private String printPermission = null;
    
    String getPrintPermission()
    {
        if (commands == null)
            readCommands();
        return printPermission;
    }
    
    private String chainName;
    
    String getChainName()
    {
        if (commands == null)
            readCommands();
        return chainName;
    }
    
    ChainManager(CommandChainPlugin plugin, String chainName)
    {
        super(plugin, "chains/" + chainName + ".yml");
        this.chainName = chainName;
    }
    
    private void readCommands()
    {
        Configuration configuration = getConfiguration();
        List<String> commands = configuration.getStringList("commands");
        this.commands = new LinkedList<>();
        for (String command : commands)
            this.commands.add(new CommandInChains(command, getPlugin()));
    
        this.goOnEvenFailure = configuration.getBoolean("go-on-even-failure");
        
        this.executePermission = configuration.getString("execute-permission");
        if ("".equals(executePermission))
            executePermission = null;
        
        this.printPermission = configuration.getString("print-permission");
        if ("".equals(printPermission))
            printPermission = null;
    }
    
    void check(RunnableTU<String, String> onInvalidLine)
    {
        if (commands == null)
            readCommands();
        for (CommandInChains command : commands)
        {
            if (!command.check())
            {
                onInvalidLine.run(chainName, command.getLine());
            }
        }
    }
    
    public boolean printChain(CommandSender sender, String[] arguments, RunnableT<String> printer)
    {
        if (commands == null)
            readCommands();
        if (printPermission == null || sender.hasPermission(printPermission))
        {
            CommandInChains.print(commands, arguments, printer);
            return true;
        }
        return false;
    }
    
    public boolean executeChain(
            CommandSender sender,
            String[] arguments,
            RunnableT<String> runOnFailure,
            RunnableT<String> runOnFailedAndStopped,
            Runnable runOnCompleted)
    {
        if (commands == null)
            readCommands();
    
        if (executePermission == null || sender.hasPermission(executePermission))
        {
            List<CommandInChains> copy = new LinkedList<>(commands);
            CommandInChains firstChain = copy.remove(0);
            firstChain.execute(arguments, copy, goOnEvenFailure,
                    runOnFailure, runOnFailedAndStopped, runOnCompleted);
            return true;
        }
        return false;
    }
}