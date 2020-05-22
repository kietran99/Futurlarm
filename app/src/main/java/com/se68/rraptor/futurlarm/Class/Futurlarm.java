package com.se68.rraptor.futurlarm.Class;

import java.io.Serializable;

public class Futurlarm implements Serializable {
    private String name, time, date, eventIcon, sender;
    private boolean important;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventIcon() {
        return eventIcon;
    }

    public void setEventIcon(String eventIcon) {
        this.eventIcon = eventIcon;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public String getHour() {
        return time.substring(0, 2);
    }

    public boolean equals(Futurlarm futurlarm){
        if (this.getName().equals(futurlarm.getName()) &&
        this.getDate().equals(futurlarm.getDate()) &&
                this.getTime().equals(futurlarm.getTime()) &&
        this.getSender().equals(futurlarm.getSender()))
            return equals(futurlarm);
        return false;
    }
}
