package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.pro.zsba.common.support.BigDecimalUtil;
import com.zebone.nhis.pro.zsba.common.support.Pinyin4jUtils;
import com.zebone.nhis.pro.zsba.common.support.StringTools;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybItemMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsItemData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbItem;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 医保目录 -- 业务处理类
 * @author zim
 *
 */
@Service
public class InsZsybItemService {

	@Autowired InsZsybItemMapper itemMapper;
	
	/**
	 * 保存医保目录
	 * @param param 实体对象数据
	 * @param user  登录用户
	 */
	@Transactional
	public void saveAndMap(String param , IUser user) {
		
		JSONObject jo = JSONObject.fromObject(param.replaceAll("\\\\", ""));
		String bgsj = jo.getString(StringTools.toLowerCaseFirstOne("BGSJ"));//变更时间
		JSONArray ja = jo.getJSONArray("itemData");
			if(ja!=null && ja.size()>0){
				//登录用户所属机构
				String pkOrg = ((User)user).getPkOrg();
				String pkEmp = ((User)user).getPkEmp();
				//获取中山医保的医保类型
				String sql = "select pk_hp from bd_hp where del_flag='0' and name = '中山普通医保'";
				List<BdHp> type = DataBaseHelper.queryForList(sql, BdHp.class);
				if(type!=null && type.size()>0){
					String pkHp = type.get(0).getPkHp(); 
					
					//清除原有数据
					sql = "update ins_item set del_flag='1', modifier=? where bgsj>=to_date(?,'yyyyMMdd') and pk_org=? and pk_hp=?";
					DataBaseHelper.update(sql, pkEmp, bgsj, pkOrg, pkHp);
					
					JSONObject obj = null;
					InsItemData item = null;
					for(int i=0; i<ja.size(); i++){
						obj = ja.getJSONObject(i);
						item = new InsItemData(obj);
						//保存医保项目
						saveInsItem(item, bgsj, pkOrg, pkHp);
					}
					
				}
			}
	}
	
	/**
	 * 保存医保目录
	 * @param item  下载的医保项目
	 * @param pkOrg 所属机构 
	 * @param pkHp  所属医保类型
	 */
	private void  saveInsItem(InsItemData item, String bgsj, String pkOrg, String pkHp) {
		InsZsBaYbItem insItem = new InsZsBaYbItem();
		insItem.setPkOrg(pkOrg);
		insItem.setPkHp(pkHp);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			insItem.setBgsj(sdf.parse(bgsj));//变更时间
		} catch (ParseException e) {
			e.printStackTrace();
		}
		insItem.setXmbh(item.getXMBH());
		insItem.setXmlb(item.getXMLB());
		insItem.setZwmc(StringTools.replaceAlls(item.getZWMC()));
		insItem.setYwmc(item.getYWMC());
		insItem.setSpcode(Pinyin4jUtils.toFirstSpell(item.getZWMC()));
		insItem.setFldm(item.getFLDM());
		insItem.setYpgg(item.getYPGG());
		insItem.setYpjx(item.getYPJX());
		insItem.setZjm(item.getZJM());
		insItem.setYwmldj(item.getYWMLDJ());
		insItem.setJbzfbl(BigDecimalUtil.divi(item.getJBZFBL(), "1", 2));
		insItem.setSqzfbl(BigDecimalUtil.divi(item.getSQZFBL(), "1", 2));
		insItem.setLxzfbl(BigDecimalUtil.divi(item.getLXZFBL(), "1", 2));
		insItem.setGszfbl(BigDecimalUtil.divi(item.getGSZFBL(), "1", 2));
		insItem.setSyzfbl(BigDecimalUtil.divi(item.getSYZFBL(), "1", 2));
		insItem.setTmzfbl(BigDecimalUtil.divi(item.getTMZFBL(), "1", 2));
		insItem.setBczfbl(BigDecimalUtil.divi(item.getBCZFBL(), "1", 2));
		insItem.setJzjg(BigDecimalUtil.divi(item.getJZJG(), "1", 2));
		insItem.setYjjypbm(item.getYJJYPBM());
		insItem.setYyclzczmc(item.getYYCLZCZMC());
		insItem.setYyclsyjzch(item.getYYCLSYJZCH());
		insItem.setTxbz(item.getTXBZ());
		insItem.setKssj(item.getKSSJ());
		insItem.setZssj(item.getZSSJ());
		insItem.setBz(StringTools.replaceAlls(item.getBZ()));
		
		DataBaseHelper.insertBean(insItem);//新增医保目录
	}
	
	
	
}
