package com.levelup.palabre.flickrforpalabre.flickr.data.contactresponse;

public class Contact
{
    private String ignored;

    private String username;

    private String location;

    private String family;

    private String iconserver;

    private String realname;

    private String path_alias;

    private String nsid;

    private String iconfarm;

    private String friend;

    private String rev_ignored;

    public String getIgnored ()
    {
        return ignored;
    }

    public void setIgnored (String ignored)
    {
        this.ignored = ignored;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
    }

    public String getFamily ()
    {
        return family;
    }

    public void setFamily (String family)
    {
        this.family = family;
    }

    public String getIconserver ()
    {
        return iconserver;
    }

    public void setIconserver (String iconserver)
    {
        this.iconserver = iconserver;
    }

    public String getRealname ()
    {
        return realname;
    }

    public void setRealname (String realname)
    {
        this.realname = realname;
    }

    public String getPath_alias ()
    {
        return path_alias;
    }

    public void setPath_alias (String path_alias)
    {
        this.path_alias = path_alias;
    }

    public String getNsid ()
    {
        return nsid;
    }

    public void setNsid (String nsid)
    {
        this.nsid = nsid;
    }

    public String getIconfarm ()
    {
        return iconfarm;
    }

    public void setIconfarm (String iconfarm)
    {
        this.iconfarm = iconfarm;
    }

    public String getFriend ()
    {
        return friend;
    }

    public void setFriend (String friend)
    {
        this.friend = friend;
    }

    public String getRev_ignored ()
    {
        return rev_ignored;
    }

    public void setRev_ignored (String rev_ignored)
    {
        this.rev_ignored = rev_ignored;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ignored = "+ignored+", username = "+username+", location = "+location+", family = "+family+", iconserver = "+iconserver+", realname = "+realname+", path_alias = "+path_alias+", nsid = "+nsid+", iconfarm = "+iconfarm+", friend = "+friend+", rev_ignored = "+rev_ignored+"]";
    }
}
