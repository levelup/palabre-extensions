package com.levelup.palabre.flickrforpalabre.flickr.data;

public class User
{
    private String id;

    private Username username;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Username getUsername ()
    {
        return username;
    }

    public void setUsername (Username username)
    {
        this.username = username;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", username = "+username+"]";
    }
}
