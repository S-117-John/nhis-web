package com.zebone.nhis.base.pub.service;

import com.zebone.nhis.base.pub.dao.DumpTableStuckMapper;
import com.zebone.nhis.base.pub.vo.BdDatadumpDic;
import com.zebone.nhis.base.pub.vo.DumpTableResVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 数据表结构同步
 */
@Service
public class DumpTableStuckService {

    @Resource
    private DumpTableStuckMapper dumpTableStuckMapper;


    /**
     * 保存同步表结构目录
     * @param param
     * @param user
     */
    public void saveDumpDataDic(String param,IUser user){
        List<BdDatadumpDic> datadumpDicList= JsonUtil.readValue(param, new TypeReference<List<BdDatadumpDic>>() {});
        if(datadumpDicList==null ||datadumpDicList.size()==0)return;

        List<BdDatadumpDic> insertList=new ArrayList<>();
        List<BdDatadumpDic> updateList=new ArrayList<>();
        User us= UserContext.getUser();
        datadumpDicList.stream().forEach(m->{
            m.setModifier(us.getPkEmp());
            m.setModityTime(new Date());
            m.setDelFlag("0");
            m.setTs(new Date());
            if(CommonUtils.isNotNull(m.getPkDatadumpDic())){
                ApplicationUtils.setDefaultValue(m,false);
                updateList.add(m);
            }else{
                ApplicationUtils.setDefaultValue(m,true);
                m.setCreateTime(new Date());
                m.setCreator(us.getPkEmp());
                insertList.add(m);
            }
        });

        if(insertList.size()>0){
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdDatadumpDic.class),insertList);
        }

        if(updateList.size()>0){
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdDatadumpDic.class),updateList);
        }
    }

    /**
     * 查询同步表结构目录
     * @param param
     * @param user
     * @return
     */
    public List<BdDatadumpDic> qryDumpDataDic(String param, IUser user){
        Map<String,Object>  paramMap=JsonUtil.readValue(param,Map.class);

        return  dumpTableStuckMapper.qryDumpTableDicList(paramMap);
    }

    /**
     * 删除需要同步表结构目录
     * @param param
     * @param user
     */
    public void delDumpDataDic(String param,IUser user){
        List<String> pkDumpList=JsonUtil.readValue(param, new TypeReference<List<String>>() { });
        if(pkDumpList==null ||pkDumpList.size()==0)return;

        String delSql="delete from BD_DATADUMP_DIC where PK_DATADUMP_DIC in ("+CommonUtils.convertListToSqlInPart(pkDumpList)+")";
        DataBaseHelper.execute(delSql,new Object[]{});
    }

    /**
     * 验证表结构同步可行性
     * @param param
     * @param user
     * @return
     */
    public void checkDumpTable(String param,IUser user){
        BdDatadumpDic dumpParam=JsonUtil.readValue(param,BdDatadumpDic.class);
        if(dumpParam==null)return ;

        // 1.验证源表是否存在 存在继续，不存在异常
        String errMsg="";
        String sussMsg="";
        String souSql="select count(1) from ALL_TABLES where OWNER=? and TABLE_NAME=?";
        int sourCount=DataBaseHelper.queryForScalar(souSql,Integer.class,new Object[]{dumpParam.getOwner(),dumpParam.getTableSource()});
        if(sourCount==0){
            errMsg=String.format("源表[%s]在用户[%s]中不存在，无法同步表结构",dumpParam.getOwner(),dumpParam.getTableSource());
            throw new BusException(errMsg);
        }

        // 2.验证目标表是否存在  不存在成功返回，并提示重新创建表
        String tarSql="select count(1) from ALL_TABLES where OWNER=? and TABLE_NAME=?";
        int tarCount=DataBaseHelper.queryForScalar(souSql,Integer.class,new Object[]{dumpParam.getOwner(),dumpParam.getTableTarget()});
        if(tarCount==0){
            return ;
        }else{
            // 3.目标表存在 验证对应表主键是否一致，false 提示异常
            StringBuffer stuckSql=new StringBuffer();
            stuckSql.append("select COLUMN_NAME from ALL_TAB_COLUMNS");
            stuckSql.append(" where OWNER = ? and COLUMN_ID='1'");
            stuckSql.append(" and TABLE_NAME in (?,?) group by COLUMN_NAME");
            List<Map<String,Object>> stuckResList=DataBaseHelper.queryForList(stuckSql.toString(),new Object[]{dumpParam.getOwner(),dumpParam.getTableTarget(),dumpParam.getTableSource()});

            if(stuckResList==null || stuckResList.size()!=1){
                errMsg=String.format("用户[%s]中源表[%s]与目标表[%s] ,主键不一致请手工核对数据表结构！",dumpParam.getOwner(),dumpParam.getTableSource(),dumpParam.getTableTarget());
                throw new BusException(errMsg);
            }
            return ;

        }
    }

    /**
     * 查看表结构关键信息
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryTableStuckInfo(String param,IUser user){

        BdDatadumpDic dumpParam=JsonUtil.readValue(param,BdDatadumpDic.class);
        if(dumpParam==null)return null;

        StringBuffer stuckSql=new StringBuffer();
        stuckSql.append("select col.TABLE_NAME,col.COLUMN_NAME, col.DATA_TYPE,col.DATA_LENGTH,col.COLUMN_ID,");
        stuckSql.append("  col.DATA_PRECISION, col.DATA_SCALE,col.CHAR_LENGTH, inx.INDEX_NAME, inx.UNIQUENESS");
        stuckSql.append(" from ALL_TAB_COLUMNS col left join ALL_IND_COLUMNS indcol");
        stuckSql.append(" on indcol.INDEX_OWNER = col.OWNER and indcol.TABLE_NAME = col.TABLE_NAME and");
        stuckSql.append(" indcol.COLUMN_NAME = col.COLUMN_NAME left join ALL_INDEXES inx ");
        stuckSql.append(" on inx.OWNER = col.OWNER and inx.TABLE_NAME = col.TABLE_NAME and inx.INDEX_NAME = indcol.INDEX_NAME");
        stuckSql.append(" where col.OWNER = ?  and col.TABLE_NAME in (?,?)");
        stuckSql.append(" order by col.COLUMN_ID");
        List<Map<String,Object>> stuckResList=DataBaseHelper.queryForList(stuckSql.toString(),new Object[]{dumpParam.getOwner(),dumpParam.getTableTarget(),dumpParam.getTableSource()});

        return stuckResList;
    }

    /**
     * 数据转储同部表结构
     * @param param
     * @param user
     */
    public void doSynchroTableStruct(String param, IUser user){
        BdDatadumpDic dumpParam=JsonUtil.readValue(param,BdDatadumpDic.class);
        if(dumpParam==null)return ;

        StringBuffer stuckSql=new StringBuffer();
        stuckSql.append("select col.TABLE_NAME,col.COLUMN_NAME, col.DATA_TYPE,col.DATA_LENGTH,");
        stuckSql.append("  col.DATA_PRECISION, col.DATA_SCALE,col.CHAR_LENGTH, inx.INDEX_NAME, inx.UNIQUENESS");
        stuckSql.append(" from ALL_TAB_COLUMNS col left join ALL_IND_COLUMNS indcol");
        stuckSql.append(" on indcol.INDEX_OWNER = col.OWNER and indcol.TABLE_NAME = col.TABLE_NAME and");
        stuckSql.append(" indcol.COLUMN_NAME = col.COLUMN_NAME left join ALL_INDEXES inx ");
        stuckSql.append(" on inx.OWNER = col.OWNER and inx.TABLE_NAME = col.TABLE_NAME and inx.INDEX_NAME = indcol.INDEX_NAME");
        stuckSql.append(" where col.OWNER = ?  and col.TABLE_NAME in (?,?)");
        stuckSql.append(" order by COL.OWNER, COL.TABLE_NAME, COL.COLUMN_NAME, inx.INDEX_NAME");

        List<DumpTableResVo> paramList =DataBaseHelper.queryForList(stuckSql.toString(),DumpTableResVo.class,new Object[]{dumpParam.getOwner(),dumpParam.getTableTarget(),dumpParam.getTableSource()});

        if(paramList==null ||paramList.size()==0)return;

        List<DumpTableResVo> sourceDumpList= paramList.stream().filter(m->dumpParam.getTableSource().equals(m.getTableName())).collect(Collectors.toList());
        if(sourceDumpList==null || sourceDumpList.size()==0){
            throw new BusException("请传入原表数据结构！");
        }

        List<String> execSql=new ArrayList<>();
        List<DumpTableResVo> copyDumpList=paramList.stream().filter(m->dumpParam.getTableTarget().equals(m.getTableName())).collect(Collectors.toList());
        if(copyDumpList==null || copyDumpList.size()==0){
            // 创建表， 创建主键， 同步索引
            String creaSql="create table "+dumpParam.getTableTarget()+" as  select * from "+dumpParam.getTableSource()+" where 1=2 ";
            DataBaseHelper.execute(creaSql);

            StringBuffer getPkSql=new StringBuffer();
            getPkSql.append("select COLUMN_NAME from ALL_TAB_COLUMNS");
            getPkSql.append(" where OWNER = ? and COLUMN_ID='1'");
            getPkSql.append(" and TABLE_NAME =?  group by COLUMN_NAME");
            Map<String,Object> pkMap=DataBaseHelper.queryForMap(getPkSql.toString(),new Object[]{dumpParam.getOwner(),dumpParam.getTableSource()});

            String pkSql="alter table "+dumpParam.getTableTarget()+"  add constraint "+dumpParam.getTableTarget()+"_PK   primary key ("+ MapUtils.getString(pkMap,"columnName")+")";
            execSql.add(pkSql);

            sourceDumpList.stream().forEach(m->{
                if(CommonUtils.isNotNull(m.getIndexName()) && !MapUtils.getString(pkMap,"columnName","").equals(m.getColumnName())) {
                    String indSql = "create index " + dumpParam.getTableTarget() + "_" + m.getColumnName() + " on " + dumpParam.getTableTarget() + "(" + m.getColumnName() + ")";
                    execSql.add(indSql);
                }
            });
        }else{
            StringBuffer getPkSql=new StringBuffer();
            getPkSql.append("select   a.column_name from user_cons_columns a, user_constraints b");
            getPkSql.append(" where a.constraint_name = b.constraint_name and b.constraint_type = 'P'");
            getPkSql.append(" AND A.OWNER=? and a.table_name = ? ");
            Map<String,Object> pkMap=DataBaseHelper.queryForMap(getPkSql.toString(),new Object[]{dumpParam.getOwner(),dumpParam.getTableTarget()});
            if(pkMap==null ||CommonUtils.isNull(MapUtils.getString(pkMap,"columnName"))){
                getPkSql=new StringBuffer();
                getPkSql.append("select COLUMN_NAME from ALL_TAB_COLUMNS");
                getPkSql.append(" where OWNER = ? and COLUMN_ID='1'");
                getPkSql.append(" and TABLE_NAME =?  group by COLUMN_NAME");
                pkMap=DataBaseHelper.queryForMap(getPkSql.toString(),new Object[]{dumpParam.getOwner(),dumpParam.getTableSource()});

                String pkSql="alter table "+dumpParam.getTableTarget()+"  add constraint "+dumpParam.getTableTarget()+"_PK   primary key ("+ MapUtils.getString(pkMap,"columnName")+")";
                execSql.add(pkSql);
            }

            // 同步表结构，索引
            Map<String, Object> finalPkMap = pkMap;
            sourceDumpList.stream().forEach(m->{
                boolean isExistCol=copyDumpList.stream().filter(f->m.getColumnName().equals(f.getColumnName())).findAny().isPresent();
                if(!isExistCol) {
                    String colSql = "alter table " + dumpParam.getTableTarget() + " add " + m.getColumnName() + " " + m.getDataType();
                    if ("FLOAT".equals(m.getDataType()) || "NUMBER".equals(m.getDataType())) {
                        if (CommonUtils.isNotNull(m.getDataPrecision())) {
                            colSql += "(" + m.getDataPrecision();
                            if (CommonUtils.isNotNull(m.getDataScale())) {
                                colSql += "," + m.getDataPrecision();
                            }
                            colSql += ")";
                        }
                    } else if (m.getDataType().contains("CHAR")) {
                        if (CommonUtils.isNotNull(m.getCharLength())) {
                            colSql += "(" + m.getCharLength() + ")";
                        }
                    }
                    execSql.add(colSql);
                }
                if(CommonUtils.isNotNull(m.getIndexName())) {
                    boolean isExistInd =copyDumpList.stream().filter(
                            f->m.getColumnName().equals(f.getColumnName())
                                    &&CommonUtils.isNull(f.getIndexName())
                            &&!MapUtils.getString(finalPkMap,"columnName").equals(f.getColumnName())
                    ).findAny().isPresent();
                    if(isExistInd) {
                        String indSql = "create index " + dumpParam.getTableTarget() + "_" + m.getColumnName() + " on " + dumpParam.getTableTarget() + "(" + m.getColumnName() + ")";
                        execSql.add(indSql);
                    }
                }
            });
        }
        if(execSql.size()>0) {
            execSql.forEach(m -> {
                        DataBaseHelper.execute(m);
                    }
            );
        }
    }



    public List<Map<String,Object>> qryDoSynchroTable(String param,IUser user){
        return null;
    }
}
