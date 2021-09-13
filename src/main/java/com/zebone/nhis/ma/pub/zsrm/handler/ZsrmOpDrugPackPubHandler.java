package com.zebone.nhis.ma.pub.zsrm.handler;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.zsrm.dao.ZsrmOpDrugPackPubMapper;
import com.zebone.platform.common.support.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 中山人民门诊药房对外推送公共服务
 */
@Service
public class ZsrmOpDrugPackPubHandler {

    private static Logger log = LoggerFactory.getLogger("nhis.drug.pack");

    @Resource
    private ZsrmOpDrugPackPubMapper opDrugPackPubMapper;

    @Resource
    private ZsrmOpHerbDrugPackHandler opHerbDrugPackHandler;
    @Resource
    private ZsrmOpWesDrugPackHandler opWesDrugPackHandler;

    public Object invokeMethod(String methodName,Object...args) {
        if(CommonUtils.isNull(methodName)) return null;
        Object obj=new Object();

        String winType = ApplicationUtils.getDeptSysparam("EX0055", UserContext.getUser().getPkDept());
        if(!"1".equals(winType)){
            return obj;
        }

        try {
            switch (methodName){
                case "upLoadPresInfo":
                    //diffPresType(methodName,args);
                    break;
                case "getMedInfo":
                    upLoadDrugInfo(args);
                    break;
            }
        }catch (Exception e){
            log.error("门诊药房第三方接口调用失败：方法["+methodName+"],失败原因：",e);
        }
        return obj;
    }
    private void upLoadDrugInfo(Object...args) throws Exception {
        opWesDrugPackHandler.sendDrugInfo(args);
    }
    /**
     * 获取结算处方类型
     * @param pkPresocces
     * @return
     */
    public List<Map<String,Object>> getPresTypeByPk(List<String> pkPresocces){
        if(pkPresocces==null ||pkPresocces.size()==0)return null;

        return opDrugPackPubMapper.getPresTypeByPk(pkPresocces);
    }

}
