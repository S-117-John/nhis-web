//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.15 at 06:26:44 PM CST 
//


package com.zebone.nhis.ma.pub.platform.receive.yh.vo.PrePay;

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

    private final static QName _DEPTINDEXNO_QNAME = new QName("", "DEPT_INDEX_NO");
    private final static QName _PAYMETHODNAME_QNAME = new QName("", "PAY_METHOD_NAME");
    private final static QName _SourceSysCode_QNAME = new QName("", "SourceSysCode");
    private final static QName _PROSPAY_QNAME = new QName("", "PROS_PAY");
    private final static QName _PAYCATEGNAME_QNAME = new QName("", "PAY_CATEG_NAME");
    private final static QName _ReturnFlag_QNAME = new QName("", "ReturnFlag");
    private final static QName _Password_QNAME = new QName("", "Password");
    private final static QName _Fid_QNAME = new QName("", "Fid");
    private final static QName _UserName_QNAME = new QName("", "UserName");
    private final static QName _INHOSPINDEXNO_QNAME = new QName("", "INHOSP_INDEX_NO");
    private final static QName _MsgDate_QNAME = new QName("", "MsgDate");
    private final static QName _TargetSysCode_QNAME = new QName("", "TargetSysCode");
    private final static QName _RECORDDRCODE_QNAME = new QName("", "RECORD_DR_CODE");
    private final static QName _SysFlag_QNAME = new QName("", "SysFlag");
    private final static QName _PATINDEXNO_QNAME = new QName("", "PAT_INDEX_NO");
    private final static QName _CHARGEFLAG_QNAME = new QName("", "CHARGE_FLAG");
    private final static QName _RECEIPTINDEXNO_QNAME = new QName("", "RECEIPT_INDEX_NO");
    private final static QName _RECEIPTNO_QNAME = new QName("", "RECEIPT_NO");

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
     * Create an instance of {@link PrePayInfo }
     * 
     */
    public PrePayInfo createMsg() {
        return new PrePayInfo();
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
     * Create an instance of {@link PrePayESBEntry }
     * 
     */
    public PrePayESBEntry createESBEntry() {
        return new PrePayESBEntry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DEPT_INDEX_NO")
    public JAXBElement<String> createDEPTINDEXNO(String value) {
        return new JAXBElement<String>(_DEPTINDEXNO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PAY_METHOD_NAME")
    public JAXBElement<String> createPAYMETHODNAME(String value) {
        return new JAXBElement<String>(_PAYMETHODNAME_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "PROS_PAY")
    public JAXBElement<String> createPROSPAY(String value) {
        return new JAXBElement<String>(_PROSPAY_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PAY_CATEG_NAME")
    public JAXBElement<String> createPAYCATEGNAME(String value) {
        return new JAXBElement<String>(_PAYCATEGNAME_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "INHOSP_INDEX_NO")
    public JAXBElement<String> createINHOSPINDEXNO(String value) {
        return new JAXBElement<String>(_INHOSPINDEXNO_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RECORD_DR_CODE")
    public JAXBElement<String> createRECORDDRCODE(String value) {
        return new JAXBElement<String>(_RECORDDRCODE_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "PAT_INDEX_NO")
    public JAXBElement<String> createPATINDEXNO(String value) {
        return new JAXBElement<String>(_PATINDEXNO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CHARGE_FLAG")
    public JAXBElement<String> createCHARGEFLAG(String value) {
        return new JAXBElement<String>(_CHARGEFLAG_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RECEIPT_INDEX_NO")
    public JAXBElement<String> createRECEIPTINDEXNO(String value) {
        return new JAXBElement<String>(_RECEIPTINDEXNO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RECEIPT_NO")
    public JAXBElement<String> createRECEIPTNO(String value) {
        return new JAXBElement<String>(_RECEIPTNO_QNAME, String.class, null, value);
    }

}
