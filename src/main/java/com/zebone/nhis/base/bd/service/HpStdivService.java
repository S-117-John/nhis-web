package com.zebone.nhis.base.bd.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.HpStdivMapper;
import com.zebone.nhis.base.bd.vo.BdHpStdivVo;
import com.zebone.nhis.common.module.base.bd.price.BdHpStdiv;
import com.zebone.nhis.common.module.base.bd.price.BdHpStdivDt;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class HpStdivService {
	
	@Autowired
	HpStdivMapper hpStdivMapper;
	
	/**
	 * 保存
	 * @param param
	 * @param user
	 */
	public void saveHpStdiv(String param, IUser user) {

		BdHpStdivVo saveParam = JsonUtil.readValue(param, BdHpStdivVo.class);

		BdHpStdiv bdHpStdiv = saveParam.getBdHpStdiv();
		List<BdHpStdivDt> bdHpStdivDtList = saveParam.getBdHpStdivDtList();
		List<BdHpStdivDt> dleBdHpStdivDtsList = saveParam.getDelBdHpStdivDtList();//列表中删除的行
		bdHpStdiv.setPkOrg("~");
		if(StringUtils.isBlank(bdHpStdiv.getPkHpstdiv())){   // 新增 
			
			//重复校验
			checkRepeatValue(bdHpStdiv);
			
			DataBaseHelper.insertBean(bdHpStdiv);	
			for(BdHpStdivDt b:bdHpStdivDtList){
				b.setPkHpstdivdt(NHISUUID.getKeyId());
				b.setPkHpstdiv(bdHpStdiv.getPkHpstdiv());
				b.setPkOrg(((User)user).getPkOrg());
				b.setCreator(((User)user).getId());
				b.setCreateTime(new Date());
				b.setTs(new Date());
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdHpStdivDt.class), bdHpStdivDtList);
			
		}else{    //修改、删除行
			
			//重复校验
			checkRepeatValue(bdHpStdiv);
			
			//更新医保结算
			DataBaseHelper.updateBeanByPk(bdHpStdiv,false);
			//删除明细
			if(dleBdHpStdivDtsList != null && dleBdHpStdivDtsList.size() > 0){
				for(int i = 0;i < dleBdHpStdivDtsList.size();i++){
					if(StringUtils.isBlank(dleBdHpStdivDtsList.get(i).getPkHpstdivdt())){
						dleBdHpStdivDtsList.remove(i);
					}
				}
				if(dleBdHpStdivDtsList != null && dleBdHpStdivDtsList.size() > 0){
					for(BdHpStdivDt b:dleBdHpStdivDtsList){
						DataBaseHelper.execute("DELETE FROM BD_HP_STDIV_DT WHERE PK_HPSTDIVDT = ?", new Object[]{b.getPkHpstdivdt()});
					}
				}
			}
			
			for(BdHpStdivDt b : bdHpStdivDtList){
				b.setPkOrg("~");
				//新增行
				if("0".equals(b.getRowStatus())){
					b.setPkHpstdiv(bdHpStdiv.getPkHpstdiv());
					DataBaseHelper.insertBean(b);
				}
				//修改行
				if("1".equals(b.getRowStatus())){
					if(b.getAmount()==null)
					{
						DataBaseHelper.execute("update BD_HP_STDIV_DT set amount = null where PK_HPSTDIVDT = ?", b.getPkHpstdivdt());
					}
					if(b.getRate()==null)
					{
						DataBaseHelper.execute("update BD_HP_STDIV_DT set rate = null where PK_HPSTDIVDT = ?", b.getPkHpstdivdt());
					}
					DataBaseHelper.updateBeanByPk(b, false);
				}
			}
		}
	}
	
	/**
	 * 查找左侧树
	 * @param param
	 * @param user
	 */
	public List<BdHpStdivVo> qryBdHpStdiv(String param, IUser user){
		String temp = JsonUtil.getFieldValue(param, "qryParam");
		String nameDiv="";
		String spcodeDiv="";
		if(isEnglish(temp)) spcodeDiv = temp;
		else nameDiv = temp;
			
		return hpStdivMapper.qryBdHpStdiv(nameDiv,spcodeDiv.toUpperCase());
	}
	/**
	 * 判断字符串是否是英文
	 * @param charaString
	 * @return
	 */
	public  boolean isEnglish(String charaString){

	      return charaString.matches("^[a-zA-Z]*");
    }
	
	/**
	 * 查询右侧详细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryBdHpStdivDt(String param,IUser user){
		BdHpStdiv bdHpStdiv = JsonUtil.readValue(param,BdHpStdiv.class);
		String sql="select dt.pk_hpstdivdt,dt.pk_hpstdiv,dt.amt_min,dt.amt_max,dt.eu_divide,dt.rate,dt.amount,dt.date_begin,dt.date_end from bd_hp_stdiv_dt dt where dt.pk_hpstdiv=? and dt.del_flag='0'";
		return DataBaseHelper.queryForList(sql, new Object[]{bdHpStdiv.getPkHpstdiv()});
	}
	
	/**
	 * 删除
	 * @param param
	 * @param user
	 */
	public void delBdHpStdiv(String param,IUser user){
		String pk = JsonUtil.getFieldValue(param, "pk");
		
		//删除校验
		List<Map<String, Object>> execute = DataBaseHelper.queryForList("select count(1) as num from bd_hp_divconfig where pk_hpstdiv=? and del_flag='0'", new Object[]{pk});
		if(Integer.parseInt((execute.get(0).get("num").toString()))>0){
			throw new BusException("该策略已被使用，请先取消该策略的使用!");
		}
		
		DataBaseHelper.execute("DELETE FROM BD_HP_STDIV_DT WHERE PK_HPSTDIV=?", new Object[]{pk});
		DataBaseHelper.execute("DELETE FROM BD_HP_STDIV WHERE PK_HPSTDIV=?", new Object[]{pk});
	}
	
	/**
	 * 校验重复值
	 * @param ticketAddCs
	 * @param param
	 * @param user
	 */
	private void checkRepeatValue(BdHpStdiv stdiv){
		
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		if(!StringUtils.isBlank(stdiv.getPkHpstdiv())){//修改校验
			list = DataBaseHelper.queryForList("select stdiv.pk_hpstdiv,stdiv.code_div,stdiv.name_div,stdiv.eu_divtype,stdiv.spcode,stdiv.d_code,stdiv.note from bd_hp_stdiv stdiv where del_flag='0' and pk_hpstdiv != ?",new Object[]{stdiv.getPkHpstdiv()});
		}else{//新增校验
			list = DataBaseHelper.queryForList("select stdiv.pk_hpstdiv,stdiv.code_div,stdiv.name_div,stdiv.eu_divtype,stdiv.spcode,stdiv.d_code,stdiv.note from bd_hp_stdiv stdiv where del_flag='0' ");
		}
		for(Map<String,Object> m : list){
			if(stdiv.getCodeDiv().equals(m.get("codeDiv"))){
				throw new BusException("您的策略编码重复!");
			}
			if(stdiv.getNameDiv().equals(m.get("nameDiv"))){
				throw new BusException("您的策略名称重复!");
			}
		}
	}
}
