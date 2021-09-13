package com.zebone.nhis.webservice.vo.tmisvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BldTypeDisInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class BldTypeDisInfoVo {
	
	
	
	@XmlElement(name = "DeptHISCode")
	public String deptHISCode;
	
	@XmlElement(name = "DeptName")
	public String deptName;
	
	@XmlElement(name = "DeptSort")
	public String deptSort;
	
	@XmlElement(name = "ACount")
	public Integer aCount;
	
	@XmlElement(name = "APosCount")
	public Integer aPosCount;
	
	@XmlElement(name = "ANegCount")
	public Integer aNegCount;
	
	@XmlElement(name = "AXCount")
	public Integer aXCount;
	
	@XmlElement(name = "BCount")
	public Integer bCount;
	
	@XmlElement(name = "BPosCount")
	public Integer bPosCount;
	
	@XmlElement(name = "BNegCount")
	public Integer bNegCount;
	
	@XmlElement(name = "BXCount")
	public Integer bXCount;
	
	@XmlElement(name = "OCount")
	public Integer oCount;
	
	@XmlElement(name = "OPosCount")
	public Integer oPosCount;
	
	@XmlElement(name = "ONegCount")
	public Integer oNegCount;
	
	@XmlElement(name = "OXCount")
	public Integer oXCount;
	
	@XmlElement(name = "ABCount")
	public Integer aBCount;
	
	@XmlElement(name = "ABPosCount")
	public Integer aBPosCount;
	
	@XmlElement(name = "ABNegCount")
	public Integer aBNegCount;
	
	@XmlElement(name = "ABXCount")
	public Integer aBXCount;
	
	@XmlElement(name = "XCount")
	public Integer xCount;
	
	@XmlElement(name = "XPosCount")
	public Integer xPosCount;
	
	@XmlElement(name = "XNegCount")
	public Integer xNegCount;
	
	@XmlElement(name = "XXCount")
	public Integer xXCount;
	
	@XmlElement(name = "RhPosCount")
	public Integer rhPosCount;
	
	@XmlElement(name = "RhNegCount")
	public Integer rhNegCount;
	
	@XmlElement(name = "RhXCount")
	public Integer rhXCount;
	
	@XmlElement(name = "AllCount")
	public Integer allCount;

	public String getDeptHISCode() {
		return deptHISCode;
	}

	public void setDeptHISCode(String deptHISCode) {
		this.deptHISCode = deptHISCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptSort() {
		return deptSort;
	}

	public void setDeptSort(String deptSort) {
		this.deptSort = deptSort;
	}

	public Integer getaCount() {
		return aCount;
	}

	public void setaCount(Integer aCount) {
		this.aCount = aCount;
	}

	public Integer getaPosCount() {
		return aPosCount;
	}

	public void setaPosCount(Integer aPosCount) {
		this.aPosCount = aPosCount;
	}

	public Integer getaNegCount() {
		return aNegCount;
	}

	public void setaNegCount(Integer aNegCount) {
		this.aNegCount = aNegCount;
	}

	public Integer getaXCount() {
		return aXCount;
	}

	public void setaXCount(Integer aXCount) {
		this.aXCount = aXCount;
	}

	public Integer getbCount() {
		return bCount;
	}

	public void setbCount(Integer bCount) {
		this.bCount = bCount;
	}

	public Integer getbPosCount() {
		return bPosCount;
	}

	public void setbPosCount(Integer bPosCount) {
		this.bPosCount = bPosCount;
	}

	public Integer getbNegCount() {
		return bNegCount;
	}

	public void setbNegCount(Integer bNegCount) {
		this.bNegCount = bNegCount;
	}

	public Integer getbXCount() {
		return bXCount;
	}

	public void setbXCount(Integer bXCount) {
		this.bXCount = bXCount;
	}

	public Integer getoCount() {
		return oCount;
	}

	public void setoCount(Integer oCount) {
		this.oCount = oCount;
	}

	public Integer getoPosCount() {
		return oPosCount;
	}

	public void setoPosCount(Integer oPosCount) {
		this.oPosCount = oPosCount;
	}

	public Integer getoNegCount() {
		return oNegCount;
	}

	public void setoNegCount(Integer oNegCount) {
		this.oNegCount = oNegCount;
	}

	public Integer getoXCount() {
		return oXCount;
	}

	public void setoXCount(Integer oXCount) {
		this.oXCount = oXCount;
	}

	public Integer getaBCount() {
		return aBCount;
	}

	public void setaBCount(Integer aBCount) {
		this.aBCount = aBCount;
	}

	public Integer getaBPosCount() {
		return aBPosCount;
	}

	public void setaBPosCount(Integer aBPosCount) {
		this.aBPosCount = aBPosCount;
	}

	public Integer getaBNegCount() {
		return aBNegCount;
	}

	public void setaBNegCount(Integer aBNegCount) {
		this.aBNegCount = aBNegCount;
	}

	public Integer getaBXCount() {
		return aBXCount;
	}

	public void setaBXCount(Integer aBXCount) {
		this.aBXCount = aBXCount;
	}

	public Integer getxCount() {
		return xCount;
	}

	public void setxCount(Integer xCount) {
		this.xCount = xCount;
	}

	public Integer getxPosCount() {
		return xPosCount;
	}

	public void setxPosCount(Integer xPosCount) {
		this.xPosCount = xPosCount;
	}

	public Integer getxNegCount() {
		return xNegCount;
	}

	public void setxNegCount(Integer xNegCount) {
		this.xNegCount = xNegCount;
	}

	public Integer getxXCount() {
		return xXCount;
	}

	public void setxXCount(Integer xXCount) {
		this.xXCount = xXCount;
	}

	public Integer getRhPosCount() {
		return rhPosCount;
	}

	public void setRhPosCount(Integer rhPosCount) {
		this.rhPosCount = rhPosCount;
	}

	public Integer getRhNegCount() {
		return rhNegCount;
	}

	public void setRhNegCount(Integer rhNegCount) {
		this.rhNegCount = rhNegCount;
	}

	public Integer getRhXCount() {
		return rhXCount;
	}

	public void setRhXCount(Integer rhXCount) {
		this.rhXCount = rhXCount;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public BldTypeDisInfoVo(){
		this.setaPosCount(0);
		this.setaNegCount(0);
		this.setaXCount(0);
		this.setbPosCount(0);
		this.setbNegCount(0);
		this.setbXCount(0);
		this.setoPosCount(0);
		this.setoNegCount(0);
		this.setoXCount(0);
		this.setaBPosCount(0);
		this.setaBNegCount(0);
		this.setaBXCount(0);
		
		this.setaCount(0);
		this.setbCount(0);
		this.setoCount(0);
		this.setaBCount(0);
		this.setxCount(0);
	}
}
