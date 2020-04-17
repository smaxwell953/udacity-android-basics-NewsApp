package com.example.udacity_android_basics_newsapp;

public class Health  {

    /** Title of the article */
    private String title;

    /** Author of the article */
    private String author;

    /** Date of the article */
    private String date;

    /** URL of the article */
    private String url;

    public Health(String title, String author, String date, String url) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.url = url;
    }

    /**
     * Returns the title of the article.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the author of the article.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the date of the article.
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the website URL to find more information about the article.
     */
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
