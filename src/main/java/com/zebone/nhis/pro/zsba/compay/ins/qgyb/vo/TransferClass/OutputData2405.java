package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.PutClass.PublicParamOut;

/**
 * 5.2.5.5【2405】出院撤销 出参
 * @author Administrator
 *
 */
public class OutputData2405 extends PublicParamOut{
	
	private OutputVoid output;//		Y	交易输出

	public OutputVoid getOutput() {
		return output;
	}

	public void setOutput(OutputVoid output) {
		this.output = output;
	}
}
