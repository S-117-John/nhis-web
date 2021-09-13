package com.zebone.nhis.base.bd.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.zebone.nhis.base.bd.dao.HpCgdivMapper;
import com.zebone.nhis.base.bd.vo.CgDivParam;
import com.zebone.nhis.common.module.base.bd.price.BdHpCgdiv;
import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItemcate;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class HpCgdivService {
	
		@Resource
	   private HpCgdivMapper hpCgdivMapper;
	   //查询 医保记费策略
       public List<Map<String,Object>> searchHpCgdiv(String param,IUser user)
       {
    	   String sql = "select cd.pk_hpcgdiv, cd.code_div,  cd.name_div,  cd.spcode,  cd.d_code, cd.note,cd.dt_hpdicttype from bd_hp_cgdiv cd  where cd.del_flag='0' order by code_div";
		   List<Map<String,Object>> rtnList = DataBaseHelper.queryForList(sql, new Object[]{});         
		   //返回查询结果List
		   return rtnList;
	   }
	       
       //查询 药品、收费明细、项目分类
       public Map<String, Object> searchDetailHpCgdiv(String param, IUser user)
       {
    	 Map<String, Object>  map= new HashMap<String,Object>();
		 
    	 Map<String,String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>() {}); //读数据，获取前台的参数
    	 String pkHpCgdiv = paramMap.get("pkHpCgdiv");
    	 
    	 List<Map<String, Object>> rtnList_pd = hpCgdivMapper.qryPd(pkHpCgdiv);  //查詢結果保存到rtnList
    	 List<Map<String, Object>> rtnList_item = hpCgdivMapper.qryItem(pkHpCgdiv);
    	 List<Map<String, Object>> rtnList_cate = hpCgdivMapper.qryCate(pkHpCgdiv);
    	 
    	 map.put("CgDivPd", rtnList_pd);
    	 map.put("CgDivItem", rtnList_item);
    	 map.put("CgDivCate", rtnList_cate);  
    	 return map;
	   }
	
	   public void saveHpCgdiv(String param,IUser user){
		
		CgDivParam divParam = JsonUtil.readValue(param, CgDivParam.class); //将JSON格式的数据解析、读出
	    //if(divParam==null) return; //判断数据是否为空				
		BdHpCgdiv cgDiv = divParam.getCgDiv(); //分别获取对应数据
		List<BdHpCgdivItem> cgDivPd = divParam.getCgDivPd();
		List<BdHpCgdivItem> cgDivItem = divParam.getCgDivItem();
		List<BdHpCgdivItemcate> cgDivCate = divParam.getCgDivCate();		
		//接收删除的表格数据
		List<BdHpCgdivItem> cgDivDeletePd = divParam.getCgDivDeletePd();
		List<BdHpCgdivItem> cgDivDeleteItem = divParam.getCgDivDeleteItem();
		List<BdHpCgdivItemcate> cgDivDeleteCate = divParam.getCgDivDeleteCate();
		User u = (User) user;
	    //删除的表格数据处理
		if (cgDivDeletePd != null)
		{
			for(BdHpCgdivItem DeletePd : cgDivDeletePd)                      //遍历实体类
			{
				DataBaseHelper.execute("delete from bd_hp_cgdiv_item where pk_hpcgdivitem=?",new Object[]{DeletePd.getPkHpcgdivitem()}) ;
			}
		}
		if (cgDivDeleteItem != null)
		{
			for(BdHpCgdivItem DeleteItem : cgDivDeleteItem)                      //遍历实体类
			{
				DataBaseHelper.execute("delete from bd_hp_cgdiv_item where pk_hpcgdivitem=?",new Object[]{DeleteItem.getPkHpcgdivitem()}) ;
			}
		}
		if (cgDivDeleteCate != null)
		{
			for(BdHpCgdivItemcate DeleteCate : cgDivDeleteCate)                      //遍历实体类
			{
				DataBaseHelper.execute("delete from bd_hp_cgdiv_itemcate where pk_hpcgdivitemcate=?",new Object[]{DeleteCate.getPkHpcgdivitemcate()}) ;
			} 
		}
		 
		//新增或修改医保记费策略
		if(!StringUtils.isBlank(cgDiv.getPkHpcgdiv())) //如果主键已存在，则为修改 医保记费策略
		{
			//更新 医保记费策略
			//DataBaseHelper.execute("update bd_hp_cgdiv set code_div=?,name_div=?,spcode=?,d_code=?,note=? where pk_hpcgdiv=?", new Object[]{cgDiv.getCodeDiv(),cgDiv.getNameDiv(),cgDiv.getSpcode(),cgDiv.getdCode(),cgDiv.getNote(),cgDiv.getPkHpcgdiv()});
			cgDiv.setModityTime(new Date());
			cgDiv.setModifier(u.getUserName());
			//DataBaseHelper.updateBeanByPk(cgDiv,false);
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(BdHpCgdiv.class), cgDiv);
		}
		else //新增 医保记费策略
		{
			cgDiv.setPkOrg("~                               ");
			String pkCgDiv = NHISUUID.getKeyId();  //获取数据库内唯一主键
			cgDiv.setPkHpcgdiv(pkCgDiv);                 //设置 医保记费策略表 主键
			DataBaseHelper.insertBean(cgDiv);           //单条记录，插入
		}		
		//现有的表格数据处理
		List<BdHpCgdivItem> divListAdd = new ArrayList<BdHpCgdivItem>();
		List<BdHpCgdivItem> divListUpdate=new ArrayList<BdHpCgdivItem>();
		if(cgDivPd!=null){			                                          //插入 药品表数据		
			for(BdHpCgdivItem pd : cgDivPd)                      //遍历cgDivPd实体类
			{        
				pd.setPkOrg("~                               ");
				if(!StringUtils.isBlank(pd.getPkHpcgdivitem()) && "1".equals(pd.getIsEdit()))  { //如果主键已存在，则为修改 	
					pd.setModityTime(new Date());
					pd.setModifier(u.getUserName());
					pd.setPkHpcgdiv(cgDiv.getPkHpcgdiv());  //设置 主键
					pd.setFlagPd("1");
					pd.setTs(new Date());
					divListUpdate.add(pd);
				}
				else {  //插入一条记录
					pd.setCreateTime(new Date());
					pd.setCreator(u.getUserName());
					pd.setPkHpcgdiv(cgDiv.getPkHpcgdiv());  //设置 主键
					pd.setPkHpcgdivitem(NHISUUID.getKeyId());
					divListAdd.add(pd);        //单条记录，插入
				}
			}
			
		}	
		if(cgDivItem!=null){                                                 //插入 收费项目表数据
			for(BdHpCgdivItem item : cgDivItem)                  //遍历cgDivItem实体类
			{
				item.setPkOrg("~                               ");
				if(!StringUtils.isBlank(item.getPkHpcgdivitem()) && "1".equals(item.getIsEdit()))  { //如果主键已存在，则为修改 
				item.setModityTime(new Date());
				item.setModifier(u.getUserName());
				divListUpdate.add(item);
				}
				else {
					item.setCreateTime(new Date());
					item.setCreator(u.getUserName());
					item.setPkHpcgdiv(cgDiv.getPkHpcgdiv());  //设置 主键
					item.setPkHpcgdivitem(NHISUUID.getKeyId());
					divListAdd.add(item);               //单条记录，插入
				}			
			}			
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdHpCgdivItem.class), divListAdd);
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdHpCgdivItem.class),divListUpdate);
		
		
		List<BdHpCgdivItemcate> itemCate = new ArrayList<BdHpCgdivItemcate>();
	    if (cgDivCate != null){                                               //插入 项目分类表数据
	    	for (BdHpCgdivItemcate cate : cgDivCate) {
	    		cate.setPkOrg("~                               ");
	    		if(!StringUtils.isBlank(cate.getPkHpcgdivitemcate()) && "1".equals(cate.getIsEdit()))  { //如果主键已存在，则为修改 
	    			if(cate.getRate()==null)
					{
	    				DataBaseHelper.execute("update BD_HP_CGDIV_ITEMCATE set rate = null where PK_HPCGDIVITEMCATE =?", cate.getPkHpcgdivitemcate());
					}
	    			cate.setModifier(u.getUserName());
	    			cate.setModityTime(new Date());
	    		    DataBaseHelper.updateBeanByPk(cate, false);
	    		}
	    		else{
	    			cate.setPkHpcgdiv(cgDiv.getPkHpcgdiv());  //设置 主键
	    			cate.setPkHpcgdivitemcate(NHISUUID.getKeyId());
	    			itemCate.add(cate);               //单条记录，插入
	    		}
	    	}
	    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdHpCgdivItemcate.class), itemCate);
	    }		
	  }
	   
	   
	  //删除 医保记费策略
      public void deleteHpCgdiv(String param, IUser user)
      { 	      	  
    	  //获取param数值
    	  String pkHpCgdiv = JsonUtil.getFieldValue(param, "pkHpCgdiv"); 
    	  List<Map<String, Object>> rtn = DataBaseHelper.queryForList("select count(1) as num from bd_hp_divconfig where pk_hpcgdiv=? and del_flag='0'", new Object[]{pkHpCgdiv});
    	  int num = Integer.parseInt(rtn.get(0).get("num").toString());
  		  if( num>0 )
  		  {
  			  throw new BusException("该策略已被使用，请先取消该策略的使用!");
  		  }   	  
  		  DataBaseHelper.execute("update bd_hp_cgdiv_itemcate set del_flag = '1' where del_flag = 0 and pk_hpcgdiv = ?", new Object[]{pkHpCgdiv});
    	  DataBaseHelper.execute("update bd_hp_cgdiv_item set del_flag = '1' where del_flag = 0 and pk_hpcgdiv = ?", new Object[]{pkHpCgdiv});
    	  DataBaseHelper.execute("delete from bd_hp_cgdiv where pk_hpcgdiv = ?", new Object[]{pkHpCgdiv});
	  }
      
      //導入药品
      public List<BdHpCgdivItem> exportsMed(String param, IUser user){
    	  Map<String, Object> map = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>() {});
    	  String pkHpcgdiv = (String) map.get("pkHpcgdiv");
    	  List<BdHpCgdivItem> list = hpCgdivMapper.exportsMed(pkHpcgdiv);
    	  return list; 	  
      }
      
      //导入收费项目
      public List<BdHpCgdivItem> exportsItem(String param, IUser user){
    	  Map<String, String> map = JsonUtil.readValue(param, new TypeReference<Map<String,String>>() {});
    	  String pkHpcgdiv = map.get("pkHpcgdiv");
    	  List<BdHpCgdivItem> list = hpCgdivMapper.exportsItem(pkHpcgdiv);
    	  return list;
      }
}
