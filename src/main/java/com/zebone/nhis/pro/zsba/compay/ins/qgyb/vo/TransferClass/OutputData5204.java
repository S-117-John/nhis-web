package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output5204Fymx;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.PutClass.PublicParamOut;

public class OutputData5204 extends PublicParamOut{
	
	private List<Output5204Fymx> output;//		Y	交易输出

	public List<Output5204Fymx> getOutput() {
		return output;
	}

	public void setOutput(List<Output5204Fymx> output) {
		this.output = output;
	}
}
