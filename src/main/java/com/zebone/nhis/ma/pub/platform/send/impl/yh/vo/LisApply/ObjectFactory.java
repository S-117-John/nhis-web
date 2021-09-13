//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.19 at 01:42:57 PM CST 
//


package com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.LisApply;

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

    private final static QName _MINIUNIT_QNAME = new QName("", "MINI_UNIT");
    private final static QName _INSTRUCTION_QNAME = new QName("", "INSTRUCTION");
    private final static QName _PARENTNO_QNAME = new QName("", "PARENT_NO");
    private final static QName _ReturnFlag_QNAME = new QName("", "ReturnFlag");
    private final static QName _Password_QNAME = new QName("", "Password");
    private final static QName _SELFBUY_QNAME = new QName("", "SELF_BUY");
    private final static QName _STOPCONFIRMOPERA_QNAME = new QName("", "STOP_CONFIRM_OPERA");
    private final static QName _CONFIRMTIME_QNAME = new QName("", "CONFIRM_TIME");
    private final static QName _UserName_QNAME = new QName("", "UserName");
    private final static QName _ORDERDOCTOR_QNAME = new QName("", "ORDER_DOCTOR");
    private final static QName _DRUGSPECIFICATION_QNAME = new QName("", "DRUG_SPECIFICATION");
    private final static QName _WARDSN_QNAME = new QName("", "WARD_SN");
    private final static QName _ADMISSTIMES_QNAME = new QName("", "ADMISS_TIMES");
    private final static QName _MsgDate_QNAME = new QName("", "MsgDate");
    private final static QName _SysFlag_QNAME = new QName("", "SysFlag");
    private final static QName _EXECUNIT_QNAME = new QName("", "EXEC_UNIT");
    private final static QName _SUPPLYCODE_QNAME = new QName("", "SUPPLY_CODE");
    private final static QName _ORDERTYPE_QNAME = new QName("", "ORDER_TYPE");
    private final static QName _SourceSysCode_QNAME = new QName("", "SourceSysCode");
    private final static QName _PATIENTID_QNAME = new QName("", "PATIENT_ID");
    private final static QName _STOPDOCTOR_QNAME = new QName("", "STOP_DOCTOR");
    private final static QName _ENTERTIME_QNAME = new QName("", "ENTER_TIME");
    private final static QName _DOSEAGEUNIT_QNAME = new QName("", "DOSEAGE_UNIT");
    private final static QName _ORDERCODE_QNAME = new QName("", "ORDER_CODE");
    private final static QName _Fid_QNAME = new QName("", "Fid");
    private final static QName _ORDERNO_QNAME = new QName("", "ORDER_NO");
    private final static QName _STARTTIME_QNAME = new QName("", "START_TIME");
    private final static QName _FREQUCODE_QNAME = new QName("", "FREQU_CODE");
    private final static QName _ORDERNAME_QNAME = new QName("", "ORDER_NAME");
    private final static QName _FITFLAG_QNAME = new QName("", "FIT_FLAG");
    private final static QName _DOSEAGE_QNAME = new QName("", "DOSEAGE");
    private final static QName _CONFIRMOPERA_QNAME = new QName("", "CONFIRM_OPERA");
    private final static QName _TargetSysCode_QNAME = new QName("", "TargetSysCode");
    private final static QName _DRUGOCC_QNAME = new QName("", "DRUG_OCC");
    private final static QName _CHARGEAMOUNT_QNAME = new QName("", "CHARGE_AMOUNT");
    private final static QName _DEPTSN_QNAME = new QName("", "DEPT_SN");
    private final static QName _SERIAL_QNAME = new QName("", "SERIAL");
    private final static QName _ENDTIME_QNAME = new QName("", "END_TIME");

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
     * Create an instance of {@link LisApplyInfo }
     * 
     */
    public LisApplyInfo createMsg() {
        return new LisApplyInfo();
    }

    /**
     * Create an instance of {@link MessageHeader }
     * 
     */
    public MessageHeader createMessageHeader() {
        return new MessageHeader();
    }

    /**
     * Create an instance of {@link LisApplyESBEntry }
     * 
     */
    public LisApplyESBEntry createESBEntry() {
        return new LisApplyESBEntry();
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
    @XmlElementDecl(namespace = "", name = "MINI_UNIT")
    public JAXBElement<String> createMINIUNIT(String value) {
        return new JAXBElement<String>(_MINIUNIT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INSTRUCTION")
    public JAXBElement<String> createINSTRUCTION(String value) {
        return new JAXBElement<String>(_INSTRUCTION_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PARENT_NO")
    public JAXBElement<String> createPARENTNO(String value) {
        return new JAXBElement<String>(_PARENTNO_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "SELF_BUY")
    public JAXBElement<String> createSELFBUY(String value) {
        return new JAXBElement<String>(_SELFBUY_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "STOP_CONFIRM_OPERA")
    public JAXBElement<String> createSTOPCONFIRMOPERA(String value) {
        return new JAXBElement<String>(_STOPCONFIRMOPERA_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CONFIRM_TIME")
    public JAXBElement<String> createCONFIRMTIME(String value) {
        return new JAXBElement<String>(_CONFIRMTIME_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "ORDER_DOCTOR")
    public JAXBElement<String> createORDERDOCTOR(String value) {
        return new JAXBElement<String>(_ORDERDOCTOR_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DRUG_SPECIFICATION")
    public JAXBElement<String> createDRUGSPECIFICATION(String value) {
        return new JAXBElement<String>(_DRUGSPECIFICATION_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "WARD_SN")
    public JAXBElement<String> createWARDSN(String value) {
        return new JAXBElement<String>(_WARDSN_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "MsgDate")
    public JAXBElement<String> createMsgDate(String value) {
        return new JAXBElement<String>(_MsgDate_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "EXEC_UNIT")
    public JAXBElement<String> createEXECUNIT(String value) {
        return new JAXBElement<String>(_EXECUNIT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SUPPLY_CODE")
    public JAXBElement<String> createSUPPLYCODE(String value) {
        return new JAXBElement<String>(_SUPPLYCODE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORDER_TYPE")
    public JAXBElement<String> createORDERTYPE(String value) {
        return new JAXBElement<String>(_ORDERTYPE_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "PATIENT_ID")
    public JAXBElement<String> createPATIENTID(String value) {
        return new JAXBElement<String>(_PATIENTID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "STOP_DOCTOR")
    public JAXBElement<String> createSTOPDOCTOR(String value) {
        return new JAXBElement<String>(_STOPDOCTOR_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ENTER_TIME")
    public JAXBElement<String> createENTERTIME(String value) {
        return new JAXBElement<String>(_ENTERTIME_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DOSEAGE_UNIT")
    public JAXBElement<String> createDOSEAGEUNIT(String value) {
        return new JAXBElement<String>(_DOSEAGEUNIT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORDER_CODE")
    public JAXBElement<String> createORDERCODE(String value) {
        return new JAXBElement<String>(_ORDERCODE_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "ORDER_NO")
    public JAXBElement<String> createORDERNO(String value) {
        return new JAXBElement<String>(_ORDERNO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "START_TIME")
    public JAXBElement<String> createSTARTTIME(String value) {
        return new JAXBElement<String>(_STARTTIME_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FREQU_CODE")
    public JAXBElement<String> createFREQUCODE(String value) {
        return new JAXBElement<String>(_FREQUCODE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORDER_NAME")
    public JAXBElement<String> createORDERNAME(String value) {
        return new JAXBElement<String>(_ORDERNAME_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FIT_FLAG")
    public JAXBElement<String> createFITFLAG(String value) {
        return new JAXBElement<String>(_FITFLAG_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DOSEAGE")
    public JAXBElement<String> createDOSEAGE(String value) {
        return new JAXBElement<String>(_DOSEAGE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CONFIRM_OPERA")
    public JAXBElement<String> createCONFIRMOPERA(String value) {
        return new JAXBElement<String>(_CONFIRMOPERA_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "DRUG_OCC")
    public JAXBElement<String> createDRUGOCC(String value) {
        return new JAXBElement<String>(_DRUGOCC_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CHARGE_AMOUNT")
    public JAXBElement<String> createCHARGEAMOUNT(String value) {
        return new JAXBElement<String>(_CHARGEAMOUNT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DEPT_SN")
    public JAXBElement<String> createDEPTSN(String value) {
        return new JAXBElement<String>(_DEPTSN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SERIAL")
    public JAXBElement<String> createSERIAL(String value) {
        return new JAXBElement<String>(_SERIAL_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "END_TIME")
    public JAXBElement<String> createENDTIME(String value) {
        return new JAXBElement<String>(_ENDTIME_QNAME, String.class, null, value);
    }

}
