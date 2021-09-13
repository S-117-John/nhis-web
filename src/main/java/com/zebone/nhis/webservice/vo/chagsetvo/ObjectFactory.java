//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.26 at 03:31:26 PM CST 
//


package com.zebone.nhis.webservice.vo.chagsetvo;

import java.math.BigDecimal;
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
 * generated in the com.zebone.nhis.webservice.vo.chagsetvo package. 
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

    private final static QName _PriceOrg_QNAME = new QName("", "priceOrg");
    private final static QName _DtPaymode_QNAME = new QName("", "dtPaymode");
    private final static QName _PresNo_QNAME = new QName("", "presNo");
    private final static QName _Ords_QNAME = new QName("", "ords");
    private final static QName _PkDeptOrd_QNAME = new QName("", "pkDeptOrd");
    private final static QName _Spec_QNAME = new QName("", "spec");
    private final static QName _AggregateAmount_QNAME = new QName("", "aggregateAmount");
    private final static QName _PkDisc_QNAME = new QName("", "pkDisc");
    private final static QName _AmountPi_QNAME = new QName("", "amountPi");
    private final static QName _Price_QNAME = new QName("", "price");
    private final static QName _PkUnitDos_QNAME = new QName("", "pkUnitDos");
    private final static QName _MedicarePayments_QNAME = new QName("", "medicarePayments");
    private final static QName _NameSupply_QNAME = new QName("", "nameSupply");
    private final static QName _AmountAdd_QNAME = new QName("", "amountAdd");
    private final static QName _NameDeptOrd_QNAME = new QName("", "nameDeptOrd");
    private final static QName _NameUnitDos_QNAME = new QName("", "nameUnitDos");
    private final static QName _Unit_QNAME = new QName("", "unit");
    private final static QName _EuDrugtype_QNAME = new QName("", "euDrugtype");
    private final static QName _PkEmpInput_QNAME = new QName("", "pkEmpInput");
    private final static QName _CodeFreq_QNAME = new QName("", "codeFreq");
    private final static QName _PkDeptEx_QNAME = new QName("", "pkDeptEx");
    private final static QName _CodeEmpSt_QNAME = new QName("", "codeEmpSt");
    private final static QName _FlagPv_QNAME = new QName("", "flagPv");
    private final static QName _NameEmpOrd_QNAME = new QName("", "nameEmpOrd");
    private final static QName _Note_QNAME = new QName("", "note");
    private final static QName _Dosage_QNAME = new QName("", "dosage");
    private final static QName _PkCgop_QNAME = new QName("", "pkCgop");
    private final static QName _RatioDisc_QNAME = new QName("", "ratioDisc");
    private final static QName _EuAdditem_QNAME = new QName("", "euAdditem");
    private final static QName _Itemcate_QNAME = new QName("", "itemcate");
    private final static QName _DateStart_QNAME = new QName("", "dateStart");
    private final static QName _PkPv_QNAME = new QName("", "pkPv");
    private final static QName _AmtInsuThird_QNAME = new QName("", "amtInsuThird");
    private final static QName _PatientsPay_QNAME = new QName("", "patientsPay");
    private final static QName _NameEmpInput_QNAME = new QName("", "nameEmpInput");
    private final static QName _PkPi_QNAME = new QName("", "pkPi");
    private final static QName _Amount_QNAME = new QName("", "amount");
    private final static QName _PkEmpOrd_QNAME = new QName("", "pkEmpOrd");
    private final static QName _Quan_QNAME = new QName("", "quan");
    private final static QName _PkDeptSt_QNAME = new QName("", "pkDeptSt");
    private final static QName _PkCnord_QNAME = new QName("", "pkCnord");
    private final static QName _NameCg_QNAME = new QName("", "nameCg");
    private final static QName _AmountHppi_QNAME = new QName("", "amountHppi");
    private final static QName _PkOrgSt_QNAME = new QName("", "pkOrgSt");
    private final static QName _Days_QNAME = new QName("", "days");
    private final static QName _RatioAdd_QNAME = new QName("", "ratioAdd");
    private final static QName _RatioSelf_QNAME = new QName("", "ratioSelf");
    private final static QName _PayInfo_QNAME = new QName("", "payInfo");
    private final static QName _FlagInsu_QNAME = new QName("", "flagInsu");
    private final static QName _Ts_QNAME = new QName("", "ts");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.zebone.nhis.webservice.vo.chagsetvo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Data }
     * 
     */
    public Data createData() {
        return new Data();
    }

    /**
     * Create an instance of {@link ChagSettList }
     * 
     */
    public ChagSettList createChagSettList() {
        return new ChagSettList();
    }

    /**
     * Create an instance of {@link ChagSett }
     * 
     */
    public ChagSett createChagSett() {
        return new ChagSett();
    }

    /**
     * Create an instance of {@link BlDeposits }
     * 
     */
    public BlDeposits createBlDeposits() {
        return new BlDeposits();
    }

    /**
     * Create an instance of {@link BlDeposit }
     * 
     */
    public BlDeposit createBlDeposit() {
        return new BlDeposit();
    }

    /**
     * Create an instance of {@link BlOpDts }
     * 
     */
    public BlOpDts createBlOpDts() {
        return new BlOpDts();
    }

    /**
     * Create an instance of {@link BlOpDt }
     * 
     */
    public BlOpDt createBlOpDt() {
        return new BlOpDt();
    }

    /**
     * Create an instance of {@link Req }
     * 
     */
    public Req createReq() {
        return new Req();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "priceOrg")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPriceOrg(String value) {
        return new JAXBElement<String>(_PriceOrg_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dtPaymode")
    public JAXBElement<BigInteger> createDtPaymode(BigInteger value) {
        return new JAXBElement<BigInteger>(_DtPaymode_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "presNo")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPresNo(String value) {
        return new JAXBElement<String>(_PresNo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ords")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createOrds(String value) {
        return new JAXBElement<String>(_Ords_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkDeptOrd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkDeptOrd(String value) {
        return new JAXBElement<String>(_PkDeptOrd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "spec")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSpec(String value) {
        return new JAXBElement<String>(_Spec_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "aggregateAmount")
    public JAXBElement<BigDecimal> createAggregateAmount(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_AggregateAmount_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkDisc")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkDisc(String value) {
        return new JAXBElement<String>(_PkDisc_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "amountPi")
    public JAXBElement<BigDecimal> createAmountPi(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_AmountPi_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "price")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPrice(String value) {
        return new JAXBElement<String>(_Price_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkUnitDos")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkUnitDos(String value) {
        return new JAXBElement<String>(_PkUnitDos_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "medicarePayments")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createMedicarePayments(String value) {
        return new JAXBElement<String>(_MedicarePayments_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nameSupply")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createNameSupply(String value) {
        return new JAXBElement<String>(_NameSupply_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "amountAdd")
    public JAXBElement<BigDecimal> createAmountAdd(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_AmountAdd_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nameDeptOrd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createNameDeptOrd(String value) {
        return new JAXBElement<String>(_NameDeptOrd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nameUnitDos")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createNameUnitDos(String value) {
        return new JAXBElement<String>(_NameUnitDos_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "unit")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createUnit(String value) {
        return new JAXBElement<String>(_Unit_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "euDrugtype")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createEuDrugtype(String value) {
        return new JAXBElement<String>(_EuDrugtype_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkEmpInput")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkEmpInput(String value) {
        return new JAXBElement<String>(_PkEmpInput_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codeFreq")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCodeFreq(String value) {
        return new JAXBElement<String>(_CodeFreq_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkDeptEx")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkDeptEx(String value) {
        return new JAXBElement<String>(_PkDeptEx_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codeEmpSt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCodeEmpSt(String value) {
        return new JAXBElement<String>(_CodeEmpSt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "flagPv")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFlagPv(String value) {
        return new JAXBElement<String>(_FlagPv_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nameEmpOrd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createNameEmpOrd(String value) {
        return new JAXBElement<String>(_NameEmpOrd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "note")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createNote(String value) {
        return new JAXBElement<String>(_Note_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dosage")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDosage(String value) {
        return new JAXBElement<String>(_Dosage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkCgop")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkCgop(String value) {
        return new JAXBElement<String>(_PkCgop_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ratioDisc")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRatioDisc(String value) {
        return new JAXBElement<String>(_RatioDisc_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "euAdditem")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createEuAdditem(String value) {
        return new JAXBElement<String>(_EuAdditem_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "itemcate")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createItemcate(String value) {
        return new JAXBElement<String>(_Itemcate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dateStart")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDateStart(String value) {
        return new JAXBElement<String>(_DateStart_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkPv")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkPv(String value) {
        return new JAXBElement<String>(_PkPv_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "amtInsuThird")
    public JAXBElement<BigDecimal> createAmtInsuThird(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_AmtInsuThird_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "patientsPay")
    public JAXBElement<BigDecimal> createPatientsPay(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_PatientsPay_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nameEmpInput")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createNameEmpInput(String value) {
        return new JAXBElement<String>(_NameEmpInput_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkPi")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkPi(String value) {
        return new JAXBElement<String>(_PkPi_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "amount")
    public JAXBElement<BigDecimal> createAmount(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Amount_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkEmpOrd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkEmpOrd(String value) {
        return new JAXBElement<String>(_PkEmpOrd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "quan")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createQuan(String value) {
        return new JAXBElement<String>(_Quan_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkDeptSt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkDeptSt(String value) {
        return new JAXBElement<String>(_PkDeptSt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkCnord")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkCnord(String value) {
        return new JAXBElement<String>(_PkCnord_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nameCg")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createNameCg(String value) {
        return new JAXBElement<String>(_NameCg_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "amountHppi")
    public JAXBElement<BigDecimal> createAmountHppi(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_AmountHppi_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkOrgSt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkOrgSt(String value) {
        return new JAXBElement<String>(_PkOrgSt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "days")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDays(String value) {
        return new JAXBElement<String>(_Days_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ratioAdd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRatioAdd(String value) {
        return new JAXBElement<String>(_RatioAdd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ratioSelf")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRatioSelf(String value) {
        return new JAXBElement<String>(_RatioSelf_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "payInfo")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPayInfo(String value) {
        return new JAXBElement<String>(_PayInfo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "flagInsu")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFlagInsu(String value) {
        return new JAXBElement<String>(_FlagInsu_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ts")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTs(String value) {
        return new JAXBElement<String>(_Ts_QNAME, String.class, null, value);
    }

}