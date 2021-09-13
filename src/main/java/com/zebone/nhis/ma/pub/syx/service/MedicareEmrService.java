package com.zebone.nhis.ma.pub.syx.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.syx.dao.MedicareEmrMapper;
import com.zebone.nhis.ma.pub.syx.vo.HomePageOrVo;
import com.zebone.nhis.ma.pub.syx.vo.HomePageVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class MedicareEmrService {
	@Autowired
	private MedicareEmrMapper medicareEmrMapper;

	// 查询患者的首页数据
	public List<HomePageVo> getHomePageList(String param, IUser user) throws ParseException{
    	Map map = JsonUtil.readValue(param, Map.class);
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dateBegin=sf.parse(map.get("dateBeginOut").toString());
		Date dateEnd=sf.parse(map.get("dateEndOut").toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(dateEnd);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sf = new SimpleDateFormat("yyyy-MM-dd");
		sf.format(dateBegin);
		map.put("dateBegin", sf.format(dateBegin));
		map.put("dateEnd", sf.format(endDate));
    	List<HomePageVo> pageList=medicareEmrMapper.getHomePageList(map);
    	for (HomePageVo homePageVo : pageList) {
    		//查询诊断列表
			homePageVo.diagList=medicareEmrMapper.getHomePageDiagList(homePageVo.getPkPage());
			//查询手术列表
			homePageVo.opsList=medicareEmrMapper.getHomePageOpList(homePageVo.getPkPage());
			//查询妇婴列表
			homePageVo.brList=medicareEmrMapper.getHomePageBrList(homePageVo.getPkPage());
			//查询肿瘤列表
			homePageVo.orList=medicareEmrMapper.getHomePageOrList(homePageVo.getPkPage());
			if(homePageVo.orList.size()>0){
				for (HomePageOrVo homePageOr : homePageVo.orList) {
					homePageOr.homePageOrDtInfo=medicareEmrMapper.getHomePageOrDtList(homePageOr.getPkPageor());
				}
			}
			//查询出院记录
			homePageVo.lhList=medicareEmrMapper.getHomePageLhList(homePageVo.getPkPv());
		}
    	return pageList;
    }
}
