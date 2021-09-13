package com.zebone.nhis.webservice.pskq.model.message;

public class EventElement {

    public EventElement() {
    }

    public EventElement(String eventCode, String eventName) {
        this.eventCode = eventCode;
        this.eventName = eventName;
    }

    private String eventCode;

    private String eventName;

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
