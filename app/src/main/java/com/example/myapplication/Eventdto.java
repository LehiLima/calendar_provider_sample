package com.example.myapplication;

public class Eventdto {
    private String id;
    private String organizer;
    private String title;
    private String description;
    private String startdate;
    private String enddate;
    private String eventlocation;

    public Eventdto(String id, String organizer, String title, String description, String startdate, String enddate, String eventlocation) {
        this.id = id;
        this.organizer = organizer;
        this.title = title;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
        this.eventlocation = eventlocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getEventlocation() {
        return eventlocation;
    }

    public void setEventlocation(String eventlocation) {
        this.eventlocation = eventlocation;
    }
}
