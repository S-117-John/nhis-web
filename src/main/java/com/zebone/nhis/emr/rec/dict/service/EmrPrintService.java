package com.zebone.nhis.emr.rec.dict.service;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDocType;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by: gao shiheng
 * create time: 10:01 2019/6/20
 *查询病历打印次数、更新病历打印次数
 * @Param: null
 * @return
 */
@Service
public class EmrPrintService {

    /**
     * create by: gao shiheng
     * create time: 8:57 2019/6/20
     *查询打印次数
     * @Param: pkRec
     * @return printTimes
     */
    public Integer getEmrPrintTimes(String param, IUser user){
        String pkRec = JsonUtil.getFieldValue(param, "pkRec");
        Integer printTimes = 0;
        if(StringUtils.isBlank(pkRec)) throw new BusException("前台传的文档id(pkRec)为空!");
        /*1、判断是普通病历还是病程记录
         * 2、如果是普通病历直接返回
         * 3、如果是病程记录 返回首次病程的打印次数*/
        EmrMedRec emrMedRec = DataBaseHelper.queryForBean("SELECT PK_REC,NAME,TYPE_CODE,PK_PV,print_times FROM EMR_MED_REC WHERE PK_REC = ?",EmrMedRec.class,new Object[]{pkRec});
        EmrDocType emrDocType = DataBaseHelper.queryForBean("SELECT FLAG_COURSE,FLAG_FIRST FROM emr_doc_type WHERE CODE = ?",EmrDocType.class,new Object[]{emrMedRec.getTypeCode()});
        /*判断是普通病历还是病程记录*/
        if (emrDocType.getFlagCourse() == null || emrDocType.getFlagCourse() == ""){throw new BusException("该病历的flagCourse字段为null");}
        if (emrDocType.getFlagCourse().equals("0") || (emrDocType.getFlagCourse().equals("1") && (emrDocType.getFlagFirst() != null && emrDocType.getFlagFirst().equals("1")))){/*是普通病程记录或者是首程*/
            printTimes = emrMedRec.getPrintTimes();
        }else if (emrDocType.getFlagCourse().equals("1") && (emrDocType.getFlagFirst() ==null || !emrDocType.getFlagFirst().equals("1"))){/*是病程记录，但不是首程*/
            List<EmrMedRec> emrMedRecs = DataBaseHelper.queryForList("SELECT * FROM EMR_MED_REC WHERE PK_PV=?  AND DEL_FLAG = '0'",EmrMedRec.class,new Object[]{emrMedRec.getPkPv()});
            for (EmrMedRec medRec : emrMedRecs){
                EmrDocType emrDocType1 = DataBaseHelper.queryForBean("SELECT FLAG_COURSE,FLAG_FIRST FROM emr_doc_type WHERE CODE = ?",EmrDocType.class,new Object[]{medRec.getTypeCode()});
                if (emrDocType.getFlagCourse() == null || emrDocType.getFlagCourse() == ""){throw new BusException("该病历的flagCourse字段为null");}
                if (emrDocType1.getFlagCourse().equals("1") && emrDocType1.getFlagFirst()!=null && emrDocType1.getFlagFirst().equals("1")){/*找到这个患者的首程记录*/
                    printTimes = medRec.getPrintTimes();
                    break;
                }
            }
        }
        return printTimes;
    }

    /**
     * create by: gao shiheng
     * create time: 10:53 2019/6/20
     *更新打印次数
     * @Param: null
     * @return 
     */
    public Integer updateMedPrintTimes(String param, IUser user){
        String pkRec = JsonUtil.getFieldValue(param,"pkRec");
        String printTimes = JsonUtil.getFieldValue(param,"printTimes");
        if (StringUtils.isBlank(pkRec) || StringUtils.isBlank(printTimes))throw new BusException("前台传的参数为空!");
        /*1、如果是普通病历直接更新
        * 2、如果是病程记录 更新首次病程的打印次数*/
        EmrMedRec emrMedRec = DataBaseHelper.queryForBean("SELECT * FROM EMR_MED_REC WHERE PK_REC = ?",EmrMedRec.class,new Object[]{pkRec});
        EmrDocType emrDocType = DataBaseHelper.queryForBean("SELECT FLAG_COURSE,FLAG_FIRST FROM emr_doc_type WHERE CODE = ?",EmrDocType.class,new Object[]{emrMedRec.getTypeCode()});
        Map map = new HashMap();
        String sql = null ;
        int flag = 0;
        if (emrDocType.getFlagCourse().equals("0") || (emrDocType.getFlagCourse().equals("1") && (emrDocType.getFlagFirst() != null && emrDocType.getFlagFirst().equals("1")))){/*是普通病历记录或者是首程*/
            map.put("printTimes",printTimes);
            map.put("pkRec",pkRec);
            sql = "update emr_med_rec set print_times=:printTimes where pk_rec=:pkRec";
        }else if (emrDocType.getFlagCourse().equals("1") && (emrDocType.getFlagFirst() ==null || !emrDocType.getFlagFirst().equals("1"))){/*是病程记录*/
            List<EmrMedRec> emrMedRecs = DataBaseHelper.queryForList("SELECT PK_REC,TYPE_CODE,PRINT_TIMES FROM EMR_MED_REC WHERE PK_PV=?  AND DEL_FLAG = '0'",EmrMedRec.class,new Object[]{emrMedRec.getPkPv()});
            String pkRecFirst = null;
            for (EmrMedRec medRec : emrMedRecs){
                EmrDocType emrDocType1 = DataBaseHelper.queryForBean("SELECT FLAG_COURSE,FLAG_FIRST FROM emr_doc_type WHERE CODE = ?",EmrDocType.class,new Object[]{medRec.getTypeCode()});
                if (emrDocType1.getFlagCourse().equals("1") && emrDocType1.getFlagFirst()!=null&& emrDocType1.getFlagFirst().equals("1")){/*找到这个患者的首程记录*/
                    pkRecFirst = medRec.getPkRec();
                    break;
                }
            }
            /*更新首程打印次数*/
            map.put("printTimes",printTimes);
            map.put("pkRec",pkRecFirst);
            sql = "update emr_med_rec set print_times=:printTimes where pk_rec=:pkRec";
        }
        flag = flag+DataBaseHelper.update(sql,map);
        return flag;
    }
}
