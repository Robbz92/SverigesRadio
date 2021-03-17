package com.example.demo.configs;

public class GenericObject {

    private Object id;
    private Object name;
    private Object image;
    private Object tagline;
    private Object siteUrl;
    private Object scheduleUrl;

    // Konstruktor för radiokanaler
    public GenericObject(Object id, Object name, Object image, Object tagline, Object siteUrl, Object scheduleUrl) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.tagline = tagline;
        this.siteUrl = siteUrl;
        this.scheduleUrl = scheduleUrl;
    }



    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public Object getTagline() {
        return tagline;
    }

    public void setTagline(Object tagline) {
        this.tagline = tagline;
    }

    public Object getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(Object siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Object getScheduleUrl() {
        return scheduleUrl;
    }

    public void setScheduleUrl(Object scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
    }
}
