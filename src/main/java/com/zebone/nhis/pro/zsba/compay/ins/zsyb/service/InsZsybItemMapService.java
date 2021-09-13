package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbItemMap;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybItemMapService {
	
	/**
	 * 保存医保目录对照
	 * @param param 实体对象数据
	 * @param user  登录用户
	 */
	@Transactional
	public void saveAndMap(String param , IUser user) {
		List<InsZsBaYbItemMap> data = JsonUtil.readValue(param, new TypeReference<List<InsZsBaYbItemMap>>(){});
		for(InsZsBaYbItemMap im : data){
			if(im.getPkInsitemmap()!=null){
				if("99".equals(im.getYxbz())){
					im.setSpbz("4");
				}
				DataBaseHelper.updateBeanByPk(im,false);
			}else{
				String pkOrg = ((User)user).getPkOrg();
				String sql = "select pk_hp from bd_hp where del_flag='0' and code='00021'";//中山普通医保
				List<BdHp> type = DataBaseHelper.queryForList(sql, BdHp.class);
				if(type!=null && type.size()>0){
					String pkHp = type.get(0).getPkHp(); 
					im.setPkOrg(pkOrg);
					im.setPkHp(pkHp);
					DataBaseHelper.insertBean(im);
				}
			}
			// TODO: 2020-04-26 更新his表数据
			if(im.getXmlb().equals("1")){
				// 药品：bd_pd，code_hp : 医保编码，code_std ：药监局编码
				String ywmldj = "";
				String yyclsyjzch = "";
				String insItemSql = "select YWMLDJ,YYCLSYJZCH from INS_ITEM where PK_INSITEM=?";
				Map<String, Object> ywmldjMap = DataBaseHelper.queryForMap(insItemSql, im.getPkIinsitem());
				if(ywmldjMap!=null){
					if(ywmldjMap.get("ywmldj")!=null){
						ywmldj = String.valueOf(ywmldjMap.get("ywmldj"));
					}
					if(ywmldjMap.get("yyclsyjzch")!=null){
						yyclsyjzch = String.valueOf(ywmldjMap.get("yyclsyjzch"));
					}
				}
				String sql = "update bd_pd set code_hp=?, code_std=?, eu_hptype=?, appr_no=?, modifier=?, ts=? where pk_pd=?"; 
				DataBaseHelper.update(sql, new Object[]{im.getXmbh(), im.getYjjypbm(), ywmldj, yyclsyjzch, UserContext.getUser().getPkEmp(), new Date(), im.getPkItem()});
				
				/*
				 * TODO: 2020-06-15，同步检查适应症目录
				 * 没有维护适应症，则插入一条对应的适应症记录
				 */
				StringBuffer indpdInsertSql = new StringBuffer(" INSERT INTO BD_PD_INDPD ");
				indpdInsertSql.append(" select substring(replace(newid(),'-',''),1,32) PK_PDINDPD, '~                               ' PK_ORG, ");
				indpdInsertSql.append(" ind.PK_PDIND PK_PDIND, pd.PK_PD PK_PD, '医保同步插入' CREATOR, getdate() CREATE_TIME, null MODIFIER, null MODITY_TIME, '0' DEL_FLAG, getdate() TS, FLAG_PD ");
				indpdInsertSql.append(" from  BD_PD pd ");
				indpdInsertSql.append(" left join BD_PD_IND ind ON pd.CODE_HP = ind.CODE_IND ");
				indpdInsertSql.append(" left join BD_PD_INDPD pind on pind.PK_PD = pd.PK_PD and pind.PK_PDIND = ind.PK_PDIND ");
				indpdInsertSql.append(" where ind.PK_PDIND is not NULL and pind.PK_PD is NULL and pd.pk_pd=? ");
				DataBaseHelper.execute(indpdInsertSql.toString(), im.getPkItem());
				
			}else{
				// 项目：bd_item，code_hp：医保编码
				String sql = "update bd_item set code_hp=?, modifier=?, ts=? where pk_item=?"; 
				DataBaseHelper.update(sql, new Object[]{im.getXmbh(), UserContext.getUser().getPkEmp(), new Date(), im.getPkItem()});
			}
			
		}
	}

	
}