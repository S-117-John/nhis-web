//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.07.15 at 08:56:47 PM CST 
//


package com.zebone.nhis.webservice.vo.payordervo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.zebone.nhis.webservice.vo.payordervo package. 
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

    private final static QName _ErrorMessage_QNAME = new QName("", "errorMessage");
    private final static QName _NameDept_QNAME = new QName("", "nameDept");
    private final static QName _NamePi_QNAME = new QName("", "namePi");
    private final static QName _Desc_QNAME = new QName("", "desc");
    private final static QName _PkDept_QNAME = new QName("", "pkDept");
    private final static QName _PkPv_QNAME = new QName("", "pkPv");
    private final static QName _CodeSt_QNAME = new QName("", "codeSt");
    private final static QName _RegistrationTime_QNAME = new QName("", "registrationTime");
    private final static QName _Status_QNAME = new QName("", "status");
    private final static QName _OrderType_QNAME = new QName("", "orderType");
    private final static QName _DateSt_QNAME = new QName("", "dateSt");
    private final static QName _DtStype_QNAME = new QName("", "dtStype");
    private final static QName _RegistrationFee_QNAME = new QName("", "registrationFee");
    private final static QName _NameEmp_QNAME = new QName("", "nameEmp");
    private final static QName _OrderSummary_QNAME = new QName("", "orderSummary");
    private final static QName _CodeIp_QNAME = new QName("", "codeIp");
    private final static QName _PkSettle_QNAME = new QName("", "pkSettle");
    private final static QName _PkPi_QNAME = new QName("", "pkPi");
    private final static QName _PkOrg_QNAME = new QName("", "pkOrg");
    private final static QName _PkEmp_QNAME = new QName("", "pkEmp");
    private final static QName _AmountSt_QNAME = new QName("", "amountSt");
    private final static QName _AmountPi_QNAME = new QName("", "amountPi");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.zebone.nhis.webservice.vo.payordervo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PayInfoRes }
     * 
     */
    public PayInfoRes createRes() {
        return new PayInfoRes();
    }

    /**
     * Create an instance of {@link PayInfoData }
     * 
     */
    public PayInfoData createData() {
        return new PayInfoData();
    }

    /**
     * Create an instance of {@link PayInfoOrderData }
     * 
     */
    public PayInfoOrderData createOrderData() {
        return new PayInfoOrderData();
    }

    /**
     * Create an instance of {@link RegDatas }
     * 
     */
    public RegDatas createRegDatas() {
        return new RegDatas();
    }

    /**
     * Create an instance of {@link RegData }
     * 
     */
    public RegData createRegData() {
        return new RegData();
    }

    /**
     * Create an instance of {@link DepositDatas }
     * 
     */
    public DepositDatas createDepositDatas() {
        return new DepositDatas();
    }

    /**
     * Create an instance of {@link DepositData }
     * 
     */
    public DepositData createDepositData() {
        return new DepositData();
    }

    /**
     * Create an instance of {@link SettleDatas }
     * 
     */
    public SettleDatas createSettleDatas() {
        return new SettleDatas();
    }

    /**
     * Create an instance of {@link SettleData }
     * 
     */
    public SettleData createSettleData() {
        return new SettleData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "errorMessage")
    public JAXBElement<String> createErrorMessage(String value) {
        return new JAXBElement<String>(_ErrorMessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nameDept")
    public JAXBElement<String> createNameDept(String value) {
        return new JAXBElement<String>(_NameDept_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "namePi")
    public JAXBElement<String> createNamePi(String value) {
        return new JAXBElement<String>(_NamePi_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "desc")
    public JAXBElement<String> createDesc(String value) {
        return new JAXBElement<String>(_Desc_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkDept")
    public JAXBElement<String> createPkDept(String value) {
        return new JAXBElement<String>(_PkDept_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkPv")
    public JAXBElement<String> createPkPv(String value) {
        return new JAXBElement<String>(_PkPv_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codeSt")
    public JAXBElement<String> createCodeSt(String value) {
        return new JAXBElement<String>(_CodeSt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "registrationTime")
    public JAXBElement<String> createRegistrationTime(String value) {
        return new JAXBElement<String>(_RegistrationTime_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "status")
    public JAXBElement<String> createStatus(String value) {
        return new JAXBElement<String>(_Status_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "orderType")
    public JAXBElement<String> createOrderType(String value) {
        return new JAXBElement<String>(_OrderType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dateSt")
    public JAXBElement<String> createDateSt(String value) {
        return new JAXBElement<String>(_DateSt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dtStype")
    public JAXBElement<String> createDtStype(String value) {
        return new JAXBElement<String>(_DtStype_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "registrationFee")
    public JAXBElement<String> createRegistrationFee(String value) {
        return new JAXBElement<String>(_RegistrationFee_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nameEmp")
    public JAXBElement<String> createNameEmp(String value) {
        return new JAXBElement<String>(_NameEmp_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "orderSummary")
    public JAXBElement<String> createOrderSummary(String value) {
        return new JAXBElement<String>(_OrderSummary_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codeIp")
    public JAXBElement<String> createCodeIp(String value) {
        return new JAXBElement<String>(_CodeIp_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkSettle")
    public JAXBElement<String> createPkSettle(String value) {
        return new JAXBElement<String>(_PkSettle_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkPi")
    public JAXBElement<String> createPkPi(String value) {
        return new JAXBElement<String>(_PkPi_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkOrg")
    public JAXBElement<String> createPkOrg(String value) {
        return new JAXBElement<String>(_PkOrg_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkEmp")
    public JAXBElement<String> createPkEmp(String value) {
        return new JAXBElement<String>(_PkEmp_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "amountSt")
    public JAXBElement<String> createAmountSt(String value) {
        return new JAXBElement<String>(_AmountSt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "amountPi")
    public JAXBElement<String> createAmountPi(String value) {
        return new JAXBElement<String>(_AmountPi_QNAME, String.class, null, value);
    }

}
