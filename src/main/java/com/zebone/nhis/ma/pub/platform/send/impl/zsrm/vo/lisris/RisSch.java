package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphJsonDateDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;
import java.util.List;

/**
 * 检查预约
 */
public class RisSch extends PhResource {

    private String  status;

    private TextElement cancelationReason;

    private String start;

    private String end;

    private String created;

    private String description;

    private String comment;

    private List<Actor> actor;


    public List<Actor> getActor() {
        return actor;
    }

    public void setActor(List<Actor> actor) {
        this.actor = actor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TextElement getCancelationReason() {
        return cancelationReason;
    }

    public void setCancelationReason(TextElement cancelationReason) {
        this.cancelationReason = cancelationReason;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
