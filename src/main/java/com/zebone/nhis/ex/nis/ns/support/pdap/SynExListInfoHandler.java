package com.zebone.nhis.ex.nis.ns.support.pdap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.vo.OrderExListVo;
import com.zebone.nhis.ex.pub.support.OrderSortUtil;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.platform.modules.exception.BusException;

/**
 * 生成请领单，同步更新执行记录的请领明细主键信息。
 * @author yangxue
 * 
 */
public class SynExListInfoHandler {

	private String pk_pdapdt;
	private List<String> pk_exLists;
	
	public String getPk_pdapdt() {
		return pk_pdapdt;
	}

	public List<String> getPk_exLists() {
		return pk_exLists;
	}
	
	public SynExListInfoHandler(){
		
	}


	public SynExListInfoHandler(ExPdApplyDetail vo, List<GeneratePdApExListVo> ordList,OrderExListVo exceVO){
		pk_pdapdt = vo.getPkPdapdt();
		pk_exLists = new ArrayList<String>();
		for(GeneratePdApExListVo ex : ordList){
			pk_exLists.add(ex.getPkExocc());
		}
	}
	
	
	/**
	 * 获取更新语句
	 * @return
	 * @throws BusinessException
	 */
	public String getUpdateSql() throws BusException{
		if(CommonUtils.isEmptyString(pk_pdapdt)){
			throw new BusException("请领明细主键为空请检查！");
		}
		if(pk_exLists==null||pk_exLists.size()<=0){
			throw new BusException("执行单主键为空请检查！");
		}
		String pks = OrderSortUtil.convertPkListToStr(pk_exLists);
		String sql = "update ex_order_occ set  pk_pdapdt = '"+pk_pdapdt+"'  where pk_exocc in ("+pks.substring(1, pks.length())+")";
		 return sql;
	}
	
}
