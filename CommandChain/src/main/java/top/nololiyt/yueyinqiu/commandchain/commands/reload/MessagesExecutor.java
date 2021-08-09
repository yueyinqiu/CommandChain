package top.nololiyt.yueyinqiu.commandchain.commands.reload;

import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;

public class MessagesExecutor extends ReloadExecutor
{
    protected final static String layerName = "messages";
    
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
        plugin.getMessagesManager().reload();
    }
}