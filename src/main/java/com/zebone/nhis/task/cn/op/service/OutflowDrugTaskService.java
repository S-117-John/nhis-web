package com.zebone.nhis.task.cn.op.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.DrugVo;
import com.zebone.nhis.pro.zsba.cn.opdw.service.PresOutflowService;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.OutflowDownResultVo;
import com.zebone.nhis.task.bl.dao.BlCcTaskMapper;
import com.zebone.nhis.task.cn.op.dao.ZsbaOpTaskMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 博爱同步药品对照信息
 */
@Service
public class OutflowDrugTaskService {

	@Resource
	private  ZsbaOpTaskMapper zsbaOpTaskMapper;
	
    @Resource
    private PresOutflowService presOutflowService;

    public Map downloadDrug(QrtzJobCfg cfg){
        String pkOrg = cfg.getJgs();
        if (StringUtils.isBlank(pkOrg)) {
            throw new BusException("请先对任务授权");
        }
        if (pkOrg != null && pkOrg.contains(",")) {
            pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(), "").replace(",", "");
        } else if (CommonUtils.getGlobalOrg().equals(pkOrg)) {
            throw new BusException("请将任务授权给具体机构");
        }
        User user = new User();
        user.setPkOrg(pkOrg);
        user.setPkEmp("task");
        UserContext.setUser(user);
        Map<String, String> result = new HashMap<String, String>();
        List<DrugVo> list = (List<DrugVo>)ExtSystemProcessUtils.processExtMethod("preOutflow","getOutCompare");
        OutflowDownResultVo outflowDrugVo = presOutflowService.addOutDrug(list);
        if(outflowDrugVo!=null && StringUtils.isNotBlank(outflowDrugVo.getFailString())){
            result.put("customStatus", "共下载数据条目："+list.size()+"。未匹配到记录："+outflowDrugVo.getFailString());
        }
        return result;
    }

    /**
     * (软删除-更新删除字段)
     * 博爱专用，查询门诊三天以上未交费门诊医嘱-需求8245
     * @param cfg
     * @return
     */
    public Map deleteOpCnInfoZsba(QrtzJobCfg cfg) {
    	List<String> pkCnords = zsbaOpTaskMapper.queryPkCnordTaskBa();
    	String msg = "删除门诊未交费门诊医嘱"+pkCnords.size()+"条！";
    	if(null != pkCnords && !pkCnords.isEmpty()) {
    		List<String> pkPress = zsbaOpTaskMapper.queryPkPresInfo(pkCnords);
    		zsbaOpTaskMapper.updateCnOrderDelFlag(pkCnords);
    		zsbaOpTaskMapper.updateExAssistOccDelFlag(pkCnords);
    		zsbaOpTaskMapper.updateBlOpDtDelFlag(pkCnords);
    		zsbaOpTaskMapper.updateCnLabApplyDelFlag(pkCnords);
    		zsbaOpTaskMapper.updateCnRisApplyDelFlag(pkCnords);
    		if(null!=pkPress && !pkPress.isEmpty()) {
    			zsbaOpTaskMapper.updateCnPrescriptionDelFlag(pkPress);
    		}
    		msg = msg+"处方："+pkPress.size();
    	}
    	Map<String, String> result = new HashMap<String, String>();
		result.put("msg",msg);
    	return result;
    }

}
