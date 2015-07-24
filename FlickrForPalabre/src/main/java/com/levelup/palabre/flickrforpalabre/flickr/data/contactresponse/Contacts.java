package com.levelup.palabre.flickrforpalabre.flickr.data.contactresponse;

public class Contacts
{
    private String total;

    private String per_page;

    private String page;

    private String pages;

    private Contact[] contact;

    private String perpage;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public String getPer_page ()
    {
        return per_page;
    }

    public void setPer_page (String per_page)
    {
        this.per_page = per_page;
    }

    public String getPage ()
    {
        return page;
    }

    public void setPage (String page)
    {
        this.page = page;
    }

    public String getPages ()
    {
        return pages;
    }

    public void setPages (String pages)
    {
        this.pages = pages;
    }

    public Contact[] getContact ()
    {
        return contact;
    }

    public void setContact (Contact[] contact)
    {
        this.contact = contact;
    }

    public String getPerpage ()
    {
        return perpage;
    }

    public void setPerpage (String perpage)
    {
        this.perpage = perpage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total = "+total+", per_page = "+per_page+", page = "+page+", pages = "+pages+", contact = "+contact+", perpage = "+perpage+"]";
    }
}
