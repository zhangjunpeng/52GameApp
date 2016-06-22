package com.test4s.gdb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table NEWS_INFO.
 */
public class NewsInfo {

    private Long id;
    private String ueser_id;
    private String title;
    private String views;
    private String comments;
    private String cover_img;
    private String time;
    private String url;

    public NewsInfo() {
    }

    public NewsInfo(Long id) {
        this.id = id;
    }

    public NewsInfo(Long id, String ueser_id, String title, String views, String comments, String cover_img, String time, String url) {
        this.id = id;
        this.ueser_id = ueser_id;
        this.title = title;
        this.views = views;
        this.comments = comments;
        this.cover_img = cover_img;
        this.time = time;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUeser_id() {
        return ueser_id;
    }

    public void setUeser_id(String ueser_id) {
        this.ueser_id = ueser_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}