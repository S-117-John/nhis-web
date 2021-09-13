package com.zebone.nhis.bl.pub.service.impl;

import com.zebone.nhis.bl.pub.service.IWinnoDrugService;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.scm.pub.support.OpDrugPubUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("defaultWinnoService")
public class DefaultWinnoDrugServiceImpl implements IWinnoDrugService {
    @Override
    public Object getDrugWinno(Object... obj) {
        Map<String, List<BlOpDt>> mapPres=(Map<String, List<BlOpDt>>)obj[1];
        List<ExPresOcc> presOccList=(List<ExPresOcc>)obj[0];
        if(presOccList==null)return null;
        List<Map<String,Object>> winnoList=new ArrayList<>();

        mapPres.forEach((m,n)->{
           n.forEach(k->{
              boolean isCheck= winnoList.stream().filter(j->k.getPkDeptEx().equals(j.get("pkDeptEx"))
                       && k.getPkDeptApp().equals(j.get("pkDeptApp"))
                       ).findAny().isPresent();
              if(!isCheck && !CommonUtils.isEmptyString(k.getPkPres())){
                  Map<String,Object> winno_temp=new HashMap<>();
                  winno_temp.put("pkDeptEx",k.getPkDeptEx());
                  winno_temp.put("pkDeptApp",k.getPkDeptApp());
                  winno_temp.put("pkDeptAreaapp",k.getPkDeptAreaapp());
                  winnoList.add(winno_temp);
              }
           });
        });

        if(winnoList==null||winnoList.size()==0){
            return null;
        }

        Map<String,Map<String,Object>> deptMap=new HashMap<>();
        winnoList.forEach(m->{
            Map<String,Object> winMap = OpDrugPubUtils.getWin(CommonUtils.getString(m.get("pkDeptEx")), CommonUtils.getString(m.get("pkDeptApp")), CommonUtils.getString(m.get("pkDeptAreaapp")));

            String deptKey=MapUtils.getString(m,"pkDeptApp")+m.get("pkDeptEx");
            deptMap.put(deptKey,winMap);
        });

        if(deptMap==null || deptMap.size()==0){
            throw new BusException("窗口数据获取有误：请核对！");
        }

        presOccList.forEach(m->{
            String deptKey=m.getPkDeptPres()+m.getPkDeptEx();
            if(deptMap.containsKey(deptKey)){
                Map<String,Object> wiMap = new HashMap<>();
                wiMap = deptMap.get(deptKey);
                if(wiMap!=null && wiMap.size()>0){
                   String winnoPrep = CommonUtils.getString(wiMap.get("winnoPrep"));
                   String winnoConf=CommonUtils.getString(wiMap.get("winnoConf"));

                   m.setWinnoConf(winnoConf);
                   m.setWinnoPrep(winnoPrep);
                }
            }
        });

        String exSql="update ex_pres_occ set winno_conf=:winnoConf,winno_prep=:winnoPrep where pk_presocc=:pkPresocc";
        DataBaseHelper.batchUpdate(exSql,presOccList);
        return null;
    }
}
