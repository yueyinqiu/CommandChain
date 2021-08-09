package top.nololiyt.yueyinqiu.commandchain.entitiesandtools;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.util.StringUtil;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;

import java.text.MessageFormat;
import java.util.List;

public class CommandInChains
{
    private String line;
    
    public String getLine()
    {
        return line;
    }
    
    private CommandChainPlugin plugin;
    
    public CommandInChains(String line, CommandChainPlugin plugin)
    {
        this.line = line;
        this.plugin = plugin;
    }
    
    private static final String KEY_WORD_RUN_COMMAND = "run-command ";
    private static final String KEY_WORD_DELAY = "delay ";
    
    private boolean isPositiveLong(String str, boolean allowZero)
    {
        try
        {
            long number = Long.valueOf(str);
            return (number > 0) || (number == 0 && allowZero);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    
    public boolean check()
    {
        String loweredLine = line.toLowerCase();
        if (loweredLine.startsWith(KEY_WORD_RUN_COMMAND))
        {
            loweredLine = loweredLine.substring(KEY_WORD_RUN_COMMAND.length());
            return !loweredLine.isEmpty();
        }
        else if (loweredLine.startsWith(KEY_WORD_DELAY))
        {
            loweredLine = loweredLine.substring(KEY_WORD_DELAY.length()).trim();
            if (isPositiveLong(loweredLine, false))
                return true;
            if (loweredLine.startsWith("{") && loweredLine.endsWith("}"))
            {
                loweredLine = loweredLine.substring(1, loweredLine.length() - 1);
                return isPositiveLong(loweredLine, true);
            }
            else
                return false;
        }
        return false;
    }
    
    public static void print(
            List<CommandInChains> commands,
            String[] arguments,
            RunnableT<String> printer)
    {
        for (CommandInChains command : commands)
            printer.run(command.fillArguments(arguments));
    }
    
    private String fillArguments(String[] arguments)
    {
        return MessageFormat.format(line, (Object[]) arguments);
    }
    
    private void executeNext(
            String filledLineIfFailure,
            String[] arguments,
            List<CommandInChains> myNextCommands,
            boolean goOnEvenFailure,
            RunnableT<String> runOnFailure,
            RunnableT<String> runOnFailedAndStopped,
            Runnable runOnCompleted)
    {
        if (filledLineIfFailure != null)
        {
            if (goOnEvenFailure)
            {
                runOnFailure.run(filledLineIfFailure);
                CommandInChains nextCommand = myNextCommands.remove(0);
                nextCommand.execute(arguments, myNextCommands, true,
                        runOnFailure, runOnFailedAndStopped, runOnCompleted);
            }
            else
                runOnFailedAndStopped.run(filledLineIfFailure);
        }
        else if(myNextCommands.size() > 0)
        {
            CommandInChains nextCommand = myNextCommands.remove(0);
            nextCommand.execute(arguments, myNextCommands, goOnEvenFailure,
                    runOnFailure, runOnFailedAndStopped, runOnCompleted);
        }
        else
        {
            runOnCompleted.run();
        }
    }
    
    public void execute(
            String[] arguments,
            List<CommandInChains> nextCommands,
            boolean goOnEvenFailure,
            RunnableT<String> runOnFailure,
            RunnableT<String> runOnFailedAndStopped,
            Runnable runOnCompleted)
    {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                () -> this.executePrivate(arguments, nextCommands,
                        goOnEvenFailure, runOnFailure, runOnFailedAndStopped, runOnCompleted)
        );
    }
    
    private void executePrivate(
            String[] arguments,
            List<CommandInChains> nextCommands,
            boolean goOnEvenFailure,
            RunnableT<String> runOnFailure,
            RunnableT<String> runOnFailedAndStopped,
            Runnable runOnCompleted)
    {
        String filledLine = fillArguments(arguments);
    
        String loweredLine = line.toLowerCase();
        if (loweredLine.startsWith(KEY_WORD_RUN_COMMAND))
        {
            String substring = filledLine.substring(KEY_WORD_RUN_COMMAND.length());
            if (substring.isEmpty())
            {
                executeNext(filledLine, arguments, nextCommands, true,
                        runOnFailure, runOnFailedAndStopped, runOnCompleted);
                return;
            }
            Server server = plugin.getServer();
            boolean succeeded;
            try
            {
                succeeded = server.dispatchCommand(server.getConsoleSender(), substring);
            }
            catch (Exception e)
            {
                executeNext(filledLine,
                        arguments, nextCommands, goOnEvenFailure,
                        runOnFailure, runOnFailedAndStopped, runOnCompleted);
                throw e;
            }
            executeNext(succeeded ? null : filledLine,
                    arguments, nextCommands, goOnEvenFailure,
                    runOnFailure, runOnFailedAndStopped, runOnCompleted);
            return;
        }
        else if (loweredLine.startsWith(KEY_WORD_DELAY))
        {
            String substring = filledLine.substring(KEY_WORD_DELAY.length()).trim();
            try
            {
                long number = Long.valueOf(substring);
    
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                        () -> executeNext(null, arguments, nextCommands, goOnEvenFailure,
                                runOnFailure, runOnFailedAndStopped, runOnCompleted),
                        number);
            }
            catch (NumberFormatException e)
            {
                executeNext(filledLine, arguments, nextCommands, goOnEvenFailure, runOnFailure,
                        runOnFailedAndStopped, runOnCompleted);
            }
            return;
        }
        executeNext(filledLine, arguments, nextCommands,
                goOnEvenFailure, runOnFailure, runOnFailedAndStopped, runOnCompleted);
    }
}