package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.pub.vo.PayPosTr;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * pos交易记录  目前只用于三代卡个账
 * @author zrj
 *
 */
@Service
public class PayPosTrService {

	/**
	 * 保存个账交易记录
	 * @param param
	 * @param user
	 * @throws Exception
	 */
	public PayPosTr savePayPosTr(String param, IUser user) throws Exception {
		PayPosTr tr = JsonUtil.readValue(param, PayPosTr.class);
		if(StringUtils.isNotEmpty(tr.getBillStatus())){
			//门诊，消费或退款 都直接保存数据
			if(tr.getIdNo()!=null){
				tr.setIdNo(tr.getIdNo().replaceAll("\u0000",""));
			}
			if("撤销".equals(tr.getJyfl())){
				tr.setSamt4(tr.getSamt4().multiply(new BigDecimal("-1")));
			}
			tr.setBillStatus(null);
			DataBaseHelper.insertBean(tr);
		}else{
			//住院：消费直接保存，退款查消费后金额变负数后保存
			if(tr.getStransid().equals("12")||tr.getStransid().equals("62")){
				PayPosTr tr2 = null;
				if(tr.getStransid().equals("62")){
					tr2 = DataBaseHelper.queryForBean("select * from pay_pos_tr where pzh=? and jyje = ? and pk_pv = ? ", PayPosTr.class, tr.getFjy(), tr.getSamt4(), tr.getPkPv());
				}else{
					tr2 = DataBaseHelper.queryForBean("select * from pay_pos_tr where pzh=? and jyje = ? and pk_pv = ? ", PayPosTr.class, tr.getScard(), tr.getSamt4(), tr.getPkPv());
				}
			    tr2.setPkPosTr(null);
			    tr2.setCreator(null);
			    tr2.setCreateTime(null);
			    tr2.setModifier(null);
			    tr2.setTs(null);
				tr2.setPkSettle("");
				tr2.setStransid(tr.getStransid());
				tr2.setStime(tr.getStime());
				tr2.setSamt2(tr2.getSamt2().subtract(tr2.getSamt2()).subtract(tr2.getSamt2()));
				tr2.setSamt3(tr2.getSamt3().subtract(tr2.getSamt3()).subtract(tr2.getSamt3()));
				tr2.setSamt4(tr.getSamt4().subtract(tr.getSamt4()).subtract(tr.getSamt4()));
				tr2.setJyje(tr2.getJyje().subtract(tr2.getJyje()).subtract(tr2.getJyje()));
				DataBaseHelper.insertBean(tr2);
				tr = tr2;
			}else{
				if(tr.getIdNo()!=null){
					tr.setIdNo(tr.getIdNo().replaceAll("\u0000",""));
				}
				DataBaseHelper.insertBean(tr);
			}
		}
		return tr;
	}
}
