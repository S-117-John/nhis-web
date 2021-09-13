package com.zebone.nhis.ma.pub.zsrm.handler;

import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.zsrm.service.ZsrmIpAutoPackagePlantService;
import com.zebone.nhis.ma.pub.zsrm.vo.AtfYpxxDetailVo;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 全自动包药机
 **/
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ZsrmIpAutoPackagePlantHandler {
    @Resource
    ZsrmIpAutoPackagePlantService zsrmIpAutoPackagePlantService;
    @Resource
    PdDeDrugVo pdDeDrugVo;
    Map<String,Object> returnMap=new LinkedHashMap<>();
    public  Object invokeMethod(String methodName, Object... args) {
        if (CommonUtils.isNull(methodName) || args==null || args.length==0) return null;
        Object obj = null;

        try{
            switch (methodName) {
                case "queryExMedBag":
                    savePackagePlant(args);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<String,Object> savePackagePlant(Object... args){
        returnMap.clear();
        PdDeDrugVo pdDeDrugVos=null;
        LinkedHashMap<String,String> map = (LinkedHashMap) args[0];
        if (!StringUtils.isBlank(map.get("pkPdapdt"))){
            pdDeDrugVo.setPkPdapdt(map.get("pkPdapdt"));
        }else if (!StringUtils.isBlank(map.get("codeDe"))){
            pdDeDrugVo.setCodeDe(map.get("codeDe"));
            pdDeDrugVos = zsrmIpAutoPackagePlantService.getPdDeDrugVo(this.pdDeDrugVo);
        }else {
            throw  new BusException("请领明细主键不能为空");
        }
        List<AtfYpxxDetailVo> packagePlant = zsrmIpAutoPackagePlantService.getPackagePlant(pdDeDrugVos);
        try{
            DataSourceRoute.putAppId("AutoPackagePlant");
            zsrmIpAutoPackagePlantService.savePackagePlant(packagePlant);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            DataSourceRoute.putAppId("default");
        }

        returnMap.put("state","success");
        return returnMap;
    }
}
