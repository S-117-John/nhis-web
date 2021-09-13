package com.zebone.nhis.ma.pub.platform.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.config.CustomJsonDateDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class RequestBody implements Serializable {

    public RequestBody() {
    }

    @JSONField(ordinal = 1)
    private ServiceElement service;

    @JSONField(ordinal = 2)
    private EventElement event;

    @JSONField(ordinal = 3)
    private String id;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JSONField(ordinal = 4,format="yyyyMMdd'T'HHmmssSSS")
    private Date creationTime;

    @JSONField(ordinal = 5)
    private SenderElement sender;

    @JSONField(ordinal = 6)
    private ReceiverElement receiver;

    @JSONField(ordinal = 7)
    private Map<String,Object> message;

    public static class Builder{
        private ServiceElement service;
        private EventElement event;
        private String id;
        private Date creationTime;
        private SenderElement sender;
        private ReceiverElement receiver;

        public Builder() {
            this.id = UUID.randomUUID().toString();
            this.creationTime = new Date();
        }

        public Builder service(String code, String name){
            this.service = new ServiceElement(code,name);
            return this;
        }

        public Builder event(String code, String name){
            this.event = new EventElement(code,name);
            return this;
        }

        public Builder sender(){
            this.sender = new SenderElement();
            return this;
        }



        public Builder receiver(){
            this.receiver = new ReceiverElement();
            return this;
        }


        public RequestBody build() {
            return new RequestBody(this);
        }


    }


    public RequestBody(Builder builder){
        this.service = builder.service;
        this.event = builder.event;
        this.id = builder.id;
        this.creationTime = builder.creationTime;
        this.sender = builder.sender;
        this.receiver = builder.receiver;
    }



    public RequestBody(ServiceElement service, EventElement event, String id, Date creationTime, SenderElement sender, ReceiverElement receiver, Map<String, Object> message) {
        this.service = service;
        this.event = event;
        this.id = id;
        this.creationTime = creationTime;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public RequestBody(ServiceElement service, EventElement event, SenderElement sender, ReceiverElement receiver, Map<String, Object> message) {
       this( service,  event, UUID.randomUUID().toString(),  new Date(),  sender,  receiver,  message);
    }

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

    public Map<String, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }
}
