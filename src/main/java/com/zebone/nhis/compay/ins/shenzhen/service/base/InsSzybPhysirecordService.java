package com.zebone.nhis.compay.ins.shenzhen.service.base;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybDictMap;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybPhysirecord;
import com.zebone.nhis.compay.ins.shenzhen.dao.base.InsSzybPhysirecordDao;
import com.zebone.nhis.compay.ins.shenzhen.dto.InsSzybDictMapDTO;
import com.zebone.nhis.compay.ins.shenzhen.dto.InsSzybPhysirecordDTO;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DBHelper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InsSzybPhysirecordService {

    @Autowired
    private InsSzybPhysirecordDao insSzybPhysirecordDao;

    /**
     * 医药师信息登记
     * 015001011066
     * InsSzybPhysirecordService.save
     * @param param
     * @param user
     * @return
     */
    public int save(String param, IUser user){
        InsSzybPhysirecord dto = JsonUtil.readValue(param,InsSzybPhysirecord.class);
        String sql = DataBaseHelper.getInsertSql(InsSzybPhysirecord.class);
        dto.setDelFlag("0");
        int result = DataBaseHelper.insertBean(dto);

        return result;
    }

    /**
     * 返回结果处理标志=1并且根据ins_szyb_dictmap.code_his、返回值：社保编码 判断是否存在对照，若不存在
     * InsSzybPhysirecordService.saveDictMap
     * 015001011067
     * @param param
     * @param user
     * @return
     */
    public int saveDictMap(String param, IUser user){
        InsSzybDictMap dto = JsonUtil.readValue(param,InsSzybDictMap.class);
        dto.setDelFlag("0");
        int result = DataBaseHelper.insertBean(dto);
        return result;
    }

    /**
     * 医药师执业状态变更
     * InsSzybPhysirecordService.updateBke155
     * 015001011068
     * @param param
     * @param user
     * @return
     */
    public int updateBke155(String param, IUser user){
        Map<String,Object> map = JsonUtil.readValue(param,Map.class);
        String dbType = MultiDataSource.getCurDbType();
        map.put("dbType",dbType);
        map.put("updater",user.getId());
        int result = insSzybPhysirecordDao.updateBke155(map);
        return result;
    }

    /**
     * 医药师信息更新
     * InsSzybPhysirecordService.update
     *
     * @param param
     * @param user
     * @return
     */
    public int update(String param, IUser user){
        InsSzybPhysirecord isps = JsonUtil.readValue(param,InsSzybPhysirecord.class);
        int result = DataBaseHelper.updateBeanByPk(isps,false);

//        int result = DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsSzybPhysirecordDTO.class),dto);
        return result;
    }

    /**
     * 根据主键查询
     * InsSzybPhysirecordService.selectByPrimaryKey
     * 015001011069
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> selectByPrimaryKey(String param, IUser user){
        String pkInsdictmap = JsonUtil.getFieldValue(param,"pkInsdictmap");
        List<Map<String,Object>> result = insSzybPhysirecordDao.selectByPrimaryKey(pkInsdictmap);
        return result;
    }

    /**
     * 条件查询INS_SZYB_DICTMAP表
     * 015001011070
     * InsSzybPhysirecordService.findInsSzybDictMap
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> findInsSzybDictMap(String param, IUser user){
        Map<String,Object> map = JsonUtil.readValue(param,Map.class);
        List<Map<String,Object>> result = insSzybPhysirecordDao.findInsSzybDictMap(map);
        return result;
    }

    /**
     * 删除
     * InsSzybPhysirecordService.delete
     * 015001011072
     * @param param
     * @param user
     * @return
     */
    public int delete(String param, IUser user){
        Map<String,Object> map = JsonUtil.readValue(param,Map.class);
        int resultRecord = insSzybPhysirecordDao.delete(map);
        int resultDict = insSzybPhysirecordDao.deleteDictMap(map);
        return resultDict+resultRecord;
    }
}
