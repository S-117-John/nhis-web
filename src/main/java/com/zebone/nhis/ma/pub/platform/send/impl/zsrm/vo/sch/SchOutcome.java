package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Period;

import java.util.List;

public class SchOutcome extends Outcome {

    /**号源类型*/
    private List<CodeableConcept>  serviceCategory;

    /**号源午别*/
    private TextElement serviceType;

    /**科室、人员*/
    private List<Outcome> actor;

    /**号源周期*/
    private Period planningHorizon;

    private List<Extension> extension;

//    extension 节点忽略掉，排班与挂号费关系


    public List<CodeableConcept> getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(List<CodeableConcept> serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public TextElement getServiceType() {
        return serviceType;
    }

    public void setServiceType(TextElement serviceType) {
        this.serviceType = serviceType;
    }

    public List<Outcome> getActor() {
        return actor;
    }

    public void setActor(List<Outcome> actor) {
        this.actor = actor;
    }

    public Period getPlanningHorizon() {
        return planningHorizon;
    }

    public void setPlanningHorizon(Period planningHorizon) {
        this.planningHorizon = planningHorizon;
    }

    public List<Extension> getExtension() {
        return extension;
    }

    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }
}
