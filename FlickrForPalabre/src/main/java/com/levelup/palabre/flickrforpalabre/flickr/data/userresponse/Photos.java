package com.levelup.palabre.flickrforpalabre.flickr.data.userresponse;

public class Photos
{
    private Firstdatetaken firstdatetaken;

    private Firstdate firstdate;

    private Count count;

    private Views views;

    public Firstdatetaken getFirstdatetaken ()
    {
        return firstdatetaken;
    }

    public void setFirstdatetaken (Firstdatetaken firstdatetaken)
    {
        this.firstdatetaken = firstdatetaken;
    }

    public Firstdate getFirstdate ()
    {
        return firstdate;
    }

    public void setFirstdate (Firstdate firstdate)
    {
        this.firstdate = firstdate;
    }

    public Count getCount ()
    {
        return count;
    }

    public void setCount (Count count)
    {
        this.count = count;
    }

    public Views getViews ()
    {
        return views;
    }

    public void setViews (Views views)
    {
        this.views = views;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [firstdatetaken = "+firstdatetaken+", firstdate = "+firstdate+", count = "+count+", views = "+views+"]";
    }
}
