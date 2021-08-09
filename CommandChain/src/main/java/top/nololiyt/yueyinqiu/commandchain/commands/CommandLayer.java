package top.nololiyt.yueyinqiu.commandchain.commands;

import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;

import java.util.List;

public interface CommandLayer
{
    String permissionName();
    
    String messageKey();
    
    void execute(CommandChainPlugin plugin,
                 DotDividedStringBuilder permission,
                 DotDividedStringBuilder messageKey,
                 CommandSender commandSender,
                 List<String> args);
    
    List<String> tabComplete(CommandChainPlugin plugin,
                             DotDividedStringBuilder permission,
                             CommandSender commandSender,
                             List<String> args);
}