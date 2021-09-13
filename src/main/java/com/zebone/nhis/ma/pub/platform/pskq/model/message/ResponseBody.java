package com.zebone.nhis.ma.pub.platform.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.config.CustomJsonDateDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ResponseBody implements Serializable {

    public ResponseBody() {
    }

    private ServiceElement service;


    private EventElement event;


    private String id;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date creationTime;


    private SenderElement sender;


    private ReceiverElement receiver;


    private Map<String,Object> ack;



    public ServiceElement getService() {
        return service;
    }

    public void setService(ServiceElement service) {
        this.service = service;
    }

    public EventElement getEvent() {
        return event;
    }

    public void setEvent(EventElement event) {
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public SenderElement getSender() {
        return sender;
    }

    public void setSender(SenderElement sender) {
        this.sender = sender;
    }

    public ReceiverElement getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverElement receiver) {
        this.receiver = receiver;
    }

    public Map<String, Object> getAck() {
        return ack;
    }

    public void setAck(Map<String, Object> ack) {
        this.ack = ack;
    }
}
