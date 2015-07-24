package com.levelup.palabre.flickrforpalabre.flickr.data.groupresponse;

public class Group
{
    private String is_moderator;

    private String invitation_only;

    private String is_admin;

    private String eighteenplus;

    private String is_member;

    private String admin;

    private String name;

    private String iconserver;

    private String nsid;

    private String iconfarm;

    private String members;

    private String pool_count;

    public String getIs_moderator ()
    {
        return is_moderator;
    }

    public void setIs_moderator (String is_moderator)
    {
        this.is_moderator = is_moderator;
    }

    public String getInvitation_only ()
    {
        return invitation_only;
    }

    public void setInvitation_only (String invitation_only)
    {
        this.invitation_only = invitation_only;
    }

    public String getIs_admin ()
    {
        return is_admin;
    }

    public void setIs_admin (String is_admin)
    {
        this.is_admin = is_admin;
    }

    public String getEighteenplus ()
    {
        return eighteenplus;
    }

    public void setEighteenplus (String eighteenplus)
    {
        this.eighteenplus = eighteenplus;
    }

    public String getIs_member ()
    {
        return is_member;
    }

    public void setIs_member (String is_member)
    {
        this.is_member = is_member;
    }

    public String getAdmin ()
    {
        return admin;
    }

    public void setAdmin (String admin)
    {
        this.admin = admin;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getIconserver ()
    {
        return iconserver;
    }

    public void setIconserver (String iconserver)
    {
        this.iconserver = iconserver;
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

    public String getMembers ()
    {
        return members;
    }

    public void setMembers (String members)
    {
        this.members = members;
    }

    public String getPool_count ()
    {
        return pool_count;
    }

    public void setPool_count (String pool_count)
    {
        this.pool_count = pool_count;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [is_moderator = "+is_moderator+", invitation_only = "+invitation_only+", is_admin = "+is_admin+", eighteenplus = "+eighteenplus+", is_member = "+is_member+", admin = "+admin+", name = "+name+", iconserver = "+iconserver+", nsid = "+nsid+", iconfarm = "+iconfarm+", members = "+members+", pool_count = "+pool_count+"]";
    }
}