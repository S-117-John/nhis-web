package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.PutClass.PublicParamOut;

/**
 * 5.2.5.4【2404】入院撤销 出参
 * @author Administrator
 *
 */
public class OutputData2404 extends PublicParamOut{
	
	private OutputVoid output;//		Y	交易输出

	public OutputVoid getOutput() {
		return output;
	}

	public void setOutput(OutputVoid output) {
		this.output = output;
	}
}
