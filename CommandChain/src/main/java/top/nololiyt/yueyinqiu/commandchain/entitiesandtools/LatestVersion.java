package top.nololiyt.yueyinqiu.commandchain.entitiesandtools;

import java.math.BigDecimal;
import java.util.Date;

public class LatestVersion
{
    private Date checkTime;
    private  BigDecimal version;
    
    public Date getCheckTime()
    {
        return checkTime;
    }
    
    public BigDecimal getVersion()
    {
        return version;
    }
    
    public LatestVersion(Date checkTime, BigDecimal version)
    {
        this.checkTime = checkTime;
        this.version = version;
    }
}
