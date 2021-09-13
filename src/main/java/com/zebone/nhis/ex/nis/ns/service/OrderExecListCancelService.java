package com.zebone.nhis.ex.nis.ns.service;

import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.base.message.SysMessage;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.service.MessageService;
import com.zebone.nhis.common.support.MessageUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.ou.BdOuUserAdd;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.dao.OrderExecListMapper;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 取消医嘱执行
 *
 * @author yangxue
 */
@Service
public class OrderExecListCancelService {
    @Resource
    private OrderExecListMapper orderExecListMapper;
    @Resource
    private BlCgExPubService blCgExPubService;

    @Autowired
    private CnNoticeService cnNoticeService;

    /**
     * 查询医嘱执行单列表
     *
     * @param param{dateBegin,cancelFlag,dateEnd,nameOrd,pkPvs,pkDeptNs}
     * @param user
     * @return
     */
    public List<ExlistPubVo> queryExlist(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String dateBegin = CommonUtils.getString(map.get("dateBegin"));
        if (dateBegin == null) {
            map.put("dateBegin", DateUtils.getDateStr(new Date()) + "000000");
        } else {
            map.put("dateBegin", dateBegin.substring(0, 8) + "000000");
        }
        String dateEnd = CommonUtils.getString(map.get("dateEnd"));
        if (dateEnd == null) {
            map.put("dateEnd", DateUtils.getDateStr(new Date()) + "235959");
        } else {
            map.put("dateEnd", dateEnd.substring(0, 8) + "235959");
        }
        //设置为取消执行功能点标志
        map.put("cancelFlag", "1");
        List<ExlistPubVo> result = orderExecListMapper.queryExecListByCon(map);
        new ExListSortByOrdUtil().ordGroup(result);
        return result;
    }

    /**
     * 取消执行
     *
     * @param param
     * @param user
     */
    public void cancelEx(String param, IUser user) {
        //前台将整行记录的数据都传回来
        List<ExlistPubVo> exList = JsonUtil.readValue(param, new TypeReference<List<ExlistPubVo>>() {
        });
        blCgExPubService.cancelExAndRtnCg(exList, (User) user);
        ExtSystemProcessUtils.processExtMethod("PIVAS", "updatePivasOutByCancelEx", param);

        //取消执行医嘱发消息
        Map<String,Object> paramMap = new HashMap<String, Object>();
		List<Map<String,Object>> exlist = new ArrayList<Map<String,Object>>();
		Set<String> pkCnords = new HashSet<>();
		for(ExlistPubVo e : exList){
			//	if("药品".equals(e.getOrdtype())){
			//	continue;
			//}
            pkCnords.add(e.getPkCnord());
			exlist.add(JsonUtil.readValue(ApplicationUtils.beanToJson(e), Map.class));
		}
		paramMap.put("exlist", exlist);
		paramMap.put("typeStatus", "DEL");
        PlatFormSendUtils.sendExConfirmMsg(paramMap);
        //sendMessage(pkCnords,"医嘱待作废");
        cnNoticeService.saveCnNoticeToBe(pkCnords,user);
        
    }

	/**
    * 恢复执行
    *
    * @param param
    * @param user
    */
   public void recoveryEx(String param, IUser user) {
       //前台将整行记录的数据都传回来
       List<ExlistPubVo> exList = JsonUtil.readValue(param, new TypeReference<List<ExlistPubVo>>() {
       });
       blCgExPubService.recoveryExAndRtnCg(exList, (User) user);  
   }

    /**
     * 功能描述：校验用户名密码，并返回数据
     * 交易号：005002002071
     *
     * @param param
     * @param user
     * @return com.zebone.nhis.common.module.base.ou.BdOuEmployee
     * @author wuqiang
     * @date 2018/10/22
     */

    public List<BdOuUserAdd> queryPw(String param, IUser user) {

        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        List<BdOuUserAdd> bdOuUserAddList = new ArrayList<>();
        if (map == null) {
            throw new BusException("未获取到待验证的用户信息！");
        }
        String a = map.get("code").toString();
        String b = new SimpleHash("md5", map.get("pwd")).toHex();

        BdOuUserAdd bdOuUserAdd = DataBaseHelper.queryForBean("select * from bd_ou_user where code_user = ? and pwd = ? "
                , BdOuUserAdd.class, new Object[]{map.get("code"), new SimpleHash("md5", map.get("pwd")).toHex()});
        if (null == bdOuUserAdd) {
            throw new BusException("账号/密码错误！");
        }
        if ("1".equals(bdOuUserAdd.getIsLock())) {
            throw new BusException("账号被锁定！");
        }
        if ("0".equals(bdOuUserAdd.getFlagActive())) {
            throw new BusException("账号未启用！");
        }
        BdOuEmployee emp = DataBaseHelper.queryForBean("select name_emp,pk_emp from bd_ou_employee where pk_emp = ?", BdOuEmployee.class, new Object[]{bdOuUserAdd.getPkEmp()});
        if (null == emp) {
            throw new BusException("该用户未维护相关人员信息！");
        } else {
            bdOuUserAddList.add(bdOuUserAdd);
            return bdOuUserAddList;

        }
    }
}
