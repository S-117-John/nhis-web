package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.SelfmacMapper;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoDetail;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoDetailReq;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoDetailRes;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoDt;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoDtVo;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoReq;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoRes;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoVo;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class SelfmacService {

	@Autowired
	private SelfmacMapper selfmacMapper;
	
	public String qrySettleInfo(String content) throws Exception {

		
		SettleInfoReq req= (SettleInfoReq)XmlUtil.XmlToBean(content, SettleInfoReq.class);
		SettleInfoRes res = new SettleInfoRes();
		
		String mes = vaildParam(req);
		if(StringUtils.isNotBlank(mes)){
			res.setResultCode("-1");
			res.setResultDesc(mes);
		}else{

			List<SettleInfoVo> list = selfmacMapper.qrySettleInfo(req);
			
			res.setResultCode("0");
			res.setResultDesc((list!=null&&list.size()>0)?"查询成功！":"查询无结果，请确认查询条件！");
			
			SettleInfoDetail detail = new SettleInfoDetail(); 
			detail.setResult(list);
			
			res.setDetail(detail);
		}
		String hospXml = XmlUtil.beanToXml(res, SettleInfoRes.class);
        return hospXml;
	}

	public String qrySettleDetail(String content) throws Exception{
		 
		SettleInfoDetailReq req= (SettleInfoDetailReq)XmlUtil.XmlToBean(content, SettleInfoDetailReq.class);
		SettleInfoDetailRes res = new SettleInfoDetailRes();

		if(StringUtils.isBlank(req.getStCode())){
			res.setResultCode("-1");
			res.setResultDesc("结算流水号为空！");
		}else{
			String dtHpdicttype = getDtHpdicttype(req);
			if(StringUtils.isBlank(dtHpdicttype)){
				res.setResultCode("-1");
				res.setResultDesc("医保参数为空！");
			}else{
				
				List<SettleInfoDtVo> list = selfmacMapper.qrySettleInfoDetail(req);
				
				res.setResultCode("0");
				res.setResultDesc((list!=null&&list.size()>0)?"查询成功！":"查询无结果，请确认查询条件！");
				
				SettleInfoDt detail = new SettleInfoDt(); 
				detail.setResult(list);
				
				res.setDetail(detail);
			}
		}
		String hospXml = XmlUtil.beanToXml(res, SettleInfoDetailRes.class);
        return hospXml;
	}
	private String getDtHpdicttype(SettleInfoDetailReq req) {
		String str="";
		if(StringUtils.isNoneBlank(req.getPkHp())){
			List<Map<String,Object>> list  = DataBaseHelper.queryForList("select attr.val_attr as dt_hpdicttype from bd_hp hp inner join bd_dictattr attr on hp.pk_hp = attr.pk_dict  inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp = tmp.pk_dictattrtemp  and tmp.code_attr = '0318' where hp.pk_hp =?", new Object[]{req.getPkHp()});
			if(list!=null&&list.size()==1){
				str=list.get(0).get("dtHpdicttype").toString();
				req.setDtHpdicttype(str);
			}
		}
		return str;
	}

	public String updatePrintList(String content) throws Exception{
		SettleInfoDetailReq req= (SettleInfoDetailReq)XmlUtil.XmlToBean(content, SettleInfoDetailReq.class);

		SettleInfoDetailRes res = new SettleInfoDetailRes();
		
		String codeSt = req.getStCode();
		if(StringUtils.isBlank(codeSt)){
			res.setResultCode("-1");
			res.setResultDesc("结算流水号为空！");
		}else{
			
			int i = DataBaseHelper.update(" update bl_settle set cnt_printlist=cnt_printlist+1 where code_st=? and cnt_printlist<2 ", new Object[]{codeSt});
			res.setResultCode(i>0?"0":"1");
			res.setResultDesc((i>0)?"更新打印次数成功！":"没有更新打印次数，请确认条件！");
		}
		String hospXml = XmlUtil.beanToXml(res, SettleInfoDetailRes.class);
        return hospXml;
	}
	private String  vaildParam(SettleInfoReq req){
		
		String mes="";
		if((StringUtils.isBlank(req.getPatientName())||StringUtils.isBlank(req.getUserCardId())) && StringUtils.isBlank(req.getIpSeqnoText())){
			mes+="请根据患者住院号查询 或者 根据患者姓名加上身份证号查询！";
		}
		boolean dateMes = req.getBeginDate()==null || req.getEndDate()==null;
		if(!dateMes){

			 Date begin = DateUtils.strToDate(req.getBeginDate(), "yyyy-MM-dd HH:mm:ss");
		     Date end =DateUtils.strToDate(req.getEndDate(), "yyyy-MM-dd HH:mm:ss");
			 dateMes = DateUtils.getDateSpace(begin,end) +1 >90;
		}
		if(dateMes) mes+=" 请查询90天内的结算单！";
	    
		return mes;
	}
}
