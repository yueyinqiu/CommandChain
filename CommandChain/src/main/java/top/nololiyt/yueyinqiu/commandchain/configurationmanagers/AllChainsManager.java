package top.nololiyt.yueyinqiu.commandchain.configurationmanagers;

import com.sun.istack.internal.Nullable;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import top.nololiyt.yueyinqiu.commandchain.CommandChainPlugin;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.RunnableTU;

import java.io.File;
import java.util.*;

public class AllChainsManager
{
    private CommandChainPlugin plugin;
    public AllChainsManager(CommandChainPlugin plugin)
    {
        this.plugin = plugin;
        reload(new RunnableTU<String, String>()
        {
            @Override
            public void run(String s, String s2)
            {
                ConsoleCommandSender console = plugin.getServer().getConsoleSender();
                plugin.getLogger().severe("Cannot resolve the line in " + s + ": " + s2);
            }
        });
    }
    
    private List<ChainManager> chainManagers;
    @Nullable
    public ChainManager getChain(String name)
    {
        for (ChainManager chain : chainManagers)
        {
            if (chain.getChainName().equals(name))
                return chain;
        }
        return null;
    }
    
    public List<String> allChainsNameWithPrintPermission(CommandSender commandSender)
    {
        List<String> result = new ArrayList<>();
        for (ChainManager chain : chainManagers)
        {
            String permission = chain.getPrintPermission();
            if (permission == null || commandSender.hasPermission(permission))
                result.add(chain.getChainName());
        }
        return result;
    }
    public List<String> allChainsNameWithExecutePermission(CommandSender commandSender)
    {
        List<String> result = new ArrayList<>();
        for (ChainManager chain : chainManagers)
        {
            String permission = chain.getExecutePermission();
            if (permission == null || commandSender.hasPermission(permission))
                result.add(chain.getChainName());
        }
        return result;
    }
    
    private void putDefault(File dir)
    {
        chainManagers = new ArrayList<>();
        boolean r = dir.mkdirs();
        assert r;
        chainManagers.add(new ChainManager(plugin, "example"));
    }
    public void reload(RunnableTU<String,String> onInvalidLine)
    {
        File file = new File(plugin.getDataFolder().getAbsolutePath(), "chains");
    
        if (!file.exists())
        {
            putDefault(file);
            return;
        }
    
        if (!file.isDirectory())
        {
            boolean r = file.delete();
            assert r;
            putDefault(file);
            return;
        }
    
        File[] files = file.listFiles((File f) -> {
            String lowerName = f.getName().toLowerCase();
            return (!f.isDirectory()) && lowerName.endsWith(".yml");
        });
        assert files != null;
    
        chainManagers = new ArrayList<>();
        for (File t : files)
        {
            String tName = t.getName();
            String chainName = tName.substring(0, tName.length() - 4);
            ChainManager chainManager = new ChainManager(plugin, chainName);
            chainManager.check(onInvalidLine);
            chainManagers.add(chainManager);
        }
    }
}