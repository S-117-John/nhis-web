package com.zebone.nhis.ex.nis.pi.service;

import com.zebone.nhis.cn.pub.dao.PatiListQryMapper;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.pi.dao.NsDeptPatiAmountMapper;
import com.zebone.nhis.ex.nis.pi.vo.NsDeptPatiAmountVo;
import com.zebone.nhis.ex.nis.qry.dao.DeptDayReportMapper;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 获取病区各类人数、明细
 * @author yangxue
 *
 */
@Service
public class NsDeptPatiAmountService {
	@Resource
	private NsDeptPatiAmountMapper nsdeptPatiAmountMapper;//护士站床位大卡人数统计
	
	@Resource
	private DeptDayReportMapper deptDayReportMapper;//病区日报人数统计
	
	@Resource
	private PatiListQryMapper patiListQryMapper;//医生站人数统计
	
	private Logger logger = LoggerFactory.getLogger("com.zebone");

	@SuppressWarnings("unchecked")
	public NsDeptPatiAmountVo queryNsDeptPatiAmount(String param, IUser user) {
		long begin = System.currentTimeMillis();
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//一级护理
		String oneLevelNum = ApplicationUtils.getSysparam("EX0011", true);
		paramMap.put("oneLevelNum", oneLevelNum);
		NsDeptPatiAmountVo amountVO = nsdeptPatiAmountMapper.getDeptNsPatiNum(paramMap);
		if(amountVO.getYr() != null){//总人数不包含婴儿
			amountVO.setTotal(amountVO.getTotal()-amountVO.getYr());
		}
		//查询当天入院与入科人数
		paramMap.put("flagIn", "1");
		paramMap.put("dateBegin", DateUtils.getDefaultDateFormat().format(new Date()).substring(0,8)+"000000");
		paramMap.put("dateEnd", DateUtils.getDefaultDateFormat().format(new Date()).substring(0,8)+"235959");
		NsDeptPatiAmountVo hospIn = nsdeptPatiAmountMapper.getHospInAndOutNum(paramMap);
		if(hospIn!=null)amountVO.setHospin(hospIn.getHospin());
		NsDeptPatiAmountVo deptIn = nsdeptPatiAmountMapper.getDeptInAndOutNum(paramMap);
		if(deptIn!=null)amountVO.setDeptin(deptIn.getDeptin());
		
		//查询当天出院与转科人数
		paramMap.put("flagIn", "0");
		NsDeptPatiAmountVo hospOut = nsdeptPatiAmountMapper.getHospInAndOutNum(paramMap);
		if(hospOut!=null)amountVO.setHospout(hospOut.getHospout());
		NsDeptPatiAmountVo deptOut = nsdeptPatiAmountMapper.getDeptInAndOutNum(paramMap);
		if(deptOut!=null)amountVO.setDeptout(deptOut.getDeptin());//查询时字段统一使用deptin接收
		
		//amountVO.setNewborn(nsdeptPatiAmountMapper.getNewborn(paramMap));
		//amountVO.setOthers(null);//暂时不知道统计啥
		
		//[2018-09-19 中二增加]查询手术|死亡人数
		paramMap.put("yesBegin", DateUtils.getDate("yyyyMMdd")+"000000");
		paramMap.put("yesEnd", DateUtils.getDate("yyyyMMdd")+"235959");
		BigDecimal dieNum =deptDayReportMapper.getDeathByDept(paramMap);//病区日报统计【死亡人数】
		amountVO.setDieNum(Integer.parseInt(dieNum.toString()));
		int opNum = patiListQryMapper.qryDeptOpToday(paramMap);//医生站公用【今日手术人数】
		amountVO.setOpNum(opNum);
		
		//【2019-02-19】中二 增加 出院未结算人数
		NsDeptPatiAmountVo outStNum = nsdeptPatiAmountMapper.getDeptOutNotSettleNum(paramMap);//病区查询【出院未结算人数】
		if(outStNum!=null)amountVO.setOutStNum(outStNum.getOutStNum());

		//增加医保患者和自费患者的统计
		if("1".equals(MapUtils.getString(paramMap,"isDisplayMedNum"))){
			NsDeptPatiAmountVo medNum = nsdeptPatiAmountMapper.getDeptMedicalInsuranceNum(paramMap);
			if(medNum!=null){
				amountVO.setMedicalInsurance(medNum.getMedicalInsurance());
				amountVO.setOwnExpense(medNum.getOwnExpense());
			}
		}

		return amountVO;

	}


    /**
     * 获取病区各类人明细
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> queryNsDeptPatiDt(String param, IUser user) {
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String,Object>> result = new ArrayList<>();
        String oneLevelNum = ApplicationUtils.getSysparam("EX0011", true);
        paramMap.put("oneLevelNum", oneLevelNum);
        paramMap.put("flagIn", "1");
        paramMap.put("dateBegin", DateUtils.getDefaultDateFormat().format(new Date()).substring(0,8)+"000000");
        paramMap.put("dateEnd", DateUtils.getDefaultDateFormat().format(new Date()).substring(0,8)+"235959");
        paramMap.put("flagIn", "0");
        paramMap.put("yesBegin", DateUtils.getDate("yyyyMMdd")+"000000");
        paramMap.put("yesEnd", DateUtils.getDate("yyyyMMdd")+"235959");
        //患者类型
        String type = paramMap.get("patientType").toString();
        if("一级护理".equals(type)){//一级护理患者明细
            result = nsdeptPatiAmountMapper.getYJPaint(paramMap);
        }else if("病重".equals(type)){//病重患者明细
            result = nsdeptPatiAmountMapper.getBZPaint(paramMap);
        }else if("病危".equals(type)){//病危患者明细
            result = nsdeptPatiAmountMapper.getBWPaint(paramMap);
        }else if("入院".equals(type)){//入院患者明细
            result = nsdeptPatiAmountMapper.getRYPaint(paramMap);
        }else if("转入".equals(type)){//转入患者明细
            result = nsdeptPatiAmountMapper.getZRPaint(paramMap);
        }else if("转出".equals(type)){//转出患者明细
            result = nsdeptPatiAmountMapper.getZCPaint(paramMap);
        }else if("出院".equals(type)){//出院患者明细
            result = nsdeptPatiAmountMapper.getCYPaint(paramMap);
        }else if("死亡".equals(type)){//死亡患者明细
            result = nsdeptPatiAmountMapper.getSWPaint(paramMap);
        }else if("手术".equals(type)){//手术患者明细
            result = nsdeptPatiAmountMapper.getSSPaint(paramMap);
        }else if("出院未结算".equals(type)){//出院未结算患者明细
            result = nsdeptPatiAmountMapper.getJSPaint(paramMap);
        }
        return result;
    }
}
