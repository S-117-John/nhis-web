package com.zebone.nhis.base.ou.service;

import ca.uhn.hl7v2.HL7Exception;

import com.zebone.nhis.base.ou.vo.UserAndPwdParam;
import com.zebone.nhis.base.ou.vo.UserAndUserUsrgrpParam;
import com.zebone.nhis.common.module.base.ou.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.RedisUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

/**
 * 用户服务
 *
 * @author Xulj
 */
@Service
public class OuUserService {

	/**
	 * 添加状态
	 **/
	public static final String AddState = "_ADD";

	/**
	 * 更新状态
	 */
	public static final String UpdateState = "_UPDATE";

	/**
	 * 删除状态
	 */
	public static final String DelState = "_DELETE";
	
	
    @Value("#{applicationProperties['nhis.super.user']}")
    private String superUser;

    //增删改标志
    private String rleCode = "";
    //用户默认密码
    @Value("#{applicationProperties['nhis.default.pwd']}")
    private String defaultPassword;

    /**
     * 新增 更新 用户
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void saveOuUserAndUserUsrgrps(String param, IUser user) {
        UserAndUserUsrgrpParam userAndUserUsrgrp = JsonUtil.readValue(param, UserAndUserUsrgrpParam.class);
        BdOuUserAdd ouser = userAndUserUsrgrp.getUser();
        List<BdOuUserUsrgrp> userUsrgrpList = userAndUserUsrgrp.getUserUsrgrpList();
        List<BdOuUserRole> userRolelist = userAndUserUsrgrp.getUserRoleList();
        //全部转换为小写（杨雪添加，实现用户名不区分大小写需求）
        ouser.setCodeUser(ouser.getCodeUser().toLowerCase());
        if (ouser.getCodeUser().equals(superUser)) {
            throw new BusException("该用户登录名已经存在！");
        }

        if (ouser.getPkUser() == null) {
            //新增
            int count_ouser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_user "
                    + "where code_user = ?", Integer.class, ouser.getCodeUser());
            int count_groupuser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_groupuser "
                    + "where code_user = ?", Integer.class, ouser.getCodeUser());
            if (count_ouser == 0 && count_groupuser == 0) {
                if ("0".equals(ouser.getEuUsertype())) {//员工
                    if (StringUtils.isBlank(ouser.getPkEmp())) {
                        throw new BusException("请设置关联人员！");
                    }
                }
                //默认密码 优先选取配置文件密码，无则默认
                String encryptPassword = null;
                if (defaultPassword != null || "".equals(defaultPassword)) {
                    encryptPassword = defaultPassword;
                } else {
                    encryptPassword = new SimpleHash("md5", "000000").toHex();
                }
                ouser.setPwd(encryptPassword);
                ouser.setCntLock("0");
                DataBaseHelper.insertBean(ouser);
                rleCode = this.AddState;
            } else {
                if (count_ouser != 0) {
                    throw new BusException("该用户编码重复！");
                }
                if (count_groupuser != 0) {
                    throw new BusException("该用户编码与集团管理员编码重复！");
                }
            }

        } else {
            //更新
            int count_ouser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_user "
                    + "where code_user = ? and pk_user != ?", Integer.class, ouser.getCodeUser(), ouser.getPkUser());
            int count_groupuser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_groupuser "
                    + "where code_user = ?", Integer.class, ouser.getCodeUser());
            if (count_ouser == 0 && count_groupuser == 0) {
                if ("0".equals(ouser.getEuUsertype())) {//员工
                    if (StringUtils.isBlank(ouser.getPkEmp())) {
                        throw new BusException("请设置关联人员！");
                    }
                }

                DataBaseHelper.updateBeanByPk(ouser, false);
                rleCode = this.UpdateState;
            } else {
                if (count_ouser != 0) {
                    throw new BusException("该用户编码重复！");
                }
                if (count_groupuser != 0) {
                    throw new BusException("该用户编码与集团管理员编码重复！");
                }
            }
        }

        /**保存到数据库*/
        String pkUser = ouser.getPkUser();
        if (userUsrgrpList != null && userUsrgrpList.size() != 0) {

            /**校验共享用户组的重复性*/
            Set pkUsrgrpSet = new HashSet();
            for (BdOuUserUsrgrp ouUserUsrgrp : userUsrgrpList) {
                String pkUsrgrp = ouUserUsrgrp.getPkUsrgrp();
                if (pkUsrgrpSet.contains(pkUsrgrp)) {
                    throw new BusException("用户共享用户组重复！");
                } else {
                    pkUsrgrpSet.add(pkUsrgrp);
                }
            }


            DataBaseHelper.update("update bd_ou_user_usrgrp set del_flag='1' where pk_user = ?", new Object[]{pkUser});
            for (BdOuUserUsrgrp ouUserUsrgrp : userUsrgrpList) {
                if (ouUserUsrgrp.getPkUserusrgrp() == null) {
                    ouUserUsrgrp.setPkUser(pkUser);
                    DataBaseHelper.insertBean(ouUserUsrgrp);
                } else {
                    ouUserUsrgrp.setPkUser(pkUser);
                    ouUserUsrgrp.setDelFlag("0");
                    DataBaseHelper.updateBeanByPk(ouUserUsrgrp, false);
                }
            }
        } else {
            DataBaseHelper.update("update bd_ou_user_usrgrp set del_flag='1' where pk_user = ?", new Object[]{pkUser});
        }

        //保存角色
        if (userRolelist != null && userRolelist.size() != 0) {

            DataBaseHelper.update("update bd_ou_user_role set del_flag='1' where pk_user = ?", new Object[]{pkUser});
            for (BdOuUserRole ouUserRole : userRolelist) {
                ouUserRole.setPkUser(pkUser);

                DataBaseHelper.insertBean(ouUserRole);
            }
        } else {
            DataBaseHelper.update("update bd_ou_user_role set del_flag='1' where pk_user = ?", new Object[]{pkUser});
        }
        //发送消息至平台
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("STATUS", rleCode);
        paramMap.put("user", JsonUtil.readValue(param, Map.class));
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        PlatFormSendUtils.sendBdOuUserMsg(paramMap);
        
    }


    /**
     * 新增 更新 集团管理员
     *
     * @param param
     * @param user
     * @return
     */
    public BdOuGroupuser saveGroupUser(String param, IUser user) {

        BdOuGroupuser groupuser = JsonUtil.readValue(param, BdOuGroupuser.class);
        //全部转换为小写（杨雪添加，实现用户名不区分大小写需求）
        groupuser.setCodeUser(groupuser.getCodeUser().toLowerCase());
        if (groupuser.getCodeUser().equals(superUser)) {
            throw new BusException("该集团管理员已经存在！");
        }
        if (groupuser.getPkGroupuser() == null) {
            //新增
            int count_ouser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_user "
                    + "where code_user = ?", Integer.class, groupuser.getCodeUser());
            int count_groupuser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_groupuser "
                    + "where code_user = ?", Integer.class, groupuser.getCodeUser());
            if (count_ouser == 0 && count_groupuser == 0) {
                //默认密码
                String encryptPassword = new SimpleHash("md5", "000000").toHex();
                groupuser.setPwd(encryptPassword);
                DataBaseHelper.insertBean(groupuser);
            } else {
                if (count_ouser != 0) {
                    throw new BusException("该集团管理员编码与普通用户编码重复！");
                }
                if (count_groupuser != 0) {
                    throw new BusException("该集团管理员编码重复！");
                }
            }

        } else {
            //更新
            int count_ouser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_user "
                    + "where code_user = ?", Integer.class, groupuser.getCodeUser());
            int count_groupuser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_groupuser "
                    + "where code_user = ? and pk_groupuser != ?", Integer.class, groupuser.getCodeUser(), groupuser.getPkGroupuser());
            if (count_ouser == 0 && count_groupuser == 0) {
                DataBaseHelper.updateBeanByPk(groupuser, false);
            } else {
                if (count_ouser != 0) {
                    throw new BusException("该集团管理员编码与普通用户编码重复！");
                }
                if (count_groupuser != 0) {
                    throw new BusException("该集团管理员编码重复！");
                }
            }
        }

        return groupuser;

    }

    /**
     * 交易号：001001002011
     * 批量新增 更新 集团管理员
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdOuGroupuser> saveGroupUserList(String param, IUser user) {

        List<BdOuGroupuser> paramList = JsonUtil.readValue(param, new TypeReference<List<BdOuGroupuser>>() {
        });

        for (BdOuGroupuser groupuser : paramList) {
            //全部转换为小写（杨雪添加，实现用户名不区分大小写需求）
            groupuser.setCodeUser(groupuser.getCodeUser());
            if (groupuser.getCodeUser().equals(superUser)) {
                throw new BusException("该集团管理员已经存在！");
            }
            if (groupuser.getPkGroupuser() == null) {
                //新增
                int count_ouser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_user "
                        + "where code_user = ?", Integer.class, groupuser.getCodeUser());
                int count_groupuser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_groupuser "
                        + "where code_user = ?", Integer.class, groupuser.getCodeUser());
                if (count_ouser == 0 && count_groupuser == 0) {
                    //默认密码
                    String encryptPassword = new SimpleHash("md5", "000000").toHex();
                    groupuser.setPwd(encryptPassword);
                    DataBaseHelper.insertBean(groupuser);
                    groupuser.setPkGroupuser(groupuser.getPkGroupuser());
                } else {
                    if (count_ouser != 0) {
                        throw new BusException("该集团管理员编码与普通用户编码重复！");
                    }
                    if (count_groupuser != 0) {
                        throw new BusException("该集团管理员编码重复！");
                    }
                }

            } else {
                //更新
                int count_ouser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_user "
                        + "where code_user = ?", Integer.class, groupuser.getCodeUser());
                int count_groupuser = DataBaseHelper.queryForScalar("select count(1) from bd_ou_groupuser "
                        + "where code_user = ? and pk_groupuser != ?", Integer.class, groupuser.getCodeUser(), groupuser.getPkGroupuser());
                if (count_ouser == 0 && count_groupuser == 0) {
                    DataBaseHelper.updateBeanByPk(groupuser, false);
                } else {
                    if (count_ouser != 0) {
                        throw new BusException("该集团管理员编码与普通用户编码重复！");
                    }
                    if (count_groupuser != 0) {
                        throw new BusException("该集团管理员编码重复！");
                    }
                }
            }
        }
        return paramList;
    }

    /***
     *
     * 删除集团管理员 <br>
     * 交易号： 001001002012
     *
     * @author wangpeng
     * @date 2017年3月15日
     */
    public void delectOuUser(String param, IUser user) {
        BdOuGroupuser groupuser = JsonUtil.readValue(param, BdOuGroupuser.class);
        String pkGroupuser = groupuser.getPkGroupuser();
        DataBaseHelper.update("update bd_ou_groupuser set del_flag = '1' where pk_groupuser=?", new Object[]{pkGroupuser});
    }

    /**
     * 保存集团管理功能权限(全删全插)
     *
     * @param param
     * @param user
     */
    public void saveGuOperList(String param, IUser user) {
        List<BdOuGuOper> guOperlist = JsonUtil.readValue(param, new TypeReference<List<BdOuGuOper>>() {
        });
        if (CollectionUtils.isNotEmpty(guOperlist)) {
            String pkEmp = ((User) user).getPkEmp();
            String pkGroupuser = guOperlist.get(0).getPkGroupuser();
            DataBaseHelper.execute("delete from bd_ou_gu_oper where pk_groupuser = ?", new Object[]{pkGroupuser});//全删

            if (guOperlist != null && guOperlist.size() != 0) {
                for (BdOuGuOper guoper : guOperlist) {
                    guoper.setPkGuoper(NHISUUID.getKeyId());
                    guoper.setCreator(pkEmp);
                    guoper.setCreateTime(new Date());
                    guoper.setModifier(pkEmp);
                    guoper.setModityTime(new Date());
                    guoper.setDelFlag("0");
                    guoper.setTs(new Date());
                }
            }
            String insertSql = DataBaseHelper.getInsertSql(BdOuGuOper.class);
            DataBaseHelper.batchUpdate(insertSql, guOperlist);
        } else {
            throw new BusException("参数错误！");
        }
    }

    /**
     * 重置用户密码
     *
     * @param param
     * @param user
     * @return
     */
    public BdOuUserAdd resertOuUserPwd(String param, IUser user) {

        BdOuUserAdd ouser = JsonUtil.readValue(param, BdOuUserAdd.class);
        //重置密码
        String encryptPassword = null;
        if (defaultPassword != null || "".equals(defaultPassword)) {
            encryptPassword = defaultPassword;
        } else {
            encryptPassword = new SimpleHash("md5", ouser.getPwd()).toHex();
        }
        ouser.setPwd(encryptPassword);
        String date= DateUtils.getDateTime();
        DataBaseHelper.update("update bd_ou_user set pwd = ?,date_pmd=to_date(?,'yyyy-MM-DD HH24:MI:SS') where pk_user = ?", ouser.getPwd(),date, ouser.getPkUser());

        return ouser;
    }

    /***
     *
     * 用户修改密码<br>
     * 交易号：001003001005
     *
     * @param  param
     * @param  user
     * @return BdOuUser
     * @throws
     *
     * @author wangpeng
     * @date 2017年3月2日
     */
    public Object updateOuUserPwd(String param, IUser user) {
        User u = (User) user;
        UserAndPwdParam uParam = JsonUtil.readValue(param, UserAndPwdParam.class);
        //全部转换为小写（杨雪添加，实现用户名不区分大小写需求）
        //uParam.setUserCode(uParam.getUserCode().toLowerCase());
        if (EnumerateParameter.TWO.equals(u.getType())) {
            return updateOuGroupuserPwd(param, user);
        }
        BdOuUser ouser = DataBaseHelper.queryForBean("select * from BD_OU_USER where CODE_USER = ?", BdOuUser.class, new Object[]{uParam.getUserCode()});
        if (ouser == null) {
            throw new BusException("此账户为超级管理员，不开放修改密码功能。");
        }
        String pwd = ouser.getPwd();
        if (pwd.equals(new SimpleHash("md5", uParam.getOldPwd()).toHex())) {
            ouser.setPwd(new SimpleHash("md5", uParam.getNewPwd()).toHex());
            String date= DateUtils.getDateTime();
            DataBaseHelper.update("update bd_ou_user set pwd = ?,date_pmd=to_date(?,'yyyy-MM-DD HH24:MI:SS') where pk_user = ?", ouser.getPwd(),date, ouser.getPkUser());
            
            //DataBaseHelper.update("update bd_ou_user set pwd = ? where code_user = ?", ouser.getPwd(), ouser.getCodeUser());
            //口腔医院修改密码发送给分诊
            Map<String,Object> map =new HashMap<String, Object>();
            Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
            map.put("codeEmp", UserContext.getUser().getCodeEmp());
            map.put("nameEmp", UserContext.getUser().getNameEmp());
            paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
            paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
            paramMap.put("emp", map);
    		paramMap.put("STATUS","_UPDATE");
    		PlatFormSendUtils.sendBdOuEmpMsg(paramMap);
        } else {
            throw new BusException("旧密码与原密码不同，不能修改！");
        }
        return ouser;
    }

    public BdOuGroupuser updateOuGroupuserPwd(String param, IUser user) {
        UserAndPwdParam uParam = JsonUtil.readValue(param, UserAndPwdParam.class);
        BdOuGroupuser ouser = DataBaseHelper.queryForBean("select * from bd_ou_groupuser where code_user = ?", BdOuGroupuser.class, new Object[]{uParam.getUserCode()});
        if (ouser == null)
            throw new BusException("未获取到集团管理员‘" + uParam.getUserCode() + "’对应的信息！");
        String pwd = ouser.getPwd();
        if (pwd.equals(new SimpleHash("md5", uParam.getOldPwd()).toHex())) {
            ouser.setPwd(new SimpleHash("md5", uParam.getNewPwd()).toHex());
            DataBaseHelper.update("update bd_ou_groupuser set pwd = ? where pk_groupuser = ?", ouser.getPwd(), ouser.getPkGroupuser());
        } else {
            throw new BusException("旧密码与原密码不同，不能修改！");
        }
        return ouser;
    }

    /**
     * 设置锁定
     *
     * @param param
     * @param user
     */
    public void setLockOnOuUsers(String param, IUser user) {
        List<BdOuUserAdd> ouserlist = JsonUtil.readValue(param, new TypeReference<List<BdOuUserAdd>>() {
        });

        if (ouserlist != null && ouserlist.size() != 0) {
            //批量设置锁定
            for (BdOuUserAdd ouser : ouserlist) {
            	if("1".equals(ouser.getIsOffLine())){
            		 RedisUtils.getRedisTemplate().delete(RedisUtils.getRedisPreName() + "redis_user:"+ ouser.getCodeUser());
            	}else{
            		 DataBaseHelper.update("update bd_ou_user set is_lock = ?,cnt_lock=0,date_locked=null where pk_user = ?", ouser.getIsLock(), ouser.getPkUser());
            	}
              }
        } else {
        	
        }

      /*  List<BdOuUser> ouserlist = JsonUtil.readValue(param, new TypeReference<List<BdOuUser>>() {
        });

        if (ouserlist != null && ouserlist.size() != 0) {
            //批量设置锁定
            for (BdOuUser ouser : ouserlist) {
                DataBaseHelper.update("update bd_ou_user set is_lock = ? where pk_user = ?", ouser.getIsLock(), ouser.getPkUser());
            }

        } else {
        }*/
    }

    /**
     * 查询用户及共享用户组
     *
     * @param param
     * @param user
     * @return
     */
    public UserAndUserUsrgrpParam queryOuUserAndUserUsrgrps(String param, IUser user) {
        String pkUser = JsonUtil.getFieldValue(param, "pkUser");
        BdOuUserAdd ouser = DataBaseHelper.queryForBean("select * from bd_ou_user where pk_user = ?", BdOuUserAdd.class, pkUser);
        List<BdOuUserUsrgrp> userUsrgrpList = DataBaseHelper.queryForList("select * from bd_ou_user_usrgrp where del_flag='0' and pk_user = ?", BdOuUserUsrgrp.class, pkUser);
        UserAndUserUsrgrpParam userAndUserUsrgrp = new UserAndUserUsrgrpParam();
        userAndUserUsrgrp.setUser(ouser);
        userAndUserUsrgrp.setUserUsrgrpList(userUsrgrpList);
        return userAndUserUsrgrp;
    }

    /**
     * 删除用户（主键）
     *
     * @param param
     * @param user
     */
    public void delOuUserAndUserUsrgrps(String param, IUser user) {
        String pkUser = JsonUtil.getFieldValue(param, "pkUser");

        //删除标志
        rleCode = this.DelState;
        DataBaseHelper.execute("delete from bd_ou_user_usrgrp where pk_user=?", new Object[]{pkUser});
        DataBaseHelper.execute("delete from bd_ou_user where pk_user=?", new Object[]{pkUser});
        DataBaseHelper.execute("delete from bd_ou_user_role where pk_user=?", new Object[]{pkUser});

        //发送消息至平台
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("STATUS", rleCode);
        paramMap.put("user", JsonUtil.readValue(param, Map.class));
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        PlatFormSendUtils.sendBdOuUserMsg(paramMap);
    }
    /**
     * 用户配置信息的保存
     * @param param
     * @param user
     */
    public void saveUserArgu(String param,IUser user){
    	List<BdEmpArgu> arguList = JsonUtil.readValue(param, new TypeReference<List<BdEmpArgu>>(){});
    	if(arguList!=null&&arguList.size()>0){
    		User u = (User)user;
    		DataBaseHelper.execute("delete from bd_emp_argu where pk_user=?", new Object[]{u.getId()});
    		Date d = new Date();
    		for(BdEmpArgu bdEmpArgu : arguList){
    			bdEmpArgu.setPkEmpargu(NHISUUID.getKeyId());
    			bdEmpArgu.setPkOrg(u.getPkOrg());
    			bdEmpArgu.setPkUser(u.getId());
    			bdEmpArgu.setPkEmp(u.getPkEmp());
    			bdEmpArgu.setNameUser(u.getUserName());
    			bdEmpArgu.setNameEmp(u.getNameEmp());
    			bdEmpArgu.setCreateTime(d);
    			bdEmpArgu.setTs(d);
    			bdEmpArgu.setCreator(u.getPkEmp());
    		}
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdEmpArgu.class), arguList); 
    	}
    }
    /**
     * 用户配置信息的查询
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryUserArgu(String param,IUser user){
    	
    	User u = (User)user; 
    	return DataBaseHelper.queryForList("select * from bd_emp_argu where pk_user=?", new Object[]{u.getId()});
    	
    }
}
