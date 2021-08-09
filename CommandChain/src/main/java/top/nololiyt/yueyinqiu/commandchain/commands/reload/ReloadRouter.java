package top.nololiyt.yueyinqiu.commandchain.commands.reload;

import top.nololiyt.yueyinqiu.commandchain.commands.CommandLayer;
import top.nololiyt.yueyinqiu.commandchain.commands.Router;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReloadRouter extends Router
{
    protected final static String layerName = "reload";
    
    @Override
    public String permissionName()
    {
        return layerName;
    }
    
    @Override
    public String messageKey()
    {
        return layerName;
    }
    
    private Map<String, CommandLayer> commandLayers = new LinkedHashMap<String, CommandLayer>()
    {
        {
            put("config", new ConfigExecutor());
            put("messages", new MessagesExecutor());
            put("chains", new ChainsExecutor());
            put("all", new AllExecutor());
        }
    };
    
    @Override
    protected Map<String, CommandLayer> nextLayers()
    {
        return commandLayers;
    }
}