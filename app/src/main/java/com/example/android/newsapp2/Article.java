package com.example.android.newsapp2;

public class Article {
    private String mSection;
    private String mTitle;
    private String mUrl;
    private String mDate;
    private String mAuthor;

    public Article(String section, String Title, String url, String date, String author) {
        this.mSection = section;
        this.mTitle = Title;
        this.mUrl = url;
        this.mDate = date;
        this.mAuthor = author;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
