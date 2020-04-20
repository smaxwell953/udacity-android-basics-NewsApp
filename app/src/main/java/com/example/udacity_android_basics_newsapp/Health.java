package com.example.udacity_android_basics_newsapp;

public class Health  {

    /** Type of the article */
    private String type;

    /** Title of the article */
    private String webTitle;

    /** Date of the article */
    private String webPublicationDate;

    /** URL of the article */
    private String webUrl;

    public Health(String type, String webTitle, String webPublicationDate, String webUrl) {
        this.type = type;
        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
    }

    /**
     * Returns the type of the article.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the title of the article.
     */
    public String getTitle() {
        return webTitle;
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
                "type='" + type + '\'' +
                ", title='" + webTitle + '\'' +
                ", date='" + webPublicationDate + '\'' +
                ", url='" + webUrl + '\'' +
                '}';
    }
}
