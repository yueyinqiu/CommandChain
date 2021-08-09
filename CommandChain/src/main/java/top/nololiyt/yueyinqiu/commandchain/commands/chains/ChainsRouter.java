package top.nololiyt.yueyinqiu.commandchain.commands.chains;

import top.nololiyt.yueyinqiu.commandchain.commands.CommandLayer;
import top.nololiyt.yueyinqiu.commandchain.commands.Router;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChainsRouter extends Router
{
    protected final static String layerName = "chains";
    
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
            put("execute", new ExecuteExecutor());
            put("print", new PrintExecutor());
        }
    };
    
    @Override
    protected Map<String, CommandLayer> nextLayers()
    {
        return commandLayers;
    }
}