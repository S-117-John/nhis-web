package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.PutClass.PublicParamOut;

public class OutputData90100 extends PublicParamOut{
	
	private List<Output90100> output;//		Y	交易输出

	public List<Output90100> getOutput() {
		return output;
	}

	public void setOutput(List<Output90100> output) {
		this.output = output;
	}
}
