package com.zebone.nhis.base.pub.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.pub.dao.SerialNoMapper;
import com.zebone.nhis.base.pub.vo.SerialNoVO;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class SerialNoService {

    @Autowired
    private SerialNoMapper serialNoMapper;

    /**
     * 查询序列号信息
     * SerialNoService.getList
     * @return
     */
    public List<SerialNoVO> getList(String param , IUser user){
        List<SerialNoVO> list = serialNoMapper.getList();
        return list;
    }

    /**
     * 保存
     * 001002002105
     * @param param
     * @param user
     * @return
     */
    public void save(String param , IUser user){
        //int result = 0;
        List<SerialNoVO> serialNoList = JsonUtil.readValue(param, new TypeReference<List<SerialNoVO>>(){});
        for (SerialNoVO serialNo : serialNoList) {
            if(StringUtils.isNotEmpty(serialNo.getPkSerialno())){

                DataBaseHelper.update(DataBaseHelper.getUpdateSql(SerialNoVO.class),serialNo);
            }else {
                DataBaseHelper.insertBean(serialNo);
            }
        }


        //return result;
    }

    /**
     * 返回序列号值
     * @param param
     * @param user
     * @return
     */
     public List<SerialNoVO> getSerialVal(String param , IUser user){
         String tbName = JsonUtil.getFieldValue(param,"tbName");
         String fdName = JsonUtil.getFieldValue(param,"fdName");
         Map<String,Object> map = new HashMap<>();
         map.put("nameTb",tbName);
         map.put("nameFd",fdName);
         List<SerialNoVO> result = serialNoMapper.getSerialVal(map);
         return result;
    }

    /**
     * SerialNoService.delete
     * 001002002108
     * @param param
     * @param user
     * @return
     */
    public int delete(String param , IUser user){
         String id = JsonUtil.getFieldValue(param,"pkSerialno");
         int result = serialNoMapper.delete(id);
         return result;
    }

    /**
     * SerialNoService.init
     *
     * 001002002109
     * @return
     */
    //yangxue -注释
//    public int init(String param , IUser user){
//        int result = serialNoMapper.init();
//        return result;
//    }
}
