package com.zebone.nhis.common.handler;

import com.zebone.nhis.common.module.pi.PiMaster;

/**
 * 客户化编码生成接口
 */
public interface PiCodeHandler {

    String genCodePi(PiMaster piMaster);

    String genCodeOp(PiMaster piMaster);

    String genCodeIp(PiMaster piMaster);

}
