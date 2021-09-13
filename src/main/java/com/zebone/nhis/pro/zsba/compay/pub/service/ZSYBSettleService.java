package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IYBSettleService;
import com.zebone.nhis.bl.pub.vo.SettleInfo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybStItemcate;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsyb;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsybJjfx;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsStWi;
import com.zebone.nhis.pro.zsba.compay.ins.ydyb.vo.InsZsYdybStSnyb;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSt;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbStItemcate;
import com.zebone.nhis.pro.zsba.compay.pub.service.PubInsSettleService;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlSettle;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaSettleInfo;

/**
 * 中山医保业务处理类
 * @author yangxue
 *
 */
@Service("zsybSettleService")
public class ZSYBSettleService {
	
	@Autowired
	private PubInsSettleService pubInsSettleService;
	
	
	public void dealYBSettleMethod(ZsbaSettleInfo settlevo,ZsbaBlSettle stVo, String pkPosTr) {
		InsZsBaYbSt insSt = settlevo.getInsSt();//中山医保结算数据
		List<InsZsBaYbStItemcate> isList = settlevo.getInsStItemcateList();
		InsZsYdybStSnyb snyb = settlevo.getInsStSnyb();//省内异地医保结算数据
		InsZsKsybStKsyb ksyb = settlevo.getInsStKsyb(); //跨省异地医保结算数据
		List<InsZsKsybStKsybJjfx> jjfxList = settlevo.getJjfxList(); //跨省医保住院结算基金分项

		//保存中山医保结算记录
		pubInsSettleService.saveYbSettlementData(insSt, isList, stVo.getPkSettle(), pkPosTr);
		//保存省内异地医保结算记录
		pubInsSettleService.saveInsStSnyb(snyb, stVo.getPkSettle());
		//保存跨省异地医保结算记录
		pubInsSettleService.saveInsStKsyb(ksyb, jjfxList, stVo.getPkSettle());
		//省内工商医保
		pubInsSettleService.saveInsStWi(stVo.getPkPv(), stVo.getPkSettle());
		//全国医保
		pubInsSettleService.saveInsStQgyb(settlevo.getInsStQgyb(), stVo.getPkSettle(), pkPosTr, settlevo.getPayPosTr());
	}
	
	/**
	 * 用于判断此次结算是否是医保结算，此方法用于pk_settle结算数据保存之前
	 * @param settlevo
	 * @param stVo
	 * @return
	 */
	public boolean checkYbSt(ZsbaSettleInfo settlevo,ZsbaBlSettle stVo){
		boolean isYb = false;
		if(settlevo.getInsSt()!=null){
			isYb = true;
		}
		if(settlevo.getInsStSnyb()!=null){
			isYb = true;
		}
		if(settlevo.getInsStKsyb()!=null){
			isYb = true;
		}
		if(settlevo.getInsStQgyb()!=null){
			isYb = true;
		}
		if(pubInsSettleService.checkInsStWi(stVo.getPkPv())){
			isYb = true;
		}
		return isYb;
	}
}
