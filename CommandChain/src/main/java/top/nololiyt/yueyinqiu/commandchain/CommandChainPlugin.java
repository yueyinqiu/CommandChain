package top.nololiyt.yueyinqiu.commandchain;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import top.nololiyt.yueyinqiu.commandchain.configurationmanagers.AllChainsManager;
import top.nololiyt.yueyinqiu.commandchain.configurationmanagers.ChainManager;
import top.nololiyt.yueyinqiu.commandchain.configurationmanagers.MessagesManager;

import java.text.MessageFormat;

public class CommandChainPlugin extends JavaPlugin
{
    private VersionManager versionManager;
    public VersionManager getVersionManager()
    {
        return versionManager;
    }
    
    private MessagesManager messagesManager;
    public MessagesManager getMessagesManager()
    {
        return messagesManager;
    }
    
    private AllChainsManager allChainsManager;
    public AllChainsManager getAllChainsManager()
    {
        return allChainsManager;
    }
    
    
    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        messagesManager = new MessagesManager(this);
        allChainsManager = new AllChainsManager(this);
        versionManager = new VersionManager(this);
    
        PluginCommand command = getCommand("commandchain");
        assert command != null;
        command.setExecutor(new RootCommandExecutor(this));
    }
}
