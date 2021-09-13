package com.zebone.nhis.emr.rec.dict.dc.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.emr.rec.dict.EmrCommonWords;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDataElement;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDataElementDc;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictClass;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictCode;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictOpt;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictRange;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictRangeCode;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDocType;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDoctor;
import com.zebone.nhis.common.module.emr.rec.dict.EmrEmpSet;
import com.zebone.nhis.common.module.emr.rec.dict.EmrGradeRule;
import com.zebone.nhis.common.module.emr.rec.dict.EmrGradeStandard;
import com.zebone.nhis.common.module.emr.rec.dict.EmrKnowledgeBase;
import com.zebone.nhis.common.module.emr.rec.dict.EmrParagraph;
import com.zebone.nhis.common.module.emr.rec.dict.EmrParagraphDc;
import com.zebone.nhis.common.module.emr.rec.dict.EmrParameter;
import com.zebone.nhis.common.module.emr.rec.dict.EmrSnGenerator;
import com.zebone.nhis.common.module.emr.rec.dict.EmrSwitch;
import com.zebone.nhis.common.module.emr.rec.dict.EmrTemplateContext;
import com.zebone.nhis.common.module.emr.rec.dict.EmrTemplateHead;
import com.zebone.nhis.common.module.emr.rec.dict.EmrTemplatePageStyle;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplate;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.emr.common.EmrConstants;
import com.zebone.nhis.emr.rec.dict.dao.EmrDictMapper;
import com.zebone.nhis.emr.rec.dict.dc.dao.DcEmrDictMapper;
import com.zebone.nhis.emr.rec.dict.vo.DictRangePrarm;
import com.zebone.nhis.emr.rec.dict.vo.EmrPatListPrarm;
import com.zebone.nhis.emr.rec.rec.dao.EmrRecMapper;
import com.zebone.nhis.emr.rec.tmp.dao.EmrTmpMapper;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 病历书写-字典管理
 * @author chengjia
 *
 */
@Service
public class DcEmrDictService {

	@Resource
	private DcEmrDictMapper dictMapper;
	
	@Resource
	private	EmrRecMapper recMapper;
	
	@Resource
	private	EmrTmpMapper tmpMapper;
	
	@Resource
	private EmrDictMapper emrDictMapper;
	
	/**
	 * 查询病历文档分类（根据eu_visit,eu_use)
	 * @return
	 */
	public List<EmrDocType> queryDocTypeListVstUsFlg(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		return dictMapper.queryDocTypeListVstUsFlg(map);
	}
	
	/**
	 * 查询病历文档分类（医师用/二级)
	 * @return
	 */
	public List<EmrDocType> queryDocTypeLTDoc(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		return dictMapper.queryDocTypeLTDoc();
	}
	
	/**
	 * 查询病历文档段落
	 * @return
	 */
	public List<EmrParagraph> queryParaList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		return dictMapper.queryParaList();
	}
	
	/** 
	 * 
	 * 
	 * 查询病历数据元素列表
	 * @return
	 */
	public List<EmrDataElement> queryDataEleList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		return dictMapper.queryDataEleList();
	}
	
	/**
	 * 查询病历值域 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRange> queryDictRangeList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		return dictMapper.queryDictRangeList();
	}
	
	/**
	 * 根据条件查询病历值域
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRange> queryDictRangeByConds(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String code=map.get("code").toString();
		List<EmrDictRange> rtnList=dictMapper.queryDictRangeByConds(code);
		return rtnList;
	}
	
	/**
	 * 查询病历值域（根据名称）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRange> queryDictRangeByName(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		//String name=map.get("name").toString();
		List<EmrDictRange> rtnList=dictMapper.queryDictRangeByName(map);
		return rtnList;
	}
	
	/**
	 * 查询病历值域编码
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRangeCode> queryDictRangeCodeByConds(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrDictRangeCode> rtnList=dictMapper.queryDictRangeCodeByConds(map);
		return rtnList;
	}
	
	/**
	 * 查询病历值域编码
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRangeCode> queryDictRangeCodeList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String pkRange=map.get("pkRange").toString();
		return dictMapper.queryDictRangeCodeList(pkRange);
	}		
	
	
	/**
	 * 根据编码查询病历选择字典
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictOpt> getDictOptByCode(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		return dictMapper.getDictOptByCode(map.get("code").toString());
	}	
	
	/**
	 * 查询病历参数列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EmrParameter> queryParameterList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		List<EmrParameter> list=dictMapper.queryParameterList(map);
		return list;
	}
	
	/**
	 * 根据编码查询病历参数
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EmrParameter queryParameterByCode(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		return dictMapper.queryParameterByCode(map);
	}
	
	/**
	 * 查询病历参数列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EmrSwitch> querySwitchList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		return dictMapper.querySwitchList(map);
	}
	
	/**
	 * 根据编码查询病历开关
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EmrSwitch querySwitchByCode(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		return dictMapper.querySwitchByCode(map);
	}
	
	
	/**
	 * 根据分类查询病历字典编码
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictCode> queryDictCodeList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String classCode=map.get("classCode").toString();
		return dictMapper.queryDictCodeList(classCode);
	}
	
	/**
	 * 保存病历值域
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrDictRange saveDictRange(String param,IUser user){
		DictRangePrarm rangeParam=JsonUtil.readValue(param, DictRangePrarm.class);
		EmrDictRange range=rangeParam.getRange();
		String rangeStatus="";
		if(StringUtils.isNoneEmpty(range.getStatus())){
			rangeStatus=range.getStatus();
		}
		if(rangeStatus.equals(EmrConstants.STATUS_NEW)){
			dictMapper.saveEmrDictRange(range);
		}else if(rangeStatus.equals(EmrConstants.STATUS_UPD)){
			dictMapper.updateEmrDictRange(range);
		}else if(rangeStatus.equals(EmrConstants.STATUS_DEL)){
			dictMapper.deleteEmrDictRange(range.getPkRange());
		}		
		
		if(rangeParam.getCodeList()!=null){
			for (EmrDictRangeCode code : rangeParam.getCodeList()) {
				String codeStatus="";
				if(StringUtils.isNoneEmpty(code.getStatus())){
					codeStatus=code.getStatus();
				}
				
				if(codeStatus.equals(EmrConstants.STATUS_NEW)){
					dictMapper.saveEmrDictRangeCode(code);
				}else if(codeStatus.equals(EmrConstants.STATUS_UPD)){
					dictMapper.updateEmrDictRangeCode(code);
				}else if(codeStatus.equals(EmrConstants.STATUS_DEL)){
					dictMapper.deleteEmrDictRangeCode(code.getPkRangeCode());
				}
				
			}
			
		}
		
		return range;
	}
	
	/**
	 * 取病历流水号
	 * @param param
	 * @param user
	 * @return
	 */
	public int genSysSn(String param,IUser user){
		int value=1;
		
		Map map = JsonUtil.readValue(param,Map.class);
		String code=map.get("code").toString();
		int rtn=dictMapper.updateSysSn(code);
		if(rtn==0){
			EmrSnGenerator emrSnGenerator=new EmrSnGenerator();
			emrSnGenerator.setCode(code);
			emrSnGenerator.setValue(1);
			emrSnGenerator.setDelFlag("0");
			
			dictMapper.saveSysSn(emrSnGenerator);
		}
		value=dictMapper.getSysSnByCode(code);
		
		return value;
	}
	
	/**
	 * 根据条件查询病历数据元素
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDataElement> queryDataElementByConds(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		List<EmrDataElement> rtnList=dictMapper.queryDataElementByConds(map);
		
		return rtnList;
	}
	
	/**
	 * 保存病历数据元素
	 * @param param
	 * @param user
	 * @return
	 */
	public int saveDataElement(String param,IUser user){
		EmrDataElement element=JsonUtil.readValue(param, EmrDataElement.class);
		if(StringUtils.isBlank(element.getEuCodeType())){
			element.setEuCodeType(null);
		}
		int rtn=0;
		if(StringUtils.isNoneEmpty(element.getStatus())&&element.getStatus().equals("new")){
			if(StringUtils.isNoneEmpty(element.getPkOrg())){
				element.setPkOrg(UserContext.getUser().getPkOrg());
			}
			//新增
			rtn=dictMapper.saveEmrDataElementDc(element);
		}else{
			//修改
			rtn=dictMapper.updateEmrDataElementDc(element);
		}
		return rtn;
		
	}
	
	/**
	 * 查询标准诊断编码
	 * @param param
	 * @param user
	 */
	public List<BdTermDiag> queryTermDiagList(String param,IUser user){
		BdTermDiag termDiag = JsonUtil.readValue(param,BdTermDiag.class);
		
		return dictMapper.queryTermDiagList(termDiag);
	}
	
	/**
	 * 查询知识库中填写过的诊断
	 * @param param
	 * @param user
	 */
	public List<BdTermDiag> queryTermDiagTextList(String param,IUser user){
		BdTermDiag termDiag = JsonUtil.readValue(param,BdTermDiag.class);
		
		return dictMapper.queryTermDiagTextList(termDiag);
	}
	
	/**
     * 查询病历字典代码表
     */
    public List<EmrDictCode> queryDictCodeAllList(String param,IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
		
		return dictMapper.queryDictCodeAllList();
    }
    
    /**
     * 查询临床知识库
     * @param param
	 * @param user
     */
    public List<EmrKnowledgeBase> queryKnowledgeBase(String param,IUser user){
    	EmrKnowledgeBase emrKnowledgeBase = JsonUtil.readValue(param,EmrKnowledgeBase.class);
    	
    	return dictMapper.queryKnowledgeBase(emrKnowledgeBase);
    }
    
    /**
     * 保存临床知识库
     * @param param
	 * @param user
     */
	public EmrKnowledgeBase saveEmrKnowledgeBase(String param , IUser user){
		EmrKnowledgeBase emrKnowledgeBase = JsonUtil.readValue(param,EmrKnowledgeBase.class);
		
		if(emrKnowledgeBase.getPkKnowBase()==null){
			DataBaseHelper.insertBean(emrKnowledgeBase);
		}else{
			DataBaseHelper.updateBeanByPk(emrKnowledgeBase,false);
		}
		return emrKnowledgeBase;
	}
	
	/**
     * 查询常用词汇所有记录
     * @param param
	 * @param user
     */
	public List<EmrCommonWords> findAllEmrCommonWords(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		String sql = "select  cw.*,dp.pk_dept,(case cw.eu_level when 0 then '全院' when 1 then '科室' when 2 then '个人' end) as euLevelName "
					+ "from emr_common_words cw LEFT OUTER JOIN bd_ou_dept dp on cw.pk_dept=dp.pk_dept"
					+ " where cw.eu_level=?";
		List<EmrCommonWords> list = DataBaseHelper.queryForList(sql, EmrCommonWords.class, new Object[]{map.get("euLevel")});
		return list;
	}
	
	/**
     * 更新常用词汇记录
     * @param param
	 * @param user
     */
	public List<EmrCommonWords> saveEmrCommonWordsBase(String param , IUser user){
		List<EmrCommonWords> list = JsonUtil.readValue(param, new TypeReference<List<EmrCommonWords>>(){});
		for(EmrCommonWords emrCommonWords : list){
			if(emrCommonWords.getCodeStatus()!=null){
				if(emrCommonWords.getCodeStatus().equals(EmrConstants.STATUS_NEW)){
					DataBaseHelper.insertBean(emrCommonWords);
					//dictMapper.saveEmrCommonWords(emrCommonWords);
				}else if(emrCommonWords.getCodeStatus().equals(EmrConstants.STATUS_UPD)){
					//dictMapper.updateEmrCommonWords(emrCommonWords);
					DataBaseHelper.updateBeanByPk(emrCommonWords,false);
				}
			}
		}
//		String sql = "select  cw.*,dp.pk_dept,(case cw.eu_level when 0 then '全院' when 1 then '科室' when 2 then '个人' end) as euLevelName "
//				+ "from emr_common_words cw LEFT OUTER JOIN bd_ou_dept dp on cw.pk_dept=dp.pk_dept";
//		List<EmrCommonWords> nlist = DataBaseHelper.queryForList(sql, EmrCommonWords.class, new Object[]{});
		
		return null;
	}
	
	/**
     * 查询病历文档分类
     * @param param
	 * @param user
     */
    public List<EmrDocType> findAllEmrDocType(String param , IUser user){
//    	Map map = JsonUtil.readValue(param,Map.class);
//    	String sql = "SELECT * FROM EMR_DOC_TYPE dt where dt.del_flag='0' order by dt.sort_num";
//    	List<EmrDocType> nlist = DataBaseHelper.queryForList(sql, EmrDocType.class, new Object[]{});
//    	return nlist;
    	
    	EmrDocType docType = JsonUtil.readValue(param,EmrDocType.class);
    	
    	return dictMapper.findAllEmrDocType(docType);
    	
    	
    }
    
    /**
     * 更新病历文档分类
     * @param param
	 * @param user
     */
    public List<EmrDocType> saveEmrDocTypesBase(String param , IUser user){
    	List<EmrDocType> list = JsonUtil.readValue(param, new TypeReference<List<EmrDocType>>(){});
    	for(EmrDocType docType : list){
    		if(docType.getCodeStatus()!=null)
	    		if(docType.getCodeStatus().equals(EmrConstants.STATUS_NEW)){
	    			DataBaseHelper.insertBean(docType);
	    		}else if(docType.getCodeStatus().equals(EmrConstants.STATUS_UPD)){
	    			DataBaseHelper.updateBeanByPk(docType,false);
	    		}
    	}
    	String sql = "SELECT * FROM EMR_DOC_TYPE dt order by dt.sort_num,dt.code";
    	List<EmrDocType> nlist = DataBaseHelper.queryForList(sql, EmrDocType.class, new Object[]{});
    	return nlist;
    }
    
    /**
     * 查询文档段落
     * @param param
	 * @param user
     */
    public List<EmrParagraphDc> findAllEmrParagraphDc(String param , IUser user){
    	//Map map = JsonUtil.readValue(param,Map.class);
    	//String sql = "SELECT * FROM emr_paragraph ph  order by ph.code";
    	
    	//List<EmrParagraphDc> list = DataBaseHelper.queryForList(sql, EmrParagraphDc.class, new Object[]{});
    	
    	EmrParagraphDc para = JsonUtil.readValue(param,EmrParagraphDc.class);
    	
    	return dictMapper.findAllEmrParagraphDc(para);
    }
    
    /**
     * 更新文档段落
     * @param param
	 * @param user
     */
    public List<EmrParagraph> saveEmrParagraphDcBase(String param , IUser user){
    	List<EmrParagraph> list = JsonUtil.readValue(param, new TypeReference<List<EmrParagraph>>(){});
    	if(list!=null){
	    	for(int index=0;index<list.size()-1;index++){
	    		for(int index2=index+1;index2<list.size();index2++){
	    			if(list.get(index).getName().equals(list.get(index2).getName())){
	    				throw new BusException("段落名称重复："+list.get(index).getName());
	    			}
	    			if(list.get(index).getCode().equals(list.get(index2).getCode())){
	    				throw new BusException("段落编码重复："+list.get(index).getCode());
	    			}
	    		}
	    		
	    	}
    	}
    	for(EmrParagraph paragraph : list){
    		if(paragraph.getCodeStatus()!=null)
	    		if(paragraph.getCodeStatus().equals(EmrConstants.STATUS_NEW)){
	    			DataBaseHelper.insertBean(paragraph);
	    		}else if(paragraph.getCodeStatus().equals(EmrConstants.STATUS_UPD)){
	    			DataBaseHelper.updateBeanByPk(paragraph,false);
	    		}
    	}
    	String sql = "SELECT * FROM emr_paragraph ph  order by ph.sort_num";
    	List<EmrParagraph> nlist = DataBaseHelper.queryForList(sql, EmrParagraph.class, new Object[]{});
    	return nlist;
    }
    
    /**
     * 查询病历元素
     * @param param
	 * @param user
     */
    public List<EmrDataElementDc> findAllEmrDataElementDcBase(String param , IUser user){
    	EmrDataElementDc dataElement = JsonUtil.readValue(param,EmrDataElementDc.class);
    	if(Application.isSqlServer()){
    		return dictMapper.findAllEmrDataElementDcBaseSql(dataElement);
    	}else{
    		return dictMapper.findAllEmrDataElementDcBase(dataElement);
    	}
    	
    }
    
    /**
     * 更新病历元素
     * @param param
	 * @param user
     */
    public List<EmrDataElementDc> saveEmrDataElementDcBase(String param, IUser user) {
        List<EmrDataElement> list = JsonUtil.readValue(param, new TypeReference<List<EmrDataElement>>() {});

        Map<String, String> codeMap = Maps.newHashMap();
        Map<String, String> nameMap = Maps.newHashMap();
        for (EmrDataElement emrDataElement : list) {
            codeMap.put(emrDataElement.getCode(), "code");
            nameMap.put(emrDataElement.getName(), "name");
        }
        if (codeMap.size() < list.size())
            throw new BusException("元素编码不能重复");
        if (nameMap.size() < list.size())
            throw new BusException("元素名称不能重复");
        
        for (EmrDataElement emrDataElement : list) {
            if (emrDataElement.getStatus() != null)
                if (emrDataElement.getEuCodeType() != null && emrDataElement.getEuCodeType().equals(""))
                    emrDataElement.setEuCodeType(null);
            
            
            if (emrDataElement.getStatus().equals(EmrConstants.STATUS_NEW)) {
                if (StringUtils.isNoneEmpty(emrDataElement.getPkOrg())) {
                    emrDataElement.setPkOrg(UserContext.getUser().getPkOrg());
                }
                int count = DataBaseHelper.queryForScalar("select count(1) from EMR_DATA_ELEMENT "
                        + "where code = ? and DEL_FLAG = '0'",Integer.class, emrDataElement.getCode());
                
                if (count > 0)
                    throw new BusException("元素编码不能重复");
                count = DataBaseHelper.queryForScalar("select count(1) from EMR_DATA_ELEMENT "
                        + "where name = ? and DEL_FLAG = '0'",Integer.class, emrDataElement.getName());
                if (count > 0)
                    throw new BusException("元素名称不能重复");
                DataBaseHelper.insertBean(emrDataElement);
            } else if (emrDataElement.getStatus().equals(EmrConstants.STATUS_UPD)) {
                int count = DataBaseHelper.queryForScalar("select count(1) from EMR_DATA_ELEMENT "
                        + "where code = ? and DEL_FLAG = '0' and pk_element != ?",Integer.class, new Object[]{emrDataElement.getCode(),emrDataElement.getPkElement()});
                
                if (count > 0)
                    throw new BusException("元素编码不能重复");
                count = DataBaseHelper.queryForScalar("select count(1) from EMR_DATA_ELEMENT "
                        + "where name = ? and DEL_FLAG = '0' and pk_element != ?",Integer.class, new Object[]{emrDataElement.getName(),emrDataElement.getPkElement()});
                if (count > 0)
                    throw new BusException("元素名称不能重复");
                DataBaseHelper.updateBeanByPk(emrDataElement, false);
            }
        }
        // String sql = "select * from emr_data_element el where el.del_flag='0'
        // order by el.code";
        // List<EmrDataElementDc> nlist = DataBaseHelper.queryForList(sql,
        // EmrDataElement.class, new Object[]{});
        return null;
    }
    
    /**
     * 查询病历系统参数
     * @param param
	 * @param user
     */
    public List<EmrParameter> findAllEmrParameterBase(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	
    	return dictMapper.findAllEmrParameterBase(map);
    }
    
    /**
     * 更新病历系统参数
     * @param param
	 * @param user
     */
    public void updateEmrParameterBase(String param , IUser user){
    	List<EmrParameter> list = JsonUtil.readValue(param, new TypeReference<List<EmrParameter>>(){});
    	for(EmrParameter emrParameter : list){
    		if(emrParameter.getStatus()!=null){
    			if(emrParameter.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrParameter,false);
    			}else if(emrParameter.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrParameter);
    			}
    		}
    	}
    	
    }
    
    /**
     * 查询病历系统开关
     * @param param
	 * @param user
     */
    public List<EmrSwitch> findAllEmrSwitchBase(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	
    	return dictMapper.findAllEmrSwitchBase(map);
    }
    
    /**
     * 更新病历系统开关
     * @param param
	 * @param user
     */
    public void updateEmrSwitchBase(String param , IUser user){
    	List<EmrSwitch> list = JsonUtil.readValue(param, new TypeReference<List<EmrSwitch>>(){});
    	for(EmrSwitch emrSwitch : list){
    		if(emrSwitch.getStatus()!=null){
    			if(emrSwitch.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrSwitch,false);
    			}else if(emrSwitch.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrSwitch);
    			}
    		}
    	}
    	
    }
    
    /**
     * 查询病历字典分类
     * @param param
	 * @param user
     */
    public List<EmrDictClass> findAllEmrDictClassBase(String param , IUser user){
    	EmrDictClass emrDictClass = JsonUtil.readValue(param,EmrDictClass.class);
    	
    	return dictMapper.findAllEmrDictClassBase(emrDictClass);
    }
    
    /**
     * 更新病历字典分类
     * @param param
	 * @param user
     */
    public List<EmrDictClass> saveEmrDictClassBase(String param , IUser user){
    	List<EmrDictClass> list = JsonUtil.readValue(param, new TypeReference<List<EmrDictClass>>(){});
    	for(EmrDictClass emrDictClass : list){
    		if(emrDictClass.getStatus()!=null){
    			if(emrDictClass.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrDictClass);
    			}else if(emrDictClass.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrDictClass,false);
    			}
    		}
    	}
    	EmrDictClass emrDictClass = new EmrDictClass();
    	return dictMapper.findAllEmrDictClassBase(emrDictClass);
    }
    
    /**
     * 查询病历字典代码表
     * @param param
	 * @param user
     */
    public List<EmrDictCode> findAllEmrDictCodeByCls(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	
    	return dictMapper.findAllEmrDictCodeByCls(map.get("pkDictcls").toString());
    }
    
    /**
     * 更新病历字典代码表
     * @param param
	 * @param user
     */
    public void saveEmrDictCodeBase(String param , IUser user){
    	List<EmrDictCode> list = JsonUtil.readValue(param, new TypeReference<List<EmrDictCode>>(){});
    	for(EmrDictCode emrDictCode : list){
    		if(emrDictCode.getStatus()!=null){
    			if(emrDictCode.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrDictCode);
    			}else if(emrDictCode.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrDictCode,false);
    			}
    		}
    	}
    }
    
    /**
     * 查询病历值域
     * @param param
	 * @param user
     */
    public List<EmrDictRange> findAllEmrDictRangeBase(String param , IUser user){
    	EmrDictRange emrDictRange = JsonUtil.readValue(param,EmrDictRange.class);
    	
    	return dictMapper.findAllEmrDictRangeBase(emrDictRange);
    }
    
    /**
     * 更新病历值域
     * @param param
	 * @param user
     */
    public List<EmrDictRange> saveEmrDictRangeBase(String param , IUser user){
    	List<EmrDictRange> list = JsonUtil.readValue(param, new TypeReference<List<EmrDictRange>>(){});
    	for(EmrDictRange emrDictRange : list){
    		if(emrDictRange.getStatus()!=null){
    			if(emrDictRange.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrDictRange);
    			}else if(emrDictRange.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrDictRange,false);
    			}
    		}
    	}
    	EmrDictRange emrDictRange = new EmrDictRange();
    	return dictMapper.findAllEmrDictRangeBase(emrDictRange);
    }
    
    /**
     * 查询病历值域代码
     * @param param
	 * @param user
     */
    public List<EmrDictRangeCode> findAllEmrDictRangeCodeByRa(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	
    	return dictMapper.findAllEmrDictRangeCodeByRa(map.get("pkRange").toString());
    }
    
    /**
     * 更新病历值域代码
     * @param param
	 * @param user
     */
    public void saveEmrDictRangeCodeBase(String param , IUser user){
    	List<EmrDictRangeCode> list = JsonUtil.readValue(param, new TypeReference<List<EmrDictRangeCode>>(){});
    	for(EmrDictRangeCode emrDictRangeCode : list){
    		if(emrDictRangeCode.getStatus()!=null){
    			if(emrDictRangeCode.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrDictRangeCode);
    			}else if(emrDictRangeCode.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrDictRangeCode,false);
    			}
    		}
    	}
    }
    
    /**
     * 查询病历医师设置
     * @param param
	 * @param user
     */
    public List<EmrDoctor> findAllEmrDoctorBase(String param , IUser user){
    	EmrDoctor emrDoctor = JsonUtil.readValue(param,EmrDoctor.class);
    	emrDoctor.setPkOrg(UserContext.getUser().getPkOrg().trim()+"%");
    	return dictMapper.findAllEmrDoctorBase(emrDoctor);
    }
    
    /**
     * 重置病历医师设置中的密码
     * @param param
	 * @param user
     */
    public int updateEmrDoctorByPwd(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);

    	return dictMapper.updateEmrDoctorByPwd(map.get("pkEmp").toString());
    }
    
    /**
     * 更新病历医师设置
     * @param param
	 * @param user
     */
    public List<EmrDoctor> saveEmrDoctorBase(String param , IUser user){
    	List<EmrDoctor> list = JsonUtil.readValue(param, new TypeReference<List<EmrDoctor>>(){});
    	for(EmrDoctor emrDoctor : list){
    		if(emrDoctor.getStatus()!=null){
    			if(emrDoctor.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrDoctor);
    			}else if(emrDoctor.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrDoctor,false);
    			}
    		}
    	}
    	EmrDoctor emrDoctor = new EmrDoctor();
    	return dictMapper.findAllEmrDoctorBase(emrDoctor);
    }
    
    /**
     * 根据主键删除病历医师设置
     * @param param
	 * @param user
     */
    public List<EmrDoctor> deleteEmrDoctorBase(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	dictMapper.deleteEmrDoctor(map.get("pkEmp").toString());
    	
    	EmrDoctor emrDoctor = new EmrDoctor();
    	return dictMapper.findAllEmrDoctorBase(emrDoctor);
    }
    
    /**
     * 根据条件查询环节质控
     * @param param
	 * @param user
     */
    public List<EmrPatListPrarm> findLinkControlByPrame(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	String sql="select  pat.*,emp.name as qcName from view_emr_pat_list pat,emr_pat_rec patrec ";
    	String where = "where pat.pk_dept='"+map.get("pkDept").toString()+"' and pat.pk_pv=patrec.pk_pv ";

    	if(!map.get("stateAll").toString().equals("")){
    		where = where + " and pat.flag_in in ("+map.get("stateAll").toString()+") ";
    	}
    	if(!map.get("levelAll").toString().equals("")){
    		where = where + " and nvl(pat.dt_level_dise,'00') in ("+map.get("levelAll").toString()+") ";
    	}
    	
    	if(!map.get("opY").toString().equals("")){
    		where = where + " and exists (select 1 from ex_order_occ occ where occ.pk_pv = pat.pk_pv) ";
    	}
    	if(!map.get("opN").toString().equals("")){
    		where = where + " and not exists (select 1 from ex_order_occ occ where occ.pk_pv = pat.pk_pv) ";
    	}
    	
    	if(!map.get("spotY").toString().equals("")){
    		where = where + " and exists (select 1 from emr_grade_rec rec where rec.pk_pv = pat.pk_pv) ";
    	}
    	if(!map.get("spotN").toString().equals("")){
    		where = where + " and not exists (select 1 from emr_grade_rec rec where rec.pk_pv = pat.pk_pv) ";
    	}
    	
    	if(!map.get("limit").toString().equals("")){
    		
    	}
    	sql = sql +" left join view_emr_emp_list emp on emp.pk_emp=patrec.pk_emp_qc " + where;
    	List<EmrPatListPrarm> nlist = DataBaseHelper.queryForList(sql, EmrPatListPrarm.class, new Object[]{});
    	return nlist;
    }
    
    /**
     * 根据条件查询科室质控
     * @param param
	 * @param user
     */
    public List<EmrPatListPrarm> findDeptControlByPrame(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	String sql= "select pat.*,"
    			+ "fin.name as finishName,"
			    + "rec.finish_date as finishDate,"
			    + "submit.name as submitName,"
			    + "rec.submit_date as submitDate,"
			    + "empqc.name as empQcName,"
			    + "rec.emp_qc_date as empQcDate,"
			    + "rec.dept_qc_date as deptQcDate,"
			    + "rec.eu_emp_qc_grade as euEmpQcGrade,"
			    + "rec.emp_qc_score as empQcScore,"
			    + "rec.eu_dept_qc_grade as euDeptQcGrade,"
			    + "rec.dept_qc_score as deptQcScore,"
			    + "rec.diag_dis_txt as diagDisTxt "
    			+ "from view_emr_pat_list pat,emr_pat_rec rec "
    			+ "left join view_emr_emp_list fin on fin.pk_emp = rec.pk_emp_finish "
    			+ "left join view_emr_emp_list submit on submit.pk_emp=rec.pk_emp_submit "
    			+ "left join view_emr_emp_list empqc on empqc.pk_emp=rec.pk_emp_emp_qc ";
    	String where = "where pat.pk_pv = rec.pk_pv and rec.del_flag<>'1' "
    			+ "and pat.pk_dept = '"+map.get("pkDept").toString()+"' ";
    			
    	if(map.get("status").toString().equals("2")){
    		where = where + "and rec.eu_status='2' order by rec.finish_date,pat.name,rec.pk_pv";
    	}else if(map.get("status").toString().equals("3")){
    		where = where + "and rec.eu_status>='3' "
    				+ "and (pat.code_pi like '%"+map.get("retrieval").toString()+"%' or pat.name like '%"+map.get("retrieval").toString()+"%' or pat.diag_dis_txt like '%"+map.get("retrieval").toString()+"%') "
    				+ "and pat.date_end>=to_date('"+map.get("beginDate").toString()+"','yyyy/mm/dd hh24:mi:ss') "
    				+ "and pat.date_end<to_date('"+map.get("endDate").toString()+"','yyyy/mm/dd hh24:mi:ss') "
    				+ "order by rec.submit_date,rec.dept_qc_date,rec.archive_date,pat.date_end,pat.name,rec.pk_pv";
    	}else{
    		where = where + "and rec.eu_status='1' "
    				+ "and (pat.code_pi like '%"+map.get("retrieval").toString()+"%' or pat.name like '%"+map.get("retrieval").toString()+"%' or pat.diag_dis_txt like '%"+map.get("retrieval").toString()+"%') "
    				+ "and pat.date_end >= to_date('"+map.get("beginDate").toString()+"','yyyy/mm/dd hh24:mi:ss') "
    				+ "and pat.date_end < to_date('"+map.get("endDate").toString()+"','yyyy/mm/dd hh24:mi:ss') "
    				+ "order by pat.date_end,pat.name,rec.pk_pv";
    	}
    	sql = sql + where;
    	List<EmrPatListPrarm> nlist = DataBaseHelper.queryForList(sql, EmrPatListPrarm.class, new Object[]{});
    	return nlist;
    }
    
    /**
     * 主键修改病历状态
     * @param param
	 * @param user
     */
    public int updateEmrPatPecByPk(String param , IUser user){
    	List<Map> list = JsonUtil.readValue(param, new TypeReference<List<Map>>(){});
    	int flag = 0;
    	Date now=new Date();
    	String format="yyyy-MM-dd HH:mm:ss";
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	String dateStr=sdf.format(now);
    	for(Map map : list){
        	String status=map.get("status").toString();
    		String sql="update emr_pat_rec set eu_status='"+status+"'"+
    				 " where pk_patrec='"+map.get("pkPatrec").toString()+"'";
    		if(status!=null&&status.equals("5")){
    			//归档
        		sql="update emr_pat_rec set eu_status='"+status+"',"+
        				" pk_emp_archive='"+UserContext.getUser().getPkEmp()+"',"
        				+ "archive_date=to_date('"+dateStr+"','yyyy-MM-dd hh24:mi:ss')"
        				+ " where pk_patrec='"+map.get("pkPatrec").toString()+"'";
    			
    		}else if(status!=null&&status.equals("1")){
    			//取消完成
    			sql="update emr_pat_rec set eu_status='"+status+"',"+
        				" pk_emp_finish=null,"
        				+ "finish_date=null"
        				+ " where pk_patrec='"+map.get("pkPatrec").toString()+"'";
    		}
    		flag = flag +DataBaseHelper.update(sql, new Object[]{});
    	}
    	return flag;
    }
    
    /**
     * 根据条件查询终末质控
     * @param param
	 * @param user
     */
    public List<EmrPatListPrarm> findEndControlByPrame(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	String sql="select pat.*,"
    			+ "emparchive.name as archiveName,"
    			+ "rec.archive_date as archiveDate,"
    			+ "deptqc.name as  deptQcName,"
    			+ "rec.dept_qc_date as deptQcDate,"
    			+ "rec.dept_qc_score as deptQcScore,"
    			+ "rec.eu_dept_qc_grade as euDeptQcGrade,"
    			+ "finalqc.name as finalQcName,"
    			+ "rec.final_qc_date as finalQcDate,"
    			+ "rec.eu_final_qc_grade as euFinalQcGrade,"
    			+ "rec.final_qc_score as finalQcScore,"
			    + "rec.emp_qc_date as empQcDate,"
			    + "rec.emp_qc_score as empQcScore,"
    			+ "empqc.name as empQcName,"
    			+ "rec.finish_date as finishDate, "
    			+ "empf.name as finishName,"
    			+ "rec.submit_date as submitDate,"
    			+ "emps.name  as submitName"
    			+ " from view_emr_pat_list pat,emr_pat_rec rec "
    			+ "left join view_emr_emp_list emparchive on emparchive.pk_emp=rec.pk_emp_archive "
    			+ "left join view_emr_emp_list deptqc on deptqc.pk_emp=rec.pk_emp_dept_qc "
    			+ "left join view_emr_emp_list finalqc on finalqc.pk_emp=rec.pk_emp_final_qc "
    			+ "left join view_emr_emp_list empqc on empqc.pk_emp=rec.pk_emp_emp_qc "
    			+ "left join view_emr_emp_list empf on empf.pk_emp=rec.pk_emp_finish "
    			+ "left join view_emr_emp_list emps on emps.pk_emp=rec.pk_emp_submit ";
    	
    	String where = "where pat.pk_pv= rec.pk_pv "
    			+ "and rec.eu_status in ('"+map.get("status").toString()+"') "		
    			+ "and rec.del_flag<>'1' "
    			+ "and pat.name like '%"+map.get("name").toString()+"%' "
    			+ "and pat.code_pi like '%"+map.get("codePi").toString()+"%' ";
    	
    	if(Boolean.valueOf(map.get("dicdate").toString())||map.get("dicdate").toString().equals("1")){
    		where = where + "and pat.date_end>=to_date('"+map.get("beginDate").toString()+"','yyyy/mm/dd hh24:mi:ss') "
    			+ "and pat.date_end<to_date('"+map.get("endDate").toString()+"','yyyy/mm/dd hh24:mi:ss') ";
    	}
		if(!map.get("pkDept").toString().equals("")){
			where = where + "and pat.pk_dept = '"+map.get("pkDept").toString()+"' ";
		}
		if(Boolean.valueOf(map.get("score").toString())){
			where = where + "and rec.pk_emp_emp_qc is not null ";
		}
    			
    	sql = sql + where + "order by rec.submit_date,rec.dept_qc_date,rec.archive_date,pat.date_end,pat.name,rec.pk_pv";
    	List<EmrPatListPrarm> nlist = DataBaseHelper.queryForList(sql, EmrPatListPrarm.class, new Object[]{});
    	return nlist;
    }
    
    /**
     * 查询出所有病历评分标准规则
     * @param param
	 * @param user
     */
    public List<EmrGradeRule> findAllEmrGradeRuleBase(String param , IUser user){
    	EmrGradeRule emrGradeRule = JsonUtil.readValue(param,EmrGradeRule.class);
    	return dictMapper.findAllEmrGradeRuleBase(emrGradeRule);
    }
    
    /**
     * 更新病历评分标准规则
     * @param param
	 * @param user
     */
    public List<EmrGradeRule> saveEmrGradeRuleBase(String param , IUser user){
    	List<EmrGradeRule> list = JsonUtil.readValue(param, new TypeReference<List<EmrGradeRule>>(){});
    	for(EmrGradeRule emrGradeRule : list){
    		if(emrGradeRule.getStatus()!=null){
    			if(emrGradeRule.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrGradeRule);
    			}else if(emrGradeRule.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrGradeRule,false);
    			}
    		}
    	}
    	EmrGradeRule emrGradeRule = new EmrGradeRule();
    	return dictMapper.findAllEmrGradeRuleBase(emrGradeRule);
    }
    
	/**
	 * 查询临床知识库诊断列表
	 * @return
	 */
	public List<EmrKnowledgeBase> queryKBIcdList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		return dictMapper.queryKBIcdList();
	}
	
	   /**
     * 根据条件查询临床知识库
     * @param typeCode/code
     * @return
     */
    public List<EmrKnowledgeBase> queryKnowledgeBaseByConds(String param,IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	String typeCode=map.get("typeCode").toString();
		String code=map.get("code").toString();
		return dictMapper.queryKnowledgeBaseByConds(typeCode, code);
    }    
    
    /**
     * 查询医生常用词汇
     * @param param
     * @param user
     * @return
     */
    public List<EmrCommonWords> queryCommonWordsList(String param,IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	String pkDept=map.get("pkDept").toString();
		String pkEmp=map.get("pkEmp").toString();
		return dictMapper.queryCommonWordsList(pkDept, pkEmp);
    }        
    
    /**
     * 保存医生常用词汇
     * @param param
     * @param user
     */
    public void saveEmrCommonWords(String param,IUser user){
    	List<EmrCommonWords> items=JsonUtil.readValue(param, new TypeReference<List<EmrCommonWords>>(){});
    	if(items==null||items.size()==0) return;
    	String status="";
    	EmrCommonWords item=null;
    	for(int i=0;i<items.size();i++){
    		item=items.get(i);
    		if(StringUtils.isNoneEmpty(item.getStatus())){
    			status=item.getStatus();
    		}
    		if(status.equals(EmrConstants.STATUS_NEW)){
    			dictMapper.saveEmrCommonWords(item);
    		}else if(status.equals(EmrConstants.STATUS_UPD)){
    			dictMapper.updateEmrCommonWords(item);
    		}else if(status.equals(EmrConstants.STATUS_DEL)){
    			dictMapper.deleteEmrCommonWords(item.getPkCword());
    		}		
    	}
	}
    
    /**
     * 查询出所有病历评分标准
     * @param param
	 * @param user
     */
    public List<EmrGradeStandard> findAllEmrGradeStandardBase(String param , IUser user){
    	EmrGradeStandard emrGradeStandard = JsonUtil.readValue(param,EmrGradeStandard.class);
    	return dictMapper.findAllEmrGradeStandardBase(emrGradeStandard);
    }
    
    /**
     * 更新病历评分标准
     * @param param
	 * @param user
     */
    public List<EmrGradeStandard> saveEmrGradeStandardBase(String param , IUser user){
    	List<EmrGradeStandard> list = JsonUtil.readValue(param, new TypeReference<List<EmrGradeStandard>>(){});
    	for(EmrGradeStandard emrGradeStandard : list){
    		if(emrGradeStandard.getStatus()!=null){
    			if(emrGradeStandard.getStatus().equals(EmrConstants.STATUS_NEW)){
    				DataBaseHelper.insertBean(emrGradeStandard);
    			}else if(emrGradeStandard.getStatus().equals(EmrConstants.STATUS_UPD)){
    				DataBaseHelper.updateBeanByPk(emrGradeStandard,false);
    			}
    		}
    	}
    	EmrGradeStandard emrGradeStandard = new EmrGradeStandard();
    	return dictMapper.findAllEmrGradeStandardBase(emrGradeStandard);
    }
    
    
    /**
     * 根据主键查询病历医师
     * @param param
     * @param user
     * @return
     */
    public EmrDoctor queryEmrDoctorById(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	return dictMapper.queryEmrDoctorById(map.get("pkEmp").toString());
    }
    
    /**
     * 查询病历模版页眉数据
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public EmrTemplateHead queryTemplateHeadElement(String param , IUser user){
		EmrTemplateHead templateHead = new EmrTemplateHead();
		
		Map params = JsonUtil.readValue(param,Map.class);
		params.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		params.put("code", "DeCodes_DocTitlePatInfo_Header");
		//查询患者信息 元素编码
		EmrParameter docTitlePatInfoHeader = dictMapper.queryParameterByCode(params);
		String values = docTitlePatInfoHeader.getValue();
		String[] valueArr = values.split(",");
		List<EmrDataElement>  patientInfoList = new ArrayList<EmrDataElement>();
		if(valueArr != null && valueArr.length>0){
			List<EmrDataElement> patientInfo = null;
			Map map = null;
			for(int i=0;i<valueArr.length;i++){
				map = new HashMap<String, String>();
				map.put("code", valueArr[i]);
				patientInfo = dictMapper.queryDataElementByConds(map);
				patientInfoList.addAll(patientInfo);
			}
			templateHead.setPatientInfoList(patientInfoList);
		}
		//查询病历模板标题医院名称设置方式
		params = new HashMap<String,String>();
		params.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		params.put("code", "DocTitleHosNameSetMode");
		EmrParameter titleSetMode = dictMapper.queryParameterByCode(params);
		
		if(titleSetMode != null && "logo".equals(titleSetMode.getValue())){
			//查询 病历模板标题医院logo存放名称
			params = new HashMap<String,String>();
			params.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
			params.put("code", "DocTitleHosLogoImgName");
			EmrParameter titleLogoName = dictMapper.queryParameterByCode(params);
			//如果图片名称存在，设置图片名称，否则设置医院默认标题元素
			if(titleLogoName != null && titleLogoName.getValue()!= null ){
				//标题使用图片
				templateHead.setIsUseHospitalImage("1");
				templateHead.setHospitalLogoFileName(titleLogoName.getValue());
			}
		}else{
			//标题不使用图片，使用元素
			templateHead.setIsUseHospitalImage("0");
			Map conds = new HashMap<String, String>();
			conds.put("code", "0002690");
			List<EmrDataElement> hospitalLogoElement = dictMapper.queryDataElementByConds(conds);
			templateHead.setHospitalLogoElement(hospitalLogoElement.get(0));
		}
		//查询 病历模板标题文档分类名称
		params = new HashMap<String,String>();
		params.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		params.put("code", "DeCode_DocTitleDocTypeName");//DeCode_DocTitleDocTypeName获取模板名称  默认元素编码 0001002
		EmrParameter titleDocTypeName = dictMapper.queryParameterByCode(params);
		Map map = new HashMap<String, String>();
		map.put("code", titleDocTypeName.getValue());
		List<EmrDataElement> templateTitleElement = dictMapper.queryDataElementByConds(map);
		//医院标题元素
		templateHead.setTemplateTitleElement(templateTitleElement.get(0));
		return templateHead;
    }
	
	/***
	 * 查询模板初始化信息时所包含的信息
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrTemplateContext queryTemplateContext(String param , IUser user){
		Map mapParam = JsonUtil.readValue(param,Map.class);
		EmrTemplateContext emrTemplateContext = new EmrTemplateContext();
		
		//查询 病历文档分类
		Map<String, Object> map = new HashMap<String,Object>();
		if(mapParam.containsKey("euVisit")){
			map.put("euVisit", mapParam.get("euVisit"));
		}else{
			map.put("euVisit", "2");
		}
		//2020-03-09暂时屏蔽首页西医中医区分
		//map.put("wmTcmType", EmrUtils.getWmTcmType(null));
		List<EmrDocType> docTypeList = dictMapper.queryDocTypeListVstUsFlg(map);
		emrTemplateContext.setDocTypeList(docTypeList);
		//查询病历文档段落
		emrTemplateContext.setParaList(dictMapper.queryParaList());
		//查询病历数据元素列表
		emrTemplateContext.setDataEleList(dictMapper.queryDataEleList());
		//查询病历参数列表
		map = new HashMap<String,Object>();
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		List<EmrParameter> parameterList = dictMapper.queryParameterList(map);
		emrTemplateContext.setParameterList(parameterList);
		//查询病历系统开关
		emrTemplateContext.setSwitchList(dictMapper.querySwitchList(map));
		//查询病历科室列表
		emrTemplateContext.setDeptList(recMapper.queryEmrDeptList(map));
		
		//页面样式设置
		EmrTemplatePageStyle emrTemplatePageStyle = new EmrTemplatePageStyle();
		for(EmrParameter emrParameter : parameterList){
			String emrParameterValue = emrParameter.getValue();
			if(emrParameterValue==null||emrParameterValue.equals("")) continue;
			String[] emrParameterArr = emrParameterValue.split(",");
			int length = emrParameterArr.length;
			if("DocDefaultCnFont".equals(emrParameter.getCode())){
				//字体名称
				emrTemplatePageStyle.setDocFontName(emrParameterArr[0]);
				//文档字体大小
				emrTemplatePageStyle.setDocFontSize(Float.parseFloat(emrParameterArr[1]==null ? "12" : emrParameterArr[1]));
			}else if("DocPageMargin".equals(emrParameter.getCode())){
				//左边距
				emrTemplatePageStyle.setPageLeft(Float.parseFloat(emrParameterArr[0]));
				//右边距
				emrTemplatePageStyle.setPageRight(Float.parseFloat(emrParameterArr[1]));
				// 上边距
				emrTemplatePageStyle.setPageTop(Float.parseFloat(emrParameterArr[2]));
				//下边距
				emrTemplatePageStyle.setPageBottom(Float.parseFloat(emrParameterArr[3]));
			}else if("DocPageFormat".equals(emrParameter.getCode())){
				//打印页面格式, A3, A4
				emrTemplatePageStyle.setPageFormat(Integer.parseInt(emrParameterArr[0]==null ? "0": emrParameterArr[0]));
				//打印纸张宽度
				emrTemplatePageStyle.setPageWidth(Float.parseFloat(emrParameterArr[1]==null ? "0": emrParameterArr[1]));
				//打印纸张高度
				emrTemplatePageStyle.setPageHeight(Float.parseFloat(emrParameterArr[2]==null ? "0": emrParameterArr[2]));
				//纸张方向,横向, 纵向
				String horOrVer = emrParameterArr[3];
				if(horOrVer==null)
					horOrVer= "false";
				emrTemplatePageStyle.setHorOrVer(Boolean.parseBoolean(horOrVer) ? "1":"0") ;
				//页面版式
				emrTemplatePageStyle.setPageLayOut(Integer.parseInt(emrParameterArr[4]==null ? "2": emrParameterArr[4]));
			}
		}
		emrTemplateContext.setPageStyle(emrTemplatePageStyle);
		
		//查询科室用模板列表
		map = new HashMap<String,Object>();
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		map.put("pkEmp", UserContext.getUser().getPkEmp());
		map.put("pkDept", UserContext.getUser().getPkDept());
		List<EmrTemplate> tmpList=tmpMapper.queryTypeTmpListDept(map);
		emrTemplateContext.setTmpList(tmpList);
		
		//病历数据元素(按分类）
		if(mapParam.containsKey("codeTypes")){
			map = new HashMap<String,Object>();
			map.put("codeTypes", mapParam.get("codeTypes"));
			List<EmrDataElement> elementListCodeType=dictMapper.queryDataElementByConds(map);
			emrTemplateContext.setElementListCodeType(elementListCodeType);
		}
		//获取当前医师审签级别
		User u = (User) user;
		String pkEmp=u.getPkEmp();
		String pkDept=u.getPkDept();
		EmrDoctor emrDoctor = null;
		map = new HashMap<String,Object>();
		map.put("pkDept", pkDept);
		map.put("pkEmp", pkEmp);
		List<EmrDoctor> docList=dictMapper.getEmrDoctorById(map);
		if(docList!=null&&docList.size()>0){
			emrDoctor=docList.get(0);
		}else{
			//医生审签级别使用范围： 0：全院通用 1：医疗组 2：科室内 3：大科内
			String empAuditLevelScope = ApplicationUtils.getSysparam("EmpAuditLevelScope", true);
			if(empAuditLevelScope==null||empAuditLevelScope.equals("")) empAuditLevelScope="0";
			if(empAuditLevelScope.equals("0")){
				//全院通用
				map = new HashMap<String,Object>();
				map.put("pkEmp", pkEmp);
				docList=dictMapper.getEmrDoctorById(map);
			}else if(empAuditLevelScope.equals("1")){
				//医疗组
				map = new HashMap<String,Object>();
				map.put("pkWg",mapParam.get("pkWg"));
				map.put("pkEmp", pkEmp);
				docList=dictMapper.getEmrDoctorById(map);
			}else if(empAuditLevelScope.equals("2")){
				//科室内
				
			}else if(empAuditLevelScope.equals("3")){
				//大科
				map = new HashMap<String,Object>();
				map.put("pkDepts", pkDept);
				map.put("pkEmp", pkEmp);
				docList=dictMapper.getEmrDoctorById(map);
			}
			//docList=dictMapper.getEmrDoctorById(null,pkEmp);
			if(docList!=null&&docList.size()>0){
				emrDoctor=docList.get(0);
			}
		}
		if(emrDoctor!=null){
			emrDoctor.setSignImage(null);
			emrTemplateContext.setEmrDoctor(emrDoctor);
		}
		map = new HashMap<String,Object>();
		map.put("pkEmp", pkEmp);
		
		List<EmrEmpSet> listEmpSet=emrDictMapper.queryEmrEmpSetList(map);
		if(listEmpSet!=null&&listEmpSet.size()>0){
			emrTemplateContext.setEmrEmpSet(listEmpSet.get(0));
		}
		return emrTemplateContext;
	}
	
	
	
}

