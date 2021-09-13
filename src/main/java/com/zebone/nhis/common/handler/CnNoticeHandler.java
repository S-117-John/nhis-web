package com.zebone.nhis.common.handler;

import com.zebone.nhis.common.dao.CnNoticeMapper;
import com.zebone.nhis.common.module.cn.ipdw.CnNotice;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.Application;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 使用缓存存储通知信息<br>
 * 1W条数据约1.7M，取1条、5000条平均耗时均为40~50MS<br>
 * 按照实际统计，满足条件放入缓存的并不多<br>
 * 首次加载或者重新刷入可直接调用 flush方法
 */
@Service
public class CnNoticeHandler {
    private Logger logger = LoggerFactory.getLogger(CnNoticeHandler.class);

    /**
     * CACHE中notice的key的后缀
     */
    private static final String NOTICE_SUFFIX = "notice_data";
    private static final String CODEOPER = "050201";

    private StringRedisSerializer redisSerializer = new StringRedisSerializer();

    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private CnNoticeMapper cnNoticeMapper;

    public boolean useCache() {
        return StringUtils.equals(EnumerateParameter.ONE,
                ApplicationUtils.getPropertyValue("cn.notice.useCache", EnumerateParameter.ZERO));
    }

    public void add(CnNotice cnNotice) {
        if (cnNotice == null
                || (cnNotice.getEuStatus() != null && cnNotice.getEuStatus().compareTo(EnumerateParameter.ONE) > 0)
                || StringUtils.isBlank(cnNotice.getPkDeptRecp())
                || StringUtils.isBlank(cnNotice.getPkCnnotice())
                || (cnNotice.getCntEmer() == null && cnNotice.getCntNew() == null
                && cnNotice.getCntEnd() == null && cnNotice.getCntVoid() == null)) {
            return;
        }
        RedisConnection connection = null;
        try {
            connection = getConn();
            connection.hSet(getName(),
                    redisSerializer.serialize(cnNotice.getPkDeptRecp() + "_" + cnNotice.getPkCnnotice()),
                    redisSerializer.serialize(buildVal(cnNotice)));
        } finally {
            closeConn(connection);
        }
    }

    public void removeData(Map<String, Object> paramMap) {
        String pkDeptRecp, dateChk;
        if (MapUtils.isNotEmpty(paramMap)
                && StringUtils.equals("2", MapUtils.getString(paramMap, "euStatus"))
                && StringUtils.isNotBlank(pkDeptRecp = MapUtils.getString(paramMap, "pkDept"))
                && StringUtils.isNotBlank(dateChk = MapUtils.getString(paramMap, "dateChk"))) {
            //将刚才按条件更新的数据，查出来清掉
            String sql=null;
            if(Application.isSqlServer()){
                sql="select PK_CNNOTICE from cn_notice where PK_DEPT_RECP=? " +
                        " and DATE_CHK=dbo.to_date(?,'YYYYMMDDHH24MISS') " +
                        "and eu_status='2'";
            }else{
                sql="select PK_CNNOTICE from cn_notice where PK_DEPT_RECP=? " +
                        " and DATE_CHK=to_date(?,'YYYYMMDDHH24MISS') " +
                        "and eu_status='2'";
            }
            List<String> list = DataBaseHelper.getJdbcTemplate().queryForList(sql
                    , String.class, new Object[]{pkDeptRecp, dateChk});
            if (CollectionUtils.isNotEmpty(list)) {
                remove(list.stream().map(str -> pkDeptRecp + "_" + str).toArray(String[]::new));
            }
        }
    }

    /**
     * 清除一条redis缓存信息
     *
     * @param keys
     */
    public void remove(String... keys) {
        if (keys != null && keys.length > 0) {
            RedisConnection connection = null;
            try {
                connection = getConn();
                byte[][] k = new byte[keys.length][];
                for (int i = 0; i < keys.length; i++) {
                    k[i] = redisSerializer.serialize((keys[i]));
                }
                connection.hDel(getName(), k);
            } catch (Exception e) {
                logger.error("remove error:", e);
            } finally {
                closeConn(connection);
            }
        }
    }

    /**
     * 获取通知数量列表
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> queryCnNoticeCnt(Map<String, Object> paramMap) {
        if (MapUtils.isEmpty(paramMap)) {
            return null;
        }
        List<Map<String, Object>> list = buildCnt();
        RedisConnection connection = null;
        try {
            //从Redis中获取数据，并按条件过滤
            String pkDeptRecp = MapUtils.getString(paramMap, "pkDept");
            ScanOptions scan = ScanOptions.scanOptions().match(pkDeptRecp + "_*").build();
            connection = getConn();
            Cursor<Map.Entry<byte[], byte[]>> cursor = connection.hScan(getName(), scan);
            while (cursor.hasNext()) {
                //合并并累加相同类型，组装返回数据格式
                Map.Entry<byte[], byte[]> entry = cursor.next();
                String obj = deserial(entry.getValue());
                if (StringUtils.isBlank(obj)) {
                    continue;
                }
                String[] data = StringUtils.split(obj, ',');
                if (data.length < list.size()) {
                    continue;
                }
                int n = data.length;
                for (int i = list.size() - 1; i >= 0; i--) {
                    incrCntNotice(list.get(i), data[--n]);
                }
            }
            IOUtils.closeQuietly(cursor);
        } catch (Exception e) {
            logger.error("获取CnNotice信息异常：", e);
        } finally {
            closeConn(connection);
        }
        return list;
    }

    /**
     * 1.清空数据
     * 2.将符合条件的所有Notice信息重新刷入缓存<br>
     * 只获取状态<=1的
     *
     * @param paramMap
     */
    public void flush(Map<String, Object> paramMap) {
        String pkDept = MapUtils.getString(paramMap, "pkDept");
        RedisConnection connection = null;
        try {
            connection = getConn();
            if (StringUtils.isNotEmpty(pkDept)) {
                Set<byte[]> keys = connection.hKeys(getName());
                ;
                if (keys.size() > 0) {
                    remove(keys.stream().filter(k -> k != null && deserial(k).indexOf(pkDept) == 0).map(k -> deserial(k)).toArray(String[]::new));
                }
            } else {
                connection.del(getName());
            }
            List<CnNotice> list = cnNoticeMapper.queryHintCnNotice(paramMap);
            if (CollectionUtils.isNotEmpty(list)) {
                for (CnNotice cnNotice : list) {
                    add(cnNotice);
                }
            }
        } catch (Exception e) {
            throw new BusException(e);
        } finally {
            closeConn(connection);
        }
    }

    private byte[] getName() {
        return redisSerializer.serialize(com.zebone.nhis.common.support.RedisUtils.getRedisPreName() + NOTICE_SUFFIX);
    }

    /**
     * 构造存储字符串(考虑到存储占用，未使用对象序列化)
     *
     * @param cnNotice
     * @return
     */
    private String buildVal(CnNotice cnNotice) {
        StringBuilder sbl = new StringBuilder();
        //字符串值在这里追加
        sbl.append(cnNotice.getCntEmer() == null ? 0 : cnNotice.getCntEmer()).append(",")
                .append(cnNotice.getCntNew() == null ? 0 : cnNotice.getCntNew()).append(",")
                .append(cnNotice.getCntEnd() == null ? 0 : cnNotice.getCntEnd()).append(",")
                .append(cnNotice.getCntVoid() == null ? 0 : cnNotice.getCntVoid())
        //统计字段在这里追加
        ;
        return sbl.toString();
    }

    /**
     * 构造不通类型的统计，注意顺序不能变，往后追加即可
     *
     * @return
     */
    private List<Map<String, Object>> buildCnt() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(buildMap("1", "急"));
        list.add(buildMap("2", "新"));
        list.add(buildMap("3", "停"));
        list.add(buildMap("4", "废"));
        return list;
    }


    private Map<String, Object> buildMap(String noticeType, String nameNotice) {
        Map<String, Object> map = new HashMap<>();
        map.put("noticeType", noticeType);
        map.put("nameNotice", nameNotice);
        map.put("codeOper", CODEOPER);
        map.put("cntNotice", 0);
        return map;
    }

    private void incrCntNotice(Map<String, Object> map, String str) {
        int val = 0;
        try {
            val = (StringUtils.isBlank(str) ? 0 : Integer.valueOf(str));
        } catch (Exception e) {
        }
        map.put("cntNotice", MapUtils.getIntValue(map, "cntNotice") + val);
    }

    private RedisConnection getConn() {
        RedisConnection connection = null;
        try {
            try {
                connection = redisConnectionFactory.getClusterConnection();
            } catch (Exception e) {
                connection = redisConnectionFactory.getConnection();
            }
        } catch (Exception e) {
            logger.error("获取Redis连接异常：", e);
        }
        return connection;
    }

    private String deserial(byte[] cont) {
        return redisSerializer.deserialize(cont);
    }

    private void closeConn(RedisConnection connection) {
        if (connection != null) {
            connection.close();
        }
    }
}
