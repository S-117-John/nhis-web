package com.zebone.nhis.common.handler;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.SysConstant;
import org.springframework.stereotype.Service;

@Service
public class DefaultPiCoderHandler implements PiCodeHandler {

    @Override
    public String genCodePi(PiMaster piMaster) {
        return ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ);
    }

    @Override
    public String genCodeOp(PiMaster piMaster) {
        return ApplicationUtils .getCode(SysConstant.ENCODERULE_CODE_MZBL);
    }

    @Override
    public String genCodeIp(PiMaster piMaster) {
        return ApplicationUtils .getCode(SysConstant.ENCODERULE_CODE_ZYBL);
    }
}
