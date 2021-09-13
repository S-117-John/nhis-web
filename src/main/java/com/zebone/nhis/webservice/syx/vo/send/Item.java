package com.zebone.nhis.webservice.syx.vo.send;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("item")
public class Item {
	@XStreamAsAttribute
	private String root;
	@XStreamAsAttribute
	private String extension;
	@XStreamAsAttribute
	private String code;
	@XStreamAsAttribute
	private String codeSystem;
	@XStreamAsAttribute
	private String codeSystemName;
	@XStreamAsAttribute
	private String value;
	@XStreamAsAttribute
	private String use;
	@XStreamAsAttribute
	private String XSI_TYPE;
	
	@XStreamAsAttribute
	private String scope;
	
	private DisplayName displayName ;
	
	private Part part;
	@XStreamImplicit
	private List<Part> parts ;//需要替换掉
	private Numerator numerator;
	
	private Denominator denominator;
	
	private OriginalText originalText;
	@XStreamAsAttribute
	private String capabilities; 
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DisplayName getDisplayName() {
		if(displayName==null)displayName= new DisplayName();
		return displayName;
	}

	public void setDisplayName(DisplayName displayName) {
		this.displayName = displayName;
	}

	public Part getPart() {
		if(part==null)part=new Part();
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public List<Part> getParts() {
		if(parts==null) parts= new ArrayList<Part>();
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getXSI_TYPE() {
		return XSI_TYPE;
	}

	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}

	public Numerator getNumerator() {
		if(numerator==null) numerator=new Numerator();
		return numerator;
	}

	public void setNumerator(Numerator numerator) {
		this.numerator = numerator;
	}

	public Denominator getDenominator() {
		if(denominator==null) denominator=new Denominator();
		return denominator;
	}

	public void setDenominator(Denominator denominator) {
		this.denominator = denominator;
	}

	public OriginalText getOriginalText() {
		if(originalText==null)originalText=new OriginalText();
		return originalText;
	}

	public void setOriginalText(OriginalText originalText) {
		this.originalText = originalText;
	}

	public String getCodeSystemName() {
		return codeSystemName;
	}

	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

	public String getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(String capabilities) {
		this.capabilities = capabilities;
	}
	
}
