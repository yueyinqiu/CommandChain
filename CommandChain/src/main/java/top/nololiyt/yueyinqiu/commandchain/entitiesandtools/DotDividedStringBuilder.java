package top.nololiyt.yueyinqiu.commandchain.entitiesandtools;

public class DotDividedStringBuilder
{
    private StringBuilder stringBuilder;
    
    @Override
    public String toString()
    {
        return stringBuilder.toString();
    }
    
    public DotDividedStringBuilder append(String name)
    {
        this.stringBuilder.append('.')
                .append(name);
        return this;
    }
    
    public DotDividedStringBuilder(String root)
    {
        this.stringBuilder = new StringBuilder();
        this.stringBuilder.append(root);
    }
    
    public DotDividedStringBuilder(DotDividedStringBuilder builder)
    {
        this.stringBuilder = new StringBuilder();
        this.stringBuilder.append(builder.stringBuilder.toString());
    }
}