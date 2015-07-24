package com.levelup.palabre.flickrforpalabre.flickr.data;

public class UserLoginResponse
{
    private User user;

    private String stat;

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    public String getStat ()
    {
        return stat;
    }

    public void setStat (String stat)
    {
        this.stat = stat;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [user = "+user+", stat = "+stat+"]";
    }
}
