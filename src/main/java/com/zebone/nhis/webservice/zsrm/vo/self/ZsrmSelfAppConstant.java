package com.zebone.nhis.webservice.zsrm.vo.self;

import com.zebone.nhis.common.support.ApplicationUtils;

/**
 * 自助机结算常量数据
 */
public class ZsrmSelfAppConstant {

    public static String PK_DEPT_CG;
    
    public static String CODE_DEPT_CG;

    public static String PK_EMP_CG;
    
    public static String CODE_EMP;

    public static String NAME_EMP;

    public static String PK_ORG;
    
    public static String PK_DEPT_NUC;
    public static String CODE_DEPT_NUC;
    public static String NAME_DEPT_NUC;
    public static String CODE_EMP_NUC;
    public static String CODE_ORD;
    
    public static String pk_Org_LF;
    public static String pk_Dept_LF;
    public static String code_Dept_LF;

    static{
        PK_DEPT_CG = ApplicationUtils.getPropertyValue("ext.self.pkDept.value","");
        CODE_DEPT_CG = ApplicationUtils.getPropertyValue("ext.self.codeDept.value","");
        PK_EMP_CG = ApplicationUtils.getPropertyValue("ext.self.pkEmp.value","");
        NAME_EMP = ApplicationUtils.getPropertyValue("ext.self.nameEmp.value","");
        CODE_EMP = ApplicationUtils.getPropertyValue("ext.self.codeEmp.value","");
        PK_ORG = ApplicationUtils.getPropertyValue("ext.self.pkOrg.value","");
        CODE_ORD = ApplicationUtils.getPropertyValue("ext.self.codeOrd.value","92040");
        PK_DEPT_NUC = ApplicationUtils.getPropertyValue("ext.self.pkDeptNuc.value","");
        CODE_DEPT_NUC = ApplicationUtils.getPropertyValue("ext.self.codeDeptNuc.value","");
        NAME_DEPT_NUC = ApplicationUtils.getPropertyValue("ext.self.nameDeptNuc.value","");
        CODE_EMP_NUC = ApplicationUtils.getPropertyValue("ext.self.codeEmpNuc.value","");
        
        pk_Org_LF = ApplicationUtils.getPropertyValue("ext.self.pkOrgLF.value","");
        pk_Dept_LF = ApplicationUtils.getPropertyValue("ext.self.pkDeptLF.value","");
        code_Dept_LF = ApplicationUtils.getPropertyValue("ext.self.codeDeptLF.value","");
        
    }
}
