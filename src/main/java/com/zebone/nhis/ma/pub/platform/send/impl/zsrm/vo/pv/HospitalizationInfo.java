package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * 住院就诊
 */
public class HospitalizationInfo {

    private String resourceType;
    private String id;
    private String type;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    private List<Entry> entry;

    public class Entry{
        private Resource resource;

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }
    }

    public class Resource {

        private String id;
        /**
         * 事件标识
         */
        private String implicitRules;
        private String resourceType;
        private List<Identifier> identifier;
        /**
         * 就诊状态
         */
        private String status;
        private ServiceType serviceType;
        private Clazz clazz;
        /**
         * 病人信息
         */
        private Subject subject;

        /**
         * 医生信息
         */
        private List<Participant> participant;
        /**
         * 就诊时间周期
         */
        private Period period;

        /**
         * 就诊原因
         */
        private List<ReasonCode> reasonCode;

        /**
         * 诊断
         */
        private List<Diagnosis> diagnosis;
        private Hospitalization hospitalization;
        private List<Location> location;
        private List<Extension> extension;
        private List<Destination> destination;
        private Source source;



        public Period getPeriod() {
            return period;
        }

        public void setPeriod(Period period) {
            this.period = period;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImplicitRules() {
            return implicitRules;
        }

        public void setImplicitRules(String implicitRules) {
            this.implicitRules = implicitRules;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public List<Identifier> getIdentifier() {
            return identifier;
        }

        public void setIdentifier(List<Identifier> identifier) {
            this.identifier = identifier;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ServiceType getServiceType() {
            return serviceType;
        }

        public void setServiceType(ServiceType serviceType) {
            this.serviceType = serviceType;
        }

        public Clazz getClazz() {
            return clazz;
        }

        public void setClazz(Clazz clazz) {
            this.clazz = clazz;
        }

        public Subject getSubject() {
            return subject;
        }

        public void setSubject(Subject subject) {
            this.subject = subject;
        }

        public List<Participant> getParticipant() {
            return participant;
        }

        public void setParticipant(List<Participant> participant) {
            this.participant = participant;
        }



        public List<ReasonCode> getReasonCode() {
            return reasonCode;
        }

        public void setReasonCode(List<ReasonCode> reasonCode) {
            this.reasonCode = reasonCode;
        }

        public List<Diagnosis> getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(List<Diagnosis> diagnosis) {
            this.diagnosis = diagnosis;
        }

        public Hospitalization getHospitalization() {
            return hospitalization;
        }

        public void setHospitalization(Hospitalization hospitalization) {
            this.hospitalization = hospitalization;
        }

        public List<Location> getLocation() {
            return location;
        }

        public void setLocation(List<Location> location) {
            this.location = location;
        }

        public List<Extension> getExtension() {
            return extension;
        }

        public void setExtension(List<Extension> extension) {
            this.extension = extension;
        }

        public List<Destination> getDestination() {
            return destination;
        }

        public void setDestination(List<Destination> destination) {
            this.destination = destination;
        }

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }
    }

    public class Destination {

        private String endpoint;
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        public String getEndpoint() {
            return endpoint;
        }

    }

    public class Source {

        private String endpoint;
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        public String getEndpoint() {
            return endpoint;
        }

    }

    public class Identifier {

        private String system;
        private String value;
        public void setSystem(String system) {
            this.system = system;
        }
        public String getSystem() {
            return system;
        }

        public void setValue(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

    }

    public class ServiceType {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }

    public class Coding {

        private String system;
        private String code;
        private String display;
        public void setSystem(String system) {
            this.system = system;
        }
        public String getSystem() {
            return system;
        }

        public void setCode(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
        public String getDisplay() {
            return display;
        }

    }

    public class Clazz {

        private String system;
        private String code;
        private String display;
        public void setSystem(String system) {
            this.system = system;
        }
        public String getSystem() {
            return system;
        }

        public void setCode(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
        public String getDisplay() {
            return display;
        }

    }

    public class Subject {

        private String resourceType;
        /**
         * 病人标识
         */
        private List<Identifier> identifier;
        /**
         * 病人姓名
         */
        private List<Name> name;
        /**
         * 联系方式
         */
        private List<Telecom> telecom;
        private Date birthDate;
        /**
         * 患者地址
         */
        private List<Address> address;

        /**
         * 婚姻状况
         */
        private MaritalStatus maritalStatus;


        /**
         * 患者联系人
         */
        private List<Contact> contact;


        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }
        public String getResourceType() {
            return resourceType;
        }

        public void setIdentifier(List<Identifier> identifier) {
            this.identifier = identifier;
        }
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        public void setName(List<Name> name) {
            this.name = name;
        }
        public List<Name> getName() {
            return name;
        }

        public void setTelecom(List<Telecom> telecom) {
            this.telecom = telecom;
        }
        public List<Telecom> getTelecom() {
            return telecom;
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }
        public Date getBirthDate() {
            return birthDate;
        }

        public void setAddress(List<Address> address) {
            this.address = address;
        }
        public List<Address> getAddress() {
            return address;
        }

        public void setMaritalStatus(MaritalStatus maritalStatus) {
            this.maritalStatus = maritalStatus;
        }
        public MaritalStatus getMaritalStatus() {
            return maritalStatus;
        }

        public void setContact(List<Contact> contact) {
            this.contact = contact;
        }
        public List<Contact> getContact() {
            return contact;
        }

    }

    public class Name {

        /**
         * 姓名文本
         */
        private String text;
        public void setText(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }

    }

    public class Telecom {

        private String system;
        private String value;
        public void setSystem(String system) {
            this.system = system;
        }
        public String getSystem() {
            return system;
        }

        public void setValue(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

    }

    public class Address {

        /**
         * 地址的文本内容
         */
        private String text;
        /**
         * 村（街、路、弄等）+门牌号
         */
        private List<String> line;
        /**
         * 市
         */
        private String city;
        /**
         * 区县
         */
        private String district;
        /**
         * 省/自治区/直辖市/特别行政区
         */
        private String state;
        /**
         * 邮编
         */
        private String postalCode;


        private String country;



        public void setText(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }

        public void setLine(List<String> line) {
            this.line = line;
        }
        public List<String> getLine() {
            return line;
        }

        public void setCity(String city) {
            this.city = city;
        }
        public String getCity() {
            return city;
        }

        public void setDistrict(String district) {
            this.district = district;
        }
        public String getDistrict() {
            return district;
        }

        public void setState(String state) {
            this.state = state;
        }
        public String getState() {
            return state;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
        public String getPostalCode() {
            return postalCode;
        }

        public void setCountry(String country) {
            this.country = country;
        }
        public String getCountry() {
            return country;
        }

    }

    public class MaritalStatus {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }

    public class Contact {

        /**
         * 联系人关系
         */
        private List<Relationship> relationship;

        /**
         * 联系人姓名
         */
        private Name name;

        /**
         * 患者联系人联系方式
         */
        private List<Telecom> telecom;

        public void setRelationship(List<Relationship> relationship) {
            this.relationship = relationship;
        }
        public List<Relationship> getRelationship() {
            return relationship;
        }

        public void setName(Name name) {
            this.name = name;
        }
        public Name getName() {
            return name;
        }

        public void setTelecom(List<Telecom> telecom) {
            this.telecom = telecom;
        }
        public List<Telecom> getTelecom() {
            return telecom;
        }

    }

    public class Relationship {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }

    public class ReasonCode {

        private String text;
        public void setText(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }

    }

    public class Location {

        private String resourceType;
        private List<Identifier> identifier;
        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }
        public String getResourceType() {
            return resourceType;
        }

        public void setIdentifier(List<Identifier> identifier) {
            this.identifier = identifier;
        }
        public List<Identifier> getIdentifier() {
            return identifier;
        }

    }

    public class Extension {

        private String url;
        private Date valueDatetime;
        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setValueDatetime(Date valueDatetime) {
            this.valueDatetime = valueDatetime;
        }
        public Date getValueDatetime() {
            return valueDatetime;
        }

    }

    public class Participant {

        /**
         * 医生类型
         */
        private List<Type> type;
        public void setType(List<Type> type) {
            this.type = type;
        }
        public List<Type> getType() {
            return type;
        }

    }

    public class Type {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }

    public class Period {

        /**
         * 入院时间
         */
        private Date start;

        /**
         * 出院时间
         */
        private Date end;
        public void setStart(Date start) {
            this.start = start;
        }
        public Date getStart() {
            return start;
        }

        public void setEnd(Date end) {
            this.end = end;
        }
        public Date getEnd() {
            return end;
        }

    }

    public class Diagnosis {

        private Condition condition;
        private Use use;
        private String rank;
        public void setCondition(Condition condition) {
            this.condition = condition;
        }
        public Condition getCondition() {
            return condition;
        }

        public void setUse(Use use) {
            this.use = use;
        }
        public Use getUse() {
            return use;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
        public String getRank() {
            return rank;
        }

    }

    public class Condition {

        private String resourceType;
        private Code code;
        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }
        public String getResourceType() {
            return resourceType;
        }

        public void setCode(Code code) {
            this.code = code;
        }
        public Code getCode() {
            return code;
        }

    }

    public class Code {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }

    public class Use {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }


    public class Hospitalization {

        /**
         * 病人来源
         */
        private AdmitSource admitSource;
        private List<DietPreference> dietPreference;
        private List<SpecialArrangement> specialArrangement;
        private DischargeDisposition dischargeDisposition;


        public void setAdmitSource(AdmitSource admitSource) {
            this.admitSource = admitSource;
        }
        public AdmitSource getAdmitSource() {
            return admitSource;
        }

        public void setDietPreference(List<DietPreference> dietPreference) {
            this.dietPreference = dietPreference;
        }
        public List<DietPreference> getDietPreference() {
            return dietPreference;
        }

        public void setSpecialArrangement(List<SpecialArrangement> specialArrangement) {
            this.specialArrangement = specialArrangement;
        }
        public List<SpecialArrangement> getSpecialArrangement() {
            return specialArrangement;
        }

        public void setDischargeDisposition(DischargeDisposition dischargeDisposition) {
            this.dischargeDisposition = dischargeDisposition;
        }
        public DischargeDisposition getDischargeDisposition() {
            return dischargeDisposition;
        }

    }

    public class AdmitSource {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }

    public class DietPreference {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }


    public class SpecialArrangement {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }

    public class DischargeDisposition {

        private List<Coding> coding;
        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
        public List<Coding> getCoding() {
            return coding;
        }

    }
































    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }



}
