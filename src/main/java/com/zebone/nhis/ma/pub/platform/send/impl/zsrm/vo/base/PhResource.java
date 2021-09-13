package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;
import java.util.List;

public class PhResource {

    private String resourceType;
    private String implicitRules;
    private String id;
    private List<Identifier> identifier;
    private List<Extension> extension;
    private List<Destination> destination;
    private Destination source;

    /**标识为his机构号*/
    private String hospitalId;
    /** 查询时用到的结构体*/
    private List<Parameter> parameter;


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getImplicitRules() {
        return implicitRules;
    }

    public void setImplicitRules(String implicitRules) {
        this.implicitRules = implicitRules;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public List<Extension> getExtension() {
        return extension;
    }

    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Destination getSource() {
        return source;
    }

    public void setSource(Destination source) {
        this.source = source;
    }

    public List<Destination> getDestination() {
        return destination;
    }

    public void setDestination(List<Destination> destination) {
        this.destination = destination;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    /**
     * 获得查询参数
     * @return
     */
    public List<Parameter> getParameter() {
        return parameter;
    }

    /**
     * 设置查询参数
     * @param parameter
     */
    public void setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
    }

}