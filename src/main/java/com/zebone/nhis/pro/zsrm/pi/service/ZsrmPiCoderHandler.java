package com.zebone.nhis.pro.zsrm.pi.service;

import com.zebone.nhis.common.handler.PiCodeHandler;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("ZsrmPiCoderHandler")
public class ZsrmPiCoderHandler implements PiCodeHandler {

    @Resource(name = "defaultPiCoderHandler")
    private PiCodeHandler delegate;

    @Override
    public String genCodePi(PiMaster piMaster) {
        //zsrn_任务[4495]-tjq,20200-12-17
        //创建StringBuffer
        StringBuffer obtainCodePi = new StringBuffer();
        //获取系统编码规则生成的CodePi
        obtainCodePi.append(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
        //获取的CodePi后固定补两个0
        obtainCodePi.append("00");
        //判断程度是否是规定长度12
        if(obtainCodePi.length()<= 12){
            //小于是编码前补0补齐到12位
            return String.format("%12d", Long.parseLong(CommonUtils.getString(obtainCodePi))).replace(" ", "0");
        }else{
            //大于12位时要重新维护编码生成规则
            throw new BusException("患者编码生成超过规定编码生成规则+固定"+00+"=总长度12,请核查编码生成规则");
        }
    }

    @Override
    public String genCodeOp(PiMaster piMaster) {
        //zsrn_任务[4495]-tjq,20200-12-17
        //判断是否能获取到CodePi
        if(StringUtils.isNotEmpty(piMaster.getCodePi())){
            //截取CodePi后固定两位-跟产品确认的
            String CodeOp = piMaster.getCodePi().substring(0,piMaster.getCodePi().length()-2);
            //去掉截取后编码前位显示的0
            return CodeOp.replaceFirst("^0*", "");
        }else{
            throw new BusException("未获取到患者编码！！！");
        }
    }

    @Override
    public String genCodeIp(PiMaster piMaster) {
        return ApplicationUtils .getCode(SysConstant.ENCODERULE_CODE_ZYBL);
    }
}
