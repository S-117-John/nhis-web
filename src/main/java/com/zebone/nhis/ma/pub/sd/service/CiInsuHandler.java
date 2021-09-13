package com.zebone.nhis.ma.pub.sd.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商保业务接口处理类
 * @author Administrator
 *
 */
@Service
public class CiInsuHandler {

    @Resource
    private CmrInsuService cmrInsuService;

    /**
     * 业务处理方法转换器
     * @param methodName
     * @param args
     */
    public Object invokeMethod(String methodName,Object...args){
        Object result = null;
        switch(methodName){
            case "sendS200":
                result = cmrInsuService.sendS200(args);//客户身份校验
                break;
            case "sendS210":
                result = cmrInsuService.sendS210(args);//建立就诊档案
                break;
            case "sendS230":
                result = cmrInsuService.sendS230(args);//修改就诊档案
                break;
            case "sendS240":
                cmrInsuService.sendS240(args);//撤销就诊档案
                break;
            case "sendS250":
                cmrInsuService.sendS250(args);//商保费用明细上传
                break;
            case "sendS260":
                cmrInsuService.sendS260(args);//撤销商保费用明细上传
                break;
            case "sendS290":
                result = cmrInsuService.sendS290(args);//商保预结算
                break;
            case "sendS300":
                result = cmrInsuService.sendS300(args);//理赔申请
                break;
            case "sendS270":
                cmrInsuService.sendS270(args);//取消结算
                break;
            case "sendS310":
                cmrInsuService.sendS310(args);//商保理赔确认
                break;
            case "sendS340":
                cmrInsuService.sendS340(args);//病历信息上传
                break;

        }

        return result;
    }
}
