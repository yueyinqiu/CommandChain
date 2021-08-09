package top.nololiyt.yueyinqiu.commandchain.configurationmanagers;

import com.sun.istack.internal.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.DotDividedStringBuilder;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.StringPair;


public class MessagesManager extends ConfigurationManager
{
    public MessagesManager(CommandChainPlugin plugin)
    {
        super(plugin, "messages.yml");
    }
    
    public @NotNull
    String getItemAndShowError(DotDividedStringBuilder node)
    {
        String key = node.toString();
        String result = getConfiguration().getString(key);
        
        if (result == null)
        {
            getPlugin().getLogger().severe(
                    "File 'messages.yml' is corrupted and '" + key
                            + "' is missing.");
            return key;
        }
        return result;
    }
    
    public void sendMessage(CommandSender target, DotDividedStringBuilder messageKey, StringPair[] args)
    {
        String message = getItemAndShowError(messageKey);
        sendMessage(target, message, args);
    }
    
    public void sendMessage(CommandSender target, @NotNull String message, StringPair[] args)
    {
        String result = message.trim();
        if (result.isEmpty())
            return;
        
        result = ChatColor.translateAlternateColorCodes('&', result);
        for (StringPair pair : args)
        {
            if (pair != null)
                result = result.replace(pair.getKey(), pair.getValue());
        }
        
        target.sendMessage(result);
    }
}