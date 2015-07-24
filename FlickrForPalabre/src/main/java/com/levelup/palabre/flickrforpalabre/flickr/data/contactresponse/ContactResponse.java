package com.levelup.palabre.flickrforpalabre.flickr.data.contactresponse;

public class ContactResponse
{
    private String stat;

    private Contacts contacts;

    public String getStat ()
    {
        return stat;
    }

    public void setStat (String stat)
    {
        this.stat = stat;
    }

    public Contacts getContacts ()
    {
        return contacts;
    }

    public void setContacts (Contacts contacts)
    {
        this.contacts = contacts;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [stat = "+stat+", contacts = "+contacts+"]";
    }
}
