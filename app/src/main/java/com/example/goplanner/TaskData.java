package com.example.goplanner;

import com.google.firebase.database.Exclude;

public class TaskData {

    private String documentId;
    private String title;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String desc;
    private String type;

    // No-argument constructor (required for Firebase Firestore)
    public TaskData() {
    }

    // Parameterized constructor without title
    public TaskData(String date, String timeStart, String timeEnd, String desc, String type) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.desc = desc;
        this.type = type;
    }

    // Parameterized constructor with title
    public TaskData(String title, String date, String timeStart, String timeEnd, String desc, String type) {
        this.title = title;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.desc = desc;
        this.type = type;
    }

    // Getters and setters
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
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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
