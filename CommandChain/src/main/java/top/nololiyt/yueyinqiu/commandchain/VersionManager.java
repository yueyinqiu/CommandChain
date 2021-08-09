package top.nololiyt.yueyinqiu.commandchain;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import top.nololiyt.yueyinqiu.commandchain.entitiesandtools.LatestVersion;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class VersionManager
{
    private Plugin plugin;
    private static final String CHECK_LINK =
            "https://yueyinqiu.github.io/CommandChain/version.html";
    
    VersionManager(Plugin plugin)
    {
        this.plugin = plugin;
        if (updateCheckerEnabled = plugin.getConfig().getBoolean("update-checker.enabled"))
        {
            long oPeriod = plugin.getConfig().getLong("update-checker.period");
            long period = oPeriod <= 0 ? 20 * (30 * 60) : oPeriod;
            if (plugin.getConfig().getBoolean("update-checker.log-after-first-check"))
            {
                checkAndLog();
                startCheckLoop(period, period);
            }
            startCheckLoop(0, period);
        }
    }
    
    private volatile LatestVersion latestVersion;
    
    public LatestVersion getLatestVersion()
    {
        return latestVersion;
    }
    
    private boolean updateCheckerEnabled;
    
    public boolean isUpdateCheckerEnabled()
    {
        return updateCheckerEnabled;
    }
    
    public BigDecimal getCurrentVersion()
    {
        return new BigDecimal(
                plugin.getDescription().getVersion());
    }
    
    private void checkAndLog()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    LatestVersion latestVersion = checkAndSave();
                    if (latestVersion == null)
                    {
                        warning("Update check failed.");
                        return;
                    }
    
                    BigDecimal current = getCurrentVersion();
                    if (latestVersion.getVersion().compareTo(current) > 0)
                        warning("Version: '" + latestVersion.getVersion().toString() + "' is available. " +
                                "And you are now using '" + current.toString() + "'. " +
                                "Download it at 'https://yueyinqiu.github.io/CommandChain/download'.");
                    else
                        warning("The current version is the latest.");
                }
                catch (Exception e)
                {
                    warning("Update check failed.");
                    printStackTrace(e);
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }
    
    private void printStackTrace(Exception e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                e.printStackTrace();
            }
        }.runTask(plugin);
    }
    
    private void warning(String s)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                plugin.getLogger().warning(s);
            }
        }.runTask(plugin);
    }
    
    private LatestVersion checkAndSave() throws IOException
    {
        try (InputStream inputStream = new URL(CHECK_LINK)
                .openStream(); Scanner scanner = new Scanner(inputStream))
        {
            if (scanner.hasNextBigDecimal())
            {
                BigDecimal latest = scanner.nextBigDecimal();
                this.latestVersion = new LatestVersion(new Date(), latest);
            }
        }
        return this.latestVersion;
    }
    
    private void startCheckLoop(long delay, long period)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    checkAndSave();
                }
                catch (Exception e)
                {
                    // DO NOTHING
                }
            }
        }.runTaskTimerAsynchronously(plugin, delay, period);
    }
}