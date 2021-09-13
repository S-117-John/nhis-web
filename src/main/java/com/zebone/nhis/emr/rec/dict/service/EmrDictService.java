package com.zebone.nhis.emr.rec.dict.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.emr.rec.dict.vo.TimeLimitVo;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.res.OrgDeptWgEmp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.emr.rec.dict.EmrCommonWords;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDataElement;
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
import com.zebone.nhis.common.module.emr.rec.dict.EmrKnowledgeBasePro;
import com.zebone.nhis.common.module.emr.rec.dict.EmrNurse;
import com.zebone.nhis.common.module.emr.rec.dict.EmrParagraph;
import com.zebone.nhis.common.module.emr.rec.dict.EmrParameter;
import com.zebone.nhis.common.module.emr.rec.dict.EmrSnGenerator;
import com.zebone.nhis.common.module.emr.rec.dict.EmrSwitch;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.emr.common.EmrConstants;
import com.zebone.nhis.emr.rec.dict.dao.EmrDictMapper;
import com.zebone.nhis.emr.rec.dict.vo.DictRangePrarm;
import com.zebone.nhis.emr.rec.dict.vo.EmrDoctorParam;
import com.zebone.nhis.emr.rec.dict.vo.EmrPatListPrarm;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病历书写-字典管理
 * 
 * @author chengjia
 * 
 */
@Service
public class EmrDictService {

	@Resource
	private EmrDictMapper dictMapper;

	/**
	 * 查询病历文档分类（根据eu_visit,eu_use)
	 * 
	 * @return
	 */
	public List<EmrDocType> queryDocTypeListVstUsFlg(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryDocTypeListVstUsFlg(map);
	}

	/**
	 * 查询病历文档分类（医师用/二级)
	 * 
	 * @return
	 */
	public List<EmrDocType> queryDocTypeLTDoc(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryDocTypeLTDoc();
	}

	/**
	 * 查询病历文档段落
	 * 
	 * @return
	 */
	public List<EmrParagraph> queryParaList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryParaList();
	}

	/**
	 * 查询病历数据元素列表
	 * 
	 * @return
	 */
	public List<EmrDataElement> queryDataEleList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryDataEleList();
	}

	/**
	 * 查询病历值域
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRange> queryDictRangeList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryDictRangeList();
	}

	/**
	 * 根据条件查询病历值域
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRange> queryDictRangeByConds(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String code = map.get("code").toString();
		List<EmrDictRange> rtnList = dictMapper.queryDictRangeByConds(code);
		return rtnList;
	}

	/**
	 * 查询病历值域（根据名称）
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRange> queryDictRangeByName(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		// String name=map.get("name").toString();
		List<EmrDictRange> rtnList = dictMapper.queryDictRangeByName(map);
		return rtnList;
	}

	/**
	 * 查询病历值域编码
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRangeCode> queryDictRangeCodeByConds(String param,
			IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrDictRangeCode> rtnList = dictMapper
				.queryDictRangeCodeByConds(map);
		return rtnList;
	}

	/**
	 * 查询病历值域编码
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictRangeCode> queryDictRangeCodeList(String param,
			IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkRange = map.get("pkRange").toString();
		return dictMapper.queryDictRangeCodeList(pkRange);
	}

	/**
	 * 根据编码查询病历选择字典
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictOpt> getDictOptByCode(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.getDictOptByCode(map.get("code").toString());
	}

	/**
	 * 查询病历参数列表
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EmrParameter> queryParameterList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		map.put("pkOrg", UserContext.getUser().getPkOrg().trim() + "%");
		List<EmrParameter> list = dictMapper.queryParameterList(map);
		return list;
	}

	/**
	 * 根据编码查询病历参数
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EmrParameter> queryParameterByCode(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrParameter> rtnList = new ArrayList<EmrParameter>();
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim() + "%");
		String queryType = map.get("queryType").toString();
		if (queryType != null && queryType.equals("like")) {

			rtnList = dictMapper.queryParameterByCodeLike(map);
		} else {
			rtnList = dictMapper.queryParameterByCode(map);
		}
		return rtnList;
	}

	/**
	 * 查询病历参数列表
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EmrSwitch> querySwitchList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim() + "%");
		return dictMapper.querySwitchList(map);
	}

	/**
	 * 根据编码查询病历开关
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EmrSwitch querySwitchByCode(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim() + "%");
		return dictMapper.querySwitchByCode(map);
	}

	/**
	 * 根据分类查询病历字典编码
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDictCode> queryDictCodeList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String classCode = map.get("classCode").toString();
		return dictMapper.queryDictCodeList(classCode);
	}

	/**
	 * 保存病历值域
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrDictRange saveDictRange(String param, IUser user) {
		DictRangePrarm rangeParam = JsonUtil.readValue(param,
				DictRangePrarm.class);
		EmrDictRange range = rangeParam.getRange();
		String rangeStatus = "";
		if (StringUtils.isNoneEmpty(range.getStatus())) {
			rangeStatus = range.getStatus();
		}
		if (rangeStatus.equals(EmrConstants.STATUS_NEW)) {
			dictMapper.saveEmrDictRange(range);
		} else if (rangeStatus.equals(EmrConstants.STATUS_UPD)) {
			dictMapper.updateEmrDictRange(range);
		} else if (rangeStatus.equals(EmrConstants.STATUS_DEL)) {
			dictMapper.deleteEmrDictRange(range.getPkRange());
		}

		if (rangeParam.getCodeList() != null) {
			for (EmrDictRangeCode code : rangeParam.getCodeList()) {
				String codeStatus = "";
				if (StringUtils.isNoneEmpty(code.getStatus())) {
					codeStatus = code.getStatus();
				}

				if (codeStatus.equals(EmrConstants.STATUS_NEW)) {
					dictMapper.saveEmrDictRangeCode(code);
				} else if (codeStatus.equals(EmrConstants.STATUS_UPD)) {
					dictMapper.updateEmrDictRangeCode(code);
				} else if (codeStatus.equals(EmrConstants.STATUS_DEL)) {
					dictMapper.deleteEmrDictRangeCode(code.getPkRangeCode());
				}

			}

		}

		return range;
	}

	/**
	 * 取病历流水号
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public int genSysSn(String param, IUser user) {
		int value = 1;

		Map map = JsonUtil.readValue(param, Map.class);
		String code = map.get("code").toString();
		int rtn = dictMapper.updateSysSn(code);
		if (rtn == 0) {
			EmrSnGenerator emrSnGenerator = new EmrSnGenerator();
			emrSnGenerator.setCode(code);
			emrSnGenerator.setValue(1);
			emrSnGenerator.setDelFlag("0");

			dictMapper.saveSysSn(emrSnGenerator);
		}
		value = dictMapper.getSysSnByCode(code);

		return value;
	}

	/**
	 * 根据条件查询病历数据元素
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDataElement> queryDataElementByConds(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		List<EmrDataElement> rtnList = dictMapper.queryDataElementByConds(map);

		return rtnList;
	}

	/**
	 * 根据条件查询病历数据元素(改)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDataElement> queryDataElementByCond(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		List<EmrDataElement> rtnList = dictMapper.queryDataElementByCond(map);

		return rtnList;
	}

	/**
	 * 保存病历数据元素
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public int saveDataElement(String param, IUser user) {
		EmrDataElement element = JsonUtil
				.readValue(param, EmrDataElement.class);
		int rtn = 0;
		if (StringUtils.isNoneEmpty(element.getStatus())
				&& element.getStatus().equals("new")) {
			// 新增
			rtn = dictMapper.saveEmrDataElement(element);
		} else {
			// 修改
			rtn = dictMapper.updateEmrDataElement(element);
		}
		return rtn;

	}

	/**
	 * 查询标准诊断编码
	 * 
	 * @param param
	 * @param user
	 */
	public List<BdTermDiag> queryTermDiagList(String param, IUser user) {
		BdTermDiag termDiag = JsonUtil.readValue(param, BdTermDiag.class);

		return dictMapper.queryTermDiagList(termDiag);
	}

	/**
	 * 查询知识库中填写过的诊断
	 * 
	 * @param param
	 * @param user
	 */
	public List<BdTermDiag> queryTermDiagTextList(String param, IUser user) {
		BdTermDiag termDiag = JsonUtil.readValue(param, BdTermDiag.class);

		return dictMapper.queryTermDiagTextList(termDiag);
	}

	/**
	 * 查询病历字典代码表
	 */
	public List<EmrDictCode> queryDictCodeAllList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryDictCodeAllList();
	}

	/**
	 * 查询临床知识库
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrKnowledgeBase> queryKnowledgeBase(String param, IUser user) {
		EmrKnowledgeBase emrKnowledgeBase = JsonUtil.readValue(param,
				EmrKnowledgeBase.class);

		return dictMapper.queryKnowledgeBase(emrKnowledgeBase);
	}

	/**
	 * 查询病历专业知识库
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrKnowledgeBasePro> queryKnowledgeBasePro(String param,
			IUser user) {
		EmrKnowledgeBasePro emrKnowledgeBasePro = JsonUtil.readValue(param,
				EmrKnowledgeBasePro.class);

		return dictMapper.queryKnowledgeBasePro(emrKnowledgeBasePro);
	}

	/**
	 * 保存临床知识库
	 * 
	 * @param param
	 * @param user
	 */
	public EmrKnowledgeBase saveEmrKnowledgeBase(String param, IUser user) {
		EmrKnowledgeBase emrKnowledgeBase = JsonUtil.readValue(param,
				EmrKnowledgeBase.class);

		if (emrKnowledgeBase.getPkKnowBase() == null) {
			DataBaseHelper.insertBean(emrKnowledgeBase);
		} else {
			DataBaseHelper.updateBeanByPk(emrKnowledgeBase, false);
		}
		return emrKnowledgeBase;
	}

	/**
	 * 保存专业知识库
	 * 
	 * @param param
	 * @param user
	 */
	public EmrKnowledgeBasePro saveEmrKnowledgeBasePro(String param, IUser user) {
		EmrKnowledgeBasePro emrKnowledgeBasePro = JsonUtil.readValue(param,
				EmrKnowledgeBasePro.class);

		if (emrKnowledgeBasePro.getPkkbPro() == null) {
			DataBaseHelper.insertBean(emrKnowledgeBasePro);
		} else {
			DataBaseHelper.updateBeanByPk(emrKnowledgeBasePro, false);
		}
		return emrKnowledgeBasePro;
	}

	/**
	 * 查询常用词汇所有记录
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrCommonWords> findAllEmrCommonWords(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		String sql = "select  cw.*,"
				+ "           dp.pk_dept,dp.name_dept,"
				+ "           (case cw.eu_level when 0 then '全院' when 1 then '科室' when 2 then '个人' end) as euLevelName "
				+ "from emr_common_words cw LEFT OUTER JOIN "
				+ "     bd_ou_dept dp on cw.pk_dept=dp.pk_dept"
				+ " where cw.eu_level=?";
		if (map.get("euLevel") != null
				&& map.get("euLevel").toString().equals("2")) {
			sql = sql + " and cw.creator='" + UserContext.getUser().getPkEmp()
					+ "'";
		}
		if (map.get("pkDept") != null) {
			sql = sql + " and cw.pk_dept='" + map.get("pkDept")	+ "'";
		}
		
		List<EmrCommonWords> list = DataBaseHelper.queryForList(sql,EmrCommonWords.class, new Object[] { map.get("euLevel") });
		return list;
	}

	/**
	 * 更新常用词汇记录
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrCommonWords> saveEmrCommonWordsBase(String param, IUser user) {
		List<EmrCommonWords> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrCommonWords>>() {
				});
		for (EmrCommonWords emrCommonWords : list) {
			if (emrCommonWords.getCodeStatus() != null) {
				if (emrCommonWords.getCodeStatus().equals(
						EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrCommonWords);
					// dictMapper.saveEmrCommonWords(emrCommonWords);
				} else if (emrCommonWords.getCodeStatus().equals(
						EmrConstants.STATUS_UPD)) {
					// dictMapper.updateEmrCommonWords(emrCommonWords);
					DataBaseHelper.updateBeanByPk(emrCommonWords, false);
				}
			}
		}
		// String sql =
		// "select  cw.*,dp.pk_dept,(case cw.eu_level when 0 then '全院' when 1 then '科室' when 2 then '个人' end) as euLevelName "
		// +
		// "from emr_common_words cw LEFT OUTER JOIN bd_ou_dept dp on cw.pk_dept=dp.pk_dept";
		// List<EmrCommonWords> nlist = DataBaseHelper.queryForList(sql,
		// EmrCommonWords.class, new Object[]{});

		return null;
	}

	/**
	 * 查询病历文档分类
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDocType> findAllEmrDocType(String param, IUser user) {
		// Map map = JsonUtil.readValue(param,Map.class);
		// String sql =
		// "SELECT * FROM EMR_DOC_TYPE dt where dt.del_flag='0' order by dt.sort_num";
		// List<EmrDocType> nlist = DataBaseHelper.queryForList(sql,
		// EmrDocType.class, new Object[]{});
		// return nlist;

		EmrDocType docType = JsonUtil.readValue(param, EmrDocType.class);

		return dictMapper.findAllEmrDocType(docType);

	}

	/**
	 * 更新病历文档分类
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDocType> saveEmrDocTypesBase(String param, IUser user) {
		List<EmrDocType> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrDocType>>() {
				});
		for (EmrDocType docType : list) {
			if (docType.getCodeStatus() != null)
				if (docType.getCodeStatus().equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(docType);
				} else if (docType.getCodeStatus().equals(
						EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(docType, false);
				}
		}
		String sql = "SELECT * FROM EMR_DOC_TYPE dt order by dt.sort_num,dt.code";
		List<EmrDocType> nlist = DataBaseHelper.queryForList(sql,
				EmrDocType.class, new Object[] {});
		return nlist;
	}

	/**
	 * 查询文档段落
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrParagraph> findAllEmrParagraph(String param, IUser user) {
		// Map map = JsonUtil.readValue(param,Map.class);
		// String sql = "SELECT * FROM emr_paragraph ph  order by ph.code";

		// List<EmrParagraph> list = DataBaseHelper.queryForList(sql,
		// EmrParagraph.class, new Object[]{});

		EmrParagraph para = JsonUtil.readValue(param, EmrParagraph.class);

		return dictMapper.findAllEmrParagraph(para);
	}

	/**
	 * 更新文档段落
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrParagraph> saveEmrParagraphBase(String param, IUser user) {
		List<EmrParagraph> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrParagraph>>() {
				});
		for (EmrParagraph paragraph : list) {
			if (paragraph.getCodeStatus() != null)
				if (paragraph.getCodeStatus().equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(paragraph);
				} else if (paragraph.getCodeStatus().equals(
						EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(paragraph, false);
				}
		}
		String sql = "SELECT * FROM emr_paragraph ph  order by ph.sort_num";
		List<EmrParagraph> nlist = DataBaseHelper.queryForList(sql,
				EmrParagraph.class, new Object[] {});
		return nlist;
	}

	/**
	 * 查询病历元素
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDataElement> findAllEmrDataElementBase(String param,
			IUser user) {
		EmrDataElement dataElement = JsonUtil.readValue(param,
				EmrDataElement.class);
		if (Application.isSqlServer()) {
			return dictMapper.findAllEmrDataElementBaseSql(dataElement);
		} else {
			return dictMapper.findAllEmrDataElementBase(dataElement);
		}

	}

	/**
	 * 更新病历元素
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDataElement> saveEmrDataElementBase(String param, IUser user) {
		List<EmrDataElement> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrDataElement>>() {
				});
		for (EmrDataElement emrDataElement : list) {
			if (emrDataElement.getStatus() != null)
				if (emrDataElement.getEuCodeType() != null
						&& emrDataElement.getEuCodeType().equals(""))
					emrDataElement.setEuCodeType(null);
			if (emrDataElement.getStatus().equals(EmrConstants.STATUS_NEW)) {
				DataBaseHelper.insertBean(emrDataElement);
			} else if (emrDataElement.getStatus().equals(
					EmrConstants.STATUS_UPD)) {
				DataBaseHelper.updateBeanByPk(emrDataElement, false);
			}
		}
		// String sql =
		// "select * from emr_data_element el where el.del_flag='0' order by el.code";
		// List<EmrDataElement> nlist = DataBaseHelper.queryForList(sql,
		// EmrDataElement.class, new Object[]{});
		return null;
	}

	/**
	 * 查询病历系统参数
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrParameter> findAllEmrParameterBase(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.findAllEmrParameterBase(map);
	}

	/**
	 * 更新病历系统参数
	 * 
	 * @param param
	 * @param user
	 */
	public void updateEmrParameterBase(String param, IUser user) {
		List<EmrParameter> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrParameter>>() {
				});
		for (EmrParameter emrParameter : list) {
			if (emrParameter.getStatus() != null) {
				if (emrParameter.getStatus().equals(EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrParameter, false);
				} else if (emrParameter.getStatus().equals(
						EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrParameter);
				}
			}
		}

	}

	/**
	 * 查询病历系统开关
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrSwitch> findAllEmrSwitchBase(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.findAllEmrSwitchBase(map);
	}

	/**
	 * 更新病历系统开关
	 * 
	 * @param param
	 * @param user
	 */
	public void updateEmrSwitchBase(String param, IUser user) {
		List<EmrSwitch> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrSwitch>>() {
				});
		for (EmrSwitch emrSwitch : list) {
			if (emrSwitch.getStatus() != null) {
				if (emrSwitch.getStatus().equals(EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrSwitch, false);
				} else if (emrSwitch.getStatus()
						.equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrSwitch);
				}
			}
		}

	}

	/**
	 * 查询病历字典分类
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDictClass> findAllEmrDictClassBase(String param, IUser user) {
		EmrDictClass emrDictClass = JsonUtil.readValue(param,
				EmrDictClass.class);

		return dictMapper.findAllEmrDictClassBase(emrDictClass);
	}

	/**
	 * 更新病历字典分类
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDictClass> saveEmrDictClassBase(String param, IUser user) {
		List<EmrDictClass> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrDictClass>>() {
				});
		for (EmrDictClass emrDictClass : list) {
			if (emrDictClass.getStatus() != null) {
				if (emrDictClass.getStatus().equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrDictClass);
				} else if (emrDictClass.getStatus().equals(
						EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrDictClass, false);
				}
			}
		}
		EmrDictClass emrDictClass = new EmrDictClass();
		return dictMapper.findAllEmrDictClassBase(emrDictClass);
	}

	/**
	 * 查询病历字典代码表
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDictCode> findAllEmrDictCodeByCls(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.findAllEmrDictCodeByCls(map.get("pkDictcls")
				.toString());
	}

	/**
	 * 更新病历字典代码表
	 * 
	 * @param param
	 * @param user
	 */
	public void saveEmrDictCodeBase(String param, IUser user) {
		List<EmrDictCode> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrDictCode>>() {
				});
		for (EmrDictCode emrDictCode : list) {
			if (emrDictCode.getStatus() != null) {
				if (emrDictCode.getStatus().equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrDictCode);
				} else if (emrDictCode.getStatus().equals(
						EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrDictCode, false);
				}
			}
		}
	}

	/**
	 * 查询病历值域
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDictRange> findAllEmrDictRangeBase(String param, IUser user) {
		EmrDictRange emrDictRange = JsonUtil.readValue(param,
				EmrDictRange.class);

		return dictMapper.findAllEmrDictRangeBase(emrDictRange);
	}

	/**
	 * 更新病历值域
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDictRange> saveEmrDictRangeBase(String param, IUser user) {
		List<EmrDictRange> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrDictRange>>() {
				});
		for (EmrDictRange emrDictRange : list) {
			if (emrDictRange.getStatus() != null) {
				if (emrDictRange.getStatus().equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrDictRange);
				} else if (emrDictRange.getStatus().equals(
						EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrDictRange, false);
				}
			}
		}
		EmrDictRange emrDictRange = new EmrDictRange();
		return dictMapper.findAllEmrDictRangeBase(emrDictRange);
	}

	/**
	 * 查询病历值域代码
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDictRangeCode> findAllEmrDictRangeCodeByRa(String param,
			IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.findAllEmrDictRangeCodeByRa(map.get("pkRange")
				.toString());
	}

	/**
	 * 更新病历值域代码
	 * 
	 * @param param
	 * @param user
	 */
	public void saveEmrDictRangeCodeBase(String param, IUser user) {
		List<EmrDictRangeCode> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrDictRangeCode>>() {
				});
		for (EmrDictRangeCode emrDictRangeCode : list) {
			if (emrDictRangeCode.getStatus() != null) {
				if (emrDictRangeCode.getStatus()
						.equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrDictRangeCode);
				} else if (emrDictRangeCode.getStatus().equals(
						EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrDictRangeCode, false);
				}
			}
		}
	}

	/**
	 * 查询病历医师设置
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDoctor> findAllEmrDoctorBase(String param, IUser user) {
		EmrDoctor emrDoctor = JsonUtil.readValue(param, EmrDoctor.class);
		emrDoctor.setPkOrg(UserContext.getUser().getPkOrg().trim() + "%");
		return dictMapper.findAllEmrDoctorBase(emrDoctor);
	}

	/**
	 * 重置病历医师设置中的密码
	 * 
	 * @param param
	 * @param user
	 */
	public int updateEmrDoctorByPwd(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.updateEmrDoctorByPwd(map.get("pkEmp").toString());
	}

	/**
	 * 更新病历医师设置
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDoctor> saveEmrDoctorBase(String param, IUser user) {
		List<EmrDoctor> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrDoctor>>() {
				});
		for (EmrDoctor emrDoctor : list) {
			if (emrDoctor.getStatus() != null) {
				if (emrDoctor.getStatus().equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrDoctor);
				} else if (emrDoctor.getStatus()
						.equals(EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrDoctor, false);
					if (emrDoctor.getSignImage() == null) {
						DataBaseHelper
								.update("update emr_doctor set sign_image=null where pk_doctor='"
										+ emrDoctor.getPkDoctor() + "'");
					}

				}
			}
		}
		EmrDoctor emrDoctor = new EmrDoctor();
		return dictMapper.findAllEmrDoctorBase(emrDoctor);
	}

	/**
	 * 根据主键删除病历医师设置
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrDoctor> deleteEmrDoctorBase(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		dictMapper.deleteEmrDoctor(map.get("pkEmp").toString());

		EmrDoctor emrDoctor = new EmrDoctor();
		return dictMapper.findAllEmrDoctorBase(emrDoctor);
	}

	/**
	 * 根据条件查询环节质控
	 * 
	 * @param param
	 * @param user
	 * @throws ParseException
	 */
	public List<EmrPatListPrarm> findLinkControlByPrame(String param, IUser user)
			throws ParseException {
		Map map = JsonUtil.readValue(param, Map.class);
		String sql = "select pat.*,"
				   + "       emp.name as qcName,"
				   + "       patrec.dept_qc_date, "
				   + "       emp_link.name_emp emp_link_qc_name, "
				   + "       patrec.eu_link_qc_grade, "
				   + "       patrec.pk_emp_link_qc,"
				   + "       patrec.link_qc_score,"
				   + "       patrec.link_qc_date "
				   + "  from view_emr_pat_list pat "
				   + "inner join emr_pat_rec patrec on pat.pk_pv=patrec.pk_pv "
				   + "left outer join bd_ou_employee emp_link on patrec.pk_emp_link_qc=emp_link.pk_emp ";
		String where = "where pat.eu_status_pv!='9'";
		// 科室
		if (map.containsKey("pkDept")) {
			if (map.get("pkDept") != null) {
				if (!map.get("pkDept").toString().equals("")) {
					//判断是否是病历查阅的转科条件
					if (map.containsKey("tranStatus") && !map.get("tranStatus").toString().equals("")) {
					
					}else{
						where = where + " and pat.pk_dept = '"
								+ map.get("pkDept").toString() + "' ";
					}
				}
			}
		}
		// 患者姓名
		if (map.containsKey("patientName")) {
			if (!map.get("patientName").toString().equals("")) {
				where = where + " and pat.name like '%"
						+ map.get("patientName").toString() + "%' ";
			}
		}
		// 病案号
		if (map.containsKey("edicalRecordCode")) {
			if (!map.get("edicalRecordCode").toString().equals("")) {
				where = where + " and pat.code_ip like '"
						+ map.get("edicalRecordCode").toString() + "%' ";
			}
		}
		//是否归档
		if (map.containsKey("archStatus")) {
			String archStatus = map.get("archStatus").toString();
			if(archStatus==null) archStatus="0";
			if(archStatus.equals("1")){
				//已归档
				where = where + " and pat.eu_status >= '4' ";
			}else if(archStatus.equals("2")){
				//未归档
				where = where + " and pat.eu_status <= '3' ";
			}
		}
		String flagIn="";
		// 在院标志(1在院,0出院)
		if (map.containsKey("ignoreTime")) {
			if (map.get("ignoreTime").toString().equals("")) {
				if(!map.containsKey("tranStatus") || (map.containsKey("tranStatus") && map.get("tranStatus").toString().equals(""))){
					where = where + " and pat.flag_in in ("+ map.get("stateAll").toString() + ") ";
					flagIn = map.get("stateAll").toString();
					if (flagIn.equals("1")) {
						if (map.containsKey("beginDate")) {
							if (!map.get("beginDate").toString().equals("")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								Date tmpb = sdf.parse(map.get("beginDate").toString());
								sdf = new SimpleDateFormat("yyyy-MM-dd");
								where = where + " and pat.date_begin >= to_date('"+ sdf.format(tmpb) + "','yyyy-mm-dd') ";
							}
						}
						if (map.containsKey("endDate")) {
							if (!map.get("endDate").toString().equals("")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								Date tmpe = sdf.parse(map.get("endDate").toString());
								sdf = new SimpleDateFormat("yyyy-MM-dd");
								where = where + " and pat.date_begin < to_date('"+ sdf.format(tmpe) + "','yyyy-mm-dd') ";
							}
						}
					}
					if (flagIn.equals("0")) {
						if (map.containsKey("beginDate")) {
							if (!map.get("beginDate").toString().equals("")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								Date tmpb = sdf.parse(map.get("beginDate").toString());
								sdf = new SimpleDateFormat("yyyy-MM-dd");
								where = where + " and pat.date_end >= to_date('"+ sdf.format(tmpb) + "','yyyy-mm-dd') ";
							}
						}
						if (map.containsKey("endDate")) {
							if (!map.get("endDate").toString().equals("")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								Date tmpe = sdf.parse(map.get("endDate").toString());
								Calendar c = Calendar.getInstance(); 
								c.setTime(tmpe);
								c.add(Calendar.DAY_OF_MONTH,1); 
								Date endDate=c.getTime();
								sdf = new SimpleDateFormat("yyyy-MM-dd");
								where = where + " and pat.date_end < to_date('"+ sdf.format(endDate) + "','yyyy-mm-dd') ";
							}
						}
					}
				}
			}
		}
		if (!map.get("levelAll").toString().equals("")) {
			where = where + " and nvl(pat.dt_level_dise,'00') in ("
					+ map.get("levelAll").toString() + ") ";
		}

		if (!map.get("opY").toString().equals("")) {
			where = where
					+ " and exists (select 1 from ex_order_occ occ where occ.pk_pv = pat.pk_pv) ";
		}
		if (!map.get("opN").toString().equals("")) {
			where = where
					+ " and not exists (select 1 from ex_order_occ occ where occ.pk_pv = pat.pk_pv) ";
		}
		if(map.get("spotY")!=null&&map.get("spotN")!=null&&map.get("spotY").toString().equals("1")&&map.get("spotN").equals("1")){
			//已抽查/未抽查=滤掉此条件
		}else{
			if (!map.get("spotY").toString().equals("")) {
				where = where
						+ " and exists (select 1 from emr_grade_rec rec where rec.pk_pv = pat.pk_pv and rec.del_flag='0' and rec.eu_grade_type='link') ";
			}
			if (!map.get("spotN").toString().equals("")) {
				where = where
						+ " and not exists (select 1 from emr_grade_rec rec where rec.pk_pv = pat.pk_pv and rec.del_flag='0' and rec.eu_grade_type='link') ";
			}
		}
		

		if (!map.get("limit").toString().equals("")) {
			where = where
					+ " and exists (select 1 from emr_med_rec_task tsk where tsk.pk_pv = pat.pk_pv and tsk.eu_rec_status='2') ";
		}
		
		//环节转科
		if (map.containsKey("isTran")) {
			if (!map.get("isTran").toString().equals("")) {
				where = where 
					+"and exists (select adtt.pk_pv from view_emr_trans_list adts inner join view_emr_trans_list adtt on adtt.pk_adt_source = adts.pk_adt where adts.pk_dept = '"+map.get("adtPkDept")+"' and adtt.eu_status = '1' and adtt.pk_pv = pat.pk_pv)";
			}
		}
		//病历查阅转科
		if (map.containsKey("tranStatus")) {
			if (!map.get("tranStatus").toString().equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date tmpb = sdf.parse(map.get("beginDate").toString());
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				String tranBegin=sdf.format(tmpb);
				sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date tmpe = sdf.parse(map.get("endDate").toString());
				Calendar c = Calendar.getInstance(); 
				c.setTime(tmpe);
				c.add(Calendar.DAY_OF_MONTH,1); 
				Date endDate=c.getTime();
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				String tranEnd=sdf.format(endDate);
				if (map.containsKey("pkDept") && map.get("pkDept") != null && !map.get("pkDept").toString().equals("")) {
					where = where 
						+"and exists (select adtt.pk_pv from view_emr_trans_list adts inner join view_emr_trans_list adtt on adtt.pk_adt_source = adts.pk_adt where adtt.eu_status = '1' and adts.PK_DEPT='"+map.get("pkDept")+"' and adtt.pk_pv = pat.pk_pv  and adtt.date_begin >= to_date('"+ tranBegin + "','yyyy-mm-dd')  and adtt.date_begin < to_date('"+ tranEnd + "','yyyy-mm-dd'))";
				}else{
					where = where 
						+"and exists (select adtt.pk_pv from view_emr_trans_list adts inner join view_emr_trans_list adtt on adtt.pk_adt_source = adts.pk_adt where adtt.eu_status = '1' and adtt.pk_pv = pat.pk_pv  and adtt.date_begin >= to_date('"+ tranBegin + "','yyyy-mm-dd')  and adtt.date_begin < to_date('"+ tranEnd + "','yyyy-mm-dd'))";
				}
			}
		}
		sql = sql
				+ " left join view_emr_emp_list emp on emp.pk_emp=patrec.pk_emp_qc "
				+ where;
		if(flagIn!=null&&flagIn.equals("1")){
			sql = sql + "order by pat.bed_no,pat.name,pat.pat_no,pat.date_begin";
		}else{
			sql = sql + "order by pat.name,pat.pat_no,pat.date_begin";
		}
		List<EmrPatListPrarm> nlist = DataBaseHelper.queryForList(sql,
				EmrPatListPrarm.class, new Object[] {});
		return nlist;
	}

	/**
	 * 根据条件查询科室质控
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrPatListPrarm> findDeptControlByPrame(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sql = "select pat.*,"
				+ "fin.name as finishName,"
				+ "rec.finish_date as finishDate,"
				+ "dept.name as deptQcName,"
				+ "emp.name as qcName,"
				+ "rec.qc_date as qcDate,"
				+ "submit.name as submitName,"
				+ "rec.submit_date as submitDate,"
				+ "empqc.name as empQcName,"
				+ "rec.emp_qc_date as empQcDate,"
				+ "rec.dept_qc_date as deptQcDate,"
				+ "rec.eu_emp_qc_grade as euEmpQcGrade,"
				+ "rec.emp_qc_score as empQcScore,"
				+ "rec.eu_dept_qc_grade as euDeptQcGrade,"
				+ "rec.dept_qc_score as deptQcScore,"
				+ "rec.diag_dis_txt as diagDisTxt,"
				+ "rec.flag_receive,"
				+ "rec.pk_emp_receive,"
				+ "rec.receive_date,"
				+ "nvl(rec.flag_amend_dept,'0') flag_amend_dept,"
				+ "emprd.name receive_name,"
				+ "grade.remark"
		        + " from view_emr_pat_list pat"
		        + " inner join emr_pat_rec rec on pat.pk_pv=rec.pk_pv"
				+ " left join view_emr_emp_list fin on fin.pk_emp = rec.pk_emp_finish "
				+ " left join view_emr_emp_list dept on dept.pk_emp = rec.pk_emp_dept_qc "
				+ " left join view_emr_emp_list emp on emp.pk_emp = rec.pk_emp_qc "
				+ " left join view_emr_emp_list submit on submit.pk_emp=rec.pk_emp_submit "
				+ " left join view_emr_emp_list empqc on empqc.pk_emp=rec.pk_emp_emp_qc "
				+ " left join view_emr_emp_list emprd on emprd.pk_emp=rec.pk_emp_receive "
				+ " left outer join emr_grade_rec grade on pat.pk_pv = grade.pk_pv and grade.del_flag='0' and grade.eu_grade_type='dept' ";
		String where = "where rec.del_flag<>'1' "
				+ "and pat.pk_dept = '" + map.get("pkDept").toString() + "' ";

		if (map.get("status").toString().equals("2")) {
			//待质控/医生完成病历
			where = where
					+ "and rec.eu_status='2' order by rec.finish_date,pat.name,rec.pk_pv";
		} else if (map.get("status").toString().equals("3")) {
			//在质控/已完成
			where = where + "and rec.eu_status='3' order by rec.dept_qc_date,pat.name,rec.pk_pv";
//					+ "and rec.eu_status>='3' "
//					+ "and (pat.code_ip like '%"
//					+ map.get("retrieval").toString()
//					+ "%' or pat.name like '%"
//					+ map.get("retrieval").toString()
//					+ "%' or pat.diag_dis_txt like '%"
//					+ map.get("retrieval").toString()
//					+ "%') "
//					+ "and ((pat.date_end>=to_date('"
//					+ map.get("beginDate").toString()
//					+ "','yyyy/mm/dd hh24:mi:ss') "
//					+ "and pat.date_end<to_date('"
//					+ map.get("endDate").toString()
//					+ "','yyyy/mm/dd hh24:mi:ss'))"
//					+ "or pat.flag_in='1')"
//					+ "order by rec.submit_date,rec.dept_qc_date,rec.archive_date,pat.date_end,pat.name,rec.pk_pv";
		} else {
//			where = where + "and rec.eu_status='1' "
//					+ "and (pat.code_ip like '%"
//					+ map.get("retrieval").toString()
//					+ "%' or pat.name like '%"
//					+ map.get("retrieval").toString()
//					+ "%' or pat.diag_dis_txt like '%"
//					+ map.get("retrieval").toString() + "%') "
//					+ "and pat.date_end >= to_date('"
//					+ map.get("beginDate").toString()
//					+ "','yyyy/mm/dd hh24:mi:ss') "
//					+ "and pat.date_end < to_date('"
//					+ map.get("endDate").toString()
//					+ "','yyyy/mm/dd hh24:mi:ss') "
//					+ "order by pat.date_end,pat.name,rec.pk_pv";
			where = where + "and rec.eu_status in ("+map.get("status").toString()+")"
			+ "and (pat.code_ip like '%"
			+ map.get("retrieval").toString()
			+ "%' or pat.name like '%"
			+ map.get("retrieval").toString()
			+ "%' or pat.diag_dis_txt like '%"
			+ map.get("retrieval").toString()
			+ "%') "
			+ "and ((pat.date_end>=to_date('"
			+ map.get("beginDate").toString()
			+ "','yyyy/mm/dd hh24:mi:ss') "
			+ "and pat.date_end<to_date('"
			+ map.get("endDate").toString()
			+ "','yyyy/mm/dd hh24:mi:ss'))"
			+ "or pat.flag_in='1')"
			+ "order by rec.submit_date,rec.dept_qc_date,pat.date_end,pat.name,rec.pk_pv";
		}
		sql = sql + where;
		List<EmrPatListPrarm> nlist = DataBaseHelper.queryForList(sql,
				EmrPatListPrarm.class, new Object[] {});
		return nlist;
	}

	/**
	 * 主键修改病历状态
	 * 
	 * @param param
	 * @param user
	 */
	public int updateEmrPatPecByPk(String param, IUser user) {
		List<Map> list = JsonUtil.readValue(param,
				new TypeReference<List<Map>>() {
				});
		int flag = 0;
		Date now = new Date();
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(now);
		for (Map map : list) {
			String status = map.get("status").toString();
			String sql = "update emr_pat_rec set eu_status='" + status + "'"
					+ " where pk_patrec='" + map.get("pkPatrec").toString()
					+ "'";
			if (status != null && status.equals("7")) {
				// 归档
				if(Application.isSqlServer()){
					sql = "update emr_pat_rec set eu_status=(case when flag_archive = '1' then '" + status+"' else eu_status end ), "
							+ " flag_emr_archive='1',pk_emp_emr_archive='"
							+ UserContext.getUser().getPkEmp()
							+ "',emr_archive_date='" + dateStr
							+ "' where PK_PATREC='"
							+ map.get("pkPatrec").toString() + "'";
				}else{
					sql = "update emr_pat_rec set eu_status=eu_status=(case when flag_archive = '1' then '" + status+"' else eu_status end ),"
							+ " pk_emp_archive='"
							+ UserContext.getUser().getPkEmp() + "',"
							+ "archive_date=to_date('" + dateStr
							+ "','yyyy-MM-dd hh24:mi:ss')" + " where pk_patrec='"
							+ map.get("pkPatrec").toString() + "'";
				}
			}else if (status != null && status.equals("6")){
				if(map.containsKey("isArch")){//状态为6两种情况，一个是终末质控完成时变成六，还有就是取消归档变成六，这里判断是否是取消归档
					sql = "update emr_pat_rec set eu_status='"
							+ status
							+ "',flag_emr_archive=null,pk_emp_emr_archive=null,emr_archive_date=null where PK_PATREC='"
							+ map.get("pkPatrec").toString() + "'";
				}
				
			} else if (status != null && status.equals("1")) {
				// 取消完成
				sql = "update emr_pat_rec set eu_status='" + status + "',"
						+ " pk_emp_finish=null," + "finish_date=null"
						+ " where pk_patrec='" + map.get("pkPatrec").toString()
						+ "'";
			}
			flag = flag + DataBaseHelper.update(sql, new Object[] {});
		}
		return flag;
	}

	/**
	 * 根据条件查询终末质控
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrPatListPrarm> findEndControlByPrame(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sql = "select pat.*,"
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
				+ "nvl(rec.flag_amend_final,'0')  flag_amend_final,"
				+ "emps.name  as submitName,"
				+ "grade.remark "
				+ "from view_emr_pat_list pat "
				+ "inner join emr_pat_rec rec on pat.pk_pv = rec.pk_pv "
				+ "left join view_emr_emp_list emparchive on emparchive.pk_emp=rec.pk_emp_archive "
				+ "left join view_emr_emp_list deptqc on deptqc.pk_emp=rec.pk_emp_dept_qc "
				+ "left join view_emr_emp_list finalqc on finalqc.pk_emp=rec.pk_emp_final_qc "
				+ "left join view_emr_emp_list empqc on empqc.pk_emp=rec.pk_emp_emp_qc "
				+ "left join view_emr_emp_list empf on empf.pk_emp=rec.pk_emp_finish "
				+ "left join view_emr_emp_list emps on emps.pk_emp=rec.pk_emp_submit "
				+ " left outer join emr_grade_rec grade on pat.pk_pv = grade.pk_pv and grade.del_flag='0' and grade.eu_grade_type='final' ";
		String where = "where "
				+ "rec.eu_status in ('" + map.get("status").toString()
				+ "') " + "and rec.del_flag<>'1' " + "and pat.name like '%"
				+ map.get("name").toString() + "%' "
				+ "and pat.code_ip like '%" + map.get("codeIp").toString()
				+ "%' ";

		if (Boolean.valueOf(map.get("dicdate").toString())
				|| map.get("dicdate").toString().equals("1")) {
			where = where + "and pat.date_end>=to_date('"
					+ map.get("beginDate").toString()
					+ "','yyyy/mm/dd hh24:mi:ss') "
					+ "and pat.date_end<to_date('"
					+ map.get("endDate").toString()
					+ "','yyyy/mm/dd hh24:mi:ss') ";
		}
		if (!map.get("pkDept").toString().equals("")) {
			where = where + "and pat.pk_dept = '"
					+ map.get("pkDept").toString() + "' ";
		}
		if (Boolean.valueOf(map.get("score").toString())) {
			where = where + "and rec.pk_emp_emp_qc is not null ";
		}
		if(map.containsKey("finalReceive")){
			where = where + "and rec.flag_final_recv is not null ";
		}else{
			if(map.get("status").toString().equals("4")){
				where = where + "and rec.flag_final_recv is null ";
			}
		} 
		sql = sql
				+ where
				+ "order by rec.submit_date,rec.dept_qc_date,rec.archive_date,pat.date_end,pat.name,rec.pk_pv";
		List<EmrPatListPrarm> nlist = DataBaseHelper.queryForList(sql,
				EmrPatListPrarm.class, new Object[] {});
		return nlist;
	}

	/**
	 * 查询出所有病历评分标准规则
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrGradeRule> findAllEmrGradeRuleBase(String param, IUser user) {
		EmrGradeRule emrGradeRule = JsonUtil.readValue(param,
				EmrGradeRule.class);
		List<EmrGradeRule> list = dictMapper.findAllEmrGradeRuleBase(emrGradeRule);
		return list;
	}

	/**
	 * 更新病历评分标准规则
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrGradeRule> saveEmrGradeRuleBase(String param, IUser user) {
		List<EmrGradeRule> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrGradeRule>>() {
				});
		for (EmrGradeRule emrGradeRule : list) {
			if (emrGradeRule.getStatus() != null) {
				if (emrGradeRule.getStatus().equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrGradeRule);
				} else if (emrGradeRule.getStatus().equals(
						EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrGradeRule, false);
				}
			}
		}
		EmrGradeRule emrGradeRule = new EmrGradeRule();
		return dictMapper.findAllEmrGradeRuleBase(emrGradeRule);
	}

	/**
	 * 查询临床知识库诊断列表
	 * 
	 * @return
	 */
	public List<EmrKnowledgeBase> queryKBIcdList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryKBIcdList();
	}

	/**
	 * 根据条件查询临床知识库
	 * 
	 * @param typeCode
	 *            /code
	 * @return
	 */
	public List<EmrKnowledgeBase> queryKnowledgeBaseByConds(String param,
			IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String typeCode = map.get("typeCode").toString();
		String code = map.get("code").toString();
		return dictMapper.queryKnowledgeBaseByConds(typeCode, code);
	}

	/**
	 * 查询医生常用词汇
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrCommonWords> queryCommonWordsList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		Integer euLevel=-1;
		if(map.get("euLevel")!=null){
			try {
				euLevel=Integer.parseInt(map.get("euLevel").toString());
			} catch (Exception e) {
				
			}
		}
		String pkDept = map.get("pkDept").toString();
		String pkEmp = map.get("pkEmp").toString();
		String euUsed = map.get("euUsed")==null?null:map.get("euUsed").toString();
		String dtType = map.get("dtType")==null?null:map.get("dtType").toString();
        if (euLevel==0)
        {
            pkDept = null;
            pkEmp = null;
        }
        else if (euLevel == 1)
        {
            pkEmp = null;

        }else if(euLevel == 2)
        {
        	pkDept = null;
        }
        if(euLevel>=0){
        	return dictMapper.queryCommonWordsList(pkDept, pkEmp,euLevel,euUsed,dtType);
        }else{
        	//查询全部可用
        	return dictMapper.queryCommonWordsListUsed(pkDept, pkEmp,euUsed,dtType);
        }
		
	}

	
	/**
	 * 保存医生常用词汇
	 * 
	 * @param param
	 * @param user
	 */
	public void saveEmrCommonWords(String param, IUser user) {
		List<EmrCommonWords> items = JsonUtil.readValue(param,
				new TypeReference<List<EmrCommonWords>>() {
				});
		if (items == null || items.size() == 0)
			return;
		String status = "";
		EmrCommonWords item = null;
		for (int i = 0; i < items.size(); i++) {
			item = items.get(i);
			if (StringUtils.isNoneEmpty(item.getStatus())) {
				status = item.getStatus();
			}
			if (status.equals(EmrConstants.STATUS_NEW)) {
				dictMapper.saveEmrCommonWords(item);
			} else if (status.equals(EmrConstants.STATUS_UPD)) {
				dictMapper.updateEmrCommonWords(item);
			} else if (status.equals(EmrConstants.STATUS_DEL)) {
				dictMapper.deleteEmrCommonWords(item.getPkCword());
			}
		}
	}

	/**
	 * 查询出所有病历评分标准
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrGradeStandard> findAllEmrGradeStandardBase(String param,
			IUser user) {
		EmrGradeStandard emrGradeStandard = JsonUtil.readValue(param,
				EmrGradeStandard.class);
		return dictMapper.findAllEmrGradeStandardBase(emrGradeStandard);
	}

	/**
	 * 更新病历评分标准
	 * 
	 * @param param
	 * @param user
	 */
	public List<EmrGradeStandard> saveEmrGradeStandardBase(String param,
			IUser user) {
		List<EmrGradeStandard> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrGradeStandard>>() {
				});
		for (EmrGradeStandard emrGradeStandard : list) {
			if (emrGradeStandard.getStatus() != null) {
				if (emrGradeStandard.getStatus()
						.equals(EmrConstants.STATUS_NEW)) {
					DataBaseHelper.insertBean(emrGradeStandard);
				} else if (emrGradeStandard.getStatus().equals(
						EmrConstants.STATUS_UPD)) {
					DataBaseHelper.updateBeanByPk(emrGradeStandard, false);
				}
			}
		}
		EmrGradeStandard emrGradeStandard = new EmrGradeStandard();
		return dictMapper.findAllEmrGradeStandardBase(emrGradeStandard);
	}

	/**
	 * 根据主键查询病历医师
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrDoctor queryEmrDoctorById(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		return dictMapper.queryEmrDoctorById(map.get("pkEmp").toString());
	}

	/**
	 * 保存引用的病历评分标准
	 * 
	 * @param param
	 * @param user
	 */
	public void saveEmrGradeDeptCite(String param, IUser user) {
		List<EmrGradeStandard> list = JsonUtil.readValue(param,
				new TypeReference<List<EmrGradeStandard>>() {
				});
		for (int i = 0; list != null && i < list.size(); i++) {
			list.get(i).setPkStand(NHISUUID.getKeyId());
			list.get(i).setCreateTime(new Date());
			list.get(i).setTs(new Date());
		}
		DataBaseHelper.batchUpdate(
				DataBaseHelper.getInsertSql(EmrGradeStandard.class), list);
	}
	
	/**
	 * 同步病历医师信息
	 * @param param
	 * @param user
	 */
	public void saveEmrDoctorSync(String param, IUser user) {
		List<EmrDoctor> docList=dictMapper.queryEmrDoctorSyncList();
		if(docList!=null&&docList.size()>0){
			Date now = new Date();
			
			for(int i=0;i<docList.size();i++){
				EmrDoctor doctor = docList.get(i);
				doctor.setSignImage(null);
				String remark = doctor.getRemark()==null?"":doctor.getRemark();
				doctor.setRemark(remark+"同步日期:"+DateUtils.formatDate(now));
				
				DataBaseHelper.insertBean(doctor);
			}
			
		}
	}

	/**
	 * 查询病历护师设置
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrNurse> queryEmrNurseList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryEmrNurseList(map);
	}
	
	/**
	 * 保存病历护师设置
	 * @param param
	 * @param user
	 */
	public void saveEmrNurses(String param, IUser user) {
		List<EmrNurse> list = JsonUtil.readValue(param,new TypeReference<List<EmrNurse>>() {});
		if (list == null || list.size() == 0) return;
		String status = "";
		EmrNurse item = null;
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			status=item.getStatus()==null?"":item.getStatus();
			
			if (status.equals(EmrConstants.STATUS_NEW)) {
				dictMapper.saveEmrNurse(item);
			} else if (status.equals(EmrConstants.STATUS_UPD)) {
				dictMapper.updateEmrNurse(item);
			} else if (status.equals(EmrConstants.STATUS_DEL)) {
				dictMapper.deleteEmrNurse(item.getPkNurse());
			}
		}
	}
	/**
	 * 根据医疗类型获取科室列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOuDept> queryDeptList(String param , IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		String dtDepttype=map.get("dtDepttype")==null?"":map.get("dtDepttype").toString();
		String pkOrg = ((User)user).getPkOrg();
		String sql = "select dept.* "
				    + " from bd_ou_dept dept " +
					 "where dept.pk_org = ? "
				     + "and dept.del_flag = '0'";

		if(!dtDepttype.equals("")){
			sql=sql+ " and dept.dt_depttype = '"+dtDepttype+"'";
		}
		sql = sql + " order by dept.name_dept,dept.code_dept ";			
		List<BdOuDept> deptList = DataBaseHelper.queryForList(sql, BdOuDept.class, pkOrg);
		
		return deptList;
	}
	/**
	 * 根据参数查询病历护师
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrNurse queryEmrNurse(String param , IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		EmrNurse emrNurse=dictMapper.queryEmrNurse(map);
		return emrNurse;
	}
	/**
	 * 根据医疗组获取人员
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrDoctor> queryEmrDoctorByWg(String param , IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		String sql="select * from emr_doctor where pk_wg=? and del_flag='0' order by code";
		List<EmrDoctor> emrDoctor=DataBaseHelper.queryForList(sql,EmrDoctor.class, map.get("pkWg"));
		return emrDoctor;
	}
	
	public void saveEmrDoctor(String param , IUser user){
		EmrDoctorParam emrDoctorParam = JsonUtil.readValue(param, EmrDoctorParam.class);
		String pkWg=emrDoctorParam.getPkWg();
		List<EmrDoctor> list = emrDoctorParam.getEmrDoctor();
		//判断人员有无重复
		if(list != null && list.size() > 1){
			Map<String, String> pkEmpMap = new HashMap<String, String>();
			int len = list.size();
			for(int i=0; i<len; i++){
				String pkEmp = list.get(i).getPkEmp();
				if(pkEmpMap.containsKey(pkEmp)){
					throw new BusException("医疗组关联的人员重复: "+list.get(i).getName());
				}
				pkEmpMap.put(pkEmp, list.get(i).getPkEmp());
			}
			
		}
		DataBaseHelper.update("update emr_doctor set del_flag = '1' where pk_wg = ?",new Object[]{pkWg});
		if(list != null && list.size() != 0){
			//先全删再恢复的方式（软删除）
			for(EmrDoctor doctor : list){
				if(doctor.getPkDoctor() != null){
					doctor.setDelFlag("0");//恢复
					DataBaseHelper.updateBeanByPk(doctor, false);
				}else{
					doctor.setDelFlag("0");
					DataBaseHelper.insertBean(doctor);
				}
				
			}
		}
	}

	public List<TimeLimitVo> findTimeLimitByPrame(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sql = ""
				+ "	SELECT "
				+ "		e.pk_pv, "
				+ "     e.flag_in,"
				+ "     rec.type_code,	"
				+ "     rec.pk_rec,	"
				+ "     d.name_dept deptName,	"
				+ "     ep.name_emp empName,	"
				+ "     dt. NAME docTypeName,	"
				+ "     rec. NAME docName,	"
				+ "     rec.submit_date,	"
				+ "     e.date_admit dateBegin,	"
				+ "     e.date_end,	"
				+ "     pi.code_pi patNo,	"
				+ "     e.name_pi AS NAME,"
				+ "		rec.creator pk_emp"
				+ "	FROM emr_med_rec rec"
				+ "	LEFT JOIN pv_encounter e ON rec.pk_pv = e.pk_pv"
				+ "	LEFT JOIN bd_ou_dept d ON rec.pk_dept = d.pk_dept"
				+ "	LEFT JOIN pi_master pi ON pi.pk_pi = e.pk_pi"
				+ "	LEFT JOIN bd_ou_employee ep ON ep.pk_emp = rec.creator"
				+ "	LEFT JOIN emr_doc_type dt ON dt.code = rec.type_code"
				+ "	WHERE d.pk_org = e.pk_org"
				+ "		AND dt.pk_org = e.pk_org"
				+ "		AND pi.pk_org = e.pk_org"
				+ "		AND rec.del_flag <> 1"
				+ "		AND pi.del_flag <> 1"
				+ "		AND e.eu_pvtype = '3' ";

		sql = sql + "and e.pk_org = '" + map.get("pkOrg").toString() + "' ";
		if (map.get("flagIn") != null && StringUtils.isNotEmpty(map.get("flagIn").toString())) {

			sql = sql + "and e.flag_in in ('" + map.get("flagIn").toString() + "' )";
		}
		if (map.get("pkEmp") != null) {
			sql = sql + "and ep.pk_emp = '" + map.get("pkEmp").toString() + "' ";
		}
		if (map.get("pkDept") != null) {
			sql = sql + "and e.pk_dept = '" + map.get("pkDept").toString() + "' ";
		}
		if (Application.isSqlServer()) {
			//出院患者查询
			if (map.get("flagIn").equals("0") && map.get("dateBegin") != null && map.get("dateEnd") != null) {
				sql = sql + "and CONVERT(varchar,e.date_begin,20)>='" + map.get("dateBegin").toString() + "'"
						+ " and CONVERT(varchar,e.date_begin,20)<='" + map.get("dateEnd").toString() + "'";
			}
			if (map.get("flagIn").toString().equals("1") && map.get("dateBegin") != null && map.get("dateEnd") != null) {
				sql = sql + "and CONVERT(varchar,e.date_begin,20)>='" + map.get("dateBegin").toString() + "'"
						+ " and CONVERT(varchar,e.date_begin,20)<='" + map.get("dateEnd").toString() + "'";
			}
		} else {
			//出院患者查询
			if (map.get("flagIn").equals("0") && map.get("dateBegin") != null && map.get("dateEnd") != null) {
				sql = sql + "and e.date_end>=to_date('" + map.get("dateBegin").toString() + "','yyyy/mm/dd hh24:mi:ss') "
						+ "and e.date_end<=to_date('" + map.get("dateEnd").toString() + "','yyyy/mm/dd hh24:mi:ss') ";
			}
			if (map.get("flagIn").toString().equals("1") && map.get("dateBegin") != null && map.get("dateEnd") != null) {
				sql = sql + "and e.date_begin>=to_date('" + map.get("dateBegin").toString() + "','yyyy/mm/dd hh24:mi:ss') "
						+ "and e.date_begin<=to_date('" + map.get("dateEnd").toString() + "','yyyy/mm/dd hh24:mi:ss') ";
			}
		}
		List<TimeLimitVo> list = DataBaseHelper.queryForList(sql, TimeLimitVo.class, new Object[]{});
		return list;
	}
	
	/**
	 * 查询病历人员设置
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrEmpSet> queryEmrEmpSetList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		return dictMapper.queryEmrEmpSetList(map);
	}
	
	/**
	 * 保存病历人员设置
	 * @param param
	 * @param user
	 */
	public void saveEmrEmpSets(String param, IUser user) {
		List<EmrEmpSet> list = JsonUtil.readValue(param,new TypeReference<List<EmrEmpSet>>() {});
		if (list == null || list.size() == 0) return;
		String status = "";
		EmrEmpSet item = null;
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			status=item.getStatus()==null?"":item.getStatus();
			
			if (status.equals(EmrConstants.STATUS_NEW)) {
				dictMapper.saveEmrEmpSet(item);
			} else if (status.equals(EmrConstants.STATUS_UPD)) {
				dictMapper.updateEmrEmpSet(item);
			} else if (status.equals(EmrConstants.STATUS_DEL)) {
				dictMapper.deleteEmrEmpSet(item.getPkEmpSet());
			}
		}
	}
}
