package com.zebone.nhis.ma.pub.arch.vo;

import javax.xml.bind.annotation.XmlElement;

public class ArchUploadBody {
	

    private String filecontent;
    
    private String cafilecontent;
    
    private String cafilename;
    
    

    @XmlElement(name="cafilecontent")
    public String getCafilecontent() {
		return cafilecontent;
	}

	public void setCafilecontent(String cafilecontent) {
		this.cafilecontent = cafilecontent;
	}

	@XmlElement(name="cafilename")
	public String getCafilename() {
		return cafilename;
	}

	public void setCafilename(String cafilename) {
		this.cafilename = cafilename;
	}

	@XmlElement(name="filecontent")
	public String getFilecontent() {
		return filecontent;
	}
	
	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}
  
  

}
