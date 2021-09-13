package com.zebone.nhis.base.bd.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.ChapMapper;
import com.zebone.nhis.common.module.base.bd.price.BdChap;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
@Service
public class ChapService {
	@Autowired
	private ChapMapper chapMapper;
	/**
	 * 保存
	 * @param param
	 * @param user
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void saveChap(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		BdChap bc = JsonUtil.readValue(param, BdChap.class);
		
		if(bc==null) return;
		Double r = bc.getRate();
		Double rate = r * 0.01;
		bc.setRate(rate);
//		String chaptype = bc.getDtChaptype();
//		String begin = bc.getValBegin();
//		String end = bc.getValEnd();
		if(bc.getPkChap()==null){
			Map<String,Object> map  = DataBaseHelper.queryForMap("select count(1) num from bd_chap where val_begin = ? and val_end = ? and dt_chaptype = ? and del_flag='0'", new Object[]{bc.getValBegin(),bc.getValEnd(),bc.getDtChaptype()});
			//if(chaptype == null && begin == null & end == null)
			Object num =  map.get("num");
			
			int parseInt = Integer.parseInt(num.toString());
			if(parseInt > 0){
				throw new BusException("相同策略下起始值终止值不能相同");
			}else{
				DataBaseHelper.insertBean(bc);
			}
			
		}else {
			Map<String,Object> map  = DataBaseHelper.queryForMap("select count(1) num from bd_chap where val_begin = ? and val_end = ? and dt_chaptype = ? and del_flag='0'", new Object[]{bc.getValBegin(),bc.getValEnd(),bc.getDtChaptype()});
			//if(chaptype == null && begin == null & end == null)
			Object num =  map.get("num");
			
			int parseInt = Integer.parseInt(num.toString());
			if(parseInt > 0){
				throw new BusException("相同策略下起始值终止值不能相同");
			}else{
				DataBaseHelper.updateBeanByPk(bc);
			//DataBaseHelper.updateBeanByPk(bc,false);
			}
		}
		List<BdChap> chapList = new ArrayList<BdChap>(); //批量带保存的数据
		List<Map<String,Object>> itemList = DataBaseHelper.queryForList("select pk_item from bd_item where pk_itemcate =?",  new Object[]{new BdItem().getPkItemcate()});
		if(itemList!=null&&itemList.size()>0){
			for(Map<String,Object> map :itemList){
				BdChap bcChap= new BdChap();
				BeanUtils.copyProperties(bcChap, bc);
				bcChap.setPkItem(map.get("pkItem").toString());
				chapList.add(bcChap);
			}
		}
		List<Map<String,Object>> pdList = DataBaseHelper.queryForList("select pk_pd from bd_pd where eu_drugtype =?",  new Object[]{new BdPd().getEuDrugtype()});
		if(pdList!=null&&pdList.size()>0){
			for(Map<String , Object> map : pdList){
				BdChap bcp = new BdChap();
				BeanUtils.copyProperties(bcp, bc);
				bcp.setPkItem(map.get("pkItem").toString());
				chapList.add(bcp);
			}
		}
		if(chapList.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdChap.class), chapList);
	}
	
	/**
	 * 通过主键删除
	 * @param param
	 * @param user
	 */
	public void delByPkChap(String param , IUser user){
		List<BdChap>  bc = JsonUtil.readValue(param,new TypeReference<List<BdChap>>(){});
		if(bc != null && bc.size() > 0){
		for (BdChap bdChap : bc) {
			String pkChap = bdChap.getPkChap();
			DataBaseHelper.execute("update bd_chap set del_flag='1' where pk_chap=? and del_flag='0'", new Object[]{pkChap});
			
			}
		
		}else{
			throw new BusException("请选择要删除的收费项目");
		}
	}
	
	/**
	 * 查询左侧树
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryBdDefdoclist(String param,IUser user){
		List<Map<String, Object>> list = DataBaseHelper.queryForList("select ct.code,ct.name,ct.py_code from bd_defdoc ct where ct.code_defdoclist='040007' and ct.del_flag='0'",new Object[]{});
		return list;
	}
	
	/**
	 * 通过左侧树或者条件查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdChap> findByPkOrg(String param , IUser user){
		//测试
		Map<String,String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>() {
		});
		User u = (User)user;
		//获取所属机构
		paramMap.put("pkOrg", u.getPkOrg());
		//获取所属标志
		String itemType = paramMap.get("itemType");
		//判断标志的类型
		paramMap.put("flagPd", "1".equals(itemType)? "1":"2".equals(itemType)?"0":null);
	//	paramMap.put("name", "%"+paramMap.get("name")+"%");
	//	String pkOrg = paramMap.get("pkOrg");
		List<BdChap> bcap = chapMapper.findByPkOrg(paramMap);
		List<BdChap> chap = new ArrayList<BdChap>();
		for (BdChap bdChap : bcap) {
			bdChap.setRate(bdChap.getRate() * 100);
			chap.add(bdChap);
		}
		return chap;
		
	}
}
