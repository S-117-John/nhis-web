//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.15 at 02:39:56 PM CST 
//


package com.zebone.nhis.ma.pub.platform.receive.yh.vo.StrikeSettle;

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
    private final static QName _SELFINDEXNO_QNAME = new QName("", "SELF_INDEX_NO");
    private final static QName _INVOICENO_QNAME = new QName("", "INVOICE_NO");
    private final static QName _ReturnFlag_QNAME = new QName("", "ReturnFlag");
    private final static QName _Password_QNAME = new QName("", "Password");
    private final static QName _DEPTINDEXNOAPP_QNAME = new QName("", "DEPT_INDEX_NO_APP");
    private final static QName _Fid_QNAME = new QName("", "Fid");
    private final static QName _MFSRESULTCODE_QNAME = new QName("", "MFS_RESULT_CODE");
    private final static QName _UserName_QNAME = new QName("", "UserName");
    private final static QName _PREPAIDMONEY_QNAME = new QName("", "PREPAID_MONEY");
    private final static QName _INHOSPINDEXNO_QNAME = new QName("", "INHOSP_INDEX_NO");
    private final static QName _MsgDate_QNAME = new QName("", "MsgDate");
    private final static QName _RECEIPTTYPE_QNAME = new QName("", "RECEIPT_TYPE");
    private final static QName _TargetSysCode_QNAME = new QName("", "TargetSysCode");
    private final static QName _MFSMETHODCODE_QNAME = new QName("", "MFS_METHOD_CODE");
    private final static QName _SELFTOTALPAYMENT_QNAME = new QName("", "SELF_TOTAL_PAYMENT");
    private final static QName _SysFlag_QNAME = new QName("", "SysFlag");
    private final static QName _PATINDEXNO_QNAME = new QName("", "PAT_INDEX_NO");
    private final static QName _SELFPAYMENTFEE_QNAME = new QName("", "SELF_PAYMENT_FEE");
    private final static QName _RECEIPTINDEXNO_QNAME = new QName("", "RECEIPT_INDEX_NO");
    private final static QName _MFSTYPECODE_QNAME = new QName("", "MFS_TYPE_CODE");

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
     * Create an instance of {@link StrikeSettleInfo }
     * 
     */
    public StrikeSettleInfo createMsg() {
        return new StrikeSettleInfo();
    }

    /**
     * Create an instance of {@link MessageHeader }
     * 
     */
    public MessageHeader createMessageHeader() {
        return new MessageHeader();
    }

    /**
     * Create an instance of {@link StrikeSettleESBEntry }
     * 
     */
    public StrikeSettleESBEntry createESBEntry() {
        return new StrikeSettleESBEntry();
    }

    /**
     * Create an instance of {@link AccessControl }
     * 
     */
    public AccessControl createAccessControl() {
        return new AccessControl();
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
    @XmlElementDecl(namespace = "", name = "SELF_INDEX_NO")
    public JAXBElement<String> createSELFINDEXNO(String value) {
        return new JAXBElement<String>(_SELFINDEXNO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INVOICE_NO")
    public JAXBElement<String> createINVOICENO(String value) {
        return new JAXBElement<String>(_INVOICENO_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "DEPT_INDEX_NO_APP")
    public JAXBElement<String> createDEPTINDEXNOAPP(String value) {
        return new JAXBElement<String>(_DEPTINDEXNOAPP_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "MFS_RESULT_CODE")
    public JAXBElement<String> createMFSRESULTCODE(String value) {
        return new JAXBElement<String>(_MFSRESULTCODE_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "PREPAID_MONEY")
    public JAXBElement<String> createPREPAIDMONEY(String value) {
        return new JAXBElement<String>(_PREPAIDMONEY_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "RECEIPT_TYPE")
    public JAXBElement<String> createRECEIPTTYPE(String value) {
        return new JAXBElement<String>(_RECEIPTTYPE_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "MFS_METHOD_CODE")
    public JAXBElement<String> createMFSMETHODCODE(String value) {
        return new JAXBElement<String>(_MFSMETHODCODE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SELF_TOTAL_PAYMENT")
    public JAXBElement<String> createSELFTOTALPAYMENT(String value) {
        return new JAXBElement<String>(_SELFTOTALPAYMENT_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "SELF_PAYMENT_FEE")
    public JAXBElement<String> createSELFPAYMENTFEE(String value) {
        return new JAXBElement<String>(_SELFPAYMENTFEE_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "MFS_TYPE_CODE")
    public JAXBElement<String> createMFSTYPECODE(String value) {
        return new JAXBElement<String>(_MFSTYPECODE_QNAME, String.class, null, value);
    }

}
