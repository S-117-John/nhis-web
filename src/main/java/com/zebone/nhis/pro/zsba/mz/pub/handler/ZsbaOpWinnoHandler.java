package com.zebone.nhis.pro.zsba.mz.pub.handler;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.mz.pub.service.ZsbaOpWinnoService;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;

/**
 * HIS门诊分窗接口服务
 * 需求来源中山人民医院项目
 */
@Service
public class ZsbaOpWinnoHandler {

    @Resource
    private ZsbaOpWinnoService zsbaOpWinnoService;
    
    /**
     * 分窗接口入口
     * @param methodName
     * @param args
     * @return
     */
    public Object invokeMethod(String methodName,Object...args) {
        if (CommonUtils.isNull(methodName) || args==null || args.length==0) return null;
        Object obj = null;

        switch (methodName) {
            case "getDrugDeptEx":
                obj=zsbaOpWinnoService.geDeptExOpDrug((Map<String,List<BlOpDt>>)args[0]);
                break;
            case "getDrugWinno":
            	zsbaOpWinnoService.getWinnoForAllRolue(args);
                break;
            case "getNotSettlePresInfo":
            	zsbaOpWinnoService.getNotSettlePresInfo((List<BlPatiCgInfoNotSettleVO>)args[0]);
                break;
        }
        return obj;
    }
    
    /**
     * 同步体检数据
     * @param param
     * @param user
     * @return
     */
	public Integer sycnData(String param, IUser user) {
		Integer insertNum = 0;
		
		Map<String, Object> resultMap = zsbaOpWinnoService.saveNhisData(param, user) ; 
		
		insertNum = Integer.parseInt(resultMap.get("insertNum").toString());
		/*
		List<String> zfSqlList = (List<String>)resultMap.get("zfSqlList");
		
		DataSourceRoute.putAppId("HIS_bayy");
		
		zsbaOpWinnoService.updateOldhisData(zfSqlList);
		*/
		return insertNum;
	}

}
