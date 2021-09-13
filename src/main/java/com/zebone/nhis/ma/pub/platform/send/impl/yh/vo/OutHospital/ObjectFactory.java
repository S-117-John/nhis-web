//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.18 at 01:42:36 PM CST 
//


package com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.OutHospital;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SourceSysCode_QNAME = new QName("", "SourceSysCode");
    private final static QName _DISDATE_QNAME = new QName("", "DIS_DATE");
    private final static QName _PATIENTID_QNAME = new QName("", "PATIENT_ID");
    private final static QName _ReturnFlag_QNAME = new QName("", "ReturnFlag");
    private final static QName _Password_QNAME = new QName("", "Password");
    private final static QName _Fid_QNAME = new QName("", "Fid");
    private final static QName _UserName_QNAME = new QName("", "UserName");
    private final static QName _ADMISSTIMES_QNAME = new QName("", "ADMISS_TIMES");
    private final static QName _CURRDEPT_QNAME = new QName("", "CURR_DEPT");
    private final static QName _MsgDate_QNAME = new QName("", "MsgDate");
    private final static QName _TargetSysCode_QNAME = new QName("", "TargetSysCode");
    private final static QName _SysFlag_QNAME = new QName("", "SysFlag");
    private final static QName _CURRWARD_QNAME = new QName("", "CURR_WARD");
    private final static QName _CURROPERA_QNAME = new QName("", "CURR_OPERA");
    private final static QName _CURRBED_QNAME = new QName("", "CURR_BED");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MsgInfo }
     * 
     */
    public MsgInfo createMsgInfo() {
        return new MsgInfo();
    }

    /**
     * Create an instance of {@link OutPatientInfo }
     * 
     */
    public OutPatientInfo createMsg() {
        return new OutPatientInfo();
    }

    /**
     * Create an instance of {@link MessageHeader }
     * 
     */
    public MessageHeader createMessageHeader() {
        return new MessageHeader();
    }

    /**
     * Create an instance of {@link AccessControl }
     * 
     */
    public AccessControl createAccessControl() {
        return new AccessControl();
    }

    /**
     * Create an instance of {@link OutHospitalESBEntry }
     * 
     */
    public OutHospitalESBEntry createESBEntry() {
        return new OutHospitalESBEntry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SourceSysCode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSourceSysCode(String value) {
        return new JAXBElement<String>(_SourceSysCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DIS_DATE")
    public JAXBElement<String> createDISDATE(String value) {
        return new JAXBElement<String>(_DISDATE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PATIENT_ID")
    public JAXBElement<String> createPATIENTID(String value) {
        return new JAXBElement<String>(_PATIENTID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ReturnFlag")
    public JAXBElement<BigInteger> createReturnFlag(BigInteger value) {
        return new JAXBElement<BigInteger>(_ReturnFlag_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Password")
    public JAXBElement<BigInteger> createPassword(BigInteger value) {
        return new JAXBElement<BigInteger>(_Password_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Fid")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFid(String value) {
        return new JAXBElement<String>(_Fid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "UserName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createUserName(String value) {
        return new JAXBElement<String>(_UserName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ADMISS_TIMES")
    public JAXBElement<String> createADMISSTIMES(String value) {
        return new JAXBElement<String>(_ADMISSTIMES_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CURR_DEPT")
    public JAXBElement<String> createCURRDEPT(String value) {
        return new JAXBElement<String>(_CURRDEPT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MsgDate")
    public JAXBElement<String> createMsgDate(String value) {
        return new JAXBElement<String>(_MsgDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TargetSysCode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTargetSysCode(String value) {
        return new JAXBElement<String>(_TargetSysCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SysFlag")
    public JAXBElement<BigInteger> createSysFlag(BigInteger value) {
        return new JAXBElement<BigInteger>(_SysFlag_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CURR_WARD")
    public JAXBElement<String> createCURRWARD(String value) {
        return new JAXBElement<String>(_CURRWARD_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CURR_OPERA")
    public JAXBElement<String> createCURROPERA(String value) {
        return new JAXBElement<String>(_CURROPERA_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CURR_BED")
    public JAXBElement<String> createCURRBED(String value) {
        return new JAXBElement<String>(_CURRBED_QNAME, String.class, null, value);
    }

}
