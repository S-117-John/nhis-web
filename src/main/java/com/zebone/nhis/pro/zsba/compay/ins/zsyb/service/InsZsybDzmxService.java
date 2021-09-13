package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybStMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.DzmxParam;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDzmx;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSt;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybDzmxService {
	
	
	@Autowired 
	private InsZsybStMapper insStMapper;


	/**
	 * 保存
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsZsybDzmx 中山医保对账单明细
	 */
	@Transactional
	public InsZsBaYbDzmx save(String param , IUser user){
		InsZsBaYbDzmx entry = JsonUtil.readValue(param, InsZsBaYbDzmx.class);
		if(entry!=null){
			if(entry.getPkInszsybdzmx()!=null){
				DataBaseHelper.updateBeanByPk(entry, false);
			}else{
				DataBaseHelper.insertBean(entry);
			}
		}
		return entry;
	}
	
	/**
	 * 保存医保对账明细并对比差异
	 * @param param 实体对象数据
	 * @param user  登录用户
	 */
	public DzmxParam saveInsZsybDzmx(String param , IUser user){
		DzmxParam dzmxParam = JsonUtil.readValue(param, DzmxParam.class);
		DzmxParam returnDzmx = new DzmxParam();
		Map<String, Object> dzmxMap = JsonUtil.readValue(param, Map.class);
		
		
		//删除以前符合此次查询条件的数据
		String sql = "delete ins_zsyb_dzmx where TJLB = ? and JSRQ >= ? and JSRQ <= ?";
		DataBaseHelper.execute(sql,dzmxParam.getTjlb(), dzmxMap.get("ksrq").toString(),dzmxMap.get("zzrq").toString());	
		//插入此次查询的数据
		for (InsZsBaYbDzmx dzmx : dzmxParam.getDzmxList()) {
			DataBaseHelper.insertBean(dzmx);
		}
		Map<String,Object> param_h = new HashMap<String,Object>();
		
		param_h.put("ksrq", dzmxMap.get("ksrq").toString());
		param_h.put("zzrq", dzmxMap.get("zzrq").toString());
		//根据日期区间查出医保结算记录
		List<InsZsBaYbSt> stList = insStMapper.getInsStList(param_h);
		//根据就诊记录号、总费用对比数据库的结算记录，判断是否有差异，无差异为0，找不到医院数据为1，找到了金额不对为2
		List<InsZsBaYbDzmx> dzmxList = new ArrayList<InsZsBaYbDzmx>();
		Double ylfyzesSumDz = 0d;
		Double ylfyzesSumYb = 0d;
		List<Integer> dgzsj = new ArrayList<Integer>();//医院对过账的数据下标
		for(InsZsBaYbDzmx dzmx : dzmxParam.getDzmxList()){
			boolean flag = false;
			for(int i=0; i< stList.size(); i++){
				if(dzmx.getSdywh().equals(stList.get(i).getJsywh())){
					if(dzmx.getYlfyze().equals(stList.get(i).getYlfyze())){
						dzmx.setFhz(0);
					}else{
						dzmx.setFhz(2);
					}
					ylfyzesSumYb += stList.get(i).getYlfyze().doubleValue();
					flag = true;
					dgzsj.add(i);
					break;
				}
			}
			if(flag==false){
				dzmx.setFhz(1);
			}
			dzmxList.add(dzmx);
			ylfyzesSumDz += dzmx.getYlfyze().doubleValue();
		}
		
		//删除那些对过账的数据，剩余的就是没对上的
		if(dgzsj.size()!=stList.size()){
			List<Integer> wdzList = new ArrayList<Integer>();
			for(int i=0; i<dgzsj.size(); i++){
				stList.remove(dgzsj.get(i)-i);
			}
		}
		
		returnDzmx.setYlfyzesSumDz(ylfyzesSumDz);
		returnDzmx.setYlfyzesSumYb(ylfyzesSumYb);
		returnDzmx.setDzmxList(dzmxList);
		returnDzmx.setStList(stList);
		return returnDzmx;
	}
}