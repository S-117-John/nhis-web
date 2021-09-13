package com.zebone.nhis.sch.shcta.vo;

import java.util.ArrayList;
import java.util.List;

public class SchTaAutoCreateParam {
    private List<SchTaEmp> taEmps = new ArrayList<SchTaEmp>();
    private List<String> taDeps = new ArrayList<String>();
   
	public List<SchTaEmp> getTaEmps() {
		return taEmps;
	}
	public void setTaEmps(List<SchTaEmp> taEmps) {
		this.taEmps = taEmps;
	}
	public List<String> getTaDeps() {
		return taDeps;
	}
	public void setTaDeps(List<String> taDeps) {
		this.taDeps = taDeps;
	}
	 
    		
}
