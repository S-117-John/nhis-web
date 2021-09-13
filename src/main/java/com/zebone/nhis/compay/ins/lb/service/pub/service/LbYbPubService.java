package com.zebone.nhis.compay.ins.lb.service.pub.service;

import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.lb.service.pub.dao.LbYbPubMapper;
import com.zebone.nhis.compay.ins.lb.vo.szyb.InsSzybItemMap;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LbYbPubService {
    @Resource
    private LbYbPubMapper lbYbPubMapper;

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 交易号：015001005018
     * 根据住院收费主键跟新bl_ip_dt中的flag_ins
     *
     * @param param
     * @param user
     */
    public void updateFlagInsuByPk(String param, IUser user) {
        List<String> pkCgips = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        if (pkCgips != null && pkCgips.size() > 0) {
            List<List<String>> splitList = split(pkCgips, 1000);
            for (int i = 0; i < splitList.size(); i++) {
                lbYbPubMapper.updateFlagInsuByPk(splitList.get(i));
            }
        } else {
            return;
        }
    }

    /**
     * 拆分集合
     *
     * @param <T>
     * @param resList 要拆分的集合
     * @param count   每个集合的元素个数
     * @return 返回拆分后的各个集合
     */
    public static <T> List<List<T>> split(List<T> resList, int count) {

        if (resList == null || count < 1)
            return null;
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }

    /**
     * 交易号：015001005018
     * 根据住院收费主键跟新bl_ip_dt中的flag_ins
     *
     * @param param
     * @param user
     */
    public void updateOpFlagInsuByPk(String param, IUser user) {
        List<String> pkCgops = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        if (pkCgops != null && pkCgops.size() > 0) {
            lbYbPubMapper.updateOpFlagInsuByPk(pkCgops);
        } else {
            return;
        }
    }

    /**
     * 交易号：015001005019
     * 更新医保结算表的结算主键,传入表名TableName, 主键名称PrimaryKeyName ,主键的值PrimaryKeyValue,需要更新的字段 PkSettle
     *
     * @param paramMap
     */
    public void updatePkSettlByYBPk(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap != null && paramMap.size() > 0) {
            paramMap.put("dateTs", sdf.format(new Date()));
            lbYbPubMapper.updatePkSettlByYBPk(paramMap);
        } else {
            return;
        }
    }

    /**
     * 交易号：015001005020
     * 通过pkpv更新门诊或住院的flag_insu标记
     *
     * @param param
     * @param user
     */
    public void updateOPorIpForInsByPkpv(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map == null || map.size() <= 0) throw new BusException("未获得相关信息");
        if ("i".equalsIgnoreCase(map.get("type").toString())) {
            String sql = "update bl_ip_dt set FLAG_INSU='0' where FLAG_SETTLE='0' and pk_pv=? and pk_settle is null";
            DataBaseHelper.execute(sql, map.get("pkPv"));
        } else if ("o".equalsIgnoreCase(map.get("type").toString())) {
            String sql = "update bl_op_dt set FLAG_INSU='0' where FLAG_SETTLE='0' and PK_PV=? and pk_settle is null";
            DataBaseHelper.execute(sql, map.get("pkPv"));
            String sql1 = "update bl_op_dt set FLAG_INSU='0' where FLAG_SETTLE='1' and PK_PV=? and quan > 0 and flag_pv = '1'";
            DataBaseHelper.execute(sql1, map.get("pkPv"));
        }
    }

    /**
     * 根据pkHp，List<pkitem>查询已匹配的项目对照项目
     * 015001005024
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<InsSzybItemMap> qrySzybItemMapInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        List<InsSzybItemMap> mapList = lbYbPubMapper.qrySzybItemMapInfo(paramMap);
        List<String> pkItems = (List<String>) paramMap.get("pkItems");
//		if(mapList!=null &&pkItems!=null && mapList.size()<pkItems.size()){
//			throw new BusException("存在未匹配的项目信息！");
//		}
        return mapList;
    }

    /**
     * 查询各个医保中不同收费编码的总额
     *
     * @param map { pkPv 就诊主键;  codeBills 收费编码（List）}
     * @return
     */
    public List<Map<String, Object>> qryLbZfSumAmount(Map<String, Object> map) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList.addAll(lbYbPubMapper.qrySzybFymx(map));
        mapList.addAll(lbYbPubMapper.qrySzlxFymx(map));
        return mapList;
    }

    /**
     * 015001004014
     * 根据项目类别和发票分类查询账单码
     *
     * @param param {"pkItem":"收费项目分类","pkInvcate":"院内票据分类主键"}
     * @param user
     * @return
     */
    public String qryCodeBill(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || paramMap.get("pkItem") == null) return null;
        String result = lbYbPubMapper.qryCodeBillByPkItem(paramMap);
        return result;
    }

    /**
     * 015001005025
     * 根据就诊主键查询本次就诊产生的费用明细
     *
     * @param param {"pkPv":"就诊主键","type":"住院（I）/门诊（O）"}
     * @param user
     * @return
     */
    public Map<String, Object> qryOpOrIpFymx(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;

        int pageIndex = CommonUtils.getInteger(paramMap.get("pageIndex"));
        int pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        if ("O".equalsIgnoreCase(paramMap.get("type").toString())) {
            resList = lbYbPubMapper.qryOpFymx(paramMap);
        } else if ("I".equalsIgnoreCase(paramMap.get("type").toString())) {
            resList = lbYbPubMapper.qryIpFymx(paramMap);
        }
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        paramMap.clear();
        paramMap.put("list", resList);
        paramMap.put("totalCount", page.getTotalCount());
        return paramMap;
    }


    /**
     * 015001005026
     * 撤销门诊或住院的全部费用明细
     *
     * @param param{"pkPv":"就诊主键","type":"门诊（O）/住院（I）"}
     * @param user
     */
    public void updateToInitUpDown(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return;
        String sql = "";
        if ("i".equalsIgnoreCase(paramMap.get("type").toString())) {
            sql = "update bl_ip_dt set flag_insu='0' , flag_settle='0' where pk_pv=?";
        } else if ("o".equalsIgnoreCase(paramMap.get("type").toString())) {
            sql = "update bl_op_dt set flag_insu='0' , flag_settle='0' where pk_pv=?";
        }
        DataBaseHelper.update(sql, new Object[]{paramMap.get("pkPv")});
    }

    /**
     * 015001005027
     * 更新医保登记信息
     *
     * @param param{"hpType":"医保类型（1：市医保；2：离休医保；3：农保）","id":"登记信息主键","pkPv":"就诊主键"}
     * @param user
     */
    public void updateYbDjInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return;
        String sql = "";
        if ("1".equals(paramMap.get("hpType"))) {//宿州市医保登记信息更新
            sql = "update ins_szyb_mzdj set pk_pv=? where id=?";
        } else if ("2".equals(paramMap.get("hpType"))) {//宿州离休医保登记信息更细
            sql = "update ins_szlx_mzdj set pk_pv? where id=?";
        } else if ("3".equals(paramMap.get("hpType"))) {
            sql = "update ins_suzhounh_reginfo set pk_pv=? where id=?";
        }
        DataBaseHelper.update(sql, new Object[]{paramMap.get("pkPv"), paramMap.get("id")});
    }

    /**
     * 015001005031
     * 根据id查询结算表的数据
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryYbJsByTableId(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        return lbYbPubMapper.qryYbJsByTableId(paramMap);
    }

    /**
     * 015001005032
     * 根据id删除医保结算表的记录
     *
     * @param param {"id":"结算表主键","tableName":"表名"}
     * @param user
     */
    public void deleteYbJsByTableId(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return;
        String sql = "update " + paramMap.get("tableName") + " set del_flag='1' where id=:id";
        DataBaseHelper.update(sql, paramMap);
    }

    /**
     * 015001005034
     * 根据pkdept和pkhp查询科室控费信息
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryBdHpFactorByPkDeptAndBdHp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        return lbYbPubMapper.qryBdHpFactorByPkDeptAndBdHp(paramMap);
    }

    /**
     * 查询疾病审批信息
     * 交易号：015001005037
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryDiseEap(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");

        List<Map<String, Object>> listMap = new ArrayList<>();
        if (CommonUtils.isEmptyString(pkPv))
            return listMap;

        List<Map<String, Object>> idList = DataBaseHelper.queryForList(
                "select id_no as id from pi_master pi inner join PV_ENCOUNTER pv on pv.pk_pi = pi.pk_pi where pv.pk_pv = ? ",
                new Object[]{pkPv});

        if (idList != null && idList.size() > 0 && idList.get(0) != null
                && idList.get(0).get("id") != null && !CommonUtils.isEmptyString(idList.get(0).get("id").toString())) {
            //查询疾病审批信息
            String upperId = idList.get(0).get("id").toString().toUpperCase();
            String lowerId = idList.get(0).get("id").toString().toLowerCase();
            listMap = DataBaseHelper.queryForList(
                    "select * from INS_SZYB_lbspxx where sfzh = ? or sfzh = ? order by yllb asc",
                    new Object[]{upperId, lowerId});
        }

        return listMap;
    }

    /**
     * 更新患者分类
     * 交易号：015001005038
     *
     * @param param
     * @param user
     */
    public void updatePvCate(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");

        if (!CommonUtils.isEmptyString(pkPv)) {
            DataBaseHelper.execute(
                    "update PV_ENCOUNTER set PK_PICATE = (select PK_PICATE from pi_cate where code = '04' and del_flag = '0') where pk_pv = ?",
                    new Object[]{pkPv});
        }
    }

    /**
     * 查询贫困患者分类\
     * 交易码：015001005039
     *
     * @param param
     * @param user
     * @return
     */
    public String searchPkCate(String param, IUser user) {
        String pkPicate = null;
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        //1:五保  2:贫困  3:贫困+五保
        String euPvtype = JsonUtil.getFieldValue(param, "euPvtype");
        
        if(!CommonUtils.isEmptyString(euPvtype)){
        	//患者分类编码
        	String cateCode = "";
        	
        	if("3".equals(euPvtype) || "2".equals(euPvtype))
        	{
        		String pkhzcode = ApplicationUtils.getSysparam("PI0015", true);
        		if (CommonUtils.isEmptyString(pkhzcode)) {
        			throw new BusException("患者城镇居民返回为贫困患者，请维护PI0015，指定患者贫困分类");
        		}
        		
        		pkhzcode.trim();
                String[] poorCodeArray = pkhzcode.split(",");
                
                if("3".equals(euPvtype)){//贫困+五保
                	cateCode = poorCodeArray[1];
                }else{ //贫困
                	cateCode = poorCodeArray[0];
                }
	            
        	}else if("1".equals(euPvtype)){
                String wbhCode = ApplicationUtils.getSysparam("PI0018", true);
                if(CommonUtils.isEmptyString(wbhCode)){
                	throw new BusException("患者城镇居民返回为五保户患者，请维护PI0018，指定患者贫困分类");
                }
                
                //五保户
                cateCode = wbhCode;
        	}
        	
        	Map<String, Object> retMap = DataBaseHelper.queryForMap(
	            "select PK_PICATE from pi_cate where code = ? and del_flag = '0'",
	            new Object[]{cateCode});
		    if (retMap != null && retMap.get("pkPicate") != null && !CommonUtils.isEmptyString(retMap.get("pkPicate").toString())) {
		        pkPicate = retMap.get("pkPicate").toString();
		    }
        }
        
//        String pkhzcode = ApplicationUtils.getSysparam("PI0015", true);
//        if (CommonUtils.isEmptyString(pkhzcode)) {
//            throw new BusException("患者城镇居民返回为贫困患者，请维护PI0015，指定患者贫困分类");
//        }
//        pkhzcode.trim();
//        String[] poorCodeStr = pkhzcode.split(",");
//        if ("".equals(pkPv)) {
//            Map<String, Object> retMap = DataBaseHelper.queryForMap(
//                    "select PK_PICATE from pi_cate where code = ? and del_flag = '0'",
//                    new Object[]{poorCodeStr[0]});
//            if (retMap != null && retMap.get("pkPicate") != null && !CommonUtils.isEmptyString(retMap.get("pkPicate").toString())) {
//                pkPicate = retMap.get("pkPicate").toString();
//
//            }
//        } else {
//            //查询此时患者分类，如果是五保，那么则是五保加贫困属性，
//            Map<String, Object> codeMap = DataBaseHelper.queryForMap(
//                    "select cate.code " +
//                            "from pv_encounter pv inner join pi_cate cate on pv.pk_picate = cate.pk_picate " +
//                            "where pv.pk_pv=?",
//                    new Object[]{pkPv});
//            if (codeMap != null && codeMap.get("code") != null && !CommonUtils.isEmptyString(codeMap.get("code").toString())) {
//                String wbhCode = ApplicationUtils.getSysparam("PI0018", true);
//                if (CommonUtils.isEmptyString(wbhCode)) {
//                    Map<String, Object> retMap = DataBaseHelper.queryForMap(
//                            "select PK_PICATE from pi_cate where code = ? and del_flag = '0'",
//                            new Object[]{poorCodeStr[0]});
//                    if (retMap != null && retMap.get("pkPicate") != null && !CommonUtils.isEmptyString(retMap.get("pkPicate").toString())) {
//                        pkPicate = retMap.get("pkPicate").toString();
//                    }
//                }else {
//                    wbhCode.trim();
//                    String[] wbhCodeStr = pkhzcode.split(",");
//                    String code = null;
//                    for (String co : wbhCodeStr) {
//                        if (codeMap.get("code").equals(co)) {
//                            code = poorCodeStr[1];
//                        } else {
//                            code = poorCodeStr[0];
//                        }
//
//                    }
//					Map<String, Object> retMap = DataBaseHelper.queryForMap(
//							"select PK_PICATE from pi_cate where code = ? and del_flag = '0'",
//							new Object[]{poorCodeStr[0]});
//					if (retMap != null && retMap.get("pkPicate") != null && !CommonUtils.isEmptyString(retMap.get("pkPicate").toString())) {
//						pkPicate = retMap.get("pkPicate").toString();
//					}
//                }
//            }else {
//                Map<String, Object> retMap = DataBaseHelper.queryForMap(
//                        "select PK_PICATE from pi_cate where code = ? and del_flag = '0'",
//                        new Object[]{poorCodeStr[0]});
//                if (retMap != null && retMap.get("pkPicate") != null && !CommonUtils.isEmptyString(retMap.get("pkPicate").toString())) {
//                    pkPicate = retMap.get("pkPicate").toString();
//                }
//
//            }
//        }
        return pkPicate;
    }


    /**
     * 校验患者医保登记号是否进行过结算
     * 交易号：015001005040
     *
     * @param param
     * @param user
     */
    public String checkStByPv(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        
        String errorMsg = null;
        //查询患者医保登记记录
        if (!CommonUtils.isEmptyString(pkPv)) {

            List<Map<String, Object>> pvMapList = DataBaseHelper.queryForList(
                    "select * from INS_SZYB_MZDJ where pk_pv = ? and del_flag='0'",
                    new Object[]{pkPv});

            if (pvMapList != null && pvMapList.size() > 0) {
                Map<String, Object> pvInfo = pvMapList.get(0);
                if (pvInfo != null && pvInfo.containsKey("lsh")
                        && pvInfo.get("lsh") != null) {

                    //查询该医保登记号是否有结算记录
                    String pvLsh = CommonUtils.getString(pvInfo.get("lsh"));//医保登记号
                    Integer chkStCnt = DataBaseHelper.queryForScalar(
                            "select count(1) from ins_szyb_js where ywlsh = ?",
                            Integer.class, new Object[]{pvLsh});

                    if (chkStCnt != null && chkStCnt > 0) {
                    	errorMsg = "该患者医保登记号已经进行过结算，请撤销上传费用，重新医保登记后再进行结算！";
                    }
                }
            }

        }
        
        return errorMsg;
    }

}
