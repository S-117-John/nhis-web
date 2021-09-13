package com.zebone.platform.common.service;

import com.zebone.nhis.common.module.base.ou.BdOuEmpIntern;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.ou.BdOuUserAdd;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.common.dao.UserMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserDept;
import com.zebone.platform.common.support.UserMenu;
import com.zebone.platform.common.support.UserMenuElement;
import com.zebone.platform.framework.security.shiro.MutiUsernamePasswordToken;
import com.zebone.platform.framework.service.AbstractUserService;
import com.zebone.platform.framework.support.ISession;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.*;

/**
 * 系统登录涉及用户的服务接口
 *
 * @author lingjun
 */
@Service
public class UserService extends AbstractUserService {

    @Value("#{applicationProperties['nhis.super.user']}")
    private String superUser;

    @Value("#{applicationProperties['nhis.super.password']}")
    private String superPassword;

    @Value("#{applicationProperties['nhis.default.pwd']}")
    private String defaultPassword;

    @Value("#{applicationProperties['session.keys']}")
    private String sessionKeys;
    //限制只能同一个客户端登录
    @Value("#{applicationProperties['login.oneclient']}")
    private String oneclientFlag;
    //密码过期校验
    @Value("#{applicationProperties['login.pwd.expire']}")
    private String pwdExpireFlag;
    
    //密码输入错误次数校验
    @Value("#{applicationProperties['login.pwd.error']}")
    private String pwdErrorFlag;
    
    
    private String keyUserPrefix = "redis_user:";
    
    @Autowired
    private UserMapper userMapper;



    /**
     * 用户验证接口中根据用户登录名称获取用户具体信息
     *
     * @param loginName 登录名
     * @param tokenMap  token列表
     * @return
     */
    @Override
    public IUser findUserByLoginName(String loginName, Map<String, String> tokenMap) {

        //判断系统最大并发数
        Set<String> keys = RedisUtils.getRedisTemplate().keys(RedisUtils.getRedisPreName() + this.keyUserPrefix + "*");
        if(!CommonUtils.isEmptyString(sessionKeys)){
            if (keys.size() >= Integer.valueOf(sessionKeys).intValue()) {
                throw new BusException("超出系统最大并发数");
            }
        }
       //增加同一个用户不允许多个客户端同时登录限制
        this.verfyUserInRedis(loginName);
        
        String isCaLogin = "0";
        
        if (tokenMap != null) {
            if (tokenMap.containsKey("isCaLogin")) {
                isCaLogin = (String) tokenMap.get("isCaLogin");
            }
        }
        if (isCaLogin == null) isCaLogin = "0";
        //杨雪添加，实现用户不区分大小写
        loginName = loginName.toLowerCase();
        if (loginName.equals(superUser)) {  //集团超级管理员
            String org = "~                               ";   //全局机构主键
            User u = new User();
            u.setType(EnumerateParameter.ONE);
            u.setId(loginName);
            u.setPkEmp(superUser);
            u.setPkOrg(org);
            u.setPassword(superPassword);
            u.setLoginName(superUser);
            u.setUserName("超级管理员");
            return u;
        } else {

            StringBuilder sql = new StringBuilder(" select a.pk_user       id,")
            .append("     a.pk_user,")
            .append("     a.pk_org,")
            .append("     org.code_org,")
            .append("     a.code_user     login_name,")
            .append("     a.name_user  as   user_name,")
            .append("     a.eu_usertype   eu_usertype,")
            .append("     a.pwd      as     password,")
            .append("     a.pk_usrgrp,")
            .append("     a.pk_emp,")
            .append("     a.flag_active," )
            .append("     a.is_lock," )
            .append("     a.caid," )
            .append("     b.name_emp,")
            .append("     a.eu_certtype,")
            .append("     b.code_emp,")
            .append("     b.dt_empsrvtype,")
            .append("     bd.name empsrvtype_name ")
            .append(" from bd_ou_user a") 
            .append(" left join bd_ou_org org on org.pk_org = a.pk_org ")
            .append(" left join bd_ou_employee b")
            .append("   on a.pk_emp = b.pk_emp")
            .append(" left  join bd_defdoc bd")
            .append("   on b.dt_empsrvtype=bd.code and bd.code_defdoclist='010305'")
            .append(" where a.code_user = ?")
            .append("   and a.flag_active = '1'");
            User u = DataBaseHelper.queryForBean(sql.toString(), User.class, loginName);
            if (u != null) {
                String euCerttype = u.getEuCerttype() == null ? "" : u.getEuCerttype();
                if (isCaLogin.equals("0") && euCerttype.equals("1")) {
                    throw new BusException("该账号必须通过CA登录!");
                }
            }
            if (u != null) {
                if ("0".equals(u.getEuUsertype())) {//员工
                    if (StringUtils.isBlank(u.getPkEmp())) {
                        throw new BusException("该账号没有关联人员");
                    }
                }
                u.setType(EnumerateParameter.THREE);
                /*if (EnumerateParameter.ONE.equals(u.getIsLock())) {
                    throw new LockedAccountException("帐号锁定");   //帐号锁定
                }*/
            } else { //机构下管理员用户
                sql = new StringBuilder(" select a.pk_user       id,")
                		.append("	  a.pk_user,")
                        .append("     a.pk_org,")
                        .append("     org.code_org,")
                        .append("     a.code_user     login_name,")
                        .append("     a.name_user  as   user_name,")
                        .append("     a.eu_usertype   eu_usertype,")
                        .append("     a.pwd     as      password,")
                        .append("     a.pk_usrgrp," )
                        .append("     a.pk_emp,")
                        .append("     a.flag_active,")
                        .append("     a.is_lock,")
                        .append("     a.caid,")
                        .append("     a.eu_certtype ")
                        .append(" from bd_ou_user a left join bd_ou_org org on org.pk_org=a.pk_org  where a.code_user = ?");
                u = DataBaseHelper.queryForBean(sql.toString(), User.class, loginName);
                if (null != u && "0".equals(u.getFlagActive())) throw new DisabledAccountException("账号被禁用");

                sql = new StringBuilder("select a.PK_GROUPUSER id,a.pk_org,a.code_user login_name,a.name_user as user_name ,a.pwd as password ")
                       .append(",a.PK_GROUPUSER pk_emp,a.name_user name_emp from BD_OU_GROUPUSER a where a.code_user = ? and a.flag_stop='0' and a.del_flag='0' ");
                u = DataBaseHelper.queryForBean(sql.toString(), User.class, loginName);
                if (u == null) {
                    throw new UnknownAccountException("无效账号");     //没找到帐号
                }
                u.setType(EnumerateParameter.TWO);
            }
            Boolean isCaLoginB = false;
            if (isCaLogin != null && isCaLogin.equals("1")) {
                isCaLoginB = true;
            }
            u.setIsCaLogin(isCaLoginB);
            
            // 添加校验密码是否过期
            this.verfyPwdExpire(loginName);
            
        	//实习生校验
        	BdOuEmpIntern intern = internValidVerify(u.getPkEmp(),u.getDtEmpsrvtype());
        	if(intern!=null){
        		u.setIntern(true);
        		u.setPkEmpTeach(intern.getPkEmpTeach());
        		u.setNameEmpTeach(intern.getNameEmpTeach());
        	}
            return u;
        }
    }

    public IUser findUserByLoginNameAdd(String loginName, Map<String, String> tokenMap) {
        String pwd = null;
        String isCaLogin = "0";
        if (tokenMap != null) {
            if (tokenMap.containsKey("isCaLogin")) {
                isCaLogin = (String) tokenMap.get("isCaLogin");
            }
        }
        if (isCaLogin == null) isCaLogin = "0";
        //杨雪添加，实现用户不区分大小写
        loginName = loginName.toLowerCase();
        if (loginName.equals(superUser)) {  //集团超级管理员
            String org = "~                               ";   //全局机构主键
            User u = new User();
            u.setType(EnumerateParameter.ONE);
            u.setId(loginName);
            u.setPkEmp(superUser);
            u.setPkOrg(org);
            u.setPassword(superPassword);
            u.setLoginName(superUser);
            u.setUserName("超级管理员");
            return u;
        } else {

        	StringBuilder sql =  new StringBuilder(" select a.pk_user       id,")
            		.append("     a.pk_user," )
                    .append("     a.pk_org,")
                    .append("     org.code_org,")
                    .append("     a.code_user     login_name,")
                    .append("     a.name_user   as  user_name,")
                    .append("     a.eu_usertype   eu_usertype,")
                    .append("     a.pwd      as     password,")
                    .append("     a.pk_usrgrp,")
                    .append("     a.pk_emp,")
                    .append("     a.flag_active,")
                    .append("     a.is_lock,")
                    .append("     a.caid,")
                    .append("     b.name_emp,")
                    .append("     a.eu_certtype,")
                    .append("     b.code_emp,")
                    .append("     b.dt_empsrvtype,")
                    .append("     bd.name empsrvtype_name")
                    .append(" from bd_ou_user a")
                    .append(" left join bd_ou_org org on org.pk_org = a.pk_org ")
                    .append(" left join bd_ou_employee b")
                    .append("   on a.pk_emp = b.pk_emp")
                    .append(" left join bd_defdoc bd")
                    .append("   on b.dt_empsrvtype=bd.code and bd.code_defdoclist='010305'")
                    .append(" where a.code_user = ?")
                    .append("   and a.flag_active = '1'");
            User u = DataBaseHelper.queryForBean(sql.toString(), User.class, loginName);
            if (u != null) {
                String euCerttype = u.getEuCerttype() == null ? "" : u.getEuCerttype();
                if (isCaLogin.equals("0") && euCerttype.equals("1")) {
                    throw new BusException("该账号必须通过CA登录!");
                }
            }
            if (u != null) {
                if ("0".equals(u.getEuUsertype())) {//员工
                    if (StringUtils.isBlank(u.getPkEmp())) {
                        throw new BusException("该账号没有关联人员");
                    }
                }
                u.setType(EnumerateParameter.THREE);
                if (EnumerateParameter.ONE.equals(u.getIsLock())) {
                    throw new LockedAccountException("帐号锁定");   //帐号锁定
                }
            } else { //机构下管理员用户
              sql = new StringBuilder(" select a.pk_user       id,")
                		.append("     a.pk_user,")
                        .append("     a.pk_org,")
                        .append("     org.code_org,")
                        .append("     a.code_user     login_name,")
                        .append("     a.name_user  as   user_name,")
                        .append("     a.eu_usertype   eu_usertype,")
                        .append("     a.pwd       as    password,")
                        .append("     a.pk_usrgrp,")
                        .append("     a.pk_emp,")
                        .append("     a.flag_active,")
                        .append("     a.is_lock,")
                        .append("     a.caid,")
                        .append("     a.eu_certtype ")
                        .append(" from bd_ou_user a left join bd_ou_org org on org.pk_org = a.pk_org  where a.code_user = ?");
                u = DataBaseHelper.queryForBean(sql.toString(), User.class, loginName);
                if (null != u && "0".equals(u.getFlagActive())) throw new DisabledAccountException("账号被禁用");

                sql = new StringBuilder("select a.PK_GROUPUSER id,a.pk_org,a.code_user login_name,a.name_user as user_name ,a.pwd as password ")
                      .append(",a.PK_GROUPUSER pk_emp,a.name_user name_emp from BD_OU_GROUPUSER a where a.code_user = ? and a.flag_stop='0' and a.del_flag='0' ");
                u = DataBaseHelper.queryForBean(sql.toString(), User.class, loginName);

                if (u == null) {
                    throw new UnknownAccountException("无效账号");     //没找到帐号
                }
                u.setType(EnumerateParameter.TWO);
            }
            Boolean isCaLoginB = false;
            if (isCaLogin != null && isCaLogin.equals("1")) {
                isCaLoginB = true;
            }
            u.setIsCaLogin(isCaLoginB);
            return u;
        }
    }

    public IUser findUserByCa(ServletRequest request, ServletResponse response) {
        String param = request.getParameter("param");
        if (StringUtils.isBlank(param)) return null;
        Map<String, String> mapParam = JsonUtil.readValue(param, HashMap.class);

        if (!mapParam.containsKey("caid")) {
            throw new BusException("用户证书号为空!");
        }
        String caid = (String) mapParam.get("caid");

        StringBuilder sql = new StringBuilder("select emp.pk_emp,emp.code_emp,emp.name_emp ")
               .append(" from bd_ou_employee emp where emp.caid = ? or emp.caid2=? ");
        List<BdOuEmployee> empList = DataBaseHelper.queryForList(sql.toString(), BdOuEmployee.class, caid, caid);
        if (empList == null || empList.size() == 0) {
            throw new BusException("找不到对应的注册信息!请查看是否进行注册");
        } else if (empList.size() > 1) {
            throw new BusException("证书号找到对应的注册信息不唯一!");
        }
        BdOuEmployee emp = empList.get(0);
        String pkEmp = emp.getPkEmp();
        sql = new StringBuilder(" select a.pk_user       id,")
        		.append("     a.pk_user ,")
                .append("     a.pk_org,")
                .append("     org.code_org,")
                .append("     a.code_user     login_name,")
                .append("     a.name_user  as   user_name,")
                .append("     a.eu_usertype   eu_usertype,")
                .append("     a.pwd      as     password,")
                .append( "     a.pk_usrgrp,")
                .append("     a.pk_emp,")
                .append("     a.flag_active,")
                .append( "     a.is_lock,")
                .append("     a.caid,")
                .append("     b.name_emp,")
                .append("     b.code_emp," )
                .append("     a.eu_certtype," )
                .append("     b.dt_empsrvtype," )
                .append("     bd.name empsrvtype_name" )
                .append(" from bd_ou_user a" )
                .append(" left join bd_ou_org org on org.pk_org = a.pk_org ")
                .append(" left join bd_ou_employee b" )
                .append("   on a.pk_emp = b.pk_emp" )
                .append(" left join bd_defdoc bd" )
                .append("   on b.dt_empsrvtype=bd.code and bd.code_defdoclist='010305'")
                .append(" where a.pk_emp = ?    and (a.eu_certtype='1' or a.eu_certtype='2') ");
        List<User> uList = DataBaseHelper.queryForList(sql.toString(), User.class, pkEmp);
        if (uList == null || uList.size() == 0) {
            throw new BusException("找不到对应的用户!");
        } else if (uList.size() > 1) {
            throw new BusException("找到对应的用户不唯一!");
        }else if (uList.size() == 1 && "0".equals( uList.get(0).getEuCerttype() ) ) {
            throw new BusException("该账号必须通过静态密码登录!");
        }
        User u = uList.get(0);
        //return null;
        return u;
    }
    
    public IUser finduserbycode(ServletRequest request, ServletResponse response) {
        String param = request.getParameter("param");
        if (StringUtils.isBlank(param)) return null;
        Map<String, String> mapParam = JsonUtil.readValue(param, HashMap.class);

        String username = (String) mapParam.get("username");

        StringBuilder sql = new StringBuilder(" select a.pk_user       id,")
        		.append("     a.pk_user ,")
                .append("     a.pk_org,")
                .append("     org.code_org,")
                .append("     a.code_user     login_name,")
                .append("     a.name_user  as   user_name,")
                .append("     a.eu_usertype   eu_usertype,")
                .append("     a.pwd      as     password,")
                .append("     a.pk_usrgrp,")
                .append("     a.pk_emp,")
                .append("     a.flag_active,")
                .append("     a.is_lock,")
                .append("     a.caid,")
                .append("     b.name_emp,")
                .append("     b.code_emp,")
                .append("     a.eu_certtype," )
                .append("     b.dt_empsrvtype," )
                .append("     bd.name empsrvtype_name" )
                .append(" from bd_ou_user a")
                .append(" left join bd_ou_employee b ")
                .append("   on a.pk_emp = b.pk_emp")
                .append(" left join bd_ou_org org ")
                .append("   on a.pk_org = org.pk_org ")
                .append(" left join bd_defdoc bd")
                .append("   on b.dt_empsrvtype=bd.code and bd.code_defdoclist='010305'")
                .append(" where a.code_user = ?")
                .append("    and (a.eu_certtype='0' or a.eu_certtype='2')");
        List<User> uList = DataBaseHelper.queryForList(sql.toString(), User.class, username);
        if (uList == null || uList.size() == 0) {
            throw new BusException("找不到对应的用户!");
        } else if (uList.size() > 1) {
            throw new BusException("找到对应的用户不唯一!");
        }
        User u = uList.get(0);
        //return null;
        return u;
    }


    /**
     * 用户登录密码加密,按照要求进行加密处理
     *
     * @param password 用户登录密码
     * @param salt     加密散列值
     * @param user     系统用户
     * @return 加密后的密码
     */
    @Override
    public String encrypt(String password, String salt, IUser user) {

        String encryptPassword = new SimpleHash("md5", password).toHex();

        return encryptPassword;
    }

    /**
     * 用户验证成功
     *
     * @param user
     * @param session
     */
    @Override
    public void success(IUser user, ISession session) {


        User u = (User) user;
        //登录成功后归零动作
        DataBaseHelper.execute("update bd_ou_user set date_locked=null,is_lock=0,cnt_lock=0 where code_user =?", user.getLoginName());

        if (u.getType().equals(EnumerateParameter.ONE)) {  //集团超级管理员
            superSuccess(user, session);
            return;
        } else if (u.getType().equals(EnumerateParameter.TWO)) {  //集团管理员
            jgAdminSuccess(user, session);
            return;
        }
        String usrgrpDeptSql="";
        List<UserDept> deptList=null;
        if(!u.isIntern()){
        	usrgrpDeptSql = "select DISTINCT t.* from ("
                    + "select a.pk_dept,b.code_dept,b.name_dept, a.pk_org ,org.code_org,a.isdefualt,a.seqno,b.py_code,b.d_code from bd_ou_usrgrp_dept a "
                    + "left join bd_ou_dept b on a.pk_dept = b.pk_dept " 
                    + "left join bd_ou_org org on org.pk_org = b.pk_org "
                    + "where  a.pk_usrgrp = ? "
                    + "union "
                    + "select ugpdept.pk_dept, dept.code_dept,dept.name_dept,ugpdept.pk_org,org.code_org,'0' isdefualt,ugpdept.seqno,dept.py_code,dept.d_code from bd_ou_user_usrgrp ugp  "
                    + "LEFT OUTER JOIN BD_OU_USRGRP_DEPT ugpdept on ugp.pk_usrgrp = ugpdept.pk_usrgrp "
                    + "left join bd_ou_dept dept on ugpdept.pk_dept = dept.pk_dept "
                    + "left join bd_ou_org org on org.pk_org = dept.pk_org "
                    + "where ugp.pk_user = ?  and ugp.del_flag = '0') t "
                    + "ORDER BY seqno";
                deptList = DataBaseHelper.queryForList(usrgrpDeptSql, UserDept.class, u.getPkUsrgrp(), u.getId());
        }else{
        	usrgrpDeptSql = "select DISTINCT t.* from ("
                    + "select a.pk_dept,b.code_dept,b.name_dept, a.pk_org ,org.code_org,a.isdefualt,a.seqno,b.py_code,b.d_code "
                    + " from bd_ou_usrgrp_dept a "
                    + "inner join bd_ou_user usr on a.pk_usrgrp = usr.pk_usrgrp "
                    + "left join bd_ou_dept b on a.pk_dept = b.pk_dept " 
                    + "left join bd_ou_org org on org.pk_org = b.pk_org "
                    + "where  usr.pk_emp = ? "
                    + "union "
                    + "select ugpdept.pk_dept,dept.code_dept,dept.name_dept,ugpdept.pk_org,org.code_org,'0' isdefualt,ugpdept.seqno,dept.py_code,dept.d_code "
                    + " from bd_ou_user_usrgrp ugp  "
                    + "INNER JOIN bd_ou_user usr on ugp.pk_user = usr.pk_user "
                    + "LEFT OUTER JOIN BD_OU_USRGRP_DEPT ugpdept on ugp.pk_usrgrp = ugpdept.pk_usrgrp "
                    + "left join bd_ou_dept dept on ugpdept.pk_dept = dept.pk_dept "
                    + "left join bd_ou_org org on org.pk_org = dept.pk_org "
                    + "where usr.pk_emp = ?  and ugp.del_flag = '0') t "
                    + "ORDER BY seqno";
                deptList = DataBaseHelper.queryForList(usrgrpDeptSql, UserDept.class, u.getPkEmpTeach(), u.getPkEmpTeach());
        }
        
        if (deptList.size() != 0) {

            //isdefault 为1去重
            String pkDept = "";
            for (UserDept userDept : deptList) {
                if ("1".equals(userDept.getIsdefualt())) {
                    pkDept = userDept.getPkDept();
                    break;
                }
            }
            for (int i = deptList.size() - 1; i >= 0; i--) {
                UserDept userDept = deptList.get(i);
                if ("0".equals(userDept.getIsdefualt()) && pkDept.equals(userDept.getPkDept())) {
                    deptList.remove(i);
                    break;
                }
            }

            boolean hasdefault = true;

            for (UserDept dept : deptList) {
                if (dept.getIsdefualt().equals("1")) {
                    u.setPkDept(dept.getPkDept());
                    u.setPkOrg(dept.getPkOrg());
                    hasdefault = false;
                    break;
                }
            }

            if (hasdefault) {
                throw new BusException("缺少默认用户组科室关系。");
            }
        }

        if (session == null) {
            u.setDepts(deptList);
            //获取用户所有菜单
			/*String oper_sql = "select c.pk_oper,c.code_oper,c.name_oper,c.pk_father ,c.oper_dll ,c.param,c.pk_org,c.flag_shortcut , c.flag_def  from bd_ou_oper c where exists "
					+ "(  select b.pk_oper from bd_ou_usrgrp_role a , bd_ou_role_oper b  where a.pk_role = b.pk_role  and b.pk_oper = c.pk_oper and a.pk_usrgrp = ? )"
					+ " and  c.flag_active = '1' and  c.flag_super = '0' order by c.code_oper";
			
			List<UserOper> operList = DataBaseHelper.queryForList(oper_sql, UserOper.class, u.getPkUsrgrp());*/
            String oper_sql = "";
            List<UserMenu> operList=null;
            //菜单下注册的元素集合
            List<UserMenuElement> elementList = null;
            if(!u.isIntern()){
            	oper_sql = "SELECT DISTINCT " +
                    "m.NAME_MENU," +
                    "m.PK_MENU," +
                    "m.code_Menu," +
                    "m.pk_father," +
                    "o.oper_dll," +
                    "m.param," +
                    "m.pk_org," +
                    "m.flag_shortcut," +
                    "m.DT_ABUTYPE," +
                    "m.flag_def " +
                    "FROM " +
                    "BD_OU_USER u " +
                    "LEFT OUTER JOIN BD_OU_USER_ROLE ur ON u.PK_USER = ur.PK_USER " +
                    "LEFT OUTER JOIN BD_OU_ROLE r ON ur.PK_ROLE = r.PK_ROLE " +
                    "LEFT OUTER JOIN BD_OU_ROLE_OPER ro ON ro.PK_ROLE = r.PK_ROLE " +
                    "LEFT OUTER JOIN BD_OU_MENU m ON m.PK_MENU = ro.PK_MENU " +
                    "LEFT OUTER JOIN BD_OU_OPER o ON m.PK_OPER = o.PK_OPER " +
                    " WHERE " +
                    "ur.DEL_FLAG = '0' " +
                    "and m.FLAG_ACTIVE = '1' and m.dt_abutype = '01' " +
                    "AND u.PK_USER = ? " +
                    "order by m.code_menu";
            	operList = DataBaseHelper.queryForList(oper_sql, UserMenu.class, u.getId());
            	elementList = userMapper.queryMenuElements(u.getId());
            }else{
            	oper_sql = "SELECT DISTINCT " +
                "m.NAME_MENU," +
                "m.PK_MENU," +
                "m.code_Menu," +
                "m.pk_father," +
                "o.oper_dll," +
                "m.param," +
                "m.pk_org," +
                "m.flag_shortcut," +
                "m.DT_ABUTYPE," +
                "m.flag_def " +
                "FROM " +
                "BD_OU_USER u " +
                "LEFT OUTER JOIN BD_OU_USER_ROLE ur ON u.PK_USER = ur.PK_USER " +
                "LEFT OUTER JOIN BD_OU_ROLE r ON ur.PK_ROLE = r.PK_ROLE " +
                "LEFT OUTER JOIN BD_OU_ROLE_OPER ro ON ro.PK_ROLE = r.PK_ROLE " +
                "LEFT OUTER JOIN BD_OU_MENU m ON m.PK_MENU = ro.PK_MENU " +
                "LEFT OUTER JOIN BD_OU_OPER o ON m.PK_OPER = o.PK_OPER " +
                " WHERE " +
                "ur.DEL_FLAG = '0' " +
                "and m.FLAG_ACTIVE = '1' and m.dt_abutype = '01' " +
                "AND u.pk_emp = ? " +
                "order by m.code_menu";
            	
            	operList = DataBaseHelper.queryForList(oper_sql, UserMenu.class, u.getPkEmpTeach());
            	elementList = userMapper.queryMenuElements(u.getPkEmpTeach());
            }
            
            //设置菜单对应的元素集合
            if(operList!=null&&operList.size()>0&&elementList!=null&&elementList.size()>0){
            	for(UserMenu menu:operList){
            		 List<UserMenuElement> elList = new ArrayList<UserMenuElement>();
            		 for(UserMenuElement el:elementList){
            			 if(menu.getPkMenu().equals(el.getPkMenu())){
            				 elList.add(el);
            			 }
            		 }
            		 if(elList!=null&&elList.size()>0){
            			 menu.setElementList(elList);
            		 }
            	}
            }
            
            u.setOpers(operList);
        }

    }

    /**
     * 集团管理员登录
     *
     * @param user
     * @param session
     */
    private void jgAdminSuccess(IUser user, ISession session) {
        if (session == null) {
            User u = (User) user;
			/*String oper_sql = "select c.pk_oper,c.code_oper,c.name_oper,c.pk_father ,c.oper_dll ,c.param,c.pk_org,c.flag_shortcut , c.flag_def  "
					+ "from bd_ou_oper c ,BD_OU_GU_OPER a   where   c.pk_oper = a.pk_oper and a.pk_groupuser = ? and a.del_flag ='0' and  c.flag_active = '1' order by c.code_oper";
			
			List<UserOper> operList = DataBaseHelper.queryForList(oper_sql, UserOper.class, u.getId());*/
            String oper_sql = "SELECT DISTINCT " +
                    "m.NAME_MENU," +
                    "m.PK_MENU," +
                    "m.code_Menu," +
                    "m.pk_father," +
                    "o.oper_dll," +
                    "o.param," +
                    "m.pk_org," +
                    "m.flag_shortcut," +
                    "m.flag_def " +
                    "FROM " +
                    "BD_OU_GU_OPER c " +
                    "LEFT OUTER JOIN BD_OU_MENU m on c.PK_MENU = m.PK_MENU " +
                    "LEFT OUTER JOIN BD_OU_OPER o on m.PK_OPER = o.PK_OPER " +
                    "WHERE " +
                    "c.pk_groupuser = ? " +
                    "AND c.del_flag = '0' " +
                    "AND m.flag_active = '1' and m.dt_abutype = '01' " +
                    "ORDER BY " +
                    "m.code_menu";

            List<UserMenu> operList = DataBaseHelper.queryForList(oper_sql, UserMenu.class, u.getId());
            u.setOpers(operList);

        }
    }

    /**
     * 集团超级管理员登录
     *
     * @param user
     * @param session
     */
    private void superSuccess(IUser user, ISession session) {
        if (session == null) {
            User u = (User) user;
			/*String oper_sql = "select c.pk_oper,c.code_oper,c.name_oper,c.pk_father ,c.oper_dll ,c.param,c.pk_org,c.flag_shortcut , c.flag_def  from bd_ou_oper c where "
					+ "  c.flag_super = '1' and  c.flag_active = '1' order by c.code_oper ";
			
			List<UserOper> operList = DataBaseHelper.queryForList(oper_sql, UserOper.class);*/
            String oper_sql = "SELECT DISTINCT " +
                    "m.NAME_MENU," +
                    "m.PK_MENU," +
                    "m.code_Menu," +
                    "m.pk_father," +
                    "o.oper_dll," +
                    "o.param," +
                    "m.pk_org," +
                    "m.flag_shortcut," +
                    "m.flag_def " +
                    "FROM " +
                    "BD_OU_MENU m " +
                    "LEFT OUTER JOIN BD_OU_OPER o ON m.PK_OPER = o.PK_OPER " +
                    " WHERE " +
                    "m.FLAG_SUPER = '1' " +
                    "AND m.FLAG_ACTIVE = '1' " +
                    "order by m.code_menu";

            List<UserMenu> operList = DataBaseHelper.queryForList(oper_sql, UserMenu.class);
            u.setOpers(operList);

        }
    }

    /**
     * 用户验证失败
     *
     * @param token
     * @param e
     */
    @Override
    public Map failure(AuthenticationToken token, AuthenticationException e) {
        MutiUsernamePasswordToken tokenM = (MutiUsernamePasswordToken) token;
        String message = e.getClass().getSimpleName();
        String loginName = tokenM.getUsername();
        //实现用户不区分大小写
        loginName = loginName.toLowerCase();
        Map<String, String> map = new HashMap<>();
        map.put("desc", "密码输入错误");
        User u = new User();
        String sql = "select a.PK_GROUPUSER id,a.pk_org,a.code_user login_name,a.name_user as user_name ,a.pwd as password "
                + ",a.PK_GROUPUSER pk_emp,a.name_user name_emp from BD_OU_GROUPUSER a where a.code_user = ? and a.flag_stop='0' and a.del_flag='0' ";
        u = DataBaseHelper.queryForBean(sql, User.class, loginName);

        if ("UnknownAccountException".equals(message) || u != null) {

        } else if ("IncorrectCredentialsException".equals(message)) {
        	this.verfyPwdErrorCount(loginName,map);
        }
        return map;
    }

    public IUser findUserByIdMaster(String idMaster, String pkDept) {
        idMaster = idMaster.toLowerCase();
        String sql = " select a.pk_user       id," +
        		"     a.pk_user," +
                "     a.pk_org," +
                "     a.code_user     login_name," +
                "     a.name_user  as   user_name," +
                "     a.eu_usertype   eu_usertype," +
                "     a.pwd    as       password," +
                "     a.pk_usrgrp," +
                "     a.pk_emp, b.code_emp,b.name_emp," +
                "     a.flag_active," +
                "     a.is_lock," +
                "     a.caid," +
                "     a.eu_certtype " +
                " from bd_ou_user a inner join bd_ou_employee b on b.pk_emp = a.pk_emp and b.del_flag='0' " + " where b.id_master =?  and a.flag_active='1' ";
        User u = DataBaseHelper.queryForBean(sql, User.class, idMaster);
        if (u == null) throw new BusException("无效账号！");
        UserDept userDept = DataBaseHelper.queryForBean("select pk_dept from bd_ou_dept where pk_dept=? ", UserDept.class, new Object[]{pkDept});
        if (userDept == null) throw new BusException("无效当前科室编码！");
        u.setPkDept(userDept.getPkDept());
        return u;
    }
	/**
	 * 校验登录用户是否已经存在于缓存中
	 */
    private void verfyUserInRedis(String loginName){
    	if (CommonUtils.isEmptyString(oneclientFlag)||!"true".equals(oneclientFlag)) return;
        //判断系统最大并发数
        Set<String> users = RedisUtils.getRedisTemplate().keys(RedisUtils.getRedisPreName() + this.keyUserPrefix + "*");
        if(users!=null&&users.size()>0){
            List<String> list = new ArrayList<>(users);
            for(String user:list){
                if(user.contains(loginName))
                    throw new BusException("该用户已在其他客户端登录！");
            }
        }
    }
    /**
     * 密码过期校验
     * @param loginName
     */
    private void verfyPwdExpire(String loginName){
    	//验证密码是否已过期

            String bdsql = "select * from bd_ou_user where code_user = ?";
            BdOuUserAdd bdOuUserAdd = new BdOuUserAdd();
            bdOuUserAdd = DataBaseHelper.queryForBean(bdsql, bdOuUserAdd.getClass(), loginName);
            Date date = new Date(); //系统当期时间
            //处理管理员锁定账号
            if (bdOuUserAdd!=null){
            if (bdOuUserAdd.getIsLock().equals("1") && bdOuUserAdd.getDateLocked() == null) {

                throw new LockedAccountException("帐号锁定");
            }

            int day = 0;
            if (bdOuUserAdd.getDatePmd() != null) {
                day = DateUtils.getDateSpace(bdOuUserAdd.getDatePmd(), date);//截止登录时间到此刻的天数
            }
            //当前更换周期>最后一次修改到现在的时间，由最后密码修改日期进行校验
            if (bdOuUserAdd.getDaysValid() >= day || bdOuUserAdd.getDaysValid() == 0 || (Integer) bdOuUserAdd.getDaysValid() == null) {

                if (bdOuUserAdd.getDateLocked() != null) {
                    int dateMIN = DateUtils.getMinsBetween(bdOuUserAdd.getDateLocked(), date);
                    if (dateMIN < 30) {
                        int dateCun = 30 - dateMIN;
                        throw new BusException("用户名密码输入错误5次，用户已被锁定，请" + dateCun + "分钟后重试！");
                    } else {
                        DataBaseHelper.execute(" update bd_ou_user set date_locked=null,is_lock=0,cnt_lock=0 where code_user =?", loginName);
                    }
                }

            } else {
                throw new BusException("密码已过期，请联系管理员更换密码");
            }

        }

    }
    
	/**
	 * 添加密码错误次数校验
	 * @param loginName
	 */
    private void verfyPwdErrorCount(String loginName,Map<String, String> map){
    	if(CommonUtils.isEmptyString(pwdErrorFlag)||!"true".equals(pwdErrorFlag))
    		return;
    	
    	   int cnt = 0;
           try {
               cnt = DataBaseHelper.queryForScalar("select cnt_lock from bd_ou_user where code_user  = ?", Integer.class, loginName);
           } catch (Exception e1) {
               cnt = 0;
           }
           if (cnt <= 4) {
               cnt++;
               String cntn = String.valueOf(cnt);
               DataBaseHelper.execute("update bd_ou_user set cnt_lock = ? where code_user =? ", new Object[]{cntn, loginName});
               map.put("desc", "密码输入错误");

           } else {
               String date = DateUtils.getDateTime();
               DataBaseHelper.execute("update bd_ou_user set IS_LOCK=1,date_locked=to_date(?,'yyyy-MM-DD HH24:MI:SS')where code_user =? ", new Object[]{date, loginName});
               map.put("desc", "用户名密码输入错误5次，用户已被锁定，请30分钟后重试！");
           }
    }
    
	/**
	 * 实习生校验（有效期、是否已维护指导老师）
	 * @param pkEmp
	 * @param dtEmpsrvtype
	 */
    private BdOuEmpIntern internValidVerify(String pkEmp,String dtEmpsrvtype){
    	if(StringUtils.isEmpty(pkEmp)||StringUtils.isEmpty(dtEmpsrvtype)||!StringUtils.isNumeric(dtEmpsrvtype)) return null;
    	BdOuEmpIntern empIntern=null;
    	int num=0;
    	try {
    		num=Integer.parseInt(dtEmpsrvtype);
    	} catch (Exception e) {
			return null;
		}
		if(num>=9){
			String sql = "select itn.pk_emp_teach,emp_teach.name_emp name_emp_teach,itn.date_begin,itn.date_end"+
					      " from bd_ou_employee emp"+
		           "  inner join bd_ou_emp_intern itn on emp.pk_emp=itn.pk_emp"+
		           "  left outer join bd_ou_employee emp_teach on itn.pk_emp_teach=emp_teach.pk_emp"+	      
		                "  where emp.flag_active='1' and emp.pk_emp=?";
			empIntern = DataBaseHelper.queryForBean(sql, BdOuEmpIntern.class, pkEmp);
			if(empIntern==null)
				throw new BusException("用户身份为学员，请维护学员信息!");
			String sdateEnd = DateUtils.addDate(empIntern.getDateEnd(), 1, 3, "yyyyMMddHHmmss");
			Date dateEnd = DateUtils.strToDate(sdateEnd,"yyyyMMddHHmmss");
			if(empIntern!=null){
				if(StringUtils.isEmpty(empIntern.getPkEmpTeach())){
    				throw new BusException("用户身份为学员，请维护指导老师!");
				}else if(!DateUtils.belongCalendar(new Date(), empIntern.getDateBegin(), dateEnd)){
    				throw new BusException("用户身份为学员，已超出注册有效期！");
				}
			}else{
				throw new BusException("用户身份为学员,必须维护对应的医师信息!");
			}
		}
		
    	return empIntern;
    }
    


}

