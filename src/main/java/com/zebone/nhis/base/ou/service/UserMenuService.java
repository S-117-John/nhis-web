package com.zebone.nhis.base.ou.service;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.ou.BdOuElement;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.base.ou.dao.MenuMapper;
import com.zebone.nhis.common.module.base.ou.BdOuMenu;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.module.base.ou.BdOuUserRole;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 处理用户管理下的菜单业务
 * 
 * @author Administrator
 */

@Service
public class UserMenuService {
    
    
    @Autowired
    private MenuMapper menuMapper;
    
    @SuppressWarnings("unchecked")
    public Page<BdOuUser> listAllUsers(String param, IUser user) {
        Map<String, Object> params = JsonUtil.readValue(param, Map.class);
        int pageNum = Integer.valueOf(params.get("pageNum").toString());
        int pageSize = Integer.valueOf(params.get("pageSize").toString());
        MyBatisPage.startPage(pageNum, pageSize);
        List<BdOuUser> users = menuMapper.listAllUsers();
        Page<BdOuUser> page = MyBatisPage.getPage();
        page.setRows(users);
        return page;
    }
    

    /**
     * 交易号：001001009002 保存功能列表
     * 
     * @param param
     * @param user
     */
    public BdOuMenu saveItemList(String param, IUser user) {
        BdOuMenu bomenu = JsonUtil.readValue(param, BdOuMenu.class);
        if (bomenu.getPkMenu() == null) {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_ou_menu " + "where code_menu=? and pk_org like ?||'%' and DEL_FLAG = '0'",
                    Integer.class, bomenu.getCodeMenu(), bomenu.getPkOrg());
            if (count_code == 0) {
                DataBaseHelper.insertBean(bomenu);
            } else {
                throw new BusException("菜单编码重复！");

            }
        } else {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_ou_menu "
                            + "where code_menu=? and pk_org like ?||'%' and pk_menu != ? and DEL_FLAG = '0'",
                    Integer.class, bomenu.getCodeMenu(), bomenu.getPkOrg(), bomenu.getPkMenu());
            if (count_code == 0) {
                DataBaseHelper.updateBeanByPk(bomenu, false);
            } else {
                throw new BusException("菜单编码和名称都重复！");

            }
        }
        return bomenu;
    }

    /**
     * 交易号：001001009009 角色关联用户
     * 
     * @param param
     * @param user
     */
    public void saveAssociateUser(String param, IUser user) {

        List<BdOuUserRole> userLists = JsonUtil.readValue(JsonUtil.getJsonNode(param, "userRoles"),
                new TypeReference<List<BdOuUserRole>>() {
                });
        String pkRole = JsonUtil.getFieldValue(param, "pkRole");
        if (pkRole != null) {
            DataBaseHelper.execute("update bd_ou_user_role set del_flag='1' where pk_role = ? ",
                    new Object[] { pkRole });
        }
        if (userLists != null && userLists.size() > 0) {
            for (BdOuUserRole bdOuUserRoles : userLists) {
                BdOuUserRole bdOuUserRole = new BdOuUserRole();
                bdOuUserRole.setPkOrg(bdOuUserRoles.getPkOrg());
                bdOuUserRole.setPkUser(bdOuUserRoles.getPkUser());
                bdOuUserRole.setPkRole(bdOuUserRoles.getPkRole());
                DataBaseHelper.insertBean(bdOuUserRole);
            }
        }

    }

    /**
     * 交易号：001001009003 删除功能列表
     * @param param
     * @param user
     */
    public void DeleteMenuInfo(String param, IUser user){
        BdOuMenu bomenu = JsonUtil.readValue(param, BdOuMenu.class);
        if (bomenu != null){
            DataBaseHelper.execute("delete from bd_ou_element where pk_menu= ? ", new Object[]{bomenu.getPkMenu()});
            DataBaseHelper.execute("delete from bd_ou_menu where pk_menu= ? ",new Object[]{bomenu.getPkMenu()});
        }else{
            throw new BusException("删除失败，请选择菜单！");
        }
    }


    /**
     * 交易号：	001001009010   查询菜单元素
     * @param param
     * @param user
     * @return
     */
    public List<BdOuElement> getElement (String param, IUser user){
        BdOuElement element = JsonUtil.readValue(param, BdOuElement.class);
        List<BdOuElement> elementList = menuMapper.getElement(element.getPkMenu());
        return elementList;
    }

    /**
     * 交易号：001001009011 保存菜单元素
     * @param param
     * @param user
     * @return
     */
    public void saveElement (String param, IUser user) {
        List<BdOuElement> elementList = JsonUtil.readValue(param, new TypeReference<List<BdOuElement>>() {
        });
        for (BdOuElement element : elementList) {
            if (StringUtils.isNotBlank(element.getPkElement())) {
                DataBaseHelper.updateBeanByPk(element, false);
            } else {
                DataBaseHelper.insertBean(element);
            }

        }
    }
    /**
     * 001001009012 删除菜单元素
     * @param param
     * @param user
     * @return
     */
    public void DeleteElement (String param, IUser user) {
        String pkElement = JsonUtil.getFieldValue(param, "pkElement");
        if (pkElement != null){
            DataBaseHelper.execute("delete from bd_ou_element where pk_element= ? ",new Object[]{pkElement});
        }
    }
}
