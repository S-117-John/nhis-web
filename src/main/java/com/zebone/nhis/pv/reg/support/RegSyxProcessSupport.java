package com.zebone.nhis.pv.reg.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.EnumerateParameter;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pv.reg.dao.RegSyxMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 中山二院版本挂号工具栏
 * @author IBM
 *
 */
@Service
public class RegSyxProcessSupport {
	@Resource
	private RegSyxMapper regSyxMapper;
	/**
	 * 根据挂号服务，构建收费项目集合
	 * @param srvParam{pkSchsrv,pkInsu,nameInsu,euPvType}
	 * @return
	 */
	public List<ItemPriceVo> constructItemParam(Map<String,Object> srvParam){
		//根据挂号服务，获取挂号服务收费项目
		List<Map<String,Object>> srvordlist = regSyxMapper.querySchSrvAndDtInfo(srvParam);
		if(srvordlist==null||srvordlist.size()<=0){
			if(EnumerateParameter.ONE.equals(MapUtils.getString(srvParam,"euType"))){
				throw new BusException("对应排班服务上没有维护职工类型的收费项目");
			}
			return null;
		}

		List<ItemPriceVo> itemlist = new ArrayList<ItemPriceVo>();
		//添加挂号服务医嘱项目
		for(Map<String,Object> ordmap : srvordlist){
			String pkOrd = CommonUtils.getString(ordmap.get("pkOrd"));
			if(pkOrd==null||"".equals(pkOrd))
				continue;
			//挂了医嘱但是医嘱没有收费项目
			if(DataBaseHelper.queryForScalar("select count(*) from bd_ord_item oi inner join bd_item item on oi.pk_item=item.pk_item where oi.pk_ord = ? and oi.del_flag = '0'",
					Integer.class, new Object[]{pkOrd}) ==0)
				continue;
			
			ItemPriceVo itemvo = new ItemPriceVo();
			itemvo.setFlagPd("0");
			itemvo.setFlagPv("1");
			itemvo.setPackSize(1);
			itemvo.setPkOrdOld(CommonUtils.getString(ordmap.get("pkOrd")));
			itemvo.setQuanOld(1.00);
			itemlist.add(itemvo);
		}
		//添加挂号服务加收及特诊项目
		Map<String,Object> srvMap = srvordlist.get(0);
		if(CommonUtils.isNotNull(srvMap.get("pkItem"))){
			ItemPriceVo itemAdd = new ItemPriceVo();
			itemAdd.setFlagPd("0");
			itemAdd.setFlagPv("1");//挂号标志
			itemAdd.setPackSize(1);
			itemAdd.setPkItemOld(CommonUtils.getString(srvMap.get("pkItem")));
			itemAdd.setQuanOld(1.00);
			itemlist.add(itemAdd);
		}
		if(CommonUtils.isNotNull(srvMap.get("pkItemSpec"))){
			ItemPriceVo itemSpec = new ItemPriceVo();
			itemSpec.setFlagPd("0");
			itemSpec.setFlagPv("1");
			itemSpec.setPackSize(1);
			itemSpec.setPkItemOld(CommonUtils.getString(srvMap.get("pkItemSpec")));
			itemSpec.setQuanOld(1.00);
			itemlist.add(itemSpec);
		}
		return itemlist;
	}
	
	public String getQcfHp(){
		Map<String,Object> mapHp = DataBaseHelper.queryForMap("select pk_hp from bd_hp where eu_hptype=0 and del_flag=0");
		if(MapUtils.isEmpty(mapHp)) {
			throw new BusException("没有全自费医保类型");
		}
		return MapUtils.getString(mapHp, "pkHp");
	}
	
	/**
	 * 获取字典中默认 预约渠道
	 * @return
	 */
	public String getDefAppType(){
		return MapUtils.getString(DataBaseHelper.queryForMap("select code from BD_DEFDOC where  DEL_FLAG = '0' and CODE_DEFDOCLIST = '020100' and DEL_FLAG='0' and FLAG_DEF='1' "),"code");
	}
}
