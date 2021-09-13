package com.zebone.nhis.cn.ipdw.service;

import com.zebone.nhis.bl.pub.vo.BlIpDtSptVo;
import com.zebone.nhis.cn.ipdw.dao.ExOpOccMapper;
import com.zebone.nhis.cn.ipdw.vo.CnOpApplyVo;
import com.zebone.nhis.cn.ipdw.vo.ExOpOccVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.nhis.common.module.cn.ipdw.ExOpOcc;
import com.zebone.nhis.common.module.cn.ipdw.ZsbaOpSpd;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class ExOpOccService {

    @Autowired
    private ExOpOccMapper exOpOccMapper;


    /**
     * 手术执行记录查询
     * @param param
     * @param user
     * @return
     */
    public List<ExOpOccVo> queryExOpOccList(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String  dateBegin = CommonUtils.getString(paramMap.get("dateBegin"));
		if(dateBegin == null){
			paramMap.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		}else{
			paramMap.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		} 
		String dateEnd = CommonUtils.getString(paramMap.get("dateEnd"));
		if(dateEnd == null){
			paramMap.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		}else{
			paramMap.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
        List<ExOpOccVo> exOpOccVoList = exOpOccMapper.queryExOpOccList(paramMap);
        for(ExOpOccVo exOpOccVo : exOpOccVoList){
            String pkCnOrd = exOpOccVo.getPkCnord();
            Map<String,Object> map = new HashMap<>();
            map.put("pkCnord",pkCnOrd);
            List<CnOpSubjoin> queSubOpList = exOpOccMapper.queSubOpListByPk(map);
            exOpOccVo.setSubOpList(queSubOpList);
        }
        return exOpOccVoList;
    }


    /**
     * 手术申请单列表查询
     * @param param
     * @param user
     * @return
     */
    public List<CnOpApplyVo> queryCnOpApplyList(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String  dateBegin = CommonUtils.getString(paramMap.get("dateBegin"));
        if(dateBegin == null){
            paramMap.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
        }else{
            paramMap.put("dateBegin", dateBegin.substring(0, 8)+"000000");
        }
        String dateEnd = CommonUtils.getString(paramMap.get("dateEnd"));
        if(dateEnd == null){
            paramMap.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
        }else{
            paramMap.put("dateEnd", dateEnd.substring(0, 8)+"235959");
        }
        List<CnOpApplyVo> cnOpApplyVoList = exOpOccMapper.queryCnOpApplyList(paramMap);
        for(CnOpApplyVo cnOpApplyVo : cnOpApplyVoList){
            String pkOrdop = cnOpApplyVo.getPkOrdop();
            Map<String,Object> map = new HashMap<>();
            map.put("pkOrdop",pkOrdop);
            List<CnOpSubjoin> queSubOpList = exOpOccMapper.queSubOpListByPk(map);
            cnOpApplyVo.setSubOpList(queSubOpList);
        }
        return cnOpApplyVoList;
    }


    /**
     * 保存手术执行记录
     * @param param
     * @param user
     */
    public CnOpApplyVo saveCnOpApply(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        CnOpApplyVo cnOpApplyVo= JsonUtil.readValue(param, CnOpApplyVo.class);
        ExOpOcc exOpOccVo = new ExOpOcc();
        User userInfo = (User)user;
        Date dateNow = new Date();
        String pkOrdOp = cnOpApplyVo.getPkOrdop();
        List<CnOpSubjoin> subjoinList = cnOpApplyVo.getSubOpList();
        List<CnOpSubjoin> subOpListForDel = cnOpApplyVo.getSubOpListForDel();
        String querySql = "select * from CN_OP_APPLY where PK_ORDOP = ?";
        CnOpApply cnOpApply = DataBaseHelper.queryForBean(querySql,CnOpApply.class,pkOrdOp);
        if(cnOpApply != null){
            String pkCnOrd = cnOpApply.getPkCnord();
            String queOccSql = "select * from EX_OP_OCC where PK_CNORD = ?";
            ExOpOcc exOpOcc = DataBaseHelper.queryForBean(queOccSql,ExOpOcc.class,pkCnOrd);
            BeanUtils.copyProperties(exOpOccVo,cnOpApply);
            exOpOccVo.setPkPv(cnOpApplyVo.getPkPv());
            exOpOccVo.setDateBegin(cnOpApplyVo.getDateBegin());
            exOpOccVo.setEuOptype(cnOpApplyVo.getEuOptype());
            exOpOccVo.setPkDiagPre(cnOpApplyVo.getPkDiagPre());
            exOpOccVo.setDescDiagPre(cnOpApplyVo.getDescDiagPre());
            exOpOccVo.setPkOp(cnOpApplyVo.getPkOp());
            exOpOccVo.setDescOp(cnOpApplyVo.getDescOp());
            exOpOccVo.setDtOplevel(cnOpApplyVo.getDtOplevel());
            exOpOccVo.setDtAnae(cnOpApplyVo.getDtAnae());
            exOpOccVo.setPkEmpPhyOp(cnOpApplyVo.getPkEmpPhyOp());
            exOpOccVo.setNameEmpPhyOp(cnOpApplyVo.getNameEmpPhyOp());
            exOpOccVo.setPkEmpAsis(cnOpApplyVo.getPkEmpAsis());
            exOpOccVo.setNameEmpAsis(cnOpApplyVo.getNameEmpAsis());
            exOpOccVo.setPkEmpAsis2(cnOpApplyVo.getPkEmpAsis2());
            exOpOccVo.setNameEmpAsis2(cnOpApplyVo.getNameEmpAsis2());
            exOpOccVo.setPkEmpAsis3(cnOpApplyVo.getPkEmpAsis3());
            exOpOccVo.setNameEmpAsis3(cnOpApplyVo.getNameEmpAsis3());
            exOpOccVo.setPkEmpAnae(cnOpApplyVo.getPkEmpAnae());
            exOpOccVo.setNameEmpAnae(cnOpApplyVo.getNameEmpAnae());
            exOpOccVo.setPkEmpScrub(cnOpApplyVo.getPkEmpScrub());
            exOpOccVo.setNameEmpScrub(cnOpApplyVo.getNameEmpScrub());
            exOpOccVo.setPkEmpCircul(cnOpApplyVo.getPkEmpCircul());
            exOpOccVo.setNameEmpCricul(cnOpApplyVo.getNameEmpCricul());
            exOpOccVo.setDtAsepsis(cnOpApplyVo.getDtAsepsis());
            exOpOccVo.setDtPosi(cnOpApplyVo.getDtPosi());
            exOpOccVo.setNote(cnOpApplyVo.getNote());
            exOpOccVo.setNote(cnOpApplyVo.getNote());
            exOpOccVo.setDelFlag("0");
            exOpOccVo.setTs(dateNow);
            exOpOccVo.setDateEnd(cnOpApplyVo.getDateEnd());
            exOpOccVo.setPkEmpTech(cnOpApplyVo.getPkEmpTech());
            exOpOccVo.setNameEmpTech(cnOpApplyVo.getNameEmpTech());
            exOpOccVo.setDsaNo(cnOpApplyVo.getDsaNo());
            exOpOccVo.setPkDiagAf(cnOpApplyVo.getPkDiagAf());
            exOpOccVo.setDescDiagAf(cnOpApplyVo.getDescDiagAf());
            exOpOccVo.setDtOpmode(cnOpApplyVo.getDtOpmode());//手术方式
            if(exOpOcc != null && exOpOcc.getPkOpocc() != null){
                exOpOccVo.setPkOpocc(exOpOcc.getPkOpocc());
                exOpOccVo.setModifier(userInfo.getPkEmp());
                exOpOccVo.setModityTime(dateNow);
                DataBaseHelper.updateBeanByPk(exOpOccVo,false);
            }else{
                exOpOccVo.setPkOpocc(NHISUUID.getKeyId());
                exOpOccVo.setCreator(userInfo.getPkEmp());
                exOpOccVo.setCreateTime(dateNow);
                DataBaseHelper.insertBean(exOpOccVo);
            }
        }

        List<CnOpSubjoin> addCnOpSubjoin = new ArrayList<CnOpSubjoin>();
        List<CnOpSubjoin> updateCnOpSubjoin = new ArrayList<CnOpSubjoin>();
        if(subjoinList != null){
            for(CnOpSubjoin cnOpSub: subjoinList ){
                cnOpSub.setPkOrdop(pkOrdOp);
                CnOpSubjoin subOp = new CnOpSubjoin();
                BeanUtils.copyProperties(subOp, cnOpSub);
                subOp.setDelFlag("0");
                subOp.setTs(dateNow);
                if(StringUtils.isBlank(subOp.getPkCnopjoin())){
                    subOp.setPkCnopjoin(NHISUUID.getKeyId());
                    subOp.setCreateTime(dateNow);
                    subOp.setCreator(userInfo.getPkEmp());
                    addCnOpSubjoin.add(subOp);
                }else{
                    subOp.setModifier(userInfo.getPkEmp());
                    subOp.setModityTime(dateNow);
                    updateCnOpSubjoin.add(subOp);
                }
            }
        }

        if (subOpListForDel != null && subOpListForDel.size() > 0) {
            DataBaseHelper.batchUpdate("delete from cn_op_subjoin where pk_cnopjoin = :pkCnopjoin ", subOpListForDel);
        }
        if (addCnOpSubjoin != null && addCnOpSubjoin.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOpSubjoin.class), addCnOpSubjoin);
        }
        if (updateCnOpSubjoin != null && updateCnOpSubjoin.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOpSubjoin.class), updateCnOpSubjoin);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("pkOrdop",pkOrdOp);
        List<CnOpSubjoin> rtnCnOpSubjoinList = exOpOccMapper.queSubOpListByPk(map);
        cnOpApplyVo.setSubOpList(rtnCnOpSubjoinList);

        return cnOpApplyVo;
    }



    /**
     * 耗材登记列表查询
     * @param param
     * @param user
     * @return
     */
    public List<BlIpDtSptVo> queryBlIpDtSptList(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<BlIpDtSptVo> blIpDtSptVoList = exOpOccMapper.queryBlIpDtSptList(paramMap);
        return blIpDtSptVoList;
    }


    /**
     * 保存手术耗材登记
     * @param param
     * @param user
     */
    public void saveZsbaOpSpdList(String param, IUser user){
        User userInfo = (User)user;
        Date nowDate = new Date();
        List<ZsbaOpSpd> zsbaOpSpds = JsonUtil.readValue(param, new TypeReference<List<ZsbaOpSpd>>() {});
        if(zsbaOpSpds.size() > 0){
            String pkOpocc = zsbaOpSpds.get(0).getPkOpocc();
            DataBaseHelper.execute("delete from ZSBA_OP_SPD where pk_opocc = ? ", new Object[]{pkOpocc});
            for(ZsbaOpSpd zsbaOpSpd : zsbaOpSpds){
                zsbaOpSpd.setPkOpspd(NHISUUID.getKeyId());
                zsbaOpSpd.setCreator(userInfo.getPkEmp());
                zsbaOpSpd.setPkOrg(userInfo.getPkOrg());
                zsbaOpSpd.setCreateTime(nowDate);
                zsbaOpSpd.setDelFlag("0");
                zsbaOpSpd.setTs(nowDate);
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ZsbaOpSpd.class), zsbaOpSpds);
        }
    }
}
