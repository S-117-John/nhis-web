package com.zebone.nhis.base.drg.service;

import com.zebone.nhis.base.drg.dao.BdTermCchiMapper;
import com.zebone.nhis.common.module.base.bd.drg.BdTermCchi;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
/**
 * CCHI字典维护
 */
public class BdTermCchiService {

    @Autowired
    private BdTermCchiMapper bdTermCchiMapper;
    /**
     * 查询CCHI
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryTermCchi(String param , IUser user) {
        Map map = JsonUtil.readValue(param, Map.class);

        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int pageIndex = Integer.parseInt(map.get("pageIndex").toString());
        MyBatisPage.startPage(pageIndex, pageSize);

        List<Map<String,Object>> cchiList = bdTermCchiMapper.qryTermCchi(map);
        Page<List<Map<String,Object>>> page = MyBatisPage.getPage();

        Map<String, Object> result = new HashMap<>();
        result.put("cchiList", cchiList);
        result.put("totalCount", page.getTotalCount());

        return result;
    }

    /***
     * 删除CCHI
     * @param param
     * @param user
     */
    public void deleteTermCchi (String param, IUser user) {
        List<BdTermCchi> cchiList = JsonUtil.readValue(param,new TypeReference<List<BdTermCchi>>(){});
        for (BdTermCchi cchi : cchiList) {
            //删除校验
            int count1 = DataBaseHelper.queryForScalar("select count(1) from bd_cchi_item where pk_cchi = ?", Integer.class , cchi.getPkCchi());
            int count2 = DataBaseHelper.queryForScalar("select count(1) from cn_cchi  where pk_cchi=?", Integer.class , cchi.getPkCchi());
            if ((count1 + count2) == 0){
                DataBaseHelper.execute("delete from bd_term_cchi where pk_cchi= ?  ", new Object[] { cchi.getPkCchi() });
            }else{
                throw new BusException("CCHI编码为："+cchi.getCodeCchi()+"，存在关联，删除失败！");
            }
        }


    }

    /***
     * 保存或修改
     * @param param
     * @param user
     */
    public void saveCchiList(String param , IUser user){
        BdTermCchi bdTermCchi = JsonUtil.readValue(param, BdTermCchi.class);
        User us = (User)user;
            if (StringUtils.isNotBlank(bdTermCchi.getPkCchi())) {
                bdTermCchi.setModifier(us.getPkEmp());
                DataBaseHelper.updateBeanByPk(bdTermCchi,false);
            }else {
                bdTermCchi.setPkOrg("~                               ");
                bdTermCchi.setCreator(us.getPkEmp());
                bdTermCchi.setCreateTime(new Date());
                bdTermCchi.setFlagDel("0");
                DataBaseHelper.insertBean(bdTermCchi);
            }
        }


}
