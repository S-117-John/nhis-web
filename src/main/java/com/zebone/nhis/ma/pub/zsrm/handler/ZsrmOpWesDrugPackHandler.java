package com.zebone.nhis.ma.pub.zsrm.handler;

import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.support.WSUtil;
import com.zebone.nhis.ma.pub.zsrm.dao.ZsrmOpDrugPackPubMapper;
import com.zebone.nhis.ma.pub.zsrm.support.ZsrmMsgUtils;
import com.zebone.nhis.ma.pub.zsrm.vo.DrugQryVo;
import com.zebone.nhis.ma.pub.zsrm.vo.DrugQryVo.WesDrugConfVo;
import com.zebone.nhis.ma.pub.zsrm.vo.MachDrug;
import com.zebone.nhis.ma.pub.zsrm.vo.MachDrug.MachDrugHeader;
import com.zebone.nhis.webservice.zsrm.utils.InputParseTool;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 西药处理
 */
@Service
public class ZsrmOpWesDrugPackHandler {
    private static Logger log = LoggerFactory.getLogger("nhis.drug.pack");

    @Value("#{applicationProperties['scm.opdt.wespack.webservice.url']}")
    private String wesDrugUrl;
    @Resource
    private ZsrmOpDrugPackPubMapper zsrmOpDrugPackPubMapper;


    public void sendOrderId(Map<String, Object> paramMap) {
        if(!"1".equals(MapUtils.getString(paramMap,"IsPackMachine")))return;
        List<String> pkPresocces= (List<String>) paramMap.get("pkPresocces");

        if(pkPresocces==null ||pkPresocces.size()==0)return;
        List<WesDrugConfVo> configList = ZsrmMsgUtils.getConfig();
        //有配置药房、或者窗口号才去查询
        if(configList.size()>0){
            DrugQryVo drugQryVo = new DrugQryVo();
            drugQryVo.setConfigs(configList);
            drugQryVo.setPkPressocc(pkPresocces);
            String orderId = zsrmOpDrugPackPubMapper.getWesOrder(drugQryVo);
            if(StringUtils.isNotBlank(orderId)) {
                log.info("sendOrderId：{}",orderId);
                try {
                    //Thread.sleep(1000L);
                    Object objtemp = WSUtil.invoke(wesDrugUrl, "OrderRequest", orderId);
                } catch (Exception e) {
                    log.info("摆药机接口异常："+e.getMessage());
                }
            }
        }
    }


    /**
     * 推送仓库药品变更给摆药机
     * @param args
     */
    public void sendDrugInfo(Object... args) throws Exception {
        if(args==null||args.length==0|| !(args[0] instanceof List)){
            log.info("sendDrugInfo未获取到有效入参");
            return;
        }
        List<BdPdStore> pdStoreList = (List<BdPdStore>) args[0];
        List<WesDrugConfVo> config = ZsrmMsgUtils.getConfig();
        if(CollectionUtils.isNotEmpty(pdStoreList) && CollectionUtils.isNotEmpty(config)) {
            Map<String, Object> deptMap = DataBaseHelper.queryForMap("select dept.CODE_DEPT from BD_PD_STORE sto inner join BD_OU_DEPT dept on sto.PK_DEPT=dept.PK_DEPT where sto.PK_PDSTORE=?",
                    new Object[]{pdStoreList.get(0).getPkPdstore()});
            String codeDept = MapUtils.getString(deptMap, "codeDept");
            boolean flag = config.stream().filter(x -> StringUtils.equals(x.getCodeDept(), codeDept)).count() > 0;
            if (!flag) {
                log.info("sendDrugInfo未匹配到需要发送数据的仓库");
                return;
            }

            WSUtil.invoke(wesDrugUrl,"UpdateDrugList",null);
        }
    }



}
