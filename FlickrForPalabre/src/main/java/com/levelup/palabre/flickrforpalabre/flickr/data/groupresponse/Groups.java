package com.levelup.palabre.flickrforpalabre.flickr.data.groupresponse;

public class Groups
{
    private Group[] group;

    public Group[] getGroup ()
    {
        return group;
    }

    public void setGroup (Group[] group)
    {
        this.group = group;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [group = "+group+"]";
    }
}

			