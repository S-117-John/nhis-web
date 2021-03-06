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
     * ????????????
     **/
    public static final String AddState = "_ADD";

    /**
     * ????????????
     */
    public static final String UpdateState = "_UPDATE";

    /**
     * ????????????
     */
    public static final String DelState = "_DELETE";

    @Autowired
    private SrvMapper srvMapper;


    //???????????????
    private String rleCode = "";

    /**
     * ?????????????????????????????????????????????
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
        bdOrdExclu.setPkOrg("~                               ");//????????????????????????
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
                    throw new BusException("????????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("????????????????????????????????????????????????");
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
                    throw new BusException("????????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("????????????????????????????????????????????????");
                }
            }
        }

        /** ????????????????????????????????? **/
        if (bdOrdExcluDtList != null && bdOrdExcluDtList.size() != 0) {
            /** ????????????????????????????????? */
            Map<String, String> nameOrdMap = new HashMap<String, String>();
            Map<String, String> codeOrdMap = new HashMap<String, String>();
            int len = bdOrdExcluDtList.size();
            for (int i = 0; i < len; i++) {
                String nameOrd = bdOrdExcluDtList.get(i).getNameOrd();
                String codeOrd = bdOrdExcluDtList.get(i).getCodeOrd();
                if (nameOrdMap.containsKey(nameOrd)) {
                    throw new BusException("???????????????????????????");
                }
                if (codeOrdMap.containsKey(codeOrd)) {
                    throw new BusException("???????????????????????????");
                }
                nameOrdMap.put(nameOrd, bdOrdExcluDtList.get(i).getPkExcludt());
                codeOrdMap.put(codeOrd, bdOrdExcluDtList.get(i).getPkExcludt());
            }

            // ??????????????????????????????????????????
            String pkexclu = bdOrdExclu.getPkExclu();
            DataBaseHelper
                    .update("update bd_ord_exclu_dt set del_flag = '1' where pk_exclu = ?",
                            new Object[]{pkexclu});
            for (BdOrdExcluDt excdt : bdOrdExcluDtList) {
                if (excdt.getPkExcludt() != null) {
                    excdt.setDelFlag("0");// ??????
                    excdt.setPkExclu(pkexclu);
                    DataBaseHelper.updateBeanByPk(excdt, false);
                } else {
                    excdt.setPkExclu(pkexclu);
                    DataBaseHelper.insertBean(excdt);
                }
                Map<String, String> map = new HashMap<>();
                if ("0".equals(bdOrdExclu.getEuExcType())) {//??????
                    map.put("euExclude", "1");
                } else if ("1".equals(bdOrdExclu.getEuExcType())) {//??????
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
     * ?????????????????????
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
     * ??????????????????
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
     * ???????????????????????????????????????
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
        // isAll=1??????????????????isAll=0????????????????????????
        // ???isAll=0????????????isUpdate
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
                    pkOrd);// ?????????????????????
            ret.setBdOrdAliases(bdOrdAliases);
        } else {
            throw new BusException("??????isAll????????????");
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
     * ???????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     * @throws Exception
     */
    public BdOrd saveBdOrdAll(String param, IUser user) throws Exception {
        /** ???????????? **/
        BdOrdAndAllParam bdOrdAndAll = JsonUtil.readValue(param,
                BdOrdAndAllParam.class);

        User u = UserContext.getUser();
        String pkOrgCurrent = u.getPkOrg(); // ????????????
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

        //'??????'/'??????'??????(??????????????????????????????????????????)
        rleCode = bdOrd.getPkOrd() == null || bdOrd.getPkOrd().equals("") ? AddState : this.UpdateState;

        String pk = null; //??????????????????????????????????????????????????????
        /** ??????????????????????????? **/
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
                pk = bdOrd.getPkOrd(); //????????????????????????
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("???????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("?????????????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("?????????????????????????????????????????????????????????");
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
                        throw new BusException("???????????????????????????");
                    }
                    if (count_code == 0 && count_name != 0) {
                        throw new BusException("?????????????????????????????????????????????");
                    }
                    if (count_code != 0 && count_name != 0) {
                        throw new BusException("?????????????????????????????????????????????????????????");
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
                log.setNote("??????????????????-??????");
                DataBaseHelper.insertBean(log);
            }
        }

        /** ????????????????????? **/
        String jg = ((User) user).getPkOrg();// ????????????????????????????????????
        if ("1".equals(isAll)) {//????????????????????????????????????
            jg = "~                               ";
        }
        if (bdOrdAliases != null && bdOrdAliases.size() != 0) {
            /** ????????????????????? ??????????????????????????????????????????????????????????????????????????????*/
//			Map<String, String> aliasmap = new HashMap<String, String>();
//			int len = bdOrdAliases.size();
//			for (int i = 0; i < len; i++) {
//				String alia = bdOrdAliases.get(i).getAlias();
//				if (aliasmap.containsKey(alia)) {
//					throw new BusException("???????????????");
//				}
//				aliasmap.put(alia, bdOrdAliases.get(i).getPkOrdalia());
//			}

            // ??????????????????????????????????????????
            String pkord = bdOrd.getPkOrd();
            DataBaseHelper.update("update bd_ord_alias set del_flag = '1' where pk_ord = ? "
            		+ ("1".equals(isAll) ? "" : " and pk_org = '" + u.getPkOrg() + "' ")
                    , new Object[]{pkord});

            for (BdOrdAlias alia : bdOrdAliases) {
                if (alia.getPkOrdalia() != null) {
                    alia.setDelFlag("0");// ??????
                    alia.setPkOrd(pkord);
                    DataBaseHelper.updateBeanByPk(alia, false);
                } else {
                    alia.setPkOrd(pkord);
//					if("1".equals(isAll)){//????????????????????????????????????
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

        /** ??????????????????????????? **/
        // ??????????????????????????????????????????bd_Ord_Org??????????????????????????????
        //if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
        if (bdOrdOrgs != null && bdOrdOrgs.size() != 0) {
            /** ????????????????????? */
            Map<String, String> pkOrgMap = new HashMap<String, String>();
            int len = bdOrdOrgs.size();
            for (int i = 0; i < len; i++) {
                String pkOrg = bdOrdOrgs.get(i).getPkOrg();
                if (pkOrgMap.containsKey(pkOrg)) {
                    throw new BusException("???????????????");
                }
                pkOrgMap.put(pkOrg, bdOrdOrgs.get(i).getPkOrdorg());
            }

            // ??????????????????????????????????????????
            String pkord = bdOrd.getPkOrd();
            int i = DataBaseHelper
                    .update("update bd_ord_org set del_flag = '1' where pk_ord = ?",
                            new Object[]{pkord});
            for (BdOrdOrg org : bdOrdOrgs) {
                if (org.getPkOrdorg() != null) {
                    org.setDelFlag("0");// ??????
                    org.setPkOrd(pkord);
                    DataBaseHelper.updateBeanByPk(org, false);
                    // ?????????????????????????????????????????????
                } else {
                    org.setPkOrd(pkord);
                    if(bdOrd!=null){
                        org.setNamePrt(bdOrd.getNamePrt());
                    }

                    DataBaseHelper.insertBean(org);
                    // ???????????????????????????????????????????????????
                    // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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

        /** ??????????????????????????? **/
        // ??????????????????????????????????????????bd_Ord_Org??????????????????????????????
        List<BdOrdEmr> delBdOrdEmrList = new ArrayList<BdOrdEmr>();//??????-?????????????????????????????????????????????
        List<BdOrdEmr> addBdOrdEmrList = new ArrayList<BdOrdEmr>();//??????-???????????????????????????????????????
        if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
            String pkord = bdOrd.getPkOrd();
            delBdOrdEmrList = DataBaseHelper.queryForList("select * from bd_ord_emr where pk_ord=? and del_flag='0' ", BdOrdEmr.class, new Object[]{pkord});
            if (bdOrdEmrs != null && bdOrdEmrs.size() != 0) {
                /** ??????????????????????????? */
                Map<String, String> emrMap = new HashMap<String, String>();
                int len = bdOrdEmrs.size();
                for (int i = 0; i < len; i++) {
                    String codeEmrtemp = bdOrdEmrs.get(i).getCodeEmrtemp();
                    String nameEmrtemp = bdOrdEmrs.get(i).getNameEmrtemp();
                    String euPvType = "|" + bdOrdEmrs.get(i).getEuPvtype();
                    if (emrMap.containsKey(codeEmrtemp + euPvType)) {
                        throw new BusException("?????????????????????");
                    }
                    if (emrMap.containsKey(nameEmrtemp + euPvType)) {
                        throw new BusException("?????????????????????");
                    }
                    emrMap.put(codeEmrtemp + euPvType, bdOrdEmrs.get(i).getCodeEmrtemp());
                    emrMap.put(nameEmrtemp + euPvType, bdOrdEmrs.get(i).getNameEmrtemp());
                }

                // ??????????????????????????????????????????
                DataBaseHelper.update("update bd_ord_emr set del_flag = '1' where pk_ord = ?", new Object[]{pkord});
                for (BdOrdEmr emr : bdOrdEmrs) {
                    if (emr.getPkOrdemr() != null) {
                        emr.setDelFlag("0");// ??????
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
         * ???????????????????????????
         */
        // ????????????????????????????????????????????????????????????????????????
        if (pkOrgCurrent.startsWith("~") || "1".equals(isAll)) {
            if (bdOrdItems != null && bdOrdItems.size() != 0) {
                /** ??????????????????????????? */
                Map<String, String> pkItemMap = new HashMap<String, String>();
                int len = bdOrdItems.size();
                for (int i = 0; i < len; i++) {
                    String pkItem = bdOrdItems.get(i).getPkItem();
                    if (pkItemMap.containsKey(pkItem)) {
                        throw new BusException("?????????????????????");
                    }
                    pkItemMap.put(pkItem, bdOrdItems.get(i).getPkOrditem());
                }

                // ??????????????????????????????????????????
                String pkord = bdOrd.getPkOrd();
                //DataBaseHelper.update("update bd_ord_item set del_flag = '1' where pk_ord = ?",
                //new Object[] { pkord });

                //???????????????????????????
                String sql = "select * from bd_ord_item where del_flag = '0' and pk_ord = ? ";
                List<BdOrdItem> list = DataBaseHelper.queryForList(sql, BdOrdItem.class, new Object[]{pkord});
                //Iterator<BdOrdItem> iterator = list.iterator();

                for (BdOrdItem item : bdOrdItems) {


                    if (item.getPkOrditem() != null) {
                        String pkOrditem = item.getPkOrditem();
                        //??????????????????????????????????????????????????????????????????list
 /*                       while (iterator.hasNext()) {

                            String allPKOrditem = iterator.next().getPkOrditem();
                            String pkOrditem = item.getPkOrditem();

                            if (allPKOrditem.equals(pkOrditem)) {
                                iterator.remove();
                                break;
                            }
                        }*/
                        //?????????????????????bug
                       if(list != null && list.size()>0){
                           for(BdOrdItem bd : list){
                               String allPKOrditem = bd.getPkOrditem();
                               if(allPKOrditem.equals(pkOrditem)){
                                   list.remove(bd);
                                   break;
                               }
                           }
                       }

                        //item.setDelFlag("0");// ??????
                        item.setPkOrd(pkord);
                        DataBaseHelper.updateBeanByPk(item, false);
                    } else {
                        item.setPkOrd(pkord);
                        DataBaseHelper.insertBean(item);
                    }
                }

                //??????????????????
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
         * ???????????????????????????
         */
        // ????????????????????????????????????????????????????????????????????????
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
                /** ??????????????????????????? */
                Map<String, String> pkdeptMap = new HashMap<String, String>();
                int len = labColList.size();
                for (int i = 0; i < len; i++) {
                    String pkDept = labColList.get(i).getPkDeptCol();
                    if(StringUtils.isNotBlank(pkDept)) {
                    	boolean isRepeat=pkdeptMap.containsKey(pkDept) && StringUtils.isNotBlank(labColList.get(i).getPkOrgarea())
                                &&StringUtils.isNotBlank(pkdeptMap.get(pkDept))&&labColList.get(i).getPkOrgarea().equals(pkdeptMap.get(pkDept));
                            if (isRepeat) {
                                throw new BusException("??????????????????????????????????????????");
                            }
                    }
                    
                    pkdeptMap.put(pkDept, labColList.get(i).getPkOrgarea());
                }

                // ??????????????????????????????????????????
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
                        dept.setDelFlag("0");// ??????
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
         * ???????????????????????????
         */
        // ????????????????????????????????????????????????????????????????????????
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

        /** ??????????????????????????? **/
        // ???????????????????????????????????????????????????????????????????????????
        if ((!pkOrgCurrent.startsWith("~")) && (!"1".equals(isAll))) {
            if (bdOrdDepts != null && bdOrdDepts.size() != 0) {
                /** ??????????????????????????? */
                Map<String, String> pkdeptMap = new HashMap<String, String>();
                int len = bdOrdDepts.size();
                for (int i = 0; i < len; i++) {
                    String pkDept = bdOrdDepts.get(i).getPkDept();
                    if (pkdeptMap.containsKey(pkDept)) {
                        throw new BusException("?????????????????????");
                    }
                    pkdeptMap.put(pkDept, bdOrdDepts.get(i).getPkOrddept());
                }

                // ??????????????????????????????????????????
                //2020-02-04 ???????????????????????????????????????????????????????????????
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
                        dept.setDelFlag("0");// ??????
                        dept.setPkOrd(pkord);
                        DataBaseHelper.updateBeanByPk(dept, false);
                    } else {
                        dept.setPkOrd(pkord);
                        // ??????????????????????????????
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

        /**???????????????????????????*/
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

        //?????????????????????
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("bdOrds", bdOrdAndAll.getBdOrd());
        paramMap.put("bdOrd", MapUtils.objectToMap(bdOrdAndAll.getBdOrd()));
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        //??????????????????
        List<BdOrdDept> ordDepts = bdOrdAndAll.getBdOrdDepts();
        if (ordDepts != null && ordDepts.size() > 0) {
            paramMap.put("ordExDept", (ordDepts.get(0)));
        }


        paramMap.put("STATUS", rleCode);
        PlatFormSendUtils.sendBdOrdMsg(paramMap);
        try {
            // ??????????????????????????????????????????????????? syx
            ExtSystemProcessUtils.processExt2HipMethod("OrderItemsAddAndUpdate", paramMap);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return bdOrd;
    }

    /**
     * ??????????????????
     *
     * @param
     */
    private void saveOrderExeDept(BdOrdAndAllParam bdOrdAndAll, IUser user) {
        String isAll = bdOrdAndAll.getIsAll();
        BdOrd bdOrd = bdOrdAndAll.getBdOrd();
        List<BdOrdDept> bdOrdDepts = bdOrdAndAll.getBdOrdDepts();

        String jg = ((User) user).getPkOrg();// ????????????????????????????????????
        if ("1".equals(isAll)) {//????????????????????????????????????
            jg = "~                               ";
        }

        //if ("1".equals(isAll)) {
        if (bdOrdDepts != null && bdOrdDepts.size() != 0) {
            /** ??????????????????????????? */
            Map<String, String> pkdeptMap = new HashMap<String, String>();
            int len = bdOrdDepts.size();
            for (int i = 0; i < len; i++) {
                String pkDept = bdOrdDepts.get(i).getPkDept();
                if (pkdeptMap.containsKey(pkDept)) {
                    throw new BusException("?????????????????????");
                }
                pkdeptMap.put(pkDept, bdOrdDepts.get(i).getPkOrddept());
            }
            // ??????????????????????????????????????????
            //2020-02-04 ???????????????????????????????????????????????????????????????
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
                    // ??????????????????????????????
                    dept.setPkOrdorg(bdOrdOrg.getPkOrdorg());
                    DataBaseHelper.insertBean(dept);
                }
            }
        }

    }

    /**
     * ???????????????????????????
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
                throw new BusException("??????????????????????????????????????????????????????");
            } else if(ordcnt>0){
                throw new BusException("?????????????????????????????????????????????????????????");
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
                //'??????'??????
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
     * ???????????????????????? ????????????(??????)??????????????????
     *
     * @param param
     * @param user
     */
    public void saveBdOrdOrgs(String param, IUser user) {
        List<BdOrdOrgVo> bdOrdOrgVos = JsonUtil.readValue(param,
                new TypeReference<List<BdOrdOrgVo>>() {
                });

        String pkOrg = ((User) user).getPkOrg();

        /** ??????????????????????????? **/
        DataBaseHelper.execute("delete from bd_ord_org where pk_org = ?",
                new Object[]{pkOrg});
        if (bdOrdOrgVos != null && bdOrdOrgVos.size() != 0) {

            for (BdOrdOrgVo orgVo : bdOrdOrgVos) {
                // ?????????????????????BdOrdOrgVo???????????????
                BdOrdOrg org = new BdOrdOrg();
                BeanUtils.copyProperties(orgVo, org);
                if ("1".equals(orgVo.getFlag())) {
                    DataBaseHelper.insertBean(org);
                    // ????????????????????????????????????????????????
                    if (pkOrg.indexOf("~") == -1) {
                        // ????????????
                        String pkOrd = orgVo.getPkOrd();
                        // ???????????????
                        List<BdOrdAlias> overallaliaslist = DataBaseHelper
                                .queryForList(
                                        "select a.* from bd_ord_alias a where a.del_flag='0' and a.pk_org like '~%' and a.pk_ord = ?",
                                        BdOrdAlias.class, pkOrd);

                        if (overallaliaslist != null
                                && overallaliaslist.size() != 0) {
                            // ????????????????????????
                            List<BdOrdAlias> orgaliaslist = DataBaseHelper
                                    .queryForList(
                                            "select a.* from bd_ord_alias a where a.del_flag='0' and a.pk_org = ? and a.pk_ord = ?",
                                            BdOrdAlias.class, pkOrg, pkOrd);
                            // "??????????????????????????????????????????"
                            List<BdOrdAlias> aliaslist = new ArrayList<BdOrdAlias>();
                            // ????????????????????????????????????????????????????????????
                            if (orgaliaslist != null
                                    && orgaliaslist.size() != 0) {
                                for (BdOrdAlias orgalia : orgaliaslist) {
                                    String name_orgalia = orgalia.getAlias();
                                    // ????????????list??????
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
                            } else {// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                                aliaslist = overallaliaslist;
                            }
                            // ???"??????????????????????????????????????????"??????????????????,????????????????????????
                            for (BdOrdAlias al : aliaslist) {
                                al.setPkOrdalia(null);
                                al.setPkOrg(pkOrg);
                                DataBaseHelper.insertBean(al);
                            }
                        }
                    }
                } else if ("-1".equals(orgVo.getFlag())) {
                    // ????????????????????????????????????????????????
                    if (pkOrg.indexOf("~") == -1) {
                        // ????????????
                        String pkOrd = orgVo.getPkOrd();
                        // ????????????????????????
                        List<BdOrdAlias> aliaslist = DataBaseHelper
                                .queryForList(
                                        "select a.* from bd_ord_alias a where a.del_flag='0' and a.pk_org = ? and a.pk_ord = ?",
                                        BdOrdAlias.class, pkOrg, pkOrd);
                        // ?????????????????????????????????????????????
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
     * ?????????????????????????????????
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

        //'??????'??????
        rleCode = this.DelState;
        DataBaseHelper
                .execute(
                        "delete from bd_ord_dept where pk_ord = ? and pk_org like ?||'%'",
                        pkOrd, jg);
        DataBaseHelper
                .execute(
                        "delete from bd_ord_org where pk_ord = ? and pk_org like ?||'%'",
                        pkOrd, jg);
        // ?????????????????????????????????????????????
        if (jg.indexOf("~") == -1) {
            // ??????????????????????????????
            DataBaseHelper
                    .update("update bd_ord_alias set del_flag='1' where pk_ord = ? and pk_org like ?||'%'",
                            pkOrd, jg);
        }
        //?????????????????????
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        paramMap.put("STATUS", rleCode);
        PlatFormSendUtils.sendBdOrdMsg(paramMap);
    }

    /**
     * ??????????????????????????????
     *
     * @param param
     * @param user
     */
    public void saveBdItemPvs(String param, IUser user) {
        List<BdItemPv> pvs = JsonUtil.readValue(param,
                new TypeReference<List<BdItemPv>>() {
                });

        if (pvs != null && pvs.size() != 0) {
            // ??????????????????????????????????????????
            String pkitem = pvs.get(0).getPkItem();
            DataBaseHelper.update(
                    "update bd_item_pv set del_flag = '1' where pk_item = ?",
                    new Object[]{pkitem});
            for (BdItemPv pv : pvs) {
                if (pv.getPkPvitem() != null) {
                    pv.setDelFlag("0");// ??????
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
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     * @throws HL7Exception
     */
    public BdItemcate saveBdItemcate(String param, IUser user)
            throws HL7Exception {

        BdItemcate itemcate = JsonUtil.readValue(param, BdItemcate.class);

        String pk = ""; //???????????????????????????????????????????????????

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
                    throw new BusException("?????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("?????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("?????????????????????????????????????????????");
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
                    throw new BusException("?????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("?????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("?????????????????????????????????????????????");
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
     * ????????????????????????
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
                throw new BusException("??????????????????????????????????????????");
            }
            if (count_item != 0) {
                throw new BusException("????????????????????????????????????");
            }
        }

        Map<String,Object> itemCate = Maps.newHashMap();
        itemCate.put("status","_DELETE");
        itemCate.put("item",DataBaseHelper.queryForMap("select * from bd_audit_itemcate where pk_itemcate=?",new Object[]{pkItemcate}));
        PlatFormSendUtils.execute(itemCate,"sendBdItemcateMsg");
    }

    /**
     * ????????????-????????????
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
        //1???????????? 0??????????????????????????????????????????
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
        // 0 ?????????????????????,1 ?????????
        if ("0".equals(isAll)) {
            List<String> orgPkList = new ArrayList<String>();
            String pkorg = ((User) user).getPkOrg();// ????????????
            orgPkList.add(pkorg);

            // ????????????????????????????????????
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
        //????????????????????????????????????
        String userPkOrg =
                ((User) user).getPkOrg().equals("~                               ") ? "" : ((User) user).getPkOrg();
        List<BdItemAttrVo> itemAttrs = srvMapper.getBdItemAttrByItem(userPkOrg, pkItem);
        //????????????
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
     * ????????????001002007057
     * ????????????????????????????????????
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
     * ????????????001002007058
     * ????????????????????????????????????
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
     * ?????????????????? ??? ????????????????????????????????????????????????????????????
     * <p>
     * ??????????????????????????????????????? ??? ???????????????????????????????????????????????????????????????
     * <p>
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ??? ???????????????????????????????????????????????????????????????
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


        //????????????.????????????
        List<BdItemSp> itemSps = itemAndHpSetPrices.getItemSps();
        List<CgdivItemVo> itemCgdivs = itemAndHpSetPrices.getItemCgDivs();
        User u = (User) user;
        String pkOrg = u.getPkOrg();

        /** ??????????????????????????? **/
        if (item.getPkItem() == null) {
            //??????
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_item "
                            + "where del_flag = '0' and code = ?",
                    Integer.class, item.getCode());
            //??????+??????+??????
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

                // ?????????????????????
                item.setState(AddState);
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("item", MapUtils.objectToMap(item));
                paramMap.put("STATUS", this.AddState);
                paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
                PlatFormSendUtils.sendBdItemMsg(paramMap);
                try {
                    //???????????????????????? ???????????????????????????????????????????????????
                    ExtSystemProcessUtils.processExt2HipMethod("ChargeDictAddAndUpdate", paramMap);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                paramMap = null;
            } else if (count_code != 0) {
                throw new BusException("???????????????????????????");
            } else if (count != 0) {
                throw new BusException("?????????????????????????????????????????????");
            }
//            else if (count_codeHp != 0) {
//                throw new BusException("?????????????????????");
//            }

        } else {
            BdItem beforeItem = DataBaseHelper.queryForBean("select * from bd_item where pk_item = ?", BdItem.class, item.getPkItem());
            if ("1".equals(isAll)) {// 0--???????????????????????????????????????
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
                //??????+??????+??????
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
                        //???????????????????????? ???????????????????????????????????????????????????
                        ExtSystemProcessUtils.processExt2HipMethod("ChargeDictAddAndUpdate", paramMap);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    paramMap = null;
                } else if (count_code != 0) {
                    throw new BusException("???????????????????????????");
                } else if (count != 0) {
                    throw new BusException("?????????????????????????????????????????????");
                }
//                else if (count_codeHp != 0) {
//                    throw new BusException("?????????????????????");
//                }
            }

            SysApplog log = new SysApplog();
            log.setPkEmpOp(u.getPkEmp());
            log.setNameEmpOp(u.getNameEmp());
            log.setEuButype("0");
            log.setEuOptype("0");
            log.setNote("??????????????????");
            log.setContentBf("?????????" + beforeItem.getPrice());
            log.setContent("?????????" + item.getPrice());
            DataBaseHelper.insertBean(log);
        }

        String pkitem = item.getPkItem();

        /**???????????????????????????????????????*/
        if (itemAttrs != null && itemAttrs.size() > 0) {
            // ??????????????????????????????????????????
            DataBaseHelper
                    .update("update bd_dictattr set del_flag = '1' where pk_dict = ?",
                            new Object[]{pkitem});
            for (BdItemAttrVo vo : itemAttrs) {
                //??????pojo??????
                BdDictattr bdDictAttr = new BdDictattr();
                //??????
                bdDictAttr.setPkDictattr(vo.getPkDictattr());
                //??????????????????????????????
                bdDictAttr.setPkOrg(((User) user).getPkOrg());
                //????????????
                bdDictAttr.setPkDictattrtemp(vo.getPkDictattrtemp());
                //?????????
                bdDictAttr.setValAttr(vo.getValAttr());
                //????????????????????????
                bdDictAttr.setPkDict(pkitem);
                bdDictAttr.setCodeAttr(vo.getCodeAttr());
                bdDictAttr.setNameAttr(vo.getNameAttr());

                if (!CommonUtils.isEmptyString(bdDictAttr.getPkDictattr())) {
                    bdDictAttr.setDelFlag("0");// ??????
                    DataBaseHelper.updateBeanByPk(bdDictAttr, false);
                } else {
                    DataBaseHelper.insertBean(bdDictAttr);
                }
            }
        }

        /** ??????????????????????????? **/
        // ?????????????????????????????????????????????????????????????????? ???????????????????????????????????????
        if (itemPrices != null && itemPrices.size() >= 2) {
            List<BdItemPrice> iPrices = new ArrayList<BdItemPrice>();
            // ???????????????
            for (BdItemPrice p : itemPrices) {
                if ("0".equals(p.getFlagStop())) {
                    iPrices.add(p);
                }
            }
            if (iPrices.size() >= 2) {
                if (isTimeRepeated(iPrices)) {
                    throw new BusException(
                            "?????????????????????????????????????????????????????????,?????????????????????????????????????????????");
                }
            }
        }

        /**
         * ???????????????????????????
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
         * ???????????????????????????
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
         * ???????????????????????????????????? ?????????????????????????????????Modifier???????????????????????????????????????
         * ????????????????????????????????????????????????????????? ??????????????????????????????????????????????????????????????????
         * huanghaisheng
         *

         String delsql = "update bd_item_price set del_flag = '1' where pk_item = ?";
         if ("1".equals(isAll)) {// ?????????
         // ??????????????????????????????????????????
         DataBaseHelper.update(delsql, new Object[] { pkitem });
         } else if ("0".equals(isAll)) {// ????????????
         delsql += " and pk_org = ?";
         // ??????????????????????????????????????????
         DataBaseHelper.update(delsql, new Object[] { pkitem, pkOrg });
         }
         */

        if (itemPricesDelPk != null && itemPricesDelPk.size() != 0) {
            DataBaseHelper.batchUpdate("update bd_item_price set del_flag = '1',FLAG_STOP='1' where pk_itemprice= :pkItemprice", itemPricesDelPk);
        }
        // ???????????????????????????????????????
        if (itemPrices != null && itemPrices.size() != 0) {

            for (BdItemPrice price : itemPrices) {

                if (price.getPkItemprice() != null) {
                    //price.setDelFlag("0");// ??????
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
         * ???????????????????????????????????????
         */
        //???????????????????????????????????????
        if ("1".equals(item.getFlagActive()))
        {
        	//?????????????????????
        	//??????bd_ord_item??????????????????
        	DataBaseHelper.update("update bd_ord_item set DEL_FLAG ='0' where PK_ITEM =  ? ",new Object[]{item.getPkItem()});
        }
                
        List<BdOrdOrg> bdOrdOrgList = DataBaseHelper.queryForList(" select ord.pk_ord from bd_ord_org ord where ord.del_flag = '0' and EXISTS(select (1) from bd_ord_item item "
        		+ " where item.pk_ord=ord.pk_ord and item.del_flag = '0' and item.pk_item = ? ) and ord.pk_org = ? group by ord.pk_ord ", BdOrdOrg.class, new Object[]{pkitem,pkOrg});
        for (BdOrdOrg bdOrdOrg : bdOrdOrgList) {
            //????????????
            double price = 0.0;
            //????????????
            double priceChd = 0.0;
            //????????????
            double priceVip = 0.0;
            if ("0".equals(item.getFlagActive())) {
            	//????????????????????????
            	Integer count = DataBaseHelper.queryForScalar(" select count(1) from bd_ord_item oi " + 
            			" left outer join bd_item item on oi.pk_item=item.pk_item and oi.flag_pd='0' " + 
            			" where oi.del_flag = '0' and item.flag_active ='1' and oi.pk_ord = ? ",Integer.class, new Object[]{bdOrdOrg.getPkOrd()});
            	if(count == 0){
            		DataBaseHelper.update("update bd_ord set FLAG_ACTIVE ='0' where PK_ORD =  ? ",new Object[]{bdOrdOrg.getPkOrd()});
            	}
            	//??????bd_ord_item??????????????????
            	DataBaseHelper.update("update bd_ord_item set DEL_FLAG ='1' where PK_ITEM =  ? ",new Object[]{item.getPkItem()});
    		} 
            //???????????????????????????????????????????????????
            List<BdOrdItemExt> bdOrdItemExts = this.srvMapper.getBdOrdItemsByOrd(bdOrdOrg.getPkOrd(),pkOrg);
            //??????????????????.????????????
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
        

        if ("1".equals(isAll)) {// 0--????????????????????????????????????????????????

            /** ??????????????????????????? **/
            if (itemHps != null && itemHps.size() != 0) {
                // ?????????????????????????????????pk_hp???????????????
				/*Map<String, String> pkHpMap = new HashMap<String, String>();
				int len = itemHps.size();
				for (int i = 0; i < len; i++) {
					String pkhp = itemHps.get(i).getPkHp();
					if (pkHpMap.containsKey(pkhp)) {
						throw new BusException("???????????????????????????????????????");
					}
					pkHpMap.put(pkhp, itemHps.get(i).getPkItemhp());
				}*/

                // ??????????????????????????????????????????
                DataBaseHelper
                        .update("update bd_item_hp set del_flag = '1' where pk_item = ?",
                                new Object[]{pkitem});
                for (BdItemHp hp : itemHps) {
                    if (hp.getPkItemhp() != null) {
                        hp.setDelFlag("0");// ??????
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

            /** ??????????????????????????? **/
            if (itemSets != null && itemSets.size() != 0) {
                // ??????????????????????????????????????????
                DataBaseHelper
                        .update("update bd_item_set set del_flag = '1' where pk_item = ?",
                                new Object[]{pkitem});
                List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
                Map<String, Object> paramMap = new HashMap<String, Object>();
                for (BdItemSet set : itemSets) {
                    if (set.getPkItemset() != null) {
                        set.setDelFlag("0");// ??????
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

                //?????????????????????
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
     * ????????????001002007056
     * ????????????????????????????????????
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
     * ??????pkitem??????????????????
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
     * ??????pkitem??????????????????
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
     * ??????????????????????????????????????????????????????????????????
     *
     * @param itemPrices ????????????????????????????????????????????????
     * @return
     */
    private boolean isTimeRepeated(List<BdItemPrice> itemPrices) {
        boolean ret = false;
        for (int i = 0; i < itemPrices.size() - 1; i++) {
            BdItemPrice price_i = itemPrices.get(i);
            for (int j = i + 1; j < itemPrices.size(); j++) {
                BdItemPrice price_j = itemPrices.get(j);
                // ????????????????????????????????????????????????
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
     * ?????????????????? ??? ??????????????????
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
            throw new BusException("??????????????????????????????????????????");
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

        // ???????????????????????????
        BdItem bdItem = DataBaseHelper
                .queryForBean("select * from bd_item where pk_item = ?",
                        BdItem.class, pkItem);
        List<BdItemSet> bdItemSetsList = DataBaseHelper.queryForList(
                "select * from bd_item_set where pk_item = ?", BdItemSet.class,
                pkItem);

        //?????????????????????
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
     * ????????????????????????
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
                    throw new BusException("???????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("???????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("???????????????????????????????????????");
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
                    throw new BusException("???????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("???????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("???????????????????????????????????????");
                }
            }
        }

        return ordtype;

    }

    /**
     * ??????????????????
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
            throw new BusException("??????????????????????????????????????????");
        } else if (srvMapper.countCnOrder((String) paramMap.get("code")) > 0) {
            throw new BusException("????????????????????????????????????");
        } else {
            srvMapper.delBdOrdtype(pkOrdtype);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @param user
     */
    public void delCateDef(String param, IUser user) {

        String pkCatedef = JsonUtil.getFieldValue(param, "pkCatedef");
        /** ??????????????????????????????????????????--?????? ?????? **/
        int count_cont = DataBaseHelper
                .queryForScalar(
                        "select count(1) from bd_cate_cont where del_flag = '0' and pk_catedef = ?",
                        Integer.class, pkCatedef);

        if (count_cont == 0) {
            DataBaseHelper
                    .update("update bd_cate_def set del_flag = '1' where pk_catedef = ?",
                            new Object[]{pkCatedef});
        } else {
            throw new BusException("????????????????????????????????????????????????");
        }
    }

    /**
     * ?????????????????????????????????--??????
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
     * ?????????????????????????????????
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
                    throw new BusException("??????????????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("??????????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("??????????????????????????????????????????????????????");
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
                    throw new BusException("??????????????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("??????????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("??????????????????????????????????????????????????????");
                }
            }
        }

    }

    /**
     * ??????????????????????????????????????? ??? ????????????
     *
     * @param param
     * @param user
     */
    public void saveContAndCateItems(String param, IUser user) {
        ContAndCateItemsParam contAndCateItems = JsonUtil.readValue(param,
                ContAndCateItemsParam.class);
        BdCateCont contInfo = contAndCateItems.getContInfo();
        List<BdCateItem> cateItemList = contAndCateItems.getCateItemList();

        /** ??????????????????????????????????????? **/
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
                    throw new BusException("??????????????????????????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("??????????????????????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("??????????????????????????????????????????????????????????????????");
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
                    throw new BusException("??????????????????????????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("??????????????????????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("??????????????????????????????????????????????????????????????????");
                }
            }
        }

        /** ?????????????????? **/
        if (cateItemList != null && cateItemList.size() != 0) {
            /** ??????????????? **/
            Map<String, String> pkItemcateMap = new HashMap<String, String>();
            int len = cateItemList.size();
            for (int i = 0; i < len; i++) {
                String pkItemcate = cateItemList.get(i).getPkItemcate();
                if (pkItemcateMap.containsKey(pkItemcate)) {
                    throw new BusException("????????????????????????????????????????????????");
                }
                pkItemcateMap.put(pkItemcate, cateItemList.get(i)
                        .getPkCateitem());
            }

            // ??????????????????????????????????????????
            String pkCatecont = contInfo.getPkCatecont();
            DataBaseHelper
                    .update("update bd_cate_item set del_flag = '1' where pk_catecont = ?",
                            new Object[]{pkCatecont});
            for (BdCateItem cateitem : cateItemList) {
                if (cateitem.getPkCateitem() != null) {
                    cateitem.setDelFlag("0");// ??????
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
     * ??????????????????????????????????????????
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
     * ?????????????????????????????????????????????
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
        /** ???????????????????????? **/
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
                    throw new BusException("???????????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("???????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("???????????????????????????????????????????????????");
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
                    throw new BusException("???????????????????????????????????????");
                }
                if (count_code == 0 && count_name != 0) {
                    throw new BusException("???????????????????????????????????????");
                }
                if (count_code != 0 && count_name != 0) {
                    throw new BusException("???????????????????????????????????????????????????");
                }
            }
        }

        /** ????????????????????????????????????????????? **/
        if (itemCateList != null && itemCateList.size() != 0) {
            /** ??????????????? **/
            Map<String, String> pkItemcateMap = new HashMap<String, String>();
            int len = itemCateList.size();
            for (int i = 0; i < len; i++) {
                String pkItemcate = itemCateList.get(i).getPkItemcate();
                if (pkItemcateMap.containsKey(pkItemcate)) {
                    throw new BusException("????????????????????????????????????????????????");
                }
                pkItemcateMap.put(pkItemcate, itemCateList.get(i)
                        .getPkAudititem());
            }

            // ??????????????????????????????????????????
            String pkAudit = auditInfo.getPkAudit();
            DataBaseHelper
                    .update("update bd_audit_itemcate set del_flag = '1' where pk_audit = ?",
                            new Object[]{pkAudit});
            for (BdAuditItemcate itemcate : itemCateList) {
                itemcate.setPkOrg("~");
                if (itemcate.getPkAudititem() != null) {
                    itemcate.setDelFlag("0");// ??????
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
     * ??????????????????
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
         * ???????????????????????????????????????????????????????????????
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
         * ????????????????????????
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
            throw new BusException("???????????????????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????
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
                throw new BusException("????????????");
            }
        }

    }

    /**
     * ????????????????????????????????????
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
     * ????????????????????????
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
                throw new BusException("????????????");
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
     * ????????????001002007063
     * ????????????????????????
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
        if ("INSERT".equals(status)) {//?????????????????????????????????
            String sql = "select count(1) from bd_hp_cgdiv_tmp where del_flag='0' and code_tmp=?";
            int count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{cgdivTmpList.get(0).getCodeTmp()});
            if (count > 0) {
                throw new BusException("?????????????????????");
            }
            sql = "select count(1) from bd_hp_cgdiv_tmp where del_flag='0' and name_tmp=?";
            count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{cgdivTmpList.get(0).getNameTmp()});
            if (count > 0) {
                throw new BusException("?????????????????????");
            }
        }
        //???????????????????????????????????????????????????
        String delSql = "delete from bd_hp_cgdiv_tmp where code_tmp=?";
        DataBaseHelper.execute(delSql, new Object[]{cgdivTmpList.get(0).getCodeTmp()});

        User doUser = (User) user;
        //?????????????????????????????????????????????
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
     * ????????????001002007064
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdHpCgdivTmp> qryCgDivTmp(String param, IUser user) {
        return srvMapper.qryCgdivTmp();
    }

    /**
     * ?????????:001002007065
     * ??????????????????????????????
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
     * ????????????001002007066
     * ??????????????????
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
     * ????????????????????????
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
            itemList.get(i).setPkOrg("~                               ");//??????
            itemList.get(i).setTs(new Date());
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdHpCgdivItem.class), itemList);
    }


    /**
     * 001002007072
     * ????????????????????????
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
     * ????????????001002007095
     * ??????????????????
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
     * ????????????-???????????????????????????????????????????????????????????????????????????
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
     * ??????????????????????????????????????????
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
     * ????????????-?????????????????????
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
     * ????????????-?????????????????????
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
     * ???????????????????????????
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
     * ??????????????????????????????????????????????????????
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
     * ??????????????????????????????????????????
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
     * ??????????????????????????????????????????
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
     * ??????????????????
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
        //??????????????????price???price_chd???price_vip
        result = updateOrdPrice(map,true);
        return result;
    }

    /**
     * ????????????????????????????????????????????????
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
     * ???????????????????????????????????????
     * @param map
     * @param isSpecial
     * @return
     */
    private int updateOrdPrice(Map<String,Object> map,boolean isSpecial){
        int result = 0;
        //???????????????????????????????????????
        if(map.containsKey("pkOrd")&&map.get("pkOrd") != null){
            String pkOrd = map.get("pkOrd").toString();
            String pkOrg = map.get("pkOrg").toString();
            List<BdOrdItem> bdItemList = srvMapper.getBdItemByPkOrd(pkOrd);
            //????????????????????????
            double priceOrd = 0;
            //??????????????????????????????
            double childOrd = 0;
            //??????????????????????????????
            double special = 0;
            if(bdItemList.size()>0){
                for (int i = 0; i < bdItemList.size(); i++) {
                    if(bdItemList.get(i)!=null){
                        String pkItem = bdItemList.get(i).getPkItem();
                        //????????????????????????
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

                        //???????????????????????????????????????
                        double chileItem = 0;
                        //???????????????????????????????????????
                        double specialItem = 0;
                        //????????????????????????
                        List<BdItemSp> bdItemSpList = srvMapper.getBdItemSpByItem(pkItem);
                        if(bdItemSpList.size()>0){
                            for (int j = 0; j < bdItemSpList.size(); j++) {
                                if(bdItemSpList.get(j)!=null){
                                    //????????????
                                    String childType = bdItemSpList.get(j).getEuCdmode();
                                    //????????????
                                    String specialType = bdItemSpList.get(j).getEuSpmode();
                                    if("3".equals(bdItemSpList.get(j).getEuPvtype())) {
                                        //?????????????????????
                                        //????????????????????????
                                        if ("0".equals(bdItemSpList.get(j).getEuCdmode())) {
                                            //???????????????
                                            chileItem += priceItem * (1 + bdItemSpList.get(j).getRatioChildren());
                                        } else if ("1".equals(bdItemSpList.get(j).getEuCdmode())) {
                                            //???????????????
                                            chileItem += priceItem + bdItemSpList.get(j).getAmountChildren();
                                        } else if ("2".equals(bdItemSpList.get(j).getEuCdmode())) {
                                            //?????????????????????
                                            chileItem += bdItemSpList.get(j).getAmountChildren();
                                        }
                                        //????????????????????????
                                        if ("0".equals(bdItemSpList.get(j).getEuSpmode())) {
                                            //???????????????
                                            specialItem += priceItem * (1 + bdItemSpList.get(j).getRatioSpec());
                                        } else if ("1".equals(bdItemSpList.get(j).getEuSpmode())) {
                                            //???????????????
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

                //??????bd_ord_org???
                Map<String, Object> mParam = new HashMap<>();
                mParam.put("pkOrd", pkOrd);
                mParam.put("pkOrg", pkOrg);
                mParam.put("price", priceOrd);
                mParam.put("priceChd",childOrd);
                mParam.put("priceVip", special);
                result = srvMapper.updateOrdOrgPrice(mParam);
            }
        }

        //?????????????????????????????????????????????????????????
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
     * ???????????????????????????????????????
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
                //????????????
                //?????????????????????????????????????????????
                //??????????????????????????????????????????????????????????????????????????????????????????????????????
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
     * ????????????????????????????????????
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
	 * ??????????????????????????????????????????
	 * ????????????001002007113
	 * @param param ??????????????????,json??????{"pkItem":"abc"}
	 * @param user
	 * @return ??????????????????
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
