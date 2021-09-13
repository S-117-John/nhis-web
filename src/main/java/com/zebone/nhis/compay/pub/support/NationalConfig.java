package com.zebone.nhis.compay.pub.support;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.compay.ins.zsrm.service.qgyb.ZsrmQGUtils;
import com.zebone.nhis.compay.pub.vo.NationalHeadReqVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class NationalConfig {

    @Value("#{applicationProperties['qgyb.url']}")
    private String hsaUrl;
    @Value("#{applicationProperties['qgyb.accountCode']}")
    private String HSA_ACCOUNT_CODE;
    @Value("#{applicationProperties['qgyb.paasid']}")
    private String HSA_PAASID;
    @Value("#{applicationProperties['qgyb.secretKey']}")
    private String SECRETKEY;
    @Value("#{applicationProperties['qgyb.fixmedins_name']}")
    private String FIXMEDINS_NAME;
    @Value("#{applicationProperties['qgyb.fixmedins_code']}")
    private String fixmedinsCode;
    @Value("#{applicationProperties['qgyb.version']}")
    private String VERSION;
    @Value("#{applicationProperties['qgyb.uploadFile.path']}")
    private String uploadFilePath;
    @Value("#{applicationProperties['qgyb.mdtrtareaAdmvs']}")
    private String MdtrtareaAdmvs;

    public NationalHeadReqVo buildReq(String port){
        User user = Optional.ofNullable(UserContext.getUser()).orElseThrow(()-> new BusException("未获取到登录状态的用户信息"));
        NationalHeadReqVo reqVo = new NationalHeadReqVo();
        reqVo.setInfno(port);
        reqVo.setMsgid(String.format("%s%s%s", fixmedinsCode, DateUtils.dateToStr("yyyyMMddHHmmss", new Date()),getSNcode()));
        reqVo.setMdtrtareaAdmvs(MdtrtareaAdmvs);//待确认是否改为配置
        reqVo.setInsuplcAdmdvs(MdtrtareaAdmvs);//待确认是否改为配置
        reqVo.setRecerSysCode("01");//待确认是否改为配置
        reqVo.setSigntype("SM3");
        reqVo.setInfver(VERSION);
        reqVo.setOpterType("1");
        reqVo.setOpterName(user.getNameEmp());
        reqVo.setOpter(user.getCodeEmp());
        reqVo.setInfTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
        reqVo.setFixmedinsCode(fixmedinsCode);
        reqVo.setFixmedinsName(FIXMEDINS_NAME);
        reqVo.setCainfo("");//签到接口(9001使用
        reqVo.setSignNo("");
        return reqVo;
    }

    public Map<String, String> getHeaderElement() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("x-tif-paasid", HSA_PAASID);
        map.put("x-tif-nonce", RandomStringUtils.random(6, true, true));
        map.put("x-tif-timestamp", Long.toString(ZsrmQGUtils.getCurrentUnixSeconds()));
        map.put("x-tif-signature", ZsrmQGUtils.getSHA256Str(
                map.get("x-tif-timestamp") + SECRETKEY + map.get("x-tif-nonce") + map.get("x-tif-timestamp")));
        return map;
    }

    /* 截取0607医保交互顺序号后四位 */
    public String getSNcode() {
        String SNcode = ApplicationUtils.getCode("0607");
        if (SNcode != null && SNcode.length() >= 4) {
            SNcode = SNcode.substring(SNcode.length() - 4, SNcode.length());
            return SNcode;
        } else {
            throw new BusException("根据编码规则【0607】未获取的有效的医保顺序号SNcode");
        }
    }

    public String getHsaUrl() {
        return hsaUrl;
    }
}
