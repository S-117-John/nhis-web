package com.zebone.nhis.ma.pub.zsrm.handler;

import com.alibaba.druid.support.json.JSONUtils;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.zsrm.vo.HightCommonResponse;
import com.zebone.nhis.ma.pub.zsrm.vo.HightQryReqVo;
import com.zebone.nhis.ma.pub.zsrm.vo.HightQryResVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Type;

/**
 * 中山人医高值耗材服务接口
 */
@Service
public class ZsrmHighConsumHandler {

    //URL 测试地址 http://192.168.21.241:8082/zs/etlInterface/getBarCode

    private static String URL= ApplicationUtils.getPropertyValue("ext.system.hight.url", "");
    public Object invokeMethod(String methodName,Object...args){
        Object obj=new Object();
        switch(methodName){
            case "saveConsumable"://高值耗材记费接口（含门诊/住院服务，注意特殊标志符）
                break;
            case "savaReturnConsumable": //高值耗材退费接口（含门诊/住院服务，注意特殊标志符）
                break;
            case "qryHightItemBybarcode"://查询高值耗材项目
                obj= this.qryHightItemBybarcode(args);
                break;
            case "checkHighValueConsum"://验证高值耗材数据
                break;
            case "qryHighIsDo": //查询当前可是是否可以开启高值耗材
                break;
        }
        return obj;


    }

    /**
     * 查询高值耗材项目
     * @param args
     * @return
     */
    private String  qryHightItemBybarcode(Object...args){
        //TODO 1.验证入参数据是否合法，是否为空
        if(args==null ||args.length==0 || CommonUtils.isNull(args[0])) {
            throw new BusException("条码录入为空，请重新录入！");
        }
        //TODO 2.验证bl_op_dt 费用明细中是否包含同样条码的明细，条码需要保证唯一性
        String chkSql="select count(1) from BL_OP_DT  where BARCODE=? and ROWNUM=1";
        Integer chkcnt= DataBaseHelper.queryForScalar(chkSql,Integer.class,new Object[]{args[0]});

        if(chkcnt>0){
            throw new BusException("当前录入条码已经使用，请重新核对条码！");
        }

        //TODO 3.调用高值webservice服务接口获取数据
        HightQryReqVo reqvo=new HightQryReqVo();
        String barcode=JsonUtil.getFieldValue(CommonUtils.getString(args[0]),"barcode");
        reqvo.setBarcode(barcode);
        String reqJson= JsonUtil.writeValueAsString(reqvo);

        String resJson= HttpClientUtil.sendHttpPostJson(URL+"getBarCode",reqJson);

        HightCommonResponse<HightQryResVo> response=JsonUtil.readValue(resJson, new TypeReference<HightCommonResponse<HightQryResVo>>() {});


        //TODO 4.根据返回结果，判断是否成功，不成功返回错误信息 ，成功根据数据反馈是否可以记费
        if(response.getSuccess() && response.getData()!=null) {//成功
            HightQryResVo resvo = response.getData();
            if (!"1".equals(resvo.getBarState())) {
                throw new BusException("当前条码[" + args[0] + "]，不是可记费状态，请核对！");
            }
            String codeItem = resvo.getMateCode();
            if (CommonUtils.isNull(codeItem)) {
                throw new BusException("当前条码未返回对应HIS收费项目编码，对应结构体字段[CHARGE_ITEM_CODE]为空！");
            }

            //TODO 5.根据返回的项目编码，获取对应医嘱项目主键返回，并做相应判断
            String itemChkSql="select pk_item from BD_ITEM where CODE=?";
            Map<String,Object> resItemMap=DataBaseHelper.queryForMap(itemChkSql,new Object[]{codeItem});
            if(resItemMap==null || CommonUtils.isNull(MapUtils.getString(resItemMap,"pkItem"))){
                throw new BusException("当前条码["+args[0]+"]，未对应HIS收费项目编码！");
            }

            String ordSql="select pk_ord from BD_ORD_ITEM where PK_ITEM=? and DEL_FLAG='0'";
            List<Map<String,Object>> ordList=DataBaseHelper.queryForList(ordSql,new Object[]{MapUtils.getString(resItemMap,"pkItem")});

            if(ordList==null || ordList.size()==0){
                throw new BusException("当前条码匹配的收费项目，未对照医嘱项目，请联系管理员维护！");
            }

            if(ordList.size()>1){
                throw new BusException("当前条码匹配的收费项目，存在多个医嘱组套，请联系管理员修改！");
            }

            if(CommonUtils.isNull(ordList.get(0).get("pkOrd"))){
                throw new BusException("数据异常，获取医嘱[pkOrd]为空！");
            }

            return MapUtils.getString(ordList.get(0),"pkOrd");

        }else{
            throw new BusException("SPD服务接口返回异常，失败原因："+response.getMessage());
        }
    }


}
