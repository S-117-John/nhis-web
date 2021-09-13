package com.zebone.nhis.ex.pub.support;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.dao.ExPubMapper;
import com.zebone.nhis.ex.pub.vo.PatiCardVo;

/**
 * 患者费用处理类
 * @author yangxue
 *
 */
@Service
public class PatiFeeHandler {
	
	@Resource
	private ExPubMapper exPubMapper;
	
	public PatiCardVo setPatiFee(PatiCardVo pativo){
		if(pativo == null) return null;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPv", pativo.getPkPv());
		if(pativo.getDateBegin()!=null && pativo.getDateSt()!=null){
			paramMap.put("dateBegin", DateUtils.dateToStr("yyyyMMddHHmmss", pativo.getDateBegin()));
			paramMap.put("dateSt", DateUtils.dateToStr("yyyyMMddHHmmss", pativo.getDateSt()));
		}
		BigDecimal yjj = processNull(exPubMapper.getYjFee(paramMap));
		//BigDecimal yjj_fac = getYjjFac(patiBedMapper.getYjFactor(paramMap));
		//yjj_fac = yjj_fac == null?new BigDecimal(1):yjj_fac;
		//yjj = yjj.multiply(yjj_fac);
		BigDecimal dbj = processNull(exPubMapper.getDbFee(paramMap));
		BigDecimal zfy = processNull(exPubMapper.getTotalFee(paramMap));
		BigDecimal ztfy_n = processNull(exPubMapper.getZtNPdFee(paramMap));
		BigDecimal ztfy = processNull(exPubMapper.getZtPdFee(paramMap));
		BigDecimal gd = processNull(exPubMapper.getGdFee(paramMap));
		
		
		pativo.setAccfee(dbj.doubleValue());
		pativo.setTotalfee(zfy.doubleValue());
		pativo.setPrefee(yjj.doubleValue());
		pativo.setGdfee(gd.doubleValue());
		
		BigDecimal zt = ztfy_n.add(ztfy);
		pativo.setZtfee(zt.doubleValue());
		
		//余额 = 预交金-总费用
		BigDecimal ye = yjj.subtract(zfy);
		pativo.setYuefee(ye.doubleValue());
		return pativo;
	}
	
//	public BigDecimal getYjjFac(List<Map<String,Object>> result){
//		
//		return fac;
//	}
	
	/**
	 * 根据pkPv查询患者余额，计算是否欠费使用，其他业务禁止使用
	 * @param paramMap
	 * @return
	 */
	public Double getRemainFee(Map<String,Object> paramMap){
		BigDecimal yjj = processNull(exPubMapper.getYjFee(paramMap));
		List<Map<String,Object>> kfresult = exPubMapper.getYjFactor(paramMap);
		BigDecimal yjj_fac = new BigDecimal(1);
		BigDecimal kfxe = new BigDecimal(0);//控费限额
		if(kfresult != null && kfresult.size()>0) {
			yjj_fac = (BigDecimal)kfresult.get(0).get("factorPrep");
			kfxe = (BigDecimal)kfresult.get(0).get("amtCred");
		}
		yjj_fac = yjj_fac == null?new BigDecimal(1):yjj_fac;
		kfxe = kfxe == null?new BigDecimal(0):kfxe;
		yjj = yjj.multiply(yjj_fac);
		
		BigDecimal zfy = processNull(exPubMapper.getTotalFee(paramMap));//问成哥sql是否正确
		BigDecimal zt = BigDecimal.ZERO;
		if("1".equals(ApplicationUtils.getSysparam("BL0046", false))){
			BigDecimal ztfy_n = processNull(exPubMapper.getZtNPdFee(paramMap));
			BigDecimal ztfy = processNull(exPubMapper.getZtPdFee(paramMap));
			zt = ztfy_n.add(ztfy);
		}
		//余额 = 预交金*系数+控费限额-总费用-在途费用
		BigDecimal ye = yjj.add(kfxe).subtract(zfy).subtract(zt);
		return ye.doubleValue();
	}
	/**
	 * 处理空值
	 * @param args
	 * @return
	 */
	private BigDecimal processNull(BigDecimal args){
		if(args == null){
			args = new BigDecimal(0);
		}
		return args;
	}
}
