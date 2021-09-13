package com.zebone.nhis.cn.ipdw.vo;

public class CnIpPressAuth {

    private boolean flagPres; ///处方权 0无 1有
    private boolean flagAnes; ///麻醉药权限 0无 1有
    private boolean flagSpirOne; ///精一权限 0无 1有
    private boolean flagSpirSec; ///精二权限 0无 1有    
    private boolean flagPoi; ///毒性药品权限 0无 1有    
    private String antiCode; ///抗菌药 02二线(受限) 03三线(特殊) 以;分隔
    private String dtEmpsrvtype ;//医疗项目权限
    private String euDrtype;//医师类别
    
    
    
	public boolean isFlagPoi() {
		return flagPoi;
	}
	public void setFlagPoi(boolean flagPoi) {
		this.flagPoi = flagPoi;
	}
	public String getDtEmpsrvtype() {
		return dtEmpsrvtype;
	}
	public void setDtEmpsrvtype(String dtEmpsrvtype) {
		this.dtEmpsrvtype = dtEmpsrvtype;
	}
	public boolean isFlagPres() {
		return flagPres;
	}
	public void setFlagPres(boolean flagPres) {
		this.flagPres = flagPres;
	}
	public boolean isFlagAnes() {
		return flagAnes;
	}
	public void setFlagAnes(boolean flagAnes) {
		this.flagAnes = flagAnes;
	}
	public boolean isFlagSpirOne() {
		return flagSpirOne;
	}
	public void setFlagSpirOne(boolean flagSpirOne) {
		this.flagSpirOne = flagSpirOne;
	}
	public boolean isFlagSpirSec() {
		return flagSpirSec;
	}
	public void setFlagSpirSec(boolean flagSpirSec) {
		this.flagSpirSec = flagSpirSec;
	}
	public String getAntiCode() {
		return antiCode;
	}
	public void setAntiCode(String antiCode) {
		this.antiCode = antiCode;
	}
	public String getEuDrtype() {
		return euDrtype;
	}
	public void setEuDrtype(String euDrtype) {
		this.euDrtype = euDrtype;
	}
    
    

}
