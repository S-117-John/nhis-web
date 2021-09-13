package com.zebone.nhis.ma.pub.zsrm.service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.ma.pub.zsrm.dao.AtfYpxxDetailMapper;
import com.zebone.nhis.ma.pub.zsrm.vo.AtfYpPageNo;
import com.zebone.nhis.ma.pub.zsrm.vo.AtfYpxx;
import com.zebone.nhis.ma.pub.zsrm.vo.AtfYpxxDetailVo;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *全自动包药机
 **/
@Service
public class ZsrmIpAutoPackagePlantService {
    @Resource
    AtfYpxxDetailMapper atfYpxxDetailMapper;
    @Autowired
    AtfYpxx atfYpxx;
    @Autowired
    AtfYpPageNo atfYpPageNo;
    /**
     * 根据请领明细主键查询数据
     */
    public List<AtfYpxxDetailVo> getPackagePlant(PdDeDrugVo pdDeDrugVo){
        List<AtfYpxxDetailVo> atfYpxxDetailVoList=atfYpxxDetailMapper.getPackagePlant(pdDeDrugVo);
        if (atfYpxxDetailVoList.size()==0){
            throw new BusException("查不到此条记录");
        }
        List<Map<String, Object>> sumQuanOcc = atfYpxxDetailMapper.getSumQuanOcc(pdDeDrugVo);
        for (AtfYpxxDetailVo detailVo:atfYpxxDetailVoList){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date occTime = detailVo.getOccTime();
            String format = simpleDateFormat.format(occTime);
            for (Map<String, Object> map:sumQuanOcc){
                if (map.get("DATEPLAN")==null){
                    throw new BusException("时间为null 无法获取Total");
                }
                if(format.equals(map.get("DATEPLAN"))){
                    detailVo.setTotal(Double.valueOf(map.get("QUANOCC").toString()));
                    break;
                }
            }
        }
        return atfYpxxDetailVoList;
    }
    /**
     * 向第三方SQL server插入数据
     */
    public void savePackagePlant(List<AtfYpxxDetailVo> atfYpxxDetailVo){
        List<AtfYpxx> atfYpxxByDetail = atfYpxx.getAtfYpxxByDetail(atfYpxxDetailVo);
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(AtfYpxx.class), atfYpxxByDetail);
        List<AtfYpPageNo> atfYpPageNoByDetail = atfYpPageNo.getAtfYpPageNoByDetail(atfYpxxDetailVo);
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(AtfYpPageNo.class), atfYpPageNoByDetail);
    }
    public PdDeDrugVo getPdDeDrugVo(PdDeDrugVo pdDeDrugVo){
        List<PdDeDrugVo> pdDeDrugVoList = atfYpxxDetailMapper.getPdDeDrugVo(pdDeDrugVo.getCodeDe());
        if (pdDeDrugVoList.size()<0){
            throw new BusException("查不到此条记录或有多条记录");
        }
        return pdDeDrugVoList.get(0);
    }
}

