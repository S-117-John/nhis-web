package com.zebone.nhis.base.bd.service;

import ca.uhn.hl7v2.HL7Exception;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zebone.nhis.base.bd.dao.SrvMapper;
import com.zebone.nhis.base.bd.vo.*;
import com.zebone.nhis.base.pub.vo.CgdivItemVo;
import com.zebone.nhis.common.module.base.bd.code.BdDictattr;
import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.nhis.common.module.base.bd.srv.*;
import com.zebone.nhis.common.module.base.transcode.SysApplog;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdComm;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.scm.pub.BdHpCgdivTmp;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MapUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class SrvService {

    /**
     * 添加状态
     **/
    public static final String AddState = "_ADD";

    /**
     * 更新状态
     */
    public static final String UpdateState = "_UPDATE";

    /**
     * 删除状态
     */
    public static final String DelState = "_DELETE";

    @Autowired
    private SrvMapper srvMapper;


    //增删改标志
    private String rleCode = "";

    /**
     * 项目互斥及服务项目的保存和更新
     *
     * @param param
     * @param user
     */
    @Transactional
    public BdOrdExcluParam saveBdOrdExclu(String param, IUser user) {
        BdOrdExcluParam bdOrdExcluParam = JsonUtil.readValue(param,
                BdOrdExcluParam.class);
        BdOrdExclu bdOrdExclu = bdOrdExcluParam.getBdOrdExclu();
        List<BdOrdExcluDt> bdOrdExcluDtList = bdOrdExcluParam
                .getBdOrdExcluDtList();
        bdOrdExclu.setPkOrg("~                               ");//默认为集团级数据
        if (bdOrdExclu.getPkExclu() == null) {
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_ord_exclu "
                                    + "where del_flag = '0' and code = ? ",
                            Integer.class, bdOrdExclu.getCode());
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_ord_exclu "
                                    + "where del_flag = '0' and name = ? ",
                            Integer.class, bdOrdExclu.getName());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.insertBean(bdOrdExclu);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("机构内项目互斥编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("机构内项目互斥名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("机构内项目互斥编码和名称都重复！");
                }
            }
        } else {
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_ord_exclu "
                                    + "where del_flag = '0' and code = ? and pk_exclu != ?",
                            Integer.class, bdOrdExclu.getCode(),
                            bdOrdExclu.getPkExclu());
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_ord_exclu "
                                    + "where del_flag = '0'  and name = ? and pk_exclu != ?",
                            Integer.class, bdOrdExclu.getName(),
                            bdOrdExclu.getPkExclu());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.updateBeanByPk(bdOrdExclu, false);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("机构内项目互斥编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("机构内项目互斥名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("机构内项目互斥编码和名称都重复！");
                }
            }
        }

        /** 新增或跟新项目互斥明细 **/
        if (bdOrdExcluDtList != null && bdOrdExcluDtList.size() != 0) {
            /** 校验项目互斥明细唯一性 */
            Map<String, String> nameOrdMap = new HashMap<String, String>();
            Map<String, String> codeOrdMap = new HashMap<String, String>();
            int len = bdOrdExcluDtList.size();
            for (int i = 0; i < len; i++) {
                String nameOrd = bdOrdExcluDtList.get(i).getNameOrd();
                String codeOrd = bdOrdExcluDtList.get(i).getCodeOrd();
                if (nameOrdMap.containsKey(nameOrd)) {
                    throw new BusException("互斥明细名称重复！");
                }
                if (codeOrdMap.containsKey(codeOrd)) {
                    throw new BusException("互斥明细编码重复！");
                }
                nameOrdMap.put(nameOrd, bdOrdExcluDtList.get(i).getPkExcludt());
                codeOrdMap.put(codeOrd, bdOrdExcluDtList.get(i).getPkExcludt());
            }

            // 先全删再恢复的方式（软删除）
            String pkexclu = bdOrdExclu.getPkExclu();
            DataBaseHelper
                    .update("update bd_ord_exclu_dt set del_flag = '1' where pk_exclu = ?",
                            new Object[]{pkexclu});
            for (BdOrdExcluDt excdt : bdOrdExcluDtList) {
                if (excdt.getPkExcludt() != null) {
                    excdt.setDelFlag("0");// 恢复
                    excdt.setPkExclu(pkexclu);
                    DataBaseHelper.updateBeanByPk(excdt, false);
                } else {
                    excdt.setPkExclu(pkexclu);
                    DataBaseHelper.insertBean(excdt);
                }
                Map<String, String> map = new HashMap<>();
                if ("0".equals(bdOrdExclu.getEuExcType())) {//全排
                    map.put("euExclude", "1");
                } else if ("1".equals(bdOrdExclu.getEuExcType())) {//组排
                    map.put("euExclude", "2");
                }
                map.put("pkOrd", excdt.getPkOrd());
                srvMapper.updateOrdBypk(map);
            }
        } else {
            String pkexclu = bdOrdExclu.getPkExclu();
            DataBaseHelper.update("update bd_ord_exclu_dt set del_flag = '1' where pk_exclu = ?", new Object[]{pkexclu});
        }

        return bdOrdExcluParam;
    }

    /**
     * 项目互斥的删除
     *
     * @param param
     * @param user
     */
    @Transactional
    public void deleteBdOrdExclu(String param, IUser user) {
        String pkExclu = JSON.parseObject(param).getString("pkExclu");
        DataBaseHelper.execute(
                "update BD_ORD_EXCLU_DT set del_flag='1' where PK_EXCLU = ?",
                new Object[]{pkExclu});
        DataBaseHelper.update(
                "update BD_ORD_EXCLU set del_flag='1' where PK_EXCLU = ?",
                new Object[]{pkExclu});
    }

    /**
     * 获取医嘱项目
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getBdOrds(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String isall = null;
        String codeOrdtype = null;
        String code = null;
        String name = null;
        String pkOrd = null;
        String spcode = null;
        String dtOrdCate = null;
        String pkDept = null;
        String searchData = null;
        String codeExt = null;
        String dtContype = null;
        String delFlag = null;
        String pkOrg = CommonUtils.getString(paramMap.get("pkOrg"));
        if (paramMap.get("isAll") != null) {
            isall = paramMap.get("isAll").toString();
        }
        if (paramMap.get("codeOrdtype") != null) {
            codeOrdtype = paramMap.get("codeOrdtype").toString();
        }
        if (paramMap.get("code") != null) {
            code = paramMap.get("code").toString();
        }
        if (paramMap.get("name") != null) {
            name = paramMap.get("name").toString();
        }
        if (paramMap.get("pkOrd") != null) {
            pkOrd = paramMap.get("pkOrd").toString();
        }
        if (paramMap.get("spcode") != null) {
            spcode = paramMap.get("spcode").toString();
        }

        if (paramMap.get("dtOrdType") != null) {
            dtOrdCate = paramMap.get("dtOrdType").toString();
        }
        if (paramMap.get("pkDept") != null) {
            pkDept = paramMap.get("pkDept").toString();
        }
        if (paramMap.get("searchData") != null) {
            searchData = paramMap.get("searchData").toString();
        }

        if (paramMap.get("codeExt") != null) {
            codeExt = paramMap.get("codeExt").toString();
        }

        if (paramMap.get("dtContype") != null) {
            dtContype = paramMap.get("dtContype").toString();
        }
        if (paramMap.get("delFlag") != null) {

            delFlag = paramMap.get("delFlag").toString();
        }
        int pageSize = Integer.parseInt(paramMap.get("pageSize").toString());
        int pageIndex = Integer.parseInt(paramMap.get("pageIndex").toString());
        if (pageIndex > 0) {
            MyBatisPage.startPage(pageIndex, pageSize);
        }

        String jg = ((User) user).getPkOrg();
        String dbType = MultiDataSource.getCurDbType();
        List<BdOrdAndPkDeptVo> ords = null;
        if ("1".equals(isall)) {
            if ("sqlserver".equals(dbType)) {
                if (StringUtils.isNotEmpty(name)) {
                    if (name.contains("[") || name.contains("]")) {
                        StringBuffer stringBuffer1 = new StringBuffer(name);
                        int index1 = name.indexOf("[");
                        if (index1 > 0) {
                            stringBuffer1.insert(index1, "#");
                        }

                        String temp = stringBuffer1.toString();
                        int index2 = temp.indexOf("]");
                        StringBuffer stringBuffer2 = new StringBuffer(temp);
                        if (index2 > 0) {
                            stringBuffer2.insert(index2, "#");
                        }


                        String result = stringBuffer2.toString();
                        name = result;

                    }
                }
                ords = this.srvMapper.getBdOrdByRequiresSqlserver(searchData, jg, codeOrdtype, code, name,
                        pkOrd, spcode, dtOrdCate, pkDept, codeExt, dtContype, delFlag,pkOrg);
            } else {
                ords = this.srvMapper.getBdOrdByRequires(searchData, jg, codeOrdtype, code, name,
                        pkOrd, spcode, dtOrdCate, pkDept, codeExt, dtContype, delFlag,pkOrg);
            }

        } else if ("0".equals(isall)) {

            if ("sqlserver".equals(dbType)) {
                if (StringUtils.isNotEmpty(name)) {
                    if (name.contains("[") || name.contains("]")) {
                        StringBuffer stringBuffer1 = new StringBuffer(name);
                        int index1 = name.indexOf("[");
                        if (index1 > 0) {
                            stringBuffer1.insert(index1, "#");
                        }

                        String temp = stringBuffer1.toString();
                        int index2 = temp.indexOf("]");
                        StringBuffer stringBuffer2 = new StringBuffer(temp);
                        if (index2 > 0) {
                            stringBuffer2.insert(index2, "#");
                        }

                        String result = stringBuffer2.toString();
                        name = result;

                    }
                }
                ords = this.srvMapper.getBdOrdByJgRequiresSqlserver(jg, codeOrdtype, code,
                        name, pkOrd, spcode, dtOrdCate, pkDept, codeExt, dtContype, delFlag);
            } else {
                ords = this.srvMapper.getBdOrdByJgRequires(jg, codeOrdtype, code,
                        name, pkOrd, spcode, dtOrdCate, pkDept, codeExt, dtContype, delFlag);
            }


        }

        if (pageIndex > 0) {
            String pkOrdStr = "'";
            List<String> pkOrds = new ArrayList<>();
            for (int i = 0; i < ords.size(); i++) {
//                if (i < ords.size() - 1) {
//                    pkOrdStr += ords.get(i).getPkOrd() + "','";
//                } else {
//                    pkOrdStr += ords.get(i).getPkOrd() + "'";
//                }
                pkOrds.add(ords.get(i).getPkOrd());
            }
            if (ords.size() > 0) {
                List<BdOrdAndPkDeptVo> ordList = this.srvMapper.getSetMealByPkOrd(pkOrds);
                for (BdOrdAndPkDeptVo ord : ords) {
                    for (BdOrdAndPkDeptVo ordMeal : ordList) {
                        if (ord.getPkOrd().equals(ordMeal.getPkOrd())) {
                            ord.setSetMeal(ordMeal.getSetMeal());
                        }
                    }
                }
            }

        }
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        Map<String, Object> result = new HashMap<>();
        result.put("list", ords);
        if (page != null) {
            result.put("totalCount", page.getTotalCount());
        } else {
            result.put("totalCount", ords.size());
        }

        return result;
    }

    /**
     * 获取医嘱项目及相关所有信息
     *
     * @param param
     * @param user
     * @return
     */
    public BdOrdAndAllParam getBdOrdAll(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String pkOrd = null;
        String isAll = null;
        if (paramMap.get("pkOrd") != null) {
            pkOrd = paramMap.get("pkOrd").toString();
        }
        String pkOrg = UserContext.getUser().getPkOrg();
        if (CommonUtils.isNotNull(paramMap.get("pkOrg"))) {
        	pkOrg = paramMap.get("pkOrg").toString();
        }
        
        String jg = ((User) user).getPkOrg();

        BdOrdAndAllParam ret = new BdOrdAndAllParam();

        BdOrd bdOrd = this.srvMapper.getBdOrdByPk(pkOrd);
        // isAll=1，查全局的；isAll=0，查当前机构的；
        // 当isAll=0时才需要isUpdate
        if (paramMap.get("isAll") != null) {
            isAll = paramMap.get("isAll").toString();
            if ("1".equals(isAll)) {
                jg = "~                               ";
            } else if ("0".equals(isAll)) {
                if ("1".equalsIgnoreCase(paramMap.get("isUpdate").toString())) {
                    jg = ((User) user).getPkOrg();
                } else {
                    jg = "~                               ";
                }
            }

            List<BdOrdAlias> bdOrdAliases = this.srvMapper.getBdOrdAliasByOrd(
                    pkOrd);// 取当前机构下的
            ret.setBdOrdAliases(bdOrdAliases);
        } else {
            throw new BusException("参数isAll不能为空");
        }

        List<BdOrdOrg> bdOrdOrgs = this.srvMapper.getBdOrdOrgsByOrd(pkOrd);


//		if("1".equals(isAll)){
//			bdOrdOrgs = this.srvMapper.getBdOrdOrgList(pkOrd);
//		}

        //List<BdOrdItem> bdOrdItems = this.srvMapper.getBdOrdItemsByOrd(pkOrd);
        List<BdOrdItem> bdOrdItems = new ArrayList<>();
        List<BdOrdItemExt> bdOrdItemExts = this.srvMapper.getBdOrdItemsByOrd(pkOrd,pkOrg);
        if (bdOrdItemExts != null && bdOrdItemExts.size() > 0) {
            for (BdOrdItemExt bd : bdOrdItemExts) {
                BdOrdItem bdOrdItem = new BdOrdItem();
                ApplicationUtils.copyProperties(bdOrdItem, bd);
                bdOrdItems.add(bdOrdItem);
            }

        }
        BdOrdLab bdOrdLab = this.srvMapper.getBdOrdLabByOrd(pkOrd);
        BdOrdRis bdOrdRis = this.srvMapper.getBdOrdRisByOrd(pkOrd);
        List<BdOrdDept> bdOrdDepts = this.srvMapper.getBdOrdDeptsByOrd(pkOrd);
        List<BdOrdEmr> bdOrdEmrs = this.srvMapper.getBdOrdEmrsByOrd(pkOrd);
        //List<Map<String,Object>>  bdOrdAttrList = this.srvMapper.getBdOrdAttrByOrd("1".equals(isAll)? null:pkOrg, pkOrd);
        List<Map<String, Object>> bdOrdAttrList = this.srvMapper.getBdOrdAttrByOrd(null, pkOrd);
        List<BdOrdLabCol> labColList = srvMapper.getBdOrdLabColList(pkOrd, "1".equals(isAll) ? null : pkOrg);
        if(bdOrd != null)
        {
        	bdOrd.setPkOrg(pkOrg);
        }
        ret.setBdOrd(bdOrd);
        ret.setBdOrdOrgs(bdOrdOrgs);
        ret.setBdOrdItems(bdOrdItems);
        ret.setBdOrdLab(bdOrdLab);
        ret.setBdOrdRis(bdOrdRis);
        ret.setBdOrdDepts(bdOrdDepts);
        ret.setBdOrdEmrs(bdOrdEmrs);
        ret.setBdOrdAttrList(bdOrdAttrList);
        ret.setLabColList(labColList);
        ret.setBdOrdItemExts(bdOrdItemExts);
        return ret;

    }


    /**
     * 保存医嘱项目及相关所有信息
     *
     * @param param
     * @param user
     * @return
     * @throws Exception
     */
    public BdOrd saveBdOrdAll(String param, IUser user) throws Exception {
        /** 接收参数 **/
        BdOrdAndAllParam bdOrdAndAll = JsonUtil.readValue(param,
                BdOrdAndAllParam.class);

        User u = UserContext.getUser();
        String pkOrgCurrent = u.getPkOrg(); // 当前机构
        String isAll = bdOrdAndAll.getIsAll();

        BdOrd bdOrd = bdOrdAndAll.getBdOrd();
        List<BdOrdAlias> bdOrdAliases = bdOrdAndAll.getBdOrdAliases();
        List<BdOrdOrg> bdOrdOrgs = bdOrdAndAll.getBdOrdOrgs();
        List<BdOrdItem> bdOrdItems = bdOrdAndAll.getBdOrdItems();
        BdOrdLab bdOrdLab = bdOrdAndAll.getBdOrdLab();
        BdOrdRis bdOrdRis = bdOrdAndAll.getBdOrdRis();
        List<BdOrdDept> bdOrdDepts = bdOrdAndAll.getBdOrdDepts();
        List<BdOrdEmr> bdOrdEmrs = bdOrdAndAll.getBdOrdEmrs();
        List<BdDictattr> dictAttrList = bdOrdAndAll.getBdOrdAttrListForSave();
        List<BdOrdLabCol> labColList = bdOrdAndAll.getLabColList();

        //'修改'/'新增'标志(通过判断主键是否为空决定标志)
        rleCode = bdOrd.getPkOrd() == null || bdOrd.getPkOrd().equals("") ? AddState : this.UpdateState;

        String pk = null; //用于存储新增后的主键，平台调用时传入
        /** 新增和更新医嘱项目 **/
        if (bdOrd.getPkOrd() == null) {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_ord "
                            + "where code = ?",
                    Integer.class, bdOrd.getCode());
            int count_name = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_ord "
                            + "where del_flag = '0' and name = ? and code_ordtype= ? and spec = ?",
                    Integer.class, bdOrd.getName(), bdOrd.getCodeOrdtype(), bdOrd.getSpec());

            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.insertBean(bdOrd);
                pk = bdOrd.getPkOrd(); //存储新增后的主键
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("医嘱项目编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("医嘱项目名称、类型、规格重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("医嘱项目编码、名称、类型、规格都重复！");
                }
            }
        } else {
            if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
                int count_code = DataBaseHelper
                        .queryForScalar(
                                "select count(1) from bd_ord "
                                        + "where del_flag = '0' and code = ? and pk_ord != ?",
                                Integer.class, bdOrd.getCode(),
                                bdOrd.getPkOrd());
                int count_name = DataBaseHelper
                        .queryForScalar(
                                "select count(1) from bd_ord "
                                        + "where del_flag = '0' and name = ? and code_ordtype= ? and spec = ? and pk_ord != ?",
                                Integer.class, bdOrd.getName(), bdOrd.getCodeOrdtype(), bdOrd.getSpec(), bdOrd.getPkOrd());
                if (count_code == 0 && count_name == 0) {
                    DataBaseHelper.updateBeanByPk(bdOrd, false);
                } else {
                    if (count_code != 0 && count_name == 0) {
                        throw new BusException("医嘱项目编码重复！");
                    }
                    if (count_code == 0 && count_name != 0) {
                        throw new BusException("医嘱项目名称、类型、规格重复！");
                    }
                    if (count_code != 0 && count_name != 0) {
                        throw new BusException("医嘱项目编码、名称、类型、规格都重复！");
                    }
                }
            } else {
                DataBaseHelper
                        .update("update bd_ord_org set NAME_PRT = ?, SPCODE = ?, D_CODE = ?, CODE_FREQ = ?,QUAN_DEF=? where PK_ORG = ? and PK_ORD = ?",
                                new Object[]{bdOrd.getNamePrt(),
                                        bdOrd.getSpcode(), bdOrd.getdCode(),
                                        bdOrd.getCodeFreq(),
                                        bdOrd.getQuanDef(), pkOrgCurrent,
                                        bdOrd.getPkOrd()});
                // DataBaseHelper.update("update bd_ord set NAME_PRT = ?, SPCODE = ?, D_CODE = ?, CODE_FREQ = ?,QUAN_DEF=?,PK_UNIT=?,FLAG_UNIT=?,EU_SEX=?,DT_ORDCATE=?,SPEC=?,PK_ORDTYPE=? where PK_ORD = ?",
                // new Object[]{bdOrd.getNamePrt(),bdOrd.getSpcode(),
                // bdOrd.getdCode(), bdOrd.getCodeFreq(),
                // bdOrd.getQuanDef(),bdOrd.getPkUnit(),bdOrd.getFlagUnit(),bdOrd.getEuSex(),bdOrd.getDtOrdcate(),bdOrd.getSpec(),bdOrd.getPkOrdtype(),
                // bdOrd.getPkOrd()});
            }
            if ("1".equals(isAll)) {
                SysApplog log = new SysApplog();
                log.setPkEmpOp(u.getPkEmp());
                log.setNameEmpOp(u.getNameEmp());
                log.setEuButype("5");
                log.setEuOptype("0");
                log.setNote("修改医嘱项目-全局");
                DataBaseHelper.insertBean(log);
            }
        }

        /** 新增或跟新别名 **/
        String jg = ((User) user).getPkOrg();// 别名只需要处理当前机构的
        if ("1".equals(isAll)) {//当机构用户打开全局菜单时
            jg = "~                               ";
        }
        if (bdOrdAliases != null && bdOrdAliases.size() != 0) {
            /** 校验别名唯一性 放在前台保存时校验相同机构不能有重名，不同机构可重名*/
//			Map<String, String> aliasmap = new HashMap<String, String>();
//			int len = bdOrdAliases.size();
//			for (int i = 0; i < len; i++) {
//				String alia = bdOrdAliases.get(i).getAlias();
//				if (aliasmap.containsKey(alia)) {
//					throw new BusException("别名重复！");
//				}
//				aliasmap.put(alia, bdOrdAliases.get(i).getPkOrdalia());
//			}

            // 先全删再恢复的方式（软删除）
            String pkord = bdOrd.getPkOrd();
            DataBaseHelper.update("update bd_ord_alias set del_flag = '1' where pk_ord = ? "
            		+ ("1".equals(isAll) ? "" : " and pk_org = '" + u.getPkOrg() + "' ")
                    , new Object[]{pkord});

            for (BdOrdAlias alia : bdOrdAliases) {
                if (alia.getPkOrdalia() != null) {
                    alia.setDelFlag("0");// 恢复
                    alia.setPkOrd(pkord);
                    DataBaseHelper.updateBeanByPk(alia, false);
                } else {
                    alia.setPkOrd(pkord);
//					if("1".equals(isAll)){//当机构用户打开全局菜单时
//						alia.setPkOrg("~");
//					}
                    DataBaseHelper.insertBean(alia);
                }
            }
        } else {
            String pkord = bdOrd.getPkOrd();
            DataBaseHelper
                    .update("update bd_ord_alias set del_flag = '1' where pk_ord = ?",
                            new Object[]{pkord});
        }

        /** 新增或跟新使用机构 **/
        // 如果当前机构是全局机构，操作bd_Ord_Org，不是全局的话不操作
        //if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
        if (bdOrdOrgs != null && bdOrdOrgs.size() != 0) {
            /** 校验机构唯一性 */
            Map<String, String> pkOrgMap = new HashMap<String, String>();
            int len = bdOrdOrgs.size();
            for (int i = 0; i < len; i++) {
                String pkOrg = bdOrdOrgs.get(i).getPkOrg();
                if (pkOrgMap.containsKey(pkOrg)) {
                    throw new BusException("机构重复！");
                }
                pkOrgMap.put(pkOrg, bdOrdOrgs.get(i).getPkOrdorg());
            }

            // 先全删再恢复的方式（软删除）
            String pkord = bdOrd.getPkOrd();
            int i = DataBaseHelper
                    .update("update bd_ord_org set del_flag = '1' where pk_ord = ?",
                            new Object[]{pkord});
            for (BdOrdOrg org : bdOrdOrgs) {
                if (org.getPkOrdorg() != null) {
                    org.setDelFlag("0");// 恢复
                    org.setPkOrd(pkord);
                    DataBaseHelper.updateBeanByPk(org, false);
                    // 使用机构更新时，不同步修改别名
                } else {
                    org.setPkOrd(pkord);
                    if(bdOrd!=null){
                        org.setNamePrt(bdOrd.getNamePrt());
                    }

                    DataBaseHelper.insertBean(org);
                    // 新增使用机构时，复制当前机构的别名
                    // 如果该机构之前使用过，后来删除了，别名还保留，再次使用时，需要先删除这些别名
						/*DataBaseHelper
								.execute(
										"delete from bd_ord_alias where pk_ord = ? and pk_org =?",
										new Object[] { pkord, org.getPkOrg() });
						for (BdOrdAlias alia : bdOrdAliases) {
							alia.setPkOrdalia(null);
							alia.setPkOrd(pkord);
							alia.setPkOrg(org.getPkOrg());
							DataBaseHelper.insertBean(alia);
						}*/
                }
            }
        } else {
            String pkord = bdOrd.getPkOrd();
            DataBaseHelper
                    .update("update bd_ord_org set del_flag = '1' where pk_ord = ?",
                            new Object[]{pkord});
        }
        //}

        /** 新增或跟新医疗记录 **/
        // 如果当前机构是全局机构，操作bd_Ord_Org，不是全局的话不操作
        List<BdOrdEmr> delBdOrdEmrList = new ArrayList<BdOrdEmr>();//医嘱-电子单取消关联（发消息给瑞联）
        List<BdOrdEmr> addBdOrdEmrList = new ArrayList<BdOrdEmr>();//医嘱-电子单关联（发消息给瑞联）
        if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
            String pkord = bdOrd.getPkOrd();
            delBdOrdEmrList = DataBaseHelper.queryForList("select * from bd_ord_emr where pk_ord=? and del_flag='0' ", BdOrdEmr.class, new Object[]{pkord});
            if (bdOrdEmrs != null && bdOrdEmrs.size() != 0) {
                /** 校验医疗记录唯一性 */
                Map<String, String> emrMap = new HashMap<String, String>();
                int len = bdOrdEmrs.size();
                for (int i = 0; i < len; i++) {
                    String codeEmrtemp = bdOrdEmrs.get(i).getCodeEmrtemp();
                    String nameEmrtemp = bdOrdEmrs.get(i).getNameEmrtemp();
                    String euPvType = "|" + bdOrdEmrs.get(i).getEuPvtype();
                    if (emrMap.containsKey(codeEmrtemp + euPvType)) {
                        throw new BusException("模板编码重复！");
                    }
                    if (emrMap.containsKey(nameEmrtemp + euPvType)) {
                        throw new BusException("模板名称重复！");
                    }
                    emrMap.put(codeEmrtemp + euPvType, bdOrdEmrs.get(i).getCodeEmrtemp());
                    emrMap.put(nameEmrtemp + euPvType, bdOrdEmrs.get(i).getNameEmrtemp());
                }

                // 先全删再恢复的方式（软删除）
                DataBaseHelper.update("update bd_ord_emr set del_flag = '1' where pk_ord = ?", new Object[]{pkord});
                for (BdOrdEmr emr : bdOrdEmrs) {
                    if (emr.getPkOrdemr() != null) {
                        emr.setDelFlag("0");// 恢复
                        emr.setPkOrd(pkord);
                        System.out.println("pkorg:" + emr.getPkOrg());
                        srvMapper.updateBdord(emr);


                    } else {
                        emr.setPkOrd(pkord);
                        DataBaseHelper.insertBean(emr);
                    }
                }
            } else {
                DataBaseHelper
                        .update("update bd_ord_emr set del_flag = '1' where pk_ord = ?",
                                new Object[]{pkord});
            }
            addBdOrdEmrList = DataBaseHelper.queryForList("select * from bd_ord_emr where pk_ord=? and del_flag='0' ", BdOrdEmr.class, new Object[]{pkord});
        }


        /**
         * 新增或跟新收费项目
         */
        // 如果当前机构是全局机构，操作，不是全局的话不操作
        if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
            if (bdOrdItems != null && bdOrdItems.size() != 0) {
                /** 校验收费项目唯一性 */
                Map<String, String> pkItemMap = new HashMap<String, String>();
                int len = bdOrdItems.size();
                for (int i = 0; i < len; i++) {
                    String pkItem = bdOrdItems.get(i).getPkItem();
                    if (pkItemMap.containsKey(pkItem)) {
                        throw new BusException("收费项目重复！");
                    }
                    pkItemMap.put(pkItem, bdOrdItems.get(i).getPkOrditem());
                }

                // 先全删再恢复的方式（软删除）
                String pkord = bdOrd.getPkOrd();
                //DataBaseHelper.update("update bd_ord_item set del_flag = '1' where pk_ord = ?",
                //new Object[] { pkord });

                //查询所有的收费项目
                String sql = "select * from bd_ord_item where del_flag = '0' and pk_ord = ? ";
                List<BdOrdItem> list = DataBaseHelper.queryForList(sql, BdOrdItem.class, new Object[]{pkord});
                //Iterator<BdOrdItem> iterator = list.iterator();

                for (BdOrdItem item : bdOrdItems) {


                    if (item.getPkOrditem() != null) {
                        String pkOrditem = item.getPkOrditem();
                        //遍历已存在的收费项目，若相同则移除出要删除的list
 /*                       while (iterator.hasNext()) {

                            String allPKOrditem = iterator.next().getPkOrditem();
                            String pkOrditem = item.getPkOrditem();

                            if (allPKOrditem.equals(pkOrditem)) {
                                iterator.remove();
                                break;
                            }
                        }*/
                        //上面的代码存在bug
                       if(list != null && list.size()>0){
                           for(BdOrdItem bd : list){
                               String allPKOrditem = bd.getPkOrditem();
                               if(allPKOrditem.equals(pkOrditem)){
                                   list.remove(bd);
                                   break;
                               }
                           }
                       }

                        //item.setDelFlag("0");// 恢复
                        item.setPkOrd(pkord);
                        DataBaseHelper.updateBeanByPk(item, false);
                    } else {
                        item.setPkOrd(pkord);
                        DataBaseHelper.insertBean(item);
                    }
                }

                //删除收费项目
                for (BdOrdItem item : list) {
                    DataBaseHelper.update("delete from bd_ord_item where pk_ord = ? and pk_item = ? and flag_pd = ?",
                            new Object[]{pkord, item.getPkItem(), item.getFlagPd()});
                }

            } else {
                String pkord = bdOrd.getPkOrd();
                DataBaseHelper
                        .update("update bd_ord_item set del_flag = '1' where pk_ord = ?",
                                new Object[]{pkord});
            }
        }

        /**
         * 新增或跟新检验属性
         */
        // 如果当前机构是全局机构，操作，不是全局的话不操作
        if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
            if (bdOrdLab != null) {
                String pkord = bdOrd.getPkOrd();
                String isnull = null;
                if (bdOrdLab.getDtSamptype() == null && bdOrdLab.getDtContype() == null
                        && bdOrdLab.getDtLisgroup() == null && bdOrdLab.getDtColltype() == null
                        && bdOrdLab.getNote() == null) {
                    isnull = null;
                } else {
                    isnull = "123";
                }
                if (bdOrdLab.getPkOrdlab() == null && isnull != null) {
                    bdOrdLab.setPkOrd(pkord);
                    DataBaseHelper.insertBean(bdOrdLab);
                } else {
                    DataBaseHelper.updateBeanByPk(bdOrdLab, false);
                }
            } else {
                String pkord = bdOrd.getPkOrd();
                DataBaseHelper
                        .update("update bd_ord_lab set del_flag = '1' where pk_ord = ?",
                                new Object[]{pkord});
            }

            if (labColList != null && labColList.size() > 0) {
                /** 校验执行科室唯一性 */
                Map<String, String> pkdeptMap = new HashMap<String, String>();
                int len = labColList.size();
                for (int i = 0; i < len; i++) {
                    String pkDept = labColList.get(i).getPkDeptCol();
                    if(StringUtils.isNotBlank(pkDept)) {
                    	boolean isRepeat=pkdeptMap.containsKey(pkDept) && StringUtils.isNotBlank(labColList.get(i).getPkOrgarea())
                                &&StringUtils.isNotBlank(pkdeptMap.get(pkDept))&&labColList.get(i).getPkOrgarea().equals(pkdeptMap.get(pkDept));
                            if (isRepeat) {
                                throw new BusException("同一机构下标本采集科室重复！");
                            }
                    }
                    
                    pkdeptMap.put(pkDept, labColList.get(i).getPkOrgarea());
                }

                // 先全删再恢复的方式（软删除）
                String pkord = bdOrd.getPkOrd();
                BdOrdOrg bdOrdOrg = DataBaseHelper
                        .queryForBean(
                                "select * from bd_ord_lab_col where pk_ord=? and pk_org = ?",
                                BdOrdOrg.class, jg, pkord);
                DataBaseHelper
                        .update("update bd_ord_lab_col set del_flag = '1' where pk_ord = ? and pk_org = ?",
                                new Object[]{pkord, UserContext.getUser().getPkOrg()});
                for (BdOrdLabCol dept : labColList) {
                    if (!CommonUtils.isEmptyString(dept.getPkOrdlabcol())) {
                        dept.setDelFlag("0");// 恢复
                        dept.setPkOrd(pkord);
                        DataBaseHelper.updateBeanByPk(dept, false);
                    } else {
                        dept.setPkOrd(pkord);
                        ApplicationUtils.setDefaultValue(dept, true);
                        DataBaseHelper.insertBean(dept);
                    }
                }
            } else {
                String pkord = bdOrd.getPkOrd();
                DataBaseHelper
                        .update("update bd_ord_lab_col set del_flag = '1' where pk_ord = ? and pk_org = ?",
                                new Object[]{pkord, UserContext.getUser().getPkOrg()});
            }
        }

        /**
         * 新增或跟新检查属性
         */
        // 如果当前机构是全局机构，操作，不是全局的话不操作
        if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
            if (bdOrdRis != null) {
                String pkord = bdOrd.getPkOrd();
                String isnull = null;
                if (bdOrdRis.getDtType() == null && bdOrdRis.getDtBody() == null && bdOrdRis.getDescAtt() == null) {
                    isnull = null;
                } else {
                    isnull = "123";
                }
                if (bdOrdRis.getPkOrdris() == null && isnull != null) {
                    bdOrdRis.setPkOrd(pkord);
                    DataBaseHelper.insertBean(bdOrdRis);
                } else {
                    DataBaseHelper.updateBeanByPk(bdOrdRis, false);
                }
            } else {
                String pkord = bdOrd.getPkOrd();
                DataBaseHelper
                        .update("update bd_ord_ris set del_flag = '1' where pk_ord = ?",
                                new Object[]{pkord});
            }
        }

        /** 新增或跟新执行科室 **/
        // 如果当前机构是全局机构，不操作，不是全局的话就操作
        if ((!pkOrgCurrent.startsWith("~")) && (!"1".equals(isAll))) {
            if (bdOrdDepts != null && bdOrdDepts.size() != 0) {
                /** 校验执行科室唯一性 */
                Map<String, String> pkdeptMap = new HashMap<String, String>();
                int len = bdOrdDepts.size();
                for (int i = 0; i < len; i++) {
                    String pkDept = bdOrdDepts.get(i).getPkDept();
                    if (pkdeptMap.containsKey(pkDept)) {
                        throw new BusException("执行科室重复！");
                    }
                    pkdeptMap.put(pkDept, bdOrdDepts.get(i).getPkOrddept());
                }

                // 先全删再恢复的方式（软删除）
                //2020-02-04 调整为真删除，因为数据库存在唯一主键的索引
                String pkord = bdOrd.getPkOrd();
                BdOrdOrg bdOrdOrg = DataBaseHelper.queryForBean(
                        "select * from bd_ord_org where del_flag = '0' and pk_org = ? and pk_ord = ?",
                        BdOrdOrg.class, jg, pkord);
                DataBaseHelper.update("delete from bd_ord_dept where pk_ord = ? and pk_org = ? and del_flag = '1' "
                        , new Object[]{pkord, UserContext.getUser().getPkOrg()});
                for (BdOrdDept dept : bdOrdDepts) {
                    if (!CommonUtils.isEmptyString(dept.getDelFlag()) && "1".equals(dept.getDelFlag())) {
                        DataBaseHelper.deleteBeanByPk(dept);
                        continue;
                    }
                    if (dept.getPkOrddept() != null) {
                        dept.setDelFlag("0");// 恢复
                        dept.setPkOrd(pkord);
                        DataBaseHelper.updateBeanByPk(dept, false);
                    } else {
                        dept.setPkOrd(pkord);
                        // 设置医嘱机构对照主键
                        dept.setPkOrdorg(bdOrdOrg.getPkOrdorg());
                        DataBaseHelper.insertBean(dept);
                    }
                }
            } else {
                String pkord = bdOrd.getPkOrd();
                DataBaseHelper.update("update bd_ord_dept set del_flag = '1' where pk_ord = ? and pk_org = ?",
                        new Object[]{pkord, UserContext.getUser().getPkOrg()});
            }
        }

        /**保存或更新拓展属性*/
        if (dictAttrList != null && dictAttrList.size() != 0) {
            for (BdDictattr dictAttr : dictAttrList) {
                if (dictAttr.getPkDictattr() != null) {
                    dictAttr.setDelFlag("0");
                    dictAttr.setPkDict(bdOrd.getPkOrd());
                    DataBaseHelper.updateBeanByPk(dictAttr, false);
                } else {
                    dictAttr.setDelFlag("0");
                    dictAttr.setPkDict(bdOrd.getPkOrd());
                    DataBaseHelper.insertBean(dictAttr);
                }
            }
        }

        saveOrderExeDept(bdOrdAndAll, user);

        //发送消息至平台
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("bdOrds", bdOrdAndAll.getBdOrd());
        paramMap.put("bdOrd", MapUtils.objectToMap(bdOrdAndAll.getBdOrd()));
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        //获取执行科室
        List<BdOrdDept> ordDepts = bdOrdAndAll.getBdOrdDepts();
        if (ordDepts != null && ordDepts.size() > 0) {
            paramMap.put("ordExDept", (ordDepts.get(0)));
        }


        paramMap.put("STATUS", rleCode);
        PlatFormSendUtils.sendBdOrdMsg(paramMap);
        try {
            // 医嘱项目注册更新服务发送消息到平台 syx
            ExtSystemProcessUtils.processExt2HipMethod("OrderItemsAddAndUpdate", paramMap);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return bdOrd;
    }

    /**
     * 保存执行科室
     *
     * @param
     */
    private void saveOrderExeDept(BdOrdAndAllParam bdOrdAndAll, IUser user) {
        String isAll = bdOrdAndAll.getIsAll();
        BdOrd bdOrd = bdOrdAndAll.getBdOrd();
        List<BdOrdDept> bdOrdDepts = bdOrdAndAll.getBdOrdDepts();

        String jg = ((User) user).getPkOrg();// 别名只需要处理当前机构的
        if ("1".equals(isAll)) {//当机构用户打开全局菜单时
            jg = "~                               ";
        }

        //if ("1".equals(isAll)) {
        if (bdOrdDepts != null && bdOrdDepts.size() != 0) {
            /** 校验执行科室唯一性 */
            Map<String, String> pkdeptMap = new HashMap<String, String>();
            int len = bdOrdDepts.size();
            for (int i = 0; i < len; i++) {
                String pkDept = bdOrdDepts.get(i).getPkDept();
                if (pkdeptMap.containsKey(pkDept)) {
                    throw new BusException("执行科室重复！");
                }
                pkdeptMap.put(pkDept, bdOrdDepts.get(i).getPkOrddept());
            }
            // 先全删再恢复的方式（软删除）
            //2020-02-04 调整为真删除，以解决与数据库中唯一索引冲突
            String pkord = bdOrd.getPkOrd();
            DataBaseHelper.update("delete from bd_ord_dept where pk_ord = ? and del_flag = '1' ", new Object[]{pkord});
            for (BdOrdDept dept : bdOrdDepts) {
                BdOrdOrg bdOrdOrg = DataBaseHelper.queryForBean("select * from bd_ord_org where del_flag = '0' and pk_ord = ?", BdOrdOrg.class, pkord);
                if (null != dept && !CommonUtils.isEmptyString(dept.getPkOrddept()) && "1".equals(dept.getDelFlag())) {
                    DataBaseHelper.deleteBeanByPk(dept);
                } else if (dept.getPkOrddept() != null) {
                    dept.setPkOrd(pkord);
                    DataBaseHelper.updateBeanByPk(dept, false);
                } else {
                    dept.setPkOrd(pkord);
                    // 设置医嘱机构对照主键
                    dept.setPkOrdorg(bdOrdOrg.getPkOrdorg());
                    DataBaseHelper.insertBean(dept);
                }
            }
        }

    }

    /**
     * 删除医嘱项目及相关
     *
     * @param param
     * @param user
     */
    public void delBdOrdAll(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkOrd = null;
        if (paramMap.get("pkOrd") != null) {
            pkOrd = paramMap.get("pkOrd").toString();
        }
        String delFlag = paramMap.get("delFlag").toString();
        if ("0".equals(delFlag)) {
            int count = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from sch_srv_ord where del_flag='0' and pk_ord = ?",
                            Integer.class, pkOrd);
            int ordcnt = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from cn_order where del_flag='0' and pk_ord = ?",
                            Integer.class, pkOrd);
            if (count != 0) {
                throw new BusException("该医嘱项目被排班服务引用，不可删除！");
            } else if(ordcnt>0){
                throw new BusException("该医嘱项目已经被开立过医嘱，不可删除！");
            } else {

                DataBaseHelper.update(
                        "update bd_ord_lab set del_flag = '1' where pk_ord = ?",
                        new Object[]{pkOrd});
                DataBaseHelper.update(
                        "update bd_ord_ris set del_flag = '1' where pk_ord = ?",
                        new Object[]{pkOrd});
                DataBaseHelper.update(
                        "delete from bd_ord_dept where pk_ord = ?",
                        new Object[]{pkOrd});
                DataBaseHelper.update(
                        "update bd_ord_org set del_flag = '1' where pk_ord = ?",
                        new Object[]{pkOrd});
                DataBaseHelper.update(
                        "update bd_ord_item set del_flag = '1' where pk_ord = ?",
                        new Object[]{pkOrd});
                DataBaseHelper.update(
                        "update bd_ord_alias set del_flag = '1' where pk_ord = ?",
                        new Object[]{pkOrd});
                DataBaseHelper.update(
                        "update bd_ord_emr set del_flag = '1' where pk_ord = ?",
                        new Object[]{pkOrd});
                DataBaseHelper
                        .update("update bd_ord_exclu_dt set del_flag = '1' where pk_ord = ?",
                                new Object[]{pkOrd});
                DataBaseHelper.update(
                        "update bd_ord set del_flag = '1' where pk_ord = ? and del_flag='0'",
                        new Object[]{pkOrd});
                //'删除'标志
                rleCode = this.DelState;
                paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
                paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
                paramMap.put("STATUS", rleCode);
                PlatFormSendUtils.sendBdOrdMsg(paramMap);
            }
        } else {
            DataBaseHelper.update("update bd_ord set del_flag='0' where pk_ord= ? and del_flag='1'",
                    new Object[]{pkOrd});
        }
    }

    /**
     * 保存医嘱项目引用 医嘱项目(机构)处使用该接口
     *
     * @param param
     * @param user
     */
    public void saveBdOrdOrgs(String param, IUser user) {
        List<BdOrdOrgVo> bdOrdOrgVos = JsonUtil.readValue(param,
                new TypeReference<List<BdOrdOrgVo>>() {
                });

        String pkOrg = ((User) user).getPkOrg();

        /** 先全删再插入的方式 **/
        DataBaseHelper.execute("delete from bd_ord_org where pk_org = ?",
                new Object[]{pkOrg});
        if (bdOrdOrgVos != null && bdOrdOrgVos.size() != 0) {

            for (BdOrdOrgVo orgVo : bdOrdOrgVos) {
                // 传过来的每一个BdOrdOrgVo都不带主键
                BdOrdOrg org = new BdOrdOrg();
                BeanUtils.copyProperties(orgVo, org);
                if ("1".equals(orgVo.getFlag())) {
                    DataBaseHelper.insertBean(org);
                    // 特殊排除：排除当前机构为全局机构
                    if (pkOrg.indexOf("~") == -1) {
                        // 别名操作
                        String pkOrd = orgVo.getPkOrd();
                        // 全局的别名
                        List<BdOrdAlias> overallaliaslist = DataBaseHelper
                                .queryForList(
                                        "select a.* from bd_ord_alias a where a.del_flag='0' and a.pk_org like '~%' and a.pk_ord = ?",
                                        BdOrdAlias.class, pkOrd);

                        if (overallaliaslist != null
                                && overallaliaslist.size() != 0) {
                            // 当前机构下的别名
                            List<BdOrdAlias> orgaliaslist = DataBaseHelper
                                    .queryForList(
                                            "select a.* from bd_ord_alias a where a.del_flag='0' and a.pk_org = ? and a.pk_ord = ?",
                                            BdOrdAlias.class, pkOrg, pkOrd);
                            // "不与当前机构下重复的全局别名"
                            List<BdOrdAlias> aliaslist = new ArrayList<BdOrdAlias>();
                            // 存在当前机构下的别名，则有必要比较重复性
                            if (orgaliaslist != null
                                    && orgaliaslist.size() != 0) {
                                for (BdOrdAlias orgalia : orgaliaslist) {
                                    String name_orgalia = orgalia.getAlias();
                                    // 迭代删除list元素
                                    Iterator<BdOrdAlias> overallaliaItor = overallaliaslist
                                            .iterator();
                                    while (overallaliaItor.hasNext()) {
                                        BdOrdAlias overallalia = overallaliaItor
                                                .next();
                                        if (name_orgalia.equals(overallalia
                                                .getAlias())) {
                                            overallaliaItor.remove();
                                        }
                                    }
                                }
                                aliaslist = overallaliaslist;
                            } else {// 不存在当前机构下的别名，没有必要比较重复性，该医嘱暂时只有全局的别名，都复制
                                aliaslist = overallaliaslist;
                            }
                            // 把"不与当前机构下重复的全局别名"复制一份插入,存为当前机构下的
                            for (BdOrdAlias al : aliaslist) {
                                al.setPkOrdalia(null);
                                al.setPkOrg(pkOrg);
                                DataBaseHelper.insertBean(al);
                            }
                        }
                    }
                } else if ("-1".equals(orgVo.getFlag())) {
                    // 特殊排除：排除当前机构为全局机构
                    if (pkOrg.indexOf("~") == -1) {
                        // 别名操作
                        String pkOrd = orgVo.getPkOrd();
                        // 当前机构下的别名
                        List<BdOrdAlias> aliaslist = DataBaseHelper
                                .queryForList(
                                        "select a.* from bd_ord_alias a where a.del_flag='0' and a.pk_org = ? and a.pk_ord = ?",
                                        BdOrdAlias.class, pkOrg, pkOrd);
                        // 把当前机构下的别名也要同时删除
                        for (BdOrdAlias al : aliaslist) {
                            al.setDelFlag("1");
                            DataBaseHelper.updateBeanByPk(al, false);
                        }
                    }
                } else {
                    DataBaseHelper.insertBean(org);
                }
            }

        }
    }

    /**
     * 删除医嘱项目引用及相关
     *
     * @param param
     * @param user
     */
    public void delBdOrdOrgsAndDepts(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkOrd = null;
        if (paramMap.get("pkOrd") != null) {
            pkOrd = paramMap.get("pkOrd").toString();
        }
        String jg = ((User) user).getPkOrg();

        //'删除'标志
        rleCode = this.DelState;
        DataBaseHelper
                .execute(
                        "delete from bd_ord_dept where pk_ord = ? and pk_org like ?||'%'",
                        pkOrd, jg);
        DataBaseHelper
                .execute(
                        "delete from bd_ord_org where pk_ord = ? and pk_org like ?||'%'",
                        pkOrd, jg);
        // 特殊排除：当前机构不为全局机构
        if (jg.indexOf("~") == -1) {
            // 删除当前机构下的别名
            DataBaseHelper
                    .update("update bd_ord_alias set del_flag='1' where pk_ord = ? and pk_org like ?||'%'",
                            pkOrd, jg);
        }
        //发送消息至平台
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        paramMap.put("STATUS", rleCode);
        PlatFormSendUtils.sendBdOrdMsg(paramMap);
    }

    /**
     * 保存就诊登记收费项目
     *
     * @param param
     * @param user
     */
    public void saveBdItemPvs(String param, IUser user) {
        List<BdItemPv> pvs = JsonUtil.readValue(param,
                new TypeReference<List<BdItemPv>>() {
                });

        if (pvs != null && pvs.size() != 0) {
            // 先全删再恢复的方式（软删除）
            String pkitem = pvs.get(0).getPkItem();
            DataBaseHelper.update(
                    "update bd_item_pv set del_flag = '1' where pk_item = ?",
                    new Object[]{pkitem});
            for (BdItemPv pv : pvs) {
                if (pv.getPkPvitem() != null) {
                    pv.setDelFlag("0");// 恢复
                    DataBaseHelper.updateBeanByPk(pv, false);
                } else {
                    DataBaseHelper.insertBean(pv);
                }
            }
        } else {
            String pkitem = pvs.get(0).getPkItem();
            DataBaseHelper.update(
                    "update bd_item_pv set del_flag = '1' where pk_item = ?",
                    new Object[]{pkitem});
        }
    }

    /**
     * 保存收费项目分类
     *
     * @param param
     * @param user
     * @return
     * @throws HL7Exception
     */
    public BdItemcate saveBdItemcate(String param, IUser user)
            throws HL7Exception {

        BdItemcate itemcate = JsonUtil.readValue(param, BdItemcate.class);

        String pk = ""; //存储新增后的主键，传入调用平台接口

        if (itemcate.getPkItemcate() == null) {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_itemcate "
                            + "where del_flag = '0' and code = ?",
                    Integer.class, itemcate.getCode());
            int count_name = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_itemcate "
                            + "where del_flag = '0' and name = ?",
                    Integer.class, itemcate.getName());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.insertBean(itemcate);
                pk = itemcate.getPkItemcate();
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("收费项目分类编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("收费项目分类名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("收费项目分类编码和名称都重复！");
                }
            }
        } else {
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_itemcate "
                                    + "where del_flag = '0' and code = ? and pk_itemcate != ?",
                            Integer.class, itemcate.getCode(),
                            itemcate.getPkItemcate());
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_itemcate "
                                    + "where del_flag = '0' and name = ? and pk_itemcate != ?",
                            Integer.class, itemcate.getName(),
                            itemcate.getPkItemcate());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.updateBeanByPk(itemcate, false);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("收费项目分类编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("收费项目分类名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("收费项目分类编码和名称都重复！");
                }
            }
        }
        Map<String,Object> cateMap = new HashMap<>();
        cateMap.put("code",itemcate.getCode());
        cateMap.put("name",itemcate.getName());
        Map<String,Object> map = Maps.newHashMap();
        map.put("item", cateMap);
        PlatFormSendUtils.execute(map,"sendBdItemcateMsg");
        return itemcate;
    }

    /**
     * 删除收费项目分类
     *
     * @param param
     * @param user
     */
    public void delBdItemcate(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String pkItemcate = null;
        if (paramMap.get("pkItemcate") != null) {
            pkItemcate = paramMap.get("pkItemcate").toString();
        }
        int count_audit_itemcate = DataBaseHelper.queryForScalar(
                "select count(1) from bd_audit_itemcate "
                        + "where del_flag = '0' and pk_itemcate = ?",
                Integer.class, pkItemcate);
        int count_item = DataBaseHelper.queryForScalar(
                "select count(1) from bd_item "
                        + "where del_flag = '0' and pk_itemcate = ?",
                Integer.class, pkItemcate);

        if (count_audit_itemcate == 0 && count_item == 0) {
            DataBaseHelper
                    .update("update bd_itemcate set del_flag = '1' where pk_itemcate = ?",
                            new Object[]{pkItemcate});
        } else {
            if (count_audit_itemcate != 0) {
                throw new BusException("收费项目分类与核算项目关联！");
            }
            if (count_item != 0) {
                throw new BusException("收费项目分类被字典引用！");
            }
        }

        Map<String,Object> itemCate = Maps.newHashMap();
        itemCate.put("status","_DELETE");
        itemCate.put("item",DataBaseHelper.queryForMap("select * from bd_audit_itemcate where pk_itemcate=?",new Object[]{pkItemcate}));
        PlatFormSendUtils.execute(itemCate,"sendBdItemcateMsg");
    }

    /**
     * 收费项目-明细查看
     *
     * @param param
     * @param user
     * @return
     */
    public BdItemAndHpSetPricesParam getItemAndHpSetPrices(String param,
                                                           IUser user) {

        Map paramMap = JsonUtil.readValue(param, Map.class);
        String pkItem = null;
        String isAll = null;
        //1标识所有 0标识去掉已删除或者已经停用的
        String isContainAll = "0";
        if (paramMap.get("pkItem") != null) {
            pkItem = paramMap.get("pkItem").toString();
        }
        if (paramMap.get("isAll") != null) {
            isAll = paramMap.get("isAll").toString();
        }
       if (paramMap.get("isFlag") != null) {
        	isContainAll = paramMap.get("isFlag").toString();
        	if(!"1".equals(isContainAll)){
        		isContainAll="0";
        	}
       }

        BdItem item = srvMapper.getBdItemByPk(pkItem);
        List<BdItemPrice> itemPrices = new ArrayList<BdItemPrice>();
        // 0 集团和本机构的,1 所有的
        if ("0".equals(isAll)) {
            List<String> orgPkList = new ArrayList<String>();
            String pkorg = ((User) user).getPkOrg();// 当前机构
            orgPkList.add(pkorg);

            // 查找本机构和其根集团机构
            Map<String, Object> org = DataBaseHelper.queryForMap(
                    "select pk_org,pk_father from bd_ou_org where pk_org = ?",
                    pkorg);
            while (org.get("pkFather") != null) {
                String pkorgtemp = "";
                pkorgtemp = (String) org.get("pkFather");
                org = DataBaseHelper
                        .queryForMap(
                                "select pk_org,pk_father from bd_ou_org where pk_org = ?",
                                pkorgtemp);
            }
            if (!pkorg.equals(org.get("pkOrg").toString())) {
                orgPkList.add(org.get("pkOrg").toString());
            }
            itemPrices = srvMapper.getBdItemPriceByItemContainDel(pkItem, orgPkList, isContainAll);
        } else {
            itemPrices = srvMapper.getBdItemPriceByItemContainDel(pkItem, null, isContainAll);
        }
        List<BdItemHp> itemHps = srvMapper.getBdItemHpByItem(pkItem);
        List<BdItemSet> itemSets = srvMapper.getBdItemSetByItem(pkItem);
        //查询收费项目扩展属性信息
        String userPkOrg =
                ((User) user).getPkOrg().equals("~                               ") ? "" : ((User) user).getPkOrg();
        List<BdItemAttrVo> itemAttrs = srvMapper.getBdItemAttrByItem(userPkOrg, pkItem);
        //特殊定价
        List<BdItemSp> itemSps = srvMapper.getBdItemSpByItem(pkItem);
        BdItemAndHpSetPricesParam ret = new BdItemAndHpSetPricesParam();
        ret.setItem(item);
        ret.setItemPrices(itemPrices);
        ret.setItemHps(itemHps);
        ret.setItemSets(itemSets);
        ret.setItemAttr(itemAttrs);
        ret.setItemSps(itemSps);

        return ret;
    }

    /**
     * 交易号：001002007057
     * 查询收费项目关联组套信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryRatStack(String param, IUser user) {
        String pkItem = JsonUtil.getFieldValue(param, "pkItem");

        return srvMapper.qryRatStack(pkItem);
    }

    /**
     * 交易号：001002007058
     * 查询收费项目关联医嘱信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryRatOrd(String param, IUser user) {
        String pkItem = JsonUtil.getFieldValue(param, "pkItem");

        return srvMapper.qryRatOrd(pkItem);
    }

    /**
     * 保存收费项目 及 医保类型，收费项目组套，收费项目价格定义
     * <p>
     * 全局：处理所有的（收费项目 及 医保类型，收费项目组套，收费项目价格定义）
     * <p>
     * 机构：只有对价格修改的权限，其他一律无权操作，虽然前台传了整个保存时的参数（收费项目 及 医保类型，收费项目组套，收费项目价格定义）
     *
     * @param param
     * @param user
     * @throws Exception
     */
    public String saveItemAndHpSetPrices(String param, IUser user) throws Exception {
        BdItemAndHpSetPricesParam itemAndHpSetPrices = JsonUtil.readValue(
                param, BdItemAndHpSetPricesParam.class);
        String isAll = itemAndHpSetPrices.getIsAll();
        BdItem item = itemAndHpSetPrices.getItem();

        item.setModityTime(new Date());
        List<BdItemPrice> itemPrices = itemAndHpSetPrices.getItemPrices();
        List<BdItemPrice> itemPricesDelPk = itemAndHpSetPrices.getItemPricesDelPk();
        List<BdItemHp> itemHps = itemAndHpSetPrices.getItemHps();
        List<BdItemSet> itemSets = itemAndHpSetPrices.getItemSets();
        List<BdItemAttrVo> itemAttrs = itemAndHpSetPrices.getItemAttr();


        //特殊定价.记费策略
        List<BdItemSp> itemSps = itemAndHpSetPrices.getItemSps();
        List<CgdivItemVo> itemCgdivs = itemAndHpSetPrices.getItemCgDivs();
        User u = (User) user;
        String pkOrg = u.getPkOrg();

        /** 新增和更新收费项目 **/
        if (item.getPkItem() == null) {
            //编码
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_item "
                            + "where del_flag = '0' and code = ?",
                    Integer.class, item.getCode());
            //名称+规格+单位
            int count = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_item "
                            + "where del_flag = '0' and code = ? and pk_item != ? and name = ? and spec = ? and pk_unit = ?",
                    Integer.class, item.getCode(), item.getPkItem(), item.getName(), item.getSpec(), item.getPkUnit());
//            int count_codeHp = DataBaseHelper.queryForScalar(
//                    "select count(1) from bd_item "
//                            + "where del_flag = '0' and code_hp = ?",
//                    Integer.class, item.getCodeHp());

            if (count_code == 0 && count == 0) {
                DataBaseHelper.insertBean(item);

                // 发送数据至平台
                item.setState(AddState);
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("item", MapUtils.objectToMap(item));
                paramMap.put("STATUS", this.AddState);
                paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
                PlatFormSendUtils.sendBdItemMsg(paramMap);
                try {
                    //收费项目新增注册 （收费项目注册更新服务）发送到平台
                    ExtSystemProcessUtils.processExt2HipMethod("ChargeDictAddAndUpdate", paramMap);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                paramMap = null;
            } else if (count_code != 0) {
                throw new BusException("收费项目编码重复！");
            } else if (count != 0) {
                throw new BusException("收费项目名称，规格，单位重复！");
            }
//            else if (count_codeHp != 0) {
//                throw new BusException("上传编码重复！");
//            }

        } else {
            BdItem beforeItem = DataBaseHelper.queryForBean("select * from bd_item where pk_item = ?", BdItem.class, item.getPkItem());
            if ("1".equals(isAll)) {// 0--机构情况时，不处理收费项目
                int count_code = DataBaseHelper
                        .queryForScalar(
                                "select count(1) from bd_item "
                                        + "where del_flag = '0' and code = ? and pk_item != ?",
                                Integer.class, item.getCode(), item.getPkItem());
//                int count_codeHp = DataBaseHelper
//                        .queryForScalar(
//                                "select count(1) from bd_item "
//                                        + "where del_flag = '0' and code_hp = ? and pk_item != ?",
//                                Integer.class, item.getCodeHp(), item.getPkItem());
                //名称+规格+单位
                int count = DataBaseHelper.queryForScalar(
                        "select count(1) from bd_item "
                                + "where del_flag = '0' and code = ? and pk_item != ? and name = ? and spec = ? and pk_unit = ?",
                        Integer.class, item.getCode(), item.getPkItem(), item.getName(), item.getSpec(), item.getPkUnit());

                if (count_code == 0 && count == 0) {
                    DataBaseHelper.updateBeanByPk(item, false);

                    item.setState(this.UpdateState);
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("item", MapUtils.objectToMap(item));
                    paramMap.put("STATUS", this.UpdateState);
                    paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
                    PlatFormSendUtils.sendBdItemMsg(paramMap);
                    try {
                        //收费项目新增注册 （收费项目注册更新服务）发送到平台
                        ExtSystemProcessUtils.processExt2HipMethod("ChargeDictAddAndUpdate", paramMap);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    paramMap = null;
                } else if (count_code != 0) {
                    throw new BusException("收费项目编码重复！");
                } else if (count != 0) {
                    throw new BusException("收费项目名称，规格，单位重复！");
                }
//                else if (count_codeHp != 0) {
//                    throw new BusException("上传编码重复！");
//                }
            }

            SysApplog log = new SysApplog();
            log.setPkEmpOp(u.getPkEmp());
            log.setNameEmpOp(u.getNameEmp());
            log.setEuButype("0");
            log.setEuOptype("0");
            log.setNote("修改收费项目");
            log.setContentBf("价格：" + beforeItem.getPrice());
            log.setContent("价格：" + item.getPrice());
            DataBaseHelper.insertBean(log);
        }

        String pkitem = item.getPkItem();

        /**新增或更新收费项目扩展属性*/
        if (itemAttrs != null && itemAttrs.size() > 0) {
            // 先全删再恢复的方式（软删除）
            DataBaseHelper
                    .update("update bd_dictattr set del_flag = '1' where pk_dict = ?",
                            new Object[]{pkitem});
            for (BdItemAttrVo vo : itemAttrs) {
                //组装pojo数据
                BdDictattr bdDictAttr = new BdDictattr();
                //主键
                bdDictAttr.setPkDictattr(vo.getPkDictattr());
                //所属机构（当前机构）
                bdDictAttr.setPkOrg(((User) user).getPkOrg());
                //属性主键
                bdDictAttr.setPkDictattrtemp(vo.getPkDictattrtemp());
                //属性值
                bdDictAttr.setValAttr(vo.getValAttr());
                //关联收费项目主键
                bdDictAttr.setPkDict(pkitem);
                bdDictAttr.setCodeAttr(vo.getCodeAttr());
                bdDictAttr.setNameAttr(vo.getNameAttr());

                if (!CommonUtils.isEmptyString(bdDictAttr.getPkDictattr())) {
                    bdDictAttr.setDelFlag("0");// 恢复
                    DataBaseHelper.updateBeanByPk(bdDictAttr, false);
                } else {
                    DataBaseHelper.insertBean(bdDictAttr);
                }
            }
        }

        /** 新增或跟新价格定义 **/
        // 校验：同一机构，同一价格类型，启用的相同价格 开始时间和结束时间不可重叠
        if (itemPrices != null && itemPrices.size() >= 2) {
            List<BdItemPrice> iPrices = new ArrayList<BdItemPrice>();
            // 过滤停用的
            for (BdItemPrice p : itemPrices) {
                if ("0".equals(p.getFlagStop())) {
                    iPrices.add(p);
                }
            }
            if (iPrices.size() >= 2) {
                if (isTimeRepeated(iPrices)) {
                    throw new BusException(
                            "同一机构，同一价格类型，启用的相同价格,其开始时间和结束时间不可重叠！");
                }
            }
        }

        /**
         * 新增或修改特殊定价
         */
        if (itemSps != null && itemSps.size() > 0) {
            for (BdItemSp Sp : itemSps) {
                if(Sp.getRatioChildren().isInfinite()||Sp.getRatioChildren().isNaN()){
                    Sp.setRatioChildren(null);
                }
                if(Sp.getRatioSpec().isInfinite()||Sp.getRatioSpec().isNaN()){
                    Sp.setRatioSpec(null);
                }
                if (Sp.getPkItemsp() != null) {
                    Sp.setPkItem(pkitem);
                    DataBaseHelper.updateBeanByPk(Sp, false);
                } else {
                    Sp.setPkItem(pkitem);
                    DataBaseHelper.insertBean(Sp);
                }
            }
        }
        /**
         * 新增或修改记费策略
         */
        if (itemCgdivs != null && itemCgdivs.size() > 0) {
            for (CgdivItemVo cgdivItemVo : itemCgdivs) {
                BdHpCgdivItem cgdiv = new BdHpCgdivItem();
                ApplicationUtils.copyProperties(cgdiv, cgdivItemVo);
                cgdiv.setPkItem(pkitem);
                cgdiv.setFlagPd("0");
                if (cgdiv.getPkHpcgdivitem() != null) {
                    DataBaseHelper.updateBeanByPk(cgdiv, false);
                } else {
                    DataBaseHelper.insertBean(cgdiv);
                }

            }
        }

        /**
         * 全删再恢复的方式的弊端： 未做修改的记录也更新了Modifier等字段，导致修改记录抹除。
         * 因此不再使用此种方式进行数据的删除操作 ，而是将修改的记录和删除的记录分开传入后台。
         * huanghaisheng
         *

         String delsql = "update bd_item_price set del_flag = '1' where pk_item = ?";
         if ("1".equals(isAll)) {// 所有的
         // 先全删再恢复的方式（软删除）
         DataBaseHelper.update(delsql, new Object[] { pkitem });
         } else if ("0".equals(isAll)) {// 本机构的
         delsql += " and pk_org = ?";
         // 先全删再恢复的方式（软删除）
         DataBaseHelper.update(delsql, new Object[] { pkitem, pkOrg });
         }
         */

        if (itemPricesDelPk != null && itemPricesDelPk.size() != 0) {
            DataBaseHelper.batchUpdate("update bd_item_price set del_flag = '1',FLAG_STOP='1' where pk_itemprice= :pkItemprice", itemPricesDelPk);
        }
        // 机构或全局下对价格统一处理
        if (itemPrices != null && itemPrices.size() != 0) {

            for (BdItemPrice price : itemPrices) {

                if (price.getPkItemprice() != null) {
                    //price.setDelFlag("0");// 恢复
                    if ("1".equals(price.getFlagModify())) {
                        price.setModifier(StringUtils.isEmpty(u.getNameEmp()) ? u.getLoginName() : u.getNameEmp());
                        price.setPkItem(pkitem);
                        DataBaseHelper.updateBeanByPk(price, false);
                    }
                } else {
                    price.setModifier(StringUtils.isEmpty(u.getNameEmp()) ? u.getLoginName() : u.getNameEmp());
                    price.setPkItem(pkitem);
                    DataBaseHelper.insertBean(price);
                }
            }
        }
        
        
        /**
         * 更新医嘱关联收费项目的价格
         */
        //获取关联收费项目的医嘱信息
        if ("1".equals(item.getFlagActive()))
        {
        	//收费项目启用时
        	//启用bd_ord_item表的收费项目
        	DataBaseHelper.update("update bd_ord_item set DEL_FLAG ='0' where PK_ITEM =  ? ",new Object[]{item.getPkItem()});
        }
                
        List<BdOrdOrg> bdOrdOrgList = DataBaseHelper.queryForList(" select ord.pk_ord from bd_ord_org ord where ord.del_flag = '0' and EXISTS(select (1) from bd_ord_item item "
        		+ " where item.pk_ord=ord.pk_ord and item.del_flag = '0' and item.pk_item = ? ) and ord.pk_org = ? group by ord.pk_ord ", BdOrdOrg.class, new Object[]{pkitem,pkOrg});
        for (BdOrdOrg bdOrdOrg : bdOrdOrgList) {
            //参考价格
            double price = 0.0;
            //儿童价格
            double priceChd = 0.0;
            //特诊价格
            double priceVip = 0.0;
            if ("0".equals(item.getFlagActive())) {
            	//收费项目不启用时
            	Integer count = DataBaseHelper.queryForScalar(" select count(1) from bd_ord_item oi " + 
            			" left outer join bd_item item on oi.pk_item=item.pk_item and oi.flag_pd='0' " + 
            			" where oi.del_flag = '0' and item.flag_active ='1' and oi.pk_ord = ? ",Integer.class, new Object[]{bdOrdOrg.getPkOrd()});
            	if(count == 0){
            		DataBaseHelper.update("update bd_ord set FLAG_ACTIVE ='0' where PK_ORD =  ? ",new Object[]{bdOrdOrg.getPkOrd()});
            	}
            	//停用bd_ord_item表的收费项目
            	DataBaseHelper.update("update bd_ord_item set DEL_FLAG ='1' where PK_ITEM =  ? ",new Object[]{item.getPkItem()});
    		} 
            //获取医嘱项目关联的更新后的收费项目
            List<BdOrdItemExt> bdOrdItemExts = this.srvMapper.getBdOrdItemsByOrd(bdOrdOrg.getPkOrd(),pkOrg);
            //计算特殊定价.记费策略
            for (BdOrdItemExt itemExt : bdOrdItemExts) { 
            	price += itemExt.getQuan() *  itemExt.getPrice();
            	if ("0".equals(itemExt.getEuCdmode()))
                {
            		priceChd  += itemExt.getRatioChildren() == 0 ? 0 : (itemExt.getPrice() * itemExt.getRatioChildren() + itemExt.getPrice());
                }
                else if ("1".equals(itemExt.getEuCdmode()))
                {
                	priceChd += itemExt.getAmountChildren() == 0 ? 0 : (itemExt.getAmountChildren() + itemExt.getPrice());
                }else if ("2".equals(itemExt.getEuCdmode()))
                {
                	priceChd += itemExt.getAmountChildren() == 0 ? 0 : itemExt.getAmountChildren();
                }
                if ("0".equals(itemExt.getEuSpmode()))
                {
                	priceVip += itemExt.getRatioSpec() == 0 ? 0 : (itemExt.getPrice() * itemExt.getRatioSpec() + itemExt.getPrice());
                }
                else if ("1".equals(itemExt.getEuSpmode()))
                {
                	priceVip += itemExt.getAmountSpec() == 0 ? 0 : (itemExt.getAmountSpec() + itemExt.getPrice());
                }
			}
        	bdOrdOrg.setPrice(price);
        	bdOrdOrg.setPriceChd(priceChd);
        	bdOrdOrg.setPriceVip(priceVip);
        	bdOrdOrg.setModifier(StringUtils.isEmpty(u.getNameEmp()) ? u.getLoginName() : u.getNameEmp());     
        	DataBaseHelper.updateBeanByPk(bdOrdOrg, false);
		}
        

        if ("1".equals(isAll)) {// 0--机构情况时，不处理医保类型和组套

            /** 新增或跟新医保类型 **/
            if (itemHps != null && itemHps.size() != 0) {
                // 校验医保信息（医保类别pk_hp）是否重复
				/*Map<String, String> pkHpMap = new HashMap<String, String>();
				int len = itemHps.size();
				for (int i = 0; i < len; i++) {
					String pkhp = itemHps.get(i).getPkHp();
					if (pkHpMap.containsKey(pkhp)) {
						throw new BusException("该收费项目下医保信息重复！");
					}
					pkHpMap.put(pkhp, itemHps.get(i).getPkItemhp());
				}*/

                // 先全删再恢复的方式（软删除）
                DataBaseHelper
                        .update("update bd_item_hp set del_flag = '1' where pk_item = ?",
                                new Object[]{pkitem});
                for (BdItemHp hp : itemHps) {
                    if (hp.getPkItemhp() != null) {
                        hp.setDelFlag("0");// 恢复
                        hp.setPkItem(pkitem);
                        hp.setEuItemType("0");
                        DataBaseHelper.updateBeanByPk(hp, false);
                    } else {
                        hp.setPkItem(pkitem);
                        hp.setEuItemType("0");
                        DataBaseHelper.insertBean(hp);
                    }
                }
            } else {
                DataBaseHelper
                        .update("update bd_item_hp set del_flag = '1' where pk_item = ?",
                                new Object[]{pkitem});
            }

            /** 新增或跟新收费组套 **/
            if (itemSets != null && itemSets.size() != 0) {
                // 先全删再恢复的方式（软删除）
                DataBaseHelper
                        .update("update bd_item_set set del_flag = '1' where pk_item = ?",
                                new Object[]{pkitem});
                List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
                Map<String, Object> paramMap = new HashMap<String, Object>();
                for (BdItemSet set : itemSets) {
                    if (set.getPkItemset() != null) {
                        set.setDelFlag("0");// 恢复
                        set.setPkItem(pkitem);
                        DataBaseHelper.updateBeanByPk(set, false);
                        set.setState(this.UpdateState);
                        updateList.add(MapUtils.objectToMap(set));
                        paramMap.put("STATUS", this.UpdateState);
                    } else {
                        set.setPkItem(pkitem);
                        DataBaseHelper.insertBean(set);
                        set.setState(this.AddState);
                        insertList.add(MapUtils.objectToMap(set));
                        paramMap.put("STATUS", this.AddState);
                    }

                }

                //发送信息至平台
                paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
                if (insertList != null && insertList.size() > 0) {
                    paramMap.put("itemSets", insertList);
                    PlatFormSendUtils.sendBdItemSetMsg(paramMap);
                }
                if (updateList != null && updateList.size() > 0) {
                    paramMap.put("itemSets", updateList);
                    PlatFormSendUtils.sendBdItemSetMsg(paramMap);
                }
                paramMap = null;

            } else {
                DataBaseHelper
                        .update("update bd_item_set set del_flag = '1' where pk_item = ?",
                                new Object[]{pkitem});
            }
        }

        return item.getPkItem();
    }

    /**
     * 交易号：001002007056
     * 获取收费项目扩展属性模板
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdItemAttrVo> qryBdItemAttrtemp(String param, IUser user) {
        String pkOrg = "";
        String userOrg = ((User) user).getPkOrg();
        if (!userOrg.equals("~                               "))
            pkOrg = userOrg;

        return srvMapper.getBdItemAttrtemp(pkOrg);
    }


    /**
     * 通过pkitem查询记费策略
     *
     * @param param
     * @param user
     * @return
     */
    public List<CgdivItemVo> qryCgdiv(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String pkItem = (String) map.get("pkItem");

        List<CgdivItemVo> qryCgDiv = srvMapper.qryCgDiv(pkItem);
        return qryCgDiv;
    }

    /**
     * 通过pkitem查询特殊定价
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdItemSp> qrySp(String param, IUser user) {
        String pkItem = JsonUtil.getFieldValue(param, "pkItem");
        List<BdItemSp> qrySp = srvMapper.qrySp(pkItem);
        return qrySp;
    }


    /**
     * 校验价格时间段的重复性（即校验价格是否冲突）
     *
     * @param itemPrices 前台所传价格（校验不包括停用的）
     * @return
     */
    private boolean isTimeRepeated(List<BdItemPrice> itemPrices) {
        boolean ret = false;
        for (int i = 0; i < itemPrices.size() - 1; i++) {
            BdItemPrice price_i = itemPrices.get(i);
            for (int j = i + 1; j < itemPrices.size(); j++) {
                BdItemPrice price_j = itemPrices.get(j);
                // 价格相同的才比较，不同的直接跳过
                if (price_j.getPrice() == price_i.getPrice()
                        && price_j.getEuPricetype().equals(
                        price_i.getEuPricetype())
                        && price_j.getPkOrg().equals(price_i.getPkOrg())) {
                    if ((price_j.getDateBegin().before(price_i.getDateEnd()) || price_j
                            .getDateBegin().equals(price_i.getDateEnd()))
                            && (price_j.getDateEnd().after(
                            price_i.getDateBegin()) || price_j
                            .getDateEnd()
                            .equals(price_i.getDateBegin()))) {
                        ret = true;
                    }
                }
            }

        }
        return ret;
    }

    /**
     * 删除收费项目 及 所有相关子表
     *
     * @param param
     * @param user
     * @throws Exception
     */
    public void delItemAll(String param, IUser user) throws Exception {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkItem = null;
        if (paramMap.get("pkItem") != null) {
            pkItem = paramMap.get("pkItem").toString();
        }

        Integer itemCount = DataBaseHelper.queryForScalar("SELECT count(1) FROM bd_ord_item WHERE PK_ITEM=? AND DEL_FLAG='0'",
                Integer.class, new Object[]{pkItem});

        Integer dtCount = DataBaseHelper.queryForScalar("SELECT count(1) FROM bl_ip_dt WHERE PK_ITEM=? AND DEL_FLAG='0'",
                Integer.class, new Object[]{pkItem});

        if (itemCount > 0 || dtCount > 0) {
            throw new BusException("收费字典已被引用，不能删除！");
        }

        DataBaseHelper.update(
                "update bd_item_hp set del_flag = '1' where pk_item = ?",
                new Object[]{pkItem});
        DataBaseHelper.update(
                "update bd_item_set set del_flag = '1' where pk_item = ?",
                new Object[]{pkItem});
        DataBaseHelper.update(
                "update bd_item_price set del_flag = '1' where pk_item = ?",
                new Object[]{pkItem});
        DataBaseHelper.update(
                "update bd_item_pv set del_flag = '1' where pk_item = ?",
                new Object[]{pkItem});
        DataBaseHelper.update(
                "delete from bd_daycg_set_item where pk_item = ?",
                new Object[]{pkItem});
        DataBaseHelper.update(
                "update bd_item set del_flag = '1' where pk_item = ?",
                new Object[]{pkItem});

        // 发生消息给集成平台
        BdItem bdItem = DataBaseHelper
                .queryForBean("select * from bd_item where pk_item = ?",
                        BdItem.class, pkItem);
        List<BdItemSet> bdItemSetsList = DataBaseHelper.queryForList(
                "select * from bd_item_set where pk_item = ?", BdItemSet.class,
                pkItem);

        //发送信息至平台
        bdItem.setState(this.DelState);
        Map<String, Object> sendParam = new HashMap<String, Object>();
        sendParam.put("item", MapUtils.objectToMap(bdItem));
        sendParam.put("STATUS", this.DelState);
        sendParam.put("codeEmp", UserContext.getUser().getCodeEmp());
        PlatFormSendUtils.sendBdItemMsg(sendParam);
        sendParam = null;
        List<Map<String, Object>> delList = new ArrayList<Map<String, Object>>();
        for (BdItemSet bdItemSet : bdItemSetsList) {
            bdItemSet.setState(this.DelState);
            delList.add(MapUtils.objectToMap(bdItemSet));
        }
        if (delList != null && delList.size() > 0) {
            sendParam = new HashMap<String, Object>();
            sendParam.put("itemSets", delList);
            PlatFormSendUtils.sendBdItemSetMsg(sendParam);
        }
        sendParam = null;
    }

    /**
     * 保存医嘱类型定义
     *
     * @param param
     * @param user
     * @return
     */
    public BdOrdtype saveBdOrdtype(String param, IUser user) {
        BdOrdtypeAndPkTempPrtVo ordtypeAndPk = JsonUtil.readValue(param, BdOrdtypeAndPkTempPrtVo.class);
        BdOrdtype ordtype = new BdOrdtype();
        ApplicationUtils.copyProperties(ordtype, ordtypeAndPk);

        if (ordtype.getPkOrdtype() == null) {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_ordtype "
                            + "where del_flag = '0' and code = ?",
                    Integer.class, ordtype.getCode());
            int count_name = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_ordtype "
                            + "where del_flag = '0' and name = ?",
                    Integer.class, ordtype.getName());
            if (count_code == 0 && count_name == 0) {
                ordtype.setPkOrg("~");
                DataBaseHelper.insertBean(ordtype);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("医嘱类型编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("医嘱类型名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("医嘱类型编码和名称都重复！");
                }
            }
        } else {
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_ordtype "
                                    + "where del_flag = '0' and code = ? and pk_ordtype != ?",
                            Integer.class, ordtype.getCode(),
                            ordtype.getPkOrdtype());
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_ordtype "
                                    + "where del_flag = '0' and name = ? and pk_ordtype != ?",
                            Integer.class, ordtype.getName(),
                            ordtype.getPkOrdtype());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.updateBeanByPk(ordtype, false);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("医嘱类型编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("医嘱类型名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("医嘱类型编码和名称都重复！");
                }
            }
        }

        return ordtype;

    }

    /**
     * 删除医嘱类型
     *
     * @param param
     * @param user
     */
    public void delBdOrdtype(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String pkOrdtype = null;
        if (paramMap.get("pkOrdtype") != null) {
            pkOrdtype = paramMap.get("pkOrdtype").toString();
        }

        if (srvMapper.countBdOrd((String) paramMap.get("code")) > 0) {
            throw new BusException("医嘱类型被医嘱项目字典引用！");
        } else if (srvMapper.countCnOrder((String) paramMap.get("code")) > 0) {
            throw new BusException("医嘱类型被医嘱业务引用！");
        } else {
            srvMapper.delBdOrdtype(pkOrdtype);
        }
    }

    /**
     * 删除收费项目自定义分类
     *
     * @param param
     * @param user
     */
    public void delCateDef(String param, IUser user) {

        String pkCatedef = JsonUtil.getFieldValue(param, "pkCatedef");
        /** 校验是否被收费项目自定义分类--内容 引用 **/
        int count_cont = DataBaseHelper
                .queryForScalar(
                        "select count(1) from bd_cate_cont where del_flag = '0' and pk_catedef = ?",
                        Integer.class, pkCatedef);

        if (count_cont == 0) {
            DataBaseHelper
                    .update("update bd_cate_def set del_flag = '1' where pk_catedef = ?",
                            new Object[]{pkCatedef});
        } else {
            throw new BusException("收费项目自定义分类已被内容引用！");
        }
    }

    /**
     * 删除收费项目自定义分类--内容
     *
     * @param param
     * @param user
     */
    public void delCateCont(String param, IUser user) {

        String pkCateconf = JsonUtil.getFieldValue(param, "pkCateconf");

        DataBaseHelper.update(
                "update bd_cate_item set del_flag = '1' where pk_catecont = ?",
                new Object[]{pkCateconf});
        DataBaseHelper.update(
                "update bd_cate_cont set del_flag = '1' where pk_catecont = ?",
                new Object[]{pkCateconf});

    }

    /**
     * 保存收费项目自定义分类
     *
     * @param param
     * @param user
     */
    public void saveCateDef(String param, IUser user) {

        BdCateDef catedef = JsonUtil.readValue(param, BdCateDef.class);

        if (catedef.getPkCatedef() == null) {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_cate_def "
                            + "where del_flag = '0' and code = ?",
                    Integer.class, catedef.getCode());
            int count_name = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_cate_def "
                            + "where del_flag = '0' and name = ?",
                    Integer.class, catedef.getName());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.insertBean(catedef);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("收费项目自定义分类编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("收费项目自定义分类名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("收费项目自定义分类编码和名称都重复！");
                }
            }
        } else {
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_cate_def "
                                    + "where del_flag = '0' and code = ? and pk_catedef != ?",
                            Integer.class, catedef.getCode(),
                            catedef.getPkCatedef());
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_cate_def "
                                    + "where del_flag = '0' and name = ? and pk_catedef != ?",
                            Integer.class, catedef.getName(),
                            catedef.getPkCatedef());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.updateBeanByPk(catedef, false);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("收费项目自定义分类编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("收费项目自定义分类名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("收费项目自定义分类编码和名称都重复！");
                }
            }
        }

    }

    /**
     * 保存收费项目自定义分类内容 和 分类对照
     *
     * @param param
     * @param user
     */
    public void saveContAndCateItems(String param, IUser user) {
        ContAndCateItemsParam contAndCateItems = JsonUtil.readValue(param,
                ContAndCateItemsParam.class);
        BdCateCont contInfo = contAndCateItems.getContInfo();
        List<BdCateItem> cateItemList = contAndCateItems.getCateItemList();

        /** 保存收费项目自定义分类内容 **/
        if (contInfo.getPkCatecont() == null) {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_cate_cont "
                            + "where del_flag = '0' and code = ? and PK_CATEDEF=?",
                    Integer.class, contInfo.getCode(), contInfo.getPkCatedef());
            int count_name = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_cate_cont "
                            + "where del_flag = '0' and name = ? and PK_CATEDEF=?",
                    Integer.class, contInfo.getName(), contInfo.getPkCatedef());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.insertBean(contInfo);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("【收费项目自定义分类内容】编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("【收费项目自定义分类内容】名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("【收费项目自定义分类内容】编码和名称都重复！");
                }
            }
        } else {
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_cate_cont "
                                    + "where del_flag = '0' and code = ? and pk_catecont != ? and PK_CATEDEF=?",
                            Integer.class, contInfo.getCode(),
                            contInfo.getPkCatecont(), contInfo.getPkCatedef());
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_cate_cont "
                                    + "where del_flag = '0' and name = ? and pk_catecont != ? and PK_CATEDEF=?",
                            Integer.class, contInfo.getName(),
                            contInfo.getPkCatecont(), contInfo.getPkCatedef());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.updateBeanByPk(contInfo, false);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("【收费项目自定义分类内容】编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("【收费项目自定义分类内容】名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("【收费项目自定义分类内容】编码和名称都重复！");
                }
            }
        }

        /** 保存分类对照 **/
        if (cateItemList != null && cateItemList.size() != 0) {
            /** 校验重复性 **/
            Map<String, String> pkItemcateMap = new HashMap<String, String>();
            int len = cateItemList.size();
            for (int i = 0; i < len; i++) {
                String pkItemcate = cateItemList.get(i).getPkItemcate();
                if (pkItemcateMap.containsKey(pkItemcate)) {
                    throw new BusException("对照的收费项目基础分类字典重复！");
                }
                pkItemcateMap.put(pkItemcate, cateItemList.get(i)
                        .getPkCateitem());
            }

            // 先全删再恢复的方式（软删除）
            String pkCatecont = contInfo.getPkCatecont();
            DataBaseHelper
                    .update("update bd_cate_item set del_flag = '1' where pk_catecont = ?",
                            new Object[]{pkCatecont});
            for (BdCateItem cateitem : cateItemList) {
                if (cateitem.getPkCateitem() != null) {
                    cateitem.setDelFlag("0");// 恢复
                    cateitem.setPkCatecont(pkCatecont);
                    DataBaseHelper.updateBeanByPk(cateitem, false);
                } else {
                    cateitem.setPkCatecont(pkCatecont);
                    DataBaseHelper.insertBean(cateitem);
                }
            }
        } else {
            String pkCatecont = contInfo.getPkCatecont();
            DataBaseHelper
                    .update("update bd_cate_item set del_flag = '1' where pk_catecont = ?",
                            new Object[]{pkCatecont});
        }

    }

    /**
     * 删除财务核算项目以及收费对照
     *
     * @param param
     * @param user
     */
    public void delAuditAndItemcates(String param, IUser user) {
        String pkAudit = JsonUtil.getFieldValue(param, "pkAudit");
        DataBaseHelper
                .update("update bd_audit_itemcate set del_flag = '1' where pk_audit = ?",
                        new Object[]{pkAudit});
        DataBaseHelper.update(
                "update bd_audit set del_flag = '1' where pk_audit = ?",
                new Object[]{pkAudit});
    }

    /**
     * 保存财务核算项目信息及收费对照
     *
     * @param param
     * @param user
     */
    public void saveAuditAndItemCates(String param, IUser user) {

        AuditAndItemCatesParam auditAndItemCates = JsonUtil.readValue(param,
                AuditAndItemCatesParam.class);
        BdAudit auditInfo = auditAndItemCates.getAuditInfo();
        List<BdAuditItemcate> itemCateList = auditAndItemCates
                .getItemCateList();
        User u = UserContext.getUser();
        auditInfo.setPkOrg("~");
        /** 保存财务核算项目 **/
        if (auditInfo.getPkAudit() == null) {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_audit "
                            + "where del_flag = '0' and code = ?",
                    Integer.class, auditInfo.getCode());
            int count_name = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_audit "
                            + "where del_flag = '0' and name = ?",
                    Integer.class, auditInfo.getName());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.insertBean(auditInfo);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("【财务核算项目】编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("【财务核算项目】名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("【财务核算项目】编码和名称都重复！");
                }
            }
        } else {
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_audit "
                                    + "where del_flag = '0' and code = ? and pk_audit != ?",
                            Integer.class, auditInfo.getCode(),
                            auditInfo.getPkAudit());
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from bd_audit "
                                    + "where del_flag = '0' and name = ? and pk_audit != ?",
                            Integer.class, auditInfo.getName(),
                            auditInfo.getPkAudit());
            if (count_code == 0 && count_name == 0) {
                DataBaseHelper.updateBeanByPk(auditInfo, false);
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("【财务核算项目】编码重复！");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("【财务核算项目】名称重复！");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("【财务核算项目】编码和名称都重复！");
                }
            }
        }

        /** 保存核算项目与收费服务分类对照 **/
        if (itemCateList != null && itemCateList.size() != 0) {
            /** 校验重复性 **/
            Map<String, String> pkItemcateMap = new HashMap<String, String>();
            int len = itemCateList.size();
            for (int i = 0; i < len; i++) {
                String pkItemcate = itemCateList.get(i).getPkItemcate();
                if (pkItemcateMap.containsKey(pkItemcate)) {
                    throw new BusException("对照的收费项目基础分类字典重复！");
                }
                pkItemcateMap.put(pkItemcate, itemCateList.get(i)
                        .getPkAudititem());
            }

            // 先全删再恢复的方式（软删除）
            String pkAudit = auditInfo.getPkAudit();
            DataBaseHelper
                    .update("update bd_audit_itemcate set del_flag = '1' where pk_audit = ?",
                            new Object[]{pkAudit});
            for (BdAuditItemcate itemcate : itemCateList) {
                itemcate.setPkOrg("~");
                if (itemcate.getPkAudititem() != null) {
                    itemcate.setDelFlag("0");// 恢复
                    itemcate.setPkAudit(pkAudit);
                    DataBaseHelper.updateBeanByPk(itemcate, false);
                } else {
                    itemcate.setPkAudit(pkAudit);
                    DataBaseHelper.insertBean(itemcate);
                }
            }
        } else {
            String pkAudit = auditInfo.getPkAudit();
            DataBaseHelper
                    .update("update bd_audit_itemcate set del_flag = '1' where pk_audit = ?",
                            new Object[]{pkAudit});
        }
    }

    /**
     * 导入医嘱项目
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdOrd> importBdOrds(String param, IUser user) {

        List<BdOrd> importords = JsonUtil.readValue(param,
                new TypeReference<List<BdOrd>>() {
                });

        /**
         * 与数据库原有的校验编码的重复性，重复就去除
         */
        List<BdOrd> ords = this.srvMapper.findAllBdOrds();
        Iterator<BdOrd> it = importords.iterator();
        while (it.hasNext()) {
            BdOrd imord = it.next();
            for (BdOrd ord : ords) {
                if (ord.getCode().equals(imord.getCode())) {
                    it.remove();
                }
            }
        }
        /**
         * 统一保存进数据库
         */
        if (importords != null && importords.size() != 0) {
            for (BdOrd imord : importords) {
                imord.setFlagActive("1");
                imord.setFlagCg("1");
                imord.setFlagDr("1");
                imord.setFlagOp("1");
                imord.setFlagEr("1");
                imord.setFlagIp("1");
                imord.setFlagPe("1");
                imord.setFlagHm("1");
                imord.setPkOrd(NHISUUID.getKeyId());
                BdOrdAlias bdOrdAlias = new BdOrdAlias();
                bdOrdAlias.setPkOrd(imord.getPkOrd());
                bdOrdAlias.setPkOrg("~");
                bdOrdAlias.setPkOrdalia(NHISUUID.getKeyId());
                bdOrdAlias.setdCode(imord.getdCode());
                bdOrdAlias.setSpcode(imord.getSpcode());
                bdOrdAlias.setAlias(imord.getName());
                BdOrdItem bdOrdItem = new BdOrdItem();
                bdOrdItem.setPkOrditem(NHISUUID.getKeyId());
                bdOrdItem.setPkOrd(imord.getPkOrd());
                bdOrdItem.setPkOrg("~");
                bdOrdItem.setQuan(1);
                bdOrdItem.setSortno(0);
                bdOrdItem.setPkItem(imord.getPkItem());
                DataBaseHelper.insertBean(imord);
                DataBaseHelper.insertBean(bdOrdAlias);
                DataBaseHelper.insertBean(bdOrdItem);
            }

            return importords;
        } else {
            //return null;
            throw new BusException("要导入的医嘱项目在数据库已经存在相同数据！");
        }
    }

    /**
     * 保存常用医嘱项目
     *
     * @param param
     * @param user
     */
    public void addOrders(String param, IUser user) {
        List<BdOrdComm> list = JsonUtil.readValue(param,
                new TypeReference<List<BdOrdComm>>() {
                });
        for (BdOrdComm bdOrdComm : list) {
            bdOrdComm.setDelFlag(EnumerateParameter.ZERO);
            int num = DataBaseHelper.insertBean(bdOrdComm);
            if (num == 0) {
                throw new BusException("保存失败");
            }
        }

    }

    /**
     * 保存常用医嘱（全删全插）
     * 001002007055
     *
     * @param param
     * @param user
     */
    public void updateOrders(String param, IUser user) {
        List<BdOrdComm> list = JsonUtil.readValue(param,
                new TypeReference<List<BdOrdComm>>() {
                });
        for (BdOrdComm bdOrdComm : list) {
            if (bdOrdComm.getPkOrdcomm() == null) {
                bdOrdComm.setDelFlag(EnumerateParameter.ZERO);
                bdOrdComm.setPkOrdcomm(NHISUUID.getKeyId());
                DataBaseHelper.insertBean(bdOrdComm);
            } else {
                DataBaseHelper.updateBeanByPk(bdOrdComm, false);
            }
        }
    }

    /**
     * 删除常用医嘱项目
     *
     * @param param
     * @param user
     */
    public void deleteOrders(String param, IUser user) {
        List<BdOrdComm> list = JsonUtil.readValue(param,
                new TypeReference<List<BdOrdComm>>() {
                });
        String sql = " update bd_ord_comm set del_flag = '1' where pk_ordcomm = ? ";
        for (BdOrdComm bdOrdComm : list) {
            int num = DataBaseHelper.update(sql,
                    new Object[]{bdOrdComm.getPkOrdcomm()});
            if (num == 0) {
                throw new BusException("删除失败");
            }
        }

    }

    /**
     * @param param
     * @param user
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<BdItemVO> getBdItemList(String param, IUser user)
            throws IllegalAccessException, InvocationTargetException {
        String sql = "select i.*,u.name as unitname from bd_item i left join bd_unit u on i.pk_unit = u.pk_unit where i.del_flag = '0' ";
        BdItem bi = JsonUtil.readValue(param, BdItem.class);

        if (!StringUtils.isEmpty(bi.getCode()))
            sql += " and i.code like '%" + bi.getCode() + "%' ";
        if (!StringUtils.isEmpty(bi.getName()))
            sql += " and  i.name like '%" + bi.getName() + "%' ";
        if (!StringUtils.isEmpty(bi.getPkItemcate()))
            sql += " and  i.pk_itemcate='" + bi.getPkItemcate() + "'";
        if (!StringUtils.isEmpty(bi.getSpcode()))
            sql += " and (i.spcode like '%" + bi.getSpcode().toLowerCase()
                    + "%' or i.spcode like '%" + bi.getSpcode().toUpperCase()
                    + "%')";

        List<Map<String, Object>> ll = DataBaseHelper.queryForList(sql);
        List<BdItemVO> ret = new ArrayList<BdItemVO>();
        ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);

        for (Map m : ll) {
            BdItemVO vo = new BdItemVO();
            org.apache.commons.beanutils.BeanUtils.copyProperties(vo, m);

            ret.add(vo);
        }

        return ret;

    }


    public String qryDefault(String param, IUser user) {
        String code = null;
        List<Map<String, Object>> list = DataBaseHelper.queryForList("select code from bd_defdoc where CODE_DEFDOCLIST = '030000' and flag_def = '1'");
        if (list.size() > 0) {
            code = (String) list.get(0).get("code");
        }
        return code;
    }

    public void delSpList(String param, IUser user) {
        String pkItemsp = JsonUtil.getFieldValue(param, "pkItemsp");
        srvMapper.delSp(pkItemsp);
    }


    /**
     * 交易号：001002007063
     * 保存记费策略模板
     *
     * @param param
     * @param user
     */
    public void saveCgDivTmp(String param, IUser user) {
        String cgdivTmps = JsonUtil.getJsonNode(param, "cgdivTmps").toString();
        List<BdHpCgdivTmp> cgdivTmpList = JsonUtil.readValue(cgdivTmps, new TypeReference<List<BdHpCgdivTmp>>() {
        });
        if (cgdivTmpList == null || cgdivTmpList.size() <= 0) return;

        String status = JsonUtil.getFieldValue(param, "status");
        if ("INSERT".equals(status)) {//校验模板编码和模板名称
            String sql = "select count(1) from bd_hp_cgdiv_tmp where del_flag='0' and code_tmp=?";
            int count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{cgdivTmpList.get(0).getCodeTmp()});
            if (count > 0) {
                throw new BusException("模板编码重复！");
            }
            sql = "select count(1) from bd_hp_cgdiv_tmp where del_flag='0' and name_tmp=?";
            count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{cgdivTmpList.get(0).getNameTmp()});
            if (count > 0) {
                throw new BusException("模板名称重复！");
            }
        }
        //删除同一模板编码的所有记费策略模板
        String delSql = "delete from bd_hp_cgdiv_tmp where code_tmp=?";
        DataBaseHelper.execute(delSql, new Object[]{cgdivTmpList.get(0).getCodeTmp()});

        User doUser = (User) user;
        //重新保存当前模板编码的记费策略
        for (int i = 0; i < cgdivTmpList.size(); i++) {
            cgdivTmpList.get(i).setPkHpcgdivtmp(NHISUUID.getKeyId());
            cgdivTmpList.get(i).setPkOrg("~                               ");
            cgdivTmpList.get(i).setCreator(doUser.getId());
            cgdivTmpList.get(i).setTs(new Date());
            cgdivTmpList.get(i).setCreateTime(new Date());
            cgdivTmpList.get(i).setDelFlag("0");
        }

        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdHpCgdivTmp.class), cgdivTmpList);
    }


    /**
     * 交易号：001002007064
     * 查询记费策略模板
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdHpCgdivTmp> qryCgDivTmp(String param, IUser user) {
        return srvMapper.qryCgdivTmp();
    }

    /**
     * 交易号:001002007065
     * 查询记费策略模板明细
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryCgDivTmpDt(String param, IUser user) {
        String codeTmp = JsonUtil.getFieldValue(param, "codeTmp");
        if (codeTmp == null || "".equals(codeTmp)) return null;

        return srvMapper.qryCgDivTmpDt(codeTmp);
    }

    /**
     * 交易号：001002007066
     * 删除记费模板
     *
     * @param param
     * @param user
     */
    public void delCgDivTmp(String param, IUser user) {
        String codeTmp = JsonUtil.getFieldValue(param, "codeTmp");
        if (codeTmp == null || "".equals(codeTmp)) return;
        String delSql = "delete from bd_hp_cgdiv_tmp where code_tmp=?";
        DataBaseHelper.execute(delSql, new Object[]{codeTmp});
    }


    /**
     * 001002007067
     * 保存记费策略数据
     *
     * @param param
     * @param user
     */
    public void saveHpCgDiv(String param, IUser user) {
        List<BdHpCgdivItem> itemList = JsonUtil.readValue(param, new TypeReference<List<BdHpCgdivItem>>() {
        });
        if (itemList == null || itemList.size() <= 0) return;

        String pkItem = itemList.get(0).getPkItem();
        String delSql = "delete from bd_hp_cgdiv_item where del_flag='0' and pk_item=?";
        DataBaseHelper.execute(delSql, new Object[]{pkItem});

        for (int i = 0; i < itemList.size(); i++) {
            itemList.get(i).setPkHpcgdivitem(NHISUUID.getKeyId());
            itemList.get(i).setFlagPd("0");
            itemList.get(i).setDelFlag("0");
            itemList.get(i).setPkOrg("~                               ");//机构
            itemList.get(i).setTs(new Date());
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdHpCgdivItem.class), itemList);
    }


    /**
     * 001002007072
     * 收费项目导出查询
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> queryByExcel(String param, IUser user) {
        Map<String, String> map = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = srvMapper.queryByExcel(map);
        return list;
    }

    /**
     * 交易码：001002007095
     * 查询收费项目
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> getChargeItemList(String param, IUser user) {
//		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        JsonNode jsonNode = JsonUtil.readTree(param);
        JsonNode paramNode = jsonNode.get("param");
        Map<String, Object> map = JsonUtil.readValue(paramNode, Map.class);
        List<Map<String, Object>> result = srvMapper.getChargeItemList(map);

        return result;
    }

    /**
     * 收费项目-新增价格页签时获取价格配置中价格类型：默认取全自费
     * 001002007097
     *
     * @param param
     * @param user
     * @return
     */
    public String getPriceCfgByHp(String param, IUser user) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pricfg.eu_pricetype from bd_pricetype_cfg pricfg");
        sql.append(" inner join bd_hp hp on hp.pk_hp=pricfg.pk_hp");
        sql.append(" where hp.eu_hptype='0' and hp.del_flag='0' and pricfg.del_flag='0'");
        List<Map<String, Object>> resList = DataBaseHelper.queryForList(sql.toString(), new Object[]{});
        if (resList != null && resList.size() > 0 && resList.get(0) != null && resList.get(0).get("euPricetype") != null) {
            return resList.get(0).get("euPricetype").toString();
        } else {
            return "";
        }
    }

    /**
     * 根据收费项目编码反查医嘱项目
     * SrvService.findOrderByItemCode
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdOrdAndPkDeptVo> findOrderByItemCode(String param, IUser user) {
        //String chargeItemCode = JsonUtil.getFieldValue(param, "chargeItemCode");
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String codeOrdtype = CommonUtils.getString(paramMap.get("codeOrdtype"));
        String code = CommonUtils.getString(paramMap.get("code"));
        String name = CommonUtils.getString(paramMap.get("name"));
        String dtOrdType = CommonUtils.getString(paramMap.get("dtOrdType"));
        String codeExt = CommonUtils.getString(paramMap.get("codeExt") );
        String dtContype = CommonUtils.getString(paramMap.get("dtContype"));
        String chargeItemCode = CommonUtils.getString(paramMap.get("chargeItemCode"));
        String pkOrg = CommonUtils.getString(paramMap.get("pkOrg"));
        List<BdOrdAndPkDeptVo> result = srvMapper.findOrderByItemCode(codeOrdtype,code,name,dtOrdType,codeExt,dtContype,chargeItemCode,pkOrg);
        return result;
    }

    /**
     * 批量修改-获取收费条目数
     *
     * @param param
     * @param user
     * @return
     */
    public String getBditemOld(String param, IUser user) {
        String pkItem = JsonUtil.getFieldValue(param, "pkItem");
        String counts = srvMapper.getBdItemCount(pkItem);
        return counts;
    }

    /**
     * 批量修改-保存关联条目数
     *
     * @param param
     * @param user
     */
    public void SaveItemOld(String param, IUser user) {
        List<BdOrdItemOldVo> bdOrdOrgVos = JsonUtil.readValue(param,
                new TypeReference<List<BdOrdItemOldVo>>() {
                });
        for (BdOrdItemOldVo bdOrdOrgVo : bdOrdOrgVos) {
            srvMapper.SaveItemOld(bdOrdOrgVo);
        }
    }

    /**
     * 恢复删除的收费项目
     * 001002007103
     *
     * @param param
     * @param user
     * @return
     */
    public int restoreChargeItem(String param, IUser user) {
        String pkItem = JsonUtil.getFieldValue(param, "pkItem");
        return srvMapper.restoreChargeItem(pkItem);
    }


    /**
     * 根据分类节点查找所有项目（含子节点）
     * SrvService.getBdItemByItemCate
     * 001002007107
     *
     * @return
     */
    public List<Map<String, Object>> getBdItemByItemCate(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String dbType = MultiDataSource.getCurDbType();
        map.put("dbType", dbType);
        List<Map<String, Object>> result = srvMapper.getBdItemByItemCate(map);
        return result;
    }


    /**
     * 根据医嘱项目查询对应收费项目
     * SrvService.findOrderPrice
     * 001002007108
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> findOrderPrice(String param, IUser user) {
        List<String> pkOrders = JsonUtil.readValue(param, List.class);
        List<Map<String, Object>> result;
        if (Application.isSqlServer()) {
            result = srvMapper.findOrderPriceSqlSer(pkOrders);
        } else {
            result = srvMapper.findOrderPrice(pkOrders);
        }

        return result;
    }
    
    /**
     * 根据医嘱项目查询对应收费项目
     * SrvService.findOrderPrice
     * 001002007108
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> findOrderSumPrice(String param, IUser user) {
        List<String> pkOrders = JsonUtil.readValue(param, List.class);
        List<Map<String, Object>> result;
        result = srvMapper.findOrderSumPrice(pkOrders);
        return result;
    }
    

    
    

    /**
     * 更新医嘱价格
     * SrvService.updateOrderPrice
     * 001002007109
     *
     * @param param
     * @param user
     * @return
     */
    public int updateOrderPrice(String param, IUser user) {
        int result = 0;
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        Date dateNow = new Date();
        map.put("dateNow", dateNow);
        //获取三项数据price，price_chd，price_vip
        result = updateOrdPrice(map,true);
        return result;
    }

    /**
     * 编辑收费项目同时更新医嘱项目价格
     * SrvService.updateOrderPriceByItem
     * 001002007110
     *
     * @return
     */
    public int updateOrderPriceByItem(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String pkOrg = "";
        int result = 0;
        if (map.containsKey("pkOrg")) {
            if (map.get("pkOrg") != null) {
                if (StringUtils.isNotEmpty(map.get("pkOrg").toString())) {
                    pkOrg = map.get("pkOrg").toString();
                }
            }
        }
        List<Map<String, Object>> ords = srvMapper.getOrdByPkItem(map);
        for (int i = 0; i < ords.size(); i++) {
            Map<String, Object> mParam = new HashMap<>();
            if (ords.get(i) != null && ords.get(i).containsKey("pkOrd")) {
                if (ords.get(i).get("pkOrd") != null && StringUtils.isNotEmpty(ords.get(i).get("pkOrd").toString())) {
                    mParam.put("pkOrd", ords.get(i).get("pkOrd").toString());
                    mParam.put("pkOrg", pkOrg);
                    mParam.put("dateNow", new Date());
                    result += updateOrdPrice(mParam,true);
                }
            }
        }
        return result;
    }



     /**
     * 按加收模式计算医嘱项目价格
     * @param map
     * @param isSpecial
     * @return
     */
    private int updateOrdPrice(Map<String,Object> map,boolean isSpecial){
        int result = 0;
        //获取医嘱项目下所有收费项目
        if(map.containsKey("pkOrd")&&map.get("pkOrd") != null){
            String pkOrd = map.get("pkOrd").toString();
            String pkOrg = map.get("pkOrg").toString();
            List<BdOrdItem> bdItemList = srvMapper.getBdItemByPkOrd(pkOrd);
            //医嘱项目收费价格
            double priceOrd = 0;
            //医嘱项目儿童加收总价
            double childOrd = 0;
            //医嘱项目特殊定价总价
            double special = 0;
            if(bdItemList.size()>0){
                for (int i = 0; i < bdItemList.size(); i++) {
                    if(bdItemList.get(i)!=null){
                        String pkItem = bdItemList.get(i).getPkItem();
                        //收费项目参考价格
                        double priceItem = 0;
                        if("0".equals(bdItemList.get(i).getFlagPd())){
                            List<String> orgPkList = new ArrayList<String>();
                            orgPkList.add(pkOrg);
                            List<BdItemPrice> bdItemPriceByItem = srvMapper.getBdItemPriceByItem(pkItem, orgPkList);
                            if(bdItemPriceByItem != null && bdItemPriceByItem.size() > 0) {
                            	for (BdItemPrice bdItemPrice : bdItemPriceByItem) {
                            	  if (DateUtils.belongCalendar(new Date(),bdItemPrice.getDateBegin(),bdItemPrice.getDateEnd()))
                                  {
                                      priceItem = priceItem + bdItemPrice.getPrice();
                                      break;
                                  }
								}
                            }
                        }else{
                            BdPd bdPd = srvMapper.getBdPdByPk(pkItem);
                            if(bdPd != null){
                                priceItem = bdPd.getPrice() / bdPd.getPackSize();
                            }
                        }

                        //收费项目下的儿童加收总金额
                        double chileItem = 0;
                        //收费项目下的特殊加收总金额
                        double specialItem = 0;
                        //查询特殊定价价格
                        List<BdItemSp> bdItemSpList = srvMapper.getBdItemSpByItem(pkItem);
                        if(bdItemSpList.size()>0){
                            for (int j = 0; j < bdItemSpList.size(); j++) {
                                if(bdItemSpList.get(j)!=null){
                                    //儿童模式
                                    String childType = bdItemSpList.get(j).getEuCdmode();
                                    //特殊模式
                                    String specialType = bdItemSpList.get(j).getEuSpmode();
                                    if("3".equals(bdItemSpList.get(j).getEuPvtype())) {
                                        //就诊类型为住院
                                        //计算儿童加收价格
                                        if ("0".equals(bdItemSpList.get(j).getEuCdmode())) {
                                            //儿童按比例
                                            chileItem += priceItem * (1 + bdItemSpList.get(j).getRatioChildren());
                                        } else if ("1".equals(bdItemSpList.get(j).getEuCdmode())) {
                                            //儿童按金额
                                            chileItem += priceItem + bdItemSpList.get(j).getAmountChildren();
                                        } else if ("2".equals(bdItemSpList.get(j).getEuCdmode())) {
                                            //儿童按固定金额
                                            chileItem += bdItemSpList.get(j).getAmountChildren();
                                        }
                                        //计算特殊加收价格
                                        if ("0".equals(bdItemSpList.get(j).getEuSpmode())) {
                                            //特殊按比例
                                            specialItem += priceItem * (1 + bdItemSpList.get(j).getRatioSpec());
                                        } else if ("1".equals(bdItemSpList.get(j).getEuSpmode())) {
                                            //特殊按金额
                                            specialItem += priceItem + bdItemSpList.get(j).getAmountSpec();
                                        }
                                    }
                                }
                            }
                        }

                        priceOrd += priceItem * bdItemList.get(i).getQuan();
                        if(chileItem == 0.0){
                            childOrd += chileItem * bdItemList.get(i).getQuan() + priceItem * bdItemList.get(i).getQuan();
                        }else{
                            childOrd += chileItem * bdItemList.get(i).getQuan();
                        }
                        if(specialItem == 0.0){
                            special += specialItem * bdItemList.get(i).getQuan()+ priceItem * bdItemList.get(i).getQuan();
                        }else{
                            special += specialItem * bdItemList.get(i).getQuan();
                        }
                    }
                }

                if(childOrd == 0.0){
                    childOrd = priceOrd;
                }

                if(special == 0.0){
                    special = priceOrd;
                }

                //更新bd_ord_org表
                Map<String, Object> mParam = new HashMap<>();
                mParam.put("pkOrd", pkOrd);
                mParam.put("pkOrg", pkOrg);
                mParam.put("price", priceOrd);
                mParam.put("priceChd",childOrd);
                mParam.put("priceVip", special);
                result = srvMapper.updateOrdOrgPrice(mParam);
            }
        }

        //判断收费项目是按比例模式还是按金额模式
        return result;
    }

    private int updateOrdPrice(Map<String, Object> map) {
        int result = 0;

        
        List<Map<String, Object>> updatePrices = srvMapper.getUpdateOrderPrice(map);
        if (updatePrices.size() > 0) {
            Map<String, Object> priceParam = updatePrices.get(0);
            if (priceParam != null) {
                if (map.containsKey("pkOrd") && map.containsKey("pkOrg")) {
                    if (map.get("pkOrd") != null && map.get("pkOrg") != null) {
                        String pkOrd = map.get("pkOrd").toString();
                        String pkOrg = map.get("pkOrg").toString();
                        if (StringUtils.isNotEmpty(pkOrd) && StringUtils.isNotEmpty(pkOrg)) {
                            Map<String, Object> mParam = new HashMap<>();
                            mParam.put("pkOrd", pkOrd);
                            mParam.put("pkOrg", pkOrg);
                            if (priceParam.containsKey("price") && priceParam.containsKey("priceChd") && priceParam.containsKey("priceVip")) {
                                if (priceParam.get("price") != null && StringUtils.isNoneEmpty(priceParam.get("price").toString())) {
                                    mParam.put("price", priceParam.get("price").toString());
                                }
                                if (priceParam.get("priceChd") != null && StringUtils.isNoneEmpty(priceParam.get("priceChd").toString())) {
                                    mParam.put("priceChd", priceParam.get("priceChd").toString());
                                }
                                if (priceParam.get("priceVip") != null && StringUtils.isNoneEmpty(priceParam.get("priceVip").toString())) {
                                    mParam.put("priceVip", priceParam.get("priceVip").toString());
                                }
                                result = srvMapper.updateOrdOrgPrice(mParam);
                            }

                        }
                    }

                }

            }

        }
        return result;
    }

    /**
     * 收费项目是否被医嘱项目引用
     * SrvService.chargeItemBeenUsed
	 * 001002007111
     * @return
     */
    public String chargeItemBeenUsed(String param, IUser user) {
        String result = "0";
		BdItemAndHpSetPricesParam itemAndHpSetPrices = JsonUtil.readValue(
				param, BdItemAndHpSetPricesParam.class);
		if(itemAndHpSetPrices!=null){
            BdItem item = itemAndHpSetPrices.getItem();
            if(item!=null){
                //停用状态
                //判断收费项目是否被医嘱项目引用
                //如果为停用状态，且被医嘱项目引用。抛出异常，该项目已被引用，不可停用
                if (StringUtils.isNotEmpty(item.getPkItem())) {
                    Integer itemCount = DataBaseHelper.queryForScalar("SELECT count(1) FROM bd_ord_item WHERE PK_ITEM=? AND DEL_FLAG='0'",
                            Integer.class, new Object[]{item.getPkItem()});

                    Integer dtCount = DataBaseHelper.queryForScalar("SELECT count(1) FROM bl_ip_dt WHERE PK_ITEM=? AND DEL_FLAG='0'",
                            Integer.class, new Object[]{item.getPkItem()});

                    if (itemCount > 0 || dtCount > 0) {
                        result = "1";
                    }
                }
            }
        }
        return result;
    }


    /**
     * 001002007112
     * 查询打印医嘱项目全局信息
     * @param param
     * @param user
     * @return
     */
    public  List<PrintItemInfoVo>  queryPrintItemInfo(String param, IUser user){
        Map paramMap = JsonUtil.readValue(param, Map.class);
        ParamBdOrdVo paramBdOrdVo = new ParamBdOrdVo();
        if (paramMap.get("codeOrdtype") != null) {
            paramBdOrdVo.setCodeOrdtype(paramMap.get("codeOrdtype").toString());
        }
        if (paramMap.get("code") != null) {
            paramBdOrdVo.setCode(paramMap.get("code").toString());
        }
        if (paramMap.get("name") != null) {
            paramBdOrdVo.setName(paramMap.get("name").toString());
        }
        if (paramMap.get("pkOrd") != null) {
            paramBdOrdVo.setPkOrd(paramMap.get("pkOrd").toString());
        }
        if (paramMap.get("spcode") != null) {
            paramBdOrdVo.setSpcode(paramMap.get("spcode").toString());
        }
        if (paramMap.get("dtOrdType") != null) {
            paramBdOrdVo.setDtOrdCate(paramMap.get("dtOrdType").toString());
        }
        if (paramMap.get("pkDept") != null) {
            paramBdOrdVo.setPkDept(paramMap.get("pkDept").toString());
        }

        if (paramMap.get("codeExt") != null) {
            paramBdOrdVo.setCodeExt(paramMap.get("codeExt").toString());
        }
        if (paramMap.get("dtContype") != null) {
            paramBdOrdVo.setDtContype(paramMap.get("dtContype").toString());
        }
        if (paramMap.get("delFlag") != null) {
            paramBdOrdVo.setDelFlag(paramMap.get("delFlag").toString());
        }


        List<PrintItemInfoVo>  printItemInfoList = srvMapper.getPrintItemInfo(paramBdOrdVo);
        List<PrintItemInfoVo>  printItemInfoGroup = srvMapper.getPrintItemInfoGroupOrdName(paramBdOrdVo);
        for (PrintItemInfoVo p:printItemInfoList) {
            for (PrintItemInfoVo g:printItemInfoGroup) {
                if(p.getOrdName().equals(g.getOrdName())){
                    p.setCountPrice(g.getCountPrice());
                }
            }
        }


        return printItemInfoList;
    }
    
    /**
	 * 根据收费项目主键查询修改日志
	 * 交易号：001002007113
	 * @param param 收费项目主键,json格式{"pkItem":"abc"}
	 * @param user
	 * @return 修改日志记录
	 */
	public List<Map<String, Object>> qrySysAppLogByPkItem(String param,IUser user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkItem = (String)paramMap.get("pkItem");
		String sql = "SELECT date_op as modify_time,name_emp_op as oper_emp_name,custip as custom_ip,objname as obj_name,content,content_bf as content_new FROM SYS_APPLOG WHERE pk_obj = ? AND objname = 'bd_item' order by date_op desc";
		List<Map<String, Object>> mapItemList = DataBaseHelper.queryForList(sql, new Object[]{pkItem});
		/*if(mapItemList != null && mapItemList.size()>0){
			for(Map<String, Object> mapItem : mapItemList){
				Date modifyTime = (Date)mapItem.get("modifyTime");
				String operEmpName = (String)mapItem.get("operEmpName");
				String customIp = (String)mapItem.get("customIp");
				String ObjName = (String)mapItem.get("objName");
				String content = (String)mapItem.get("content");
				String contentNew = (String)mapItem.get("contentNew");
			}
		}*/
		
		return mapItemList;		
	}

}
