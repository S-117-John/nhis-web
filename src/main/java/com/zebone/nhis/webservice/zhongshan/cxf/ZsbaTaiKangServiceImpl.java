package com.zebone.nhis.webservice.zhongshan.cxf;

import com.zebone.nhis.webservice.zhongshan.service.TaiKangService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wq
 * @Classname ZsbaTaiKangServiceImpl
 * @Description 泰康接口实现
 * @Date 2020-11-24 9:49
 */
public class ZsbaTaiKangServiceImpl implements IZsbaTaiKangService {

    private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");

    @Autowired
    private TaiKangService taiKangService;

    /**
     * @Description 泰康人寿接口实现
     * @auther wuqiang
     * @Date 2020-11-24
     * @Param [inxml,
     * tradetype：01 入院信息提取
     * 02 处方提取
     * 04 医疗目录信息提取
     * 05 出院信息提取，医院结算信息
     * 07 电子病历提取
     * 08 理赔金回写]
     * @return java.lang.String
     */
    @Override
    public String GetTradeInfo(String inxml, String tradetype) {
        boolean boolQuery= StringUtils.isAnyBlank(inxml)||StringUtils.isAnyBlank(tradetype);
         if (boolQuery){
             logger.error("ZsbaTaiKangServiceImpl-GetTradeInfo 参数为空");
             return "";
         }
         String retuxml="";
          switch (tradetype){
              case "01":
                  retuxml=taiKangService.getPatientAdmInf(inxml);
                  break;
              case "02":
                  retuxml=taiKangService.getPatientCostInfList(inxml);
                  break;
              case "04":
                  retuxml=taiKangService.getTaiKangMedicalDirectoryList(inxml);
                  break;
              case "05":
                  retuxml=taiKangService.getPatientOutInf(inxml);
                  break;
              case "07":
            	  retuxml = taiKangService.getEmrAdmitRecs(inxml);
                  break;
              case "08":
                  retuxml=taiKangService.writeSettleInfomation(inxml);
                  break;

              default:
                  break;

          }

        return retuxml;
    }


}
