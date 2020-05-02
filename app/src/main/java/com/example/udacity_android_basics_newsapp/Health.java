package com.example.udacity_android_basics_newsapp;

public class Health  {

    /** Section name of the article */
    private String sectionName;

    /** Title of the article */
    private String webTitle;

    /** Author of the article */
    private String webAuthor;

    /** Date of the article */
    private String webPublicationDate;

    /** URL of the article */
    private String webUrl;

    public Health(String sectionName, String webTitle, String webAuthor, String webPublicationDate, String webUrl) {
        this.sectionName = sectionName;
        this.webTitle = webTitle;
        this.webAuthor = webAuthor;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
    }

    /**
     * Returns the section name of the article.
     */
    public String getsectionName() {
        return sectionName;
    }

    /**
     * Returns the title of the article.
     */
    public String getTitle() {
        return webTitle;
    }

    /**
     * Returns the author of the article.
     */
    public String getwebAuthor() {
        return webAuthor;
    }

    /**
     * Returns the date of the article.
     */
    public String getDate() {
        return webPublicationDate;
    }

    /**
     * Returns the website URL to find more information about the article.
     */
    public String getUrl() {
        return webUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "sectionName='" + sectionName + '\'' +
                ", title='" + webTitle + '\'' +
                ", webAuthor='" + webAuthor + '\'' +
                ", date='" + webPublicationDate + '\'' +
                ", url='" + webUrl + '\'' +
                '}';
    }
}
