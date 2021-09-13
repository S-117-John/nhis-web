package com.zebone.nhis.scm.dict.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.scm.dict.dao.PdBdMapMapper;
import com.zebone.nhis.scm.dict.vo.BdPdMapVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PdBdMapService {


    @Resource
    private PdBdMapMapper pdBdMapMapper;
    /**
     * 写入草药饮片和颗粒对照表
     */
    public void savePdbdMap(String param, IUser user){
        List<BdPdMapVo> bdPdMapList = JsonUtil.readValue(param, new TypeReference<List<BdPdMapVo>>() {});
        if(bdPdMapList==null ||bdPdMapList.size()==0)return;
        List<BdPdMapVo> insert_list=new ArrayList<>();
        List<BdPdMapVo> update_list=new ArrayList<>();
        for (BdPdMapVo mapvo:bdPdMapList) {
            if(CommonUtils.isNull(mapvo.getPkPdtrans())){
                mapvo.setCreator(UserContext.getUser().getPkEmp());
                mapvo.setCreateTime(new Date());
                mapvo.setDelFlag("0");
                mapvo.setTs(new Date());
                mapvo.setPkPdtrans(NHISUUID.getKeyId());
                mapvo.setPkOrg("~                               ");
                insert_list.add(mapvo);
            }else{
                mapvo.setModifier(UserContext.getUser().getPkEmp());
                mapvo.setModity_time(new Date());
                mapvo.setTs(new Date());
                update_list.add(mapvo);
            }
        }
        if(insert_list.size()>0){
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdMapVo.class),insert_list);
        }
        if(update_list.size()>0){
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdPdMapVo.class),update_list);
        }
    }


    /**
     * 删除草药饮片和颗粒对照表
     */
    public void delPdbdMap(String param, IUser user){
        BdPdMapVo bdPdMapVo = JsonUtil.readValue(param, BdPdMapVo.class);

        DataBaseHelper.execute("delete from  BD_PD_TRANS where PK_PDTRANS = '"+bdPdMapVo.pkPdtrans+"' ");
    }


    /**
     * 查询草药饮片和颗粒对照表
     */
    public List<BdPdMapVo> queryPdbdMap(String param, IUser user) {

        return pdBdMapMapper.qryHerbPdRateList();
    }



}
