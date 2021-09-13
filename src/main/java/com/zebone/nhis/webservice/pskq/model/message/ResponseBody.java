package com.zebone.nhis.webservice.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.webservice.pskq.config.CustomJsonDateDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ResponseBody implements Serializable {

    public ResponseBody() {
    }

    @JSONField(ordinal = 1)
    private ServiceElement service;

    @JSONField(ordinal = 2)
    private EventElement event;

    @JSONField(ordinal = 3)
    private String id;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JSONField(format="yyyyMMdd'T'HHmmss",ordinal = 4)
    private Date creationTime;

    @JSONField(ordinal = 5)
    private SenderElement sender;


    @JSONField(ordinal = 6)
    private ReceiverElement receiver;

    @JSONField(ordinal = 7)
    private Map<String,Object> ack;

    @JSONField(ordinal = 8)
    private AckElement queryAck;

    public static ResponseBody newInstance() {
        return new ResponseBody();
    }

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

        public Builder service(ServiceElement service){
            this.service = service;
            return this;
        }

        public Builder event(EventElement event){
            this.event = event;
            return this;
        }

        public Builder sender(SenderElement sender){
            this.sender = sender;
            return this;
        }

        public Builder sender(ReceiverElement receiver){
            this.sender = new SenderElement(receiver.getId(),
                    receiver.getSoftwareName(),
                    receiver.getSoftwareProvider(),
                    receiver.getOrganization());
            return this;
        }

        public Builder receiver(ReceiverElement receiver){
            this.receiver = receiver;
            return this;
        }

        public Builder receiver(SenderElement sender){
            this.receiver = new ReceiverElement(sender.getId(),
                    sender.getSoftwareName(),
                    sender.getSoftwareProvider(),
                    sender.getOrganization());
            return this;
        }

        public ResponseBody build() {
            return new ResponseBody(this);
        }


    }

    private ResponseBody(Builder builder){
        this.service = builder.service;
        this.event = builder.event;
        this.id = builder.id;
        this.creationTime = builder.creationTime;
        this.sender = builder.sender;
        this.receiver = builder.receiver;
    }


    public AckElement getQueryAck() {
        return queryAck;
    }

    public void setQueryAck(AckElement queryAck) {
        this.queryAck = queryAck;
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

    public Map<String, Object> getAck() {
        return ack;
    }

    public void setAck(Map<String, Object> ack) {
        this.ack = ack;
    }
}
