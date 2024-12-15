package com.example.goplanner;

public class TaskData {

    private String title;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String desc;
    private String type;

    public TaskData(String date, String timeStart, String timeEnd, String desc, String type) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.desc = desc;
        this.type = type;
    }

    public TaskData(String title, String date, String timeStart, String timeEnd, String desc, String type) {
        title = title;
        date = date;
        timeStart = timeStart;
        timeEnd = timeEnd;
        desc = desc;
        type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
