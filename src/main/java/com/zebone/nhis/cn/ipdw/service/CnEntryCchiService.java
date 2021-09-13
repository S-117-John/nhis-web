package com.zebone.nhis.cn.ipdw.service;



import com.zebone.nhis.cn.ipdw.dao.CnEntryCchiMapper;
import com.zebone.nhis.cn.ipdw.vo.BdTermCchiSaveParamVo;
import com.zebone.nhis.common.module.cn.ipdw.CnCchi;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CnEntryCchiService {

    @Autowired
    private CnEntryCchiMapper cnEntryCchiMapper;

    /**
     * 查询CCHI
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryTermCchi(String param , IUser user) {
        Map map = JsonUtil.readValue(param, Map.class);

        List<Map<String,Object>> pvCchi = cnEntryCchiMapper.queryPvCchi(map);


        return pvCchi;
    }

    /**
     * 保存并删除
     * @param param
     * @param user
     */
    public void saveTermCchi(String param , IUser user){
        BdTermCchiSaveParamVo saveParam = JsonUtil.readValue(param, BdTermCchiSaveParamVo.class);

        List<CnCchi> cnCchiList = saveParam.getCnCchiList();
        List<CnCchi> delCnCchiList = saveParam.getDelCnCchiList();

        if (delCnCchiList!=null) {
            for (CnCchi DeletePd : delCnCchiList)     //遍历实体类
            {
                DataBaseHelper.update("update cn_cchi set flag_del = '1', flag_maj = '0'  where pk_cncchi = ? ", new Object[] {DeletePd.getPkCncchi()  });
            }
        }
        if (cnCchiList!=null) {
            for (CnCchi termCchi : cnCchiList) {
                if (StringUtils.isBlank(termCchi.getPkCncchi())) {//新增
                    List<Map<String, Object>> EmpList = cnEntryCchiMapper.emp(termCchi.getPkEmpEntry());
                    for (Map<String, Object> map : EmpList) {
                        if (map.containsKey("pkEmp")){
                            termCchi.setPkEmpEntry(map.get("pkEmp").toString());
                        }else{
                            termCchi.setPkEmpEntry(null);
                        }
                        if (map.containsKey("nameUser")){
                            termCchi.setNameEmpEntry(map.get("nameUser").toString());
                        }else {
                            termCchi.setNameEmpEntry(null);
                        }
                    }
                    termCchi.setPkOrg("~");
                    termCchi.setFlagDel("0");
                    termCchi.setFlagDrg("1");
                    termCchi.setCreator(((User) user).getId());
                    termCchi.setCreateTime(new Date());
                    DataBaseHelper.insertBean(termCchi);
                } else {//修改
                    termCchi.setModifier(user.getUserName());
                    DataBaseHelper.updateBeanByPk(termCchi);
                }

            }
        }
    }

    /**
     * 返回最大序号
     * @param param
     * @param user
     * @return
     */
    public Integer getSortno(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        List<Integer> Sortnos = cnEntryCchiMapper.getSortno(pkPv);
        if (Sortnos.size() == 0 || Sortnos == null){
            return 1;
        }
        int Sortno = Sortnos.get(0);

        for (int i = 0; i < Sortnos.size(); i++) {
            if (Sortno < Sortnos.get(i)) {
                Sortno = Sortnos.get(i);
            }
        }
        return (Sortno + 1);
    }

    /**
     * 查询参照 部位、属性、修饰符
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> searchDefdoc(String param , IUser user) {
        Map map = JsonUtil.readValue(param, Map.class);

        List<Map<String,Object>> defdocVo = cnEntryCchiMapper.searchDefdoc(map);

        return defdocVo;
    }

    /**
     * 查询科室模板
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> searchTemp(String param , IUser user) {
        Map map = JsonUtil.readValue(param, Map.class);

        List<Map<String,Object>> tept = cnEntryCchiMapper.searchTemp(map);


        return tept;
    }
    /**
     * 查询医嘱模板
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> searchOrd(String param , IUser user) {
        Map map = JsonUtil.readValue(param, Map.class);

        List<Map<String,Object>> ord = cnEntryCchiMapper.searchOrd(map);


        return ord;
    }

    public Map<String, Object> check(String param, IUser user) {
        Map map = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> CnCchiList = cnEntryCchiMapper.check(map);
        List<Map<String, Object>> TermCchiList = cnEntryCchiMapper.sex(map);
        Map<String, Object> result = new HashMap<>();
        result.put("cnCchiList", CnCchiList);
        result.put("termCchiList",TermCchiList);
        return result;


    }



}
