//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.26 at 03:17:42 PM CST 
//


package com.zebone.nhis.webservice.vo.ticketvo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.zebone.nhis.webservice.vo.ticketvo package. 
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

    private final static QName _PkSch_QNAME = new QName("", "pkSch");
    private final static QName _ErrorMessage_QNAME = new QName("", "errorMessage");
    private final static QName _Type_QNAME = new QName("", "type");
    private final static QName _Ticketno_QNAME = new QName("", "ticketno");
    private final static QName _FlagAppt_QNAME = new QName("", "flagAppt");
    private final static QName _EuPvtype_QNAME = new QName("", "euPvtype");
    private final static QName _BeginTime_QNAME = new QName("", "beginTime");
    private final static QName _EndTime_QNAME = new QName("", "endTime");
    private final static QName _FlagUsed_QNAME = new QName("", "flagUsed");
    private final static QName _PkSchticket_QNAME = new QName("", "pkSchticket");
    private final static QName _FlagStop_QNAME = new QName("", "flagStop");
    private final static QName _Desc_QNAME = new QName("", "desc");
    private final static QName _Status_QNAME = new QName("", "status");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.zebone.nhis.webservice.vo.ticketvo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Res }
     * 
     */
    public Res createRes() {
        return new Res();
    }

    /**
     * Create an instance of {@link Data }
     * 
     */
    public Data createData() {
        return new Data();
    }

    /**
     * Create an instance of {@link ScheList }
     * 
     */
    public ScheList createScheList() {
        return new ScheList();
    }

    /**
     * Create an instance of {@link Sche }
     * 
     */
    public Sche createSche() {
        return new Sche();
    }

    /**
     * Create an instance of {@link SchTicketSecs }
     * 
     */
    public SchTicketSecs createSchTicketSecs() {
        return new SchTicketSecs();
    }

    /**
     * Create an instance of {@link SchTicketSec }
     * 
     */
    public SchTicketSec createSchTicketSec() {
        return new SchTicketSec();
    }

    /**
     * Create an instance of {@link Schschs }
     * 
     */
    public Schschs createSchschs() {
        return new Schschs();
    }

    /**
     * Create an instance of {@link Schsch }
     * 
     */
    public Schsch createSchsch() {
        return new Schsch();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkSch")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkSch(String value) {
        return new JAXBElement<String>(_PkSch_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "errorMessage")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createErrorMessage(String value) {
        return new JAXBElement<String>(_ErrorMessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createType(String value) {
        return new JAXBElement<String>(_Type_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ticketno")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTicketno(String value) {
        return new JAXBElement<String>(_Ticketno_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "flagAppt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFlagAppt(String value) {
        return new JAXBElement<String>(_FlagAppt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "euPvtype")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createEuPvtype(String value) {
        return new JAXBElement<String>(_EuPvtype_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "beginTime")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createBeginTime(String value) {
        return new JAXBElement<String>(_BeginTime_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "endTime")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createEndTime(String value) {
        return new JAXBElement<String>(_EndTime_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "flagUsed")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFlagUsed(String value) {
        return new JAXBElement<String>(_FlagUsed_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pkSchticket")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPkSchticket(String value) {
        return new JAXBElement<String>(_PkSchticket_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "flagStop")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFlagStop(String value) {
        return new JAXBElement<String>(_FlagStop_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "desc")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDesc(String value) {
        return new JAXBElement<String>(_Desc_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "status")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createStatus(String value) {
        return new JAXBElement<String>(_Status_QNAME, String.class, null, value);
    }

}
