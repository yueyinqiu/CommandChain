package top.nololiyt.yueyinqiu.commandchain.configurationmanagers;

import org.bukkit.configuration.file.YamlConfiguration;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;

import java.io.File;
import java.io.IOException;

public abstract class ConfigurationManager
{
    private String fileName;
    protected final String getFileName()
    {
        return fileName;
    }
    private CommandChainPlugin plugin;
    protected CommandChainPlugin getPlugin()
    {
        return plugin;
    }
    
    ConfigurationManager(CommandChainPlugin plugin, String fileName)
    {
        this.plugin = plugin;
        this.fileName = fileName;
        createIfNotExist();
    }
    
    protected File createIfNotExist()
    {
        File file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
        if (!file.exists())
            plugin.saveResource(fileName, false);
        return file;
    }
    
    private YamlConfiguration configuration;
    public void reload()
    {
        File file = createIfNotExist();
        configuration = YamlConfiguration.loadConfiguration(file);
    }
    
    protected YamlConfiguration getConfiguration()
    {
        if(configuration == null)
            reload();
        return configuration;
    }
    
    protected void saveConfiguration() throws IOException
    {
        configuration.save(new File(
                getPlugin().getDataFolder().getAbsolutePath(), fileName));
    }
}
