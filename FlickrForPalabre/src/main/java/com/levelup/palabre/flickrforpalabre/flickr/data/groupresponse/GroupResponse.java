package com.levelup.palabre.flickrforpalabre.flickr.data.groupresponse;

public class GroupResponse
{
    private String stat;

    private Groups groups;

    public String getStat ()
    {
        return stat;
    }

    public void setStat (String stat)
    {
        this.stat = stat;
    }

    public Groups getGroups ()
    {
        return groups;
    }

    public void setGroups (Groups groups)
    {
        this.groups = groups;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [stat = "+stat+", groups = "+groups+"]";
    }
}