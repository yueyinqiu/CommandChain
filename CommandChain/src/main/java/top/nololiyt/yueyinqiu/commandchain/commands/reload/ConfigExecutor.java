package top.nololiyt.yueyinqiu.commandchain.commands.reload;

import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;

public class ConfigExecutor extends ReloadExecutor
{
    protected final static String layerName = "config";
    
    @Override
    public String permissionName()
    {
        return null;
    }
    
    @Override
    public String messageKey()
    {
        return layerName;
    }
    
    @Override
    public void reload(CommandChainPlugin plugin)
    {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }
}
