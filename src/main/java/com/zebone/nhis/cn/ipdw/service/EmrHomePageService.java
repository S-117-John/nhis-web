package com.zebone.nhis.cn.ipdw.service;

import com.zebone.nhis.cn.ipdw.dao.EmrHomePageMapper;
import com.zebone.nhis.cn.ipdw.vo.*;
import com.zebone.nhis.common.module.arch.BdArchDoctype;
import com.zebone.nhis.common.module.emr.rec.rec.*;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 病案首页
 * @author 李晋
 */
@Service
public class EmrHomePageService {

    @Autowired
    private EmrHomePageMapper emrHomePageMapper;

    /**
     * 查询病案首页数据
     * 004004008007
     * EmrHomePageService.getEmrHomePageByPkPv
     * @param
     * @return
     */
    public List<EmrHomePageVO> getEmrHomePageByPkPv(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        String pkPage = "";
        String pkPageOr = "";
        List<EmrHomePageVO> result = emrHomePageMapper.getEmrHomePageByPkPv(pkPv);
        //查询病情转归
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("SELECT dt_outcomes from pv_ip where pk_pv=:pkPv ");
        String sqlOutcomes = sbSql.toString();
        List<Map<String, Object>> lstDtOutcomes = DataBaseHelper.queryForList(sqlOutcomes, new Object[]{pkPv});
        
        String dtOutcomes = "";
        if(lstDtOutcomes != null && lstDtOutcomes.size() > 0)
        {
        	Map<String, Object> mapItem = lstDtOutcomes.get(0);
        	if(mapItem.containsKey("dtOutcomes"))
        	{
        		dtOutcomes = (String)mapItem.get("dtOutcomes");
        	}
        }
        for (EmrHomePageVO emrHomePageVO : result) {
            if (emrHomePageVO != null) {
                pkPage = emrHomePageVO.getPkPage();
                if (pkPage != null) {
                    List<EmrHomePageOps> opsList = emrHomePageMapper.findOps(pkPage);
                    List<EmrHomePageDiags> diagsList = emrHomePageMapper.findDiags(pkPage);
                    List<EmrHomePageCharges> chargesList = emrHomePageMapper.findCharges(pkPage);
                    List<EmrHomePageBr> brs = emrHomePageMapper.findBrs(pkPage);
                    List<EmrHomePageTrans> emrHomePageTrans = emrHomePageMapper.findTrans(pkPage);
                    List<EmrHomePageOr> emrHomePageOrList = emrHomePageMapper.findOr(pkPage);
                    if (emrHomePageOrList.size() > 0) {
                        pkPageOr = emrHomePageOrList.get(0).getPkPageor();
                        List<EmrHomePageOrDt> orDtList = emrHomePageMapper.findOrDt(pkPageOr);
                        emrHomePageVO.setOr(emrHomePageOrList.get(0));
                        emrHomePageVO.setOrdts(orDtList);
                    }
                    emrHomePageVO.setOps(opsList);
                    emrHomePageVO.setCharges(chargesList);
                    emrHomePageVO.setBrs(brs);
                    emrHomePageVO.setTrans(emrHomePageTrans);
                    emrHomePageVO.setDiags(diagsList);
                    //根据地区术语code查询名称
                    if(diagsList != null)
                    {
            	        Set<String> setCodes = new HashSet<String>();
            	        for (EmrHomePageDiags item : diagsList) 
            	        {
            	        	if(item.getCodeDcdt() != null && !"".equals(item.getCodeDcdt()))
            	        	{
            	        		setCodes.add(item.getCodeDcdt());	        		
            	        	}
            	        }
            	        StringBuilder sb = new StringBuilder();
                        sb.append("SELECT dcdt.code_dcdt,dcdt.name_dcdt FROM bd_term_dcdt dcdt ");
                        sb.append(" where (dcdt.code_dcdt IN ( ");
                        sb.append(CommonUtils.convertSetToSqlInPart(setCodes, "dcdt.code_dcdt"));
                        sb.append(")) ");
                        String sql = sb.toString();
                        List<Map<String, Object>> lstDcdts = DataBaseHelper.queryForList(sql, new Object[]{});
                        emrHomePageVO.setLstDcdts(lstDcdts);
                    }
                    //设置病情转归
                    emrHomePageVO.setDtOutcomes(dtOutcomes);
                }
            }
        }
        return result;
    }

    /**
     * 取数就诊信息
     * 004004008008
     * EmrHomePageService.getVisitInformationById
     * @param param
     * @param user
     * @return
     */
    public List<EmrVisitInformationVO> getVisitInformationById(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return emrHomePageMapper.getVisitInformationById(pkPv);
    }

    /**
     * 取数转科信息
     * 004004008009
     * EmrHomePageService.getTransferInformationById
     * @param param
     * @param user
     * @return
     */
    public List<EmrTransferInformationVO> getTransferInformationById(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return emrHomePageMapper.getTransferInformationById(pkPv);
    }

    /**
     * 取数诊断信息
     * 004004008010
     * EmrHomePageService.getTransferInformationById
     * @param param
     * @param user
     * @return
     */
    public List<EmrDiagnosticInformationVO> getDiagnosticInformationById(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        List<EmrDiagnosticInformationVO> lstEmrDiagnosticInformationVO = emrHomePageMapper.getDiagnosticInformationById(pkPv);
        //根据地区术语code查询名称
        if(lstEmrDiagnosticInformationVO != null)
        {
	        Set<String> setCodes = new HashSet<String>();
	        for (EmrDiagnosticInformationVO item : lstEmrDiagnosticInformationVO) 
	        {
	        	if(item.getCodeDcdt() != null && !"".equals(item.getCodeDcdt()))
	        	{
	        		setCodes.add(item.getCodeDcdt());	        		
	        	}
	        }
	        StringBuilder sb = new StringBuilder();
            sb.append("SELECT dcdt.code_dcdt,dcdt.name_dcdt FROM bd_term_dcdt dcdt ");
            sb.append(" where (dcdt.code_dcdt IN ( ");
            sb.append(CommonUtils.convertSetToSqlInPart(setCodes, "dcdt.code_dcdt"));
            sb.append(")) ");
            String sql = sb.toString();
            List<Map<String, Object>> retList = DataBaseHelper.queryForList(sql, new Object[]{});
            if(retList != null)
            {
            	//list 转 map
            	Map<String, Object> mapCodeDcdt = new HashMap<String, Object>();
                for(Map<String, Object> mapItem : retList) 
    			{
    				if(mapItem.get("codeDcdt") != null)
    				{
    					mapCodeDcdt.put(mapItem.get("codeDcdt").toString(), mapItem);
    				}
    			}   
                for (EmrDiagnosticInformationVO item : lstEmrDiagnosticInformationVO) 
                {
    	        	if(item.getCodeDcdt() != null && !"".equals(item.getCodeDcdt()))
    	        	{
    	        		if(mapCodeDcdt.containsKey(item.getCodeDcdt()))
    	        		{
    	        			@SuppressWarnings("unchecked")
							Map<String, Object> mapItem = (Map<String, Object>)mapCodeDcdt.get(item.getCodeDcdt());
    	        			if(mapItem.get("nameDcdt") != null)
    	        			{
    	        				item.setNameDcdt(mapItem.get("nameDcdt").toString());
    	        			}
    	        		}
    	        	}
    	        }
            }
        }
        return lstEmrDiagnosticInformationVO;
    }

    /**
     * 取数手术记录
     * 004004008011
     * EmrHomePageService.getSurgeryRecordById
     * @param param
     * @param user
     * @return
     */
    public List<EmrSurgeryRecordVO> getSurgeryRecordById(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return emrHomePageMapper.getSurgeryRecordById(pkPv);
    }

    /**
     * 取数费用信息
     * EmrHomePageService.getCostInformationById
     * 004004008012
     * 遍历结果集，如果项目编码长度为4位，
     * 将其金额加入编码为前两位的项目金额中，
     * 如：编码为【01】的项目金额=编码为【01】的项目金额+编码为【0101】~【0105】的项目金额的合计
     * @param param
     * @param user
     * @return
     */
    public List<EmrCostInformationVO> getCostInformationById(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return emrHomePageMapper.getCostInformationById(pkPv);
    }

    /**
     * 取数婴儿记录
     * EmrHomePageService.getBabyRecordById
     * 004004008013
     * @param param
     * @param user
     * @return
     */
    public List<EmrHomePageBr> getBabyRecordById(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        List<EmrBabyRecordVO> recordVOList = emrHomePageMapper.getBabyRecordById(pkPv);
        List<EmrHomePageBr> brList = new ArrayList<>();
        for (EmrBabyRecordVO record: recordVOList) {
            EmrHomePageBr emrHomePageBr = new EmrHomePageBr();
            emrHomePageBr.setSeqNo(record.getSortNo());
            emrHomePageBr.setDtSex(record.getDtSex());
            emrHomePageBr.setNameCb(record.getDtLabresult2());
            if(record.getWeight()!=null){
                emrHomePageBr.setWeight(BigDecimal.valueOf(record.getWeight()));
            }
            if(record.getDtInfantoutcome() != null){
                emrHomePageBr.setNameOc(record.getDtInfantoutcome());
            }
            //emrHomePageBr.setNameBreath(record.getDtBreath());
            if(record.getDtBreath() != null){            	
            	emrHomePageBr.setCodeBreath(record.getDtBreath());
            }
            if(record.getCntRescue()!=null){
                emrHomePageBr.setNumRes(BigDecimal.valueOf(record.getCntRescue()));
            }
            if(record.getCntSucceed()!=null){
                emrHomePageBr.setNumSucc(BigDecimal.valueOf(record.getCntSucceed()));
            }
            brList.add(emrHomePageBr);
        }
        return brList;
    }

    /**
     * 取数重症监护信息
     * EmrHomePageService.getIntensiveCareById
     * 004004008014
     * @param param
     * @param user
     * @return
     */
    public List<EmrHomePageTransVO> getIntensiveCareById(String param , IUser user){
        String pkPage = JsonUtil.getFieldValue(param,"pkPage");
        return emrHomePageMapper.getIntensiveCareById(pkPage);
    }

    /**
     * 取数CCHI信息
     * EmrHomePageService.getCchiById
     * 004004008015
     * @param param
     * @param user
     * @return
     */
    public List<EmrCchiVO> getCchiById(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return emrHomePageMapper.getCchiById(pkPv);
    }

    /**
     * 提交
     * EmrHomePageService.submit
     * 004004008016
     * @param param
     * @param user
     * @return
     */
    public int submit(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        Map<String,Object> params = new HashMap<String,Object>();// new Map<String, Object>();

        //判断患者信息
        PvEncounter pvInfo=DataBaseHelper.queryForBean(" select * from PV_ENCOUNTER WHERE PK_PV =:pkPv ",PvEncounter.class,new Object[]{pkPv});
        if(pvInfo==null || pvInfo.getDateEnd()==null){
            throw new BusException("该患者未出院，请等待患者出院后提交！");
        }

        params.put("pkPv",pkPv);
        params.put("date",new Date());

        return emrHomePageMapper.submit(params);
    }

    /**
     * 撤销
     * 004004008017
     * EmrHomePageService.revoke
     * @param param
     * @param user
     * @return
     */
    public int revoke(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return emrHomePageMapper.revoke(pkPv);
    }

    /**保存病案首页数据
     * EmrHomePageService.save
     * 004004008018
     * @param param
     * @param user
     */
    public void save(String param , IUser user){
        EmrParam emrParam = JsonUtil.readValue(param,EmrParam.class);
        EmrHomePage emrHomePage = JsonUtil.readValue(param,EmrHomePage.class);
        List<EmrHomePageTrans> emrHomePageTrans = emrHomePage.getTrans();
        List<EmrHomePageDiags> emrHomePageDiags = emrHomePage.getDiags();
        List<EmrHomePageOps> emrHomePageOps = emrHomePage.getOps();
        List<EmrHomePageCharges> emrHomePageCharges = emrHomePage.getCharges();
        List<EmrHomePageBr> emrHomePageBr = emrHomePage.getBrs();
        EmrHomePageOr emrHomePageOr = emrHomePage.getOr();
        List<EmrHomePageOrDt> emrHomePageOrDt = emrHomePage.getOrdts();
        saveHomePage(emrHomePage);
        String pkPage = emrHomePage.getPkPage();
        saveTrans(emrHomePageTrans, pkPage);
        saveDiags(emrHomePageDiags, pkPage);
        saveOps(emrHomePageOps, pkPage);
        saveCharges(emrHomePageCharges, pkPage);
        saveBr(emrHomePageBr, pkPage);
        String pkOr = "";
        pkOr = saveOr(emrHomePageOr, pkPage);
        saveOrDt(emrHomePageOrDt, pkOr);
        if("1".equals(emrParam.getDtOutcomesShowFlag()))
        {
            //保存病情转归
            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("pkPv", emrHomePage.getPkPv());
            mapParam.put("dtOutcomes", emrParam.getDtOutcomes());		
            DataBaseHelper.update("update pv_ip set dt_outcomes =:dtOutcomes where pk_pv =:pkPv", mapParam);
        }        	
    }

    /**
     * 保存EMR_HOME_PAGE表
     * @param emrHomePage
     */
    private void saveHomePage(EmrHomePage emrHomePage) {
        if(StringUtils.isNotEmpty(emrHomePage.getPkPage())){
            DataBaseHelper.update(DataBaseHelper.getUpdateSql(EmrHomePage.class),emrHomePage);
            if(emrHomePage.getSelfCost() == null){
                String sql=" update emr_home_page set self_cost = null where pk_page=:pkPage";
                DataBaseHelper.update(sql,emrHomePage);
            }
        }else {
            emrHomePage.setDelFlag("0");
            DataBaseHelper.insertBean(emrHomePage);
        }
    }

    /**
     * 保存EMR_HOME_PAGE_OR_DT表
     * @param emrHomePageOrDt
     * @param pkOr
     */
    private void saveOrDt(List<EmrHomePageOrDt> emrHomePageOrDt, String pkOr) {
        if(emrHomePageOrDt != null && emrHomePageOrDt.size()>0){
            for (EmrHomePageOrDt e: emrHomePageOrDt) {
                if(StringUtils.isNotEmpty(e.getPkOrdt())){
                    DataBaseHelper.update(DataBaseHelper.getUpdateSql(EmrHomePageOrDt.class),e);
                }else {
                    e.setPkPageor(pkOr);
                    e.setDelFlag("0");
                    DataBaseHelper.insertBean(e);
                }
            }
        }
    }

    /**
     * 保存EMR_HOME_PAGE_OR表
     * @param emrHomePageOr
     * @param pkPage
     * @return
     */
    private String saveOr(EmrHomePageOr emrHomePageOr, String pkPage) {
        String pkOr = "";
        if(emrHomePageOr != null){
            if(StringUtils.isNotEmpty(emrHomePageOr.getPkPageor())){
                DataBaseHelper.update(DataBaseHelper.getUpdateSql(EmrHomePageOr.class),emrHomePageOr);
            }else {
                emrHomePageOr.setPkPage(pkPage);
                emrHomePageOr.setDelFlag("0");
                DataBaseHelper.insertBean(emrHomePageOr);
            }
            pkOr = emrHomePageOr.getPkPageor();
        }
        return pkOr;
    }

    /**
     * 保存EMR_HOME_PAGE_BR表
     * @param emrHomePageBr
     * @param pkPage
     */
    private void saveBr(List<EmrHomePageBr> emrHomePageBr, String pkPage) {
        if(emrHomePageBr != null && emrHomePageBr.size()>0){
            //物理删除出生记录
            emrHomePageMapper.deleteHomePageBr(pkPage);
            for (EmrHomePageBr e: emrHomePageBr) {
                e.setPkBr(null);
                e.setPkPage(pkPage);
                e.setDelFlag("0");
                DataBaseHelper.insertBean(e);
            }
        }
    }

    /**
     * 保存EMR_HOME_PAGE_CHARGES表
     * @param emrHomePageCharges
     * @param pkPage
     */
    private void saveCharges(List<EmrHomePageCharges> emrHomePageCharges, String pkPage) {
        if(emrHomePageCharges != null && emrHomePageCharges.size()>0){
            //物理删除费用信息
            emrHomePageMapper.deleteHomePageCharges(pkPage);
            for (EmrHomePageCharges e: emrHomePageCharges) {
                e.setPkCharge(null);
                e.setPkPage(pkPage);
                e.setDelFlag("0");
                DataBaseHelper.insertBean(e);
            }
        }
    }

    /**
     * 保存EMR_HOME_PAGE_OPS表
     * @param emrHomePageOps
     * @param pkPage
     */
    private void saveOps(List<EmrHomePageOps> emrHomePageOps, String pkPage) {
        if(emrHomePageOps != null && emrHomePageOps.size()>0){
            //物理删除手术表
            emrHomePageMapper.deleteHomePageOps(pkPage);
            for (EmrHomePageOps e: emrHomePageOps) {
                e.setPkOps(null);
                e.setPkPage(pkPage);
                e.setDelFlag("0");
                DataBaseHelper.insertBean(e);
            }
        }
    }

    /**
     * 保存EMR_HOME_PAGE_DIAGS表
     * @param emrHomePageDiags
     * @param pkPage
     */
    private void saveDiags(List<EmrHomePageDiags> emrHomePageDiags, String pkPage) {
        //物理删除诊断表
        emrHomePageMapper.deleteHomePageDiags(pkPage);
        if(emrHomePageDiags != null && emrHomePageDiags.size()>0){
            for (EmrHomePageDiags e: emrHomePageDiags) {
                e.setPkDiag(null);
                e.setPkPage(pkPage);
                e.setDelFlag("0");
                DataBaseHelper.insertBean(e);
            }
        }
    }

    /**
     * 保存EMR_HOME_PAGE_TRANS表
     * @param emrHomePageTrans
     * @param pkPage
     */
    private void saveTrans(List<EmrHomePageTrans> emrHomePageTrans, String pkPage) {
        //物理删除EMR_HOME_PAGE_TRANS
        emrHomePageMapper.deleteHomePageTrans(pkPage);
        if(emrHomePageTrans != null && emrHomePageTrans.size()>0){
            for (EmrHomePageTrans e: emrHomePageTrans) {
                e.setPkTrans(null);
                e.setPkPage(pkPage);
                e.setDelFlag("0");
                DataBaseHelper.insertBean(e);
            }
        }
    }

    /**
     * 004004008011
     * EmrHomePageService.findTransByPkPv
     *
     * @param param
     * @param user
     * @return
     */
    public List<EmrHomePageTransVO> findTransByPkPv(String param , IUser user){
        String pkPage = JsonUtil.getFieldValue(param,"pkPage");
        return emrHomePageMapper.findTransByPkPage(pkPage);
    }

    /**
     * 取数护理天数/小时数信息
     * 004004008020
     * EmrHomePageService.getNurseDays
     * @param param
     * @param user
     * @return
     */
    public List<EmrPageNurseLevelVo> getNurseDays(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return emrHomePageMapper.getNurseDays(pkPv);
    }

    /**
     * 取数呼吸机使用时间信息
     * 004004008019
     * EmrHomePageService.getQuanHours
     * @param param
     * @param user
     * @return
     */
    public Double getQuanHours(String param , IUser user){
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return emrHomePageMapper.getQuanHours(pkPv);
    }

    /**
     * 根据主键删除数据
     * 004004008021
     * EmrHomePageService.delEmrHomeRecord
     * @param param
     * @param user
     * @return
     */
    public int delEmrHomeRecord(String param , IUser user){
        String pkPage = JsonUtil.getFieldValue(param,"pkPage");
        String tbFlag = JsonUtil.getFieldValue(param,"tbFlag");
        int result = -1;
        if("".equals(tbFlag))
        {
            return result;
        }

        if("0".equals(tbFlag))
        {
            //删除手术
            result = emrHomePageMapper.deleteHomePageOpsByPk(pkPage);
        }else if("1".equals(tbFlag))
        {
            //删除婴儿
            result = emrHomePageMapper.deleteHomePageBrByPk(pkPage);
        }else if("2".equals(tbFlag))
        {
            //删除肿瘤详情
            result = emrHomePageMapper.deleteHomePageOrDtByPk(pkPage);
        }else{
            result=-1;
        }
        return  result;
    }

    /**
     * 根据就诊主键查询质控项目列表
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> getQcEhpList(String param,IUser user) {
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return  emrHomePageMapper.getQcEhpList(pkPv);
    }

    /**
     * 根据就诊主键查询质控项目列表
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> getQcEhpDetail(String param,IUser user) {
        String pkPv = JsonUtil.getFieldValue(param,"pkPv");
        return  emrHomePageMapper.getQcEhpDetail(pkPv);
    }
}
