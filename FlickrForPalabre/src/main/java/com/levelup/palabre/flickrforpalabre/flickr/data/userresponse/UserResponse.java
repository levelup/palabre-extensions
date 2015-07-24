package com.levelup.palabre.flickrforpalabre.flickr.data.userresponse;

public class UserResponse
{
    private Person person;

    private String stat;

    public Person getPerson ()
    {
        return person;
    }

    public void setPerson (Person person)
    {
        this.person = person;
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
        return "ClassPojo [person = "+person+", stat = "+stat+"]";
    }
}
