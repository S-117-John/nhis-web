package com.zebone.nhis.scm.pub.dao;

import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.scm.pub.vo.ExPresOccDtVO;
import com.zebone.nhis.scm.pub.vo.ExPresOccPddtVO;
import com.zebone.nhis.scm.pub.vo.OpPreRtnVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper
public interface SchOpdsPreMapper {

    public List<Map<String, Object>> DispensingPatis(@Param("pkDept") String pkDept, @Param("winno") String winno);

    public List<OpPreRtnVo> DispensingPresDt(@Param("pkStore") String pkStore, @Param("pkPresocc") String pkPresocc);

    @Select("SELECT pres.pk_pv,dt.* FROM ex_pres_occ_dt  dt inner join ex_pres_occ pres on pres.pk_presocc = dt.pk_presocc  WHERE (dt.quan_ret='0' or dt.quan_ret is null) and dt.pk_presocc = #{pkPresocc}")
    public List<ExPresOccDtVO> QryPresDt(@Param("pkPresocc") String pkPresocc);

    public void updExPresOcc(Map<String, Object> map);

    @Select("SELECT * FROM ex_pres_occ WHERE pk_presocc = #{pkPresocc}")
    public ExPresOcc QryPresByPk(@Param("pkPresocc") String pkPresocc);


    public ExPresOccDt QryPresDtByPk(@Param("pkPresoccdt") String pkPresoccdt);

    public List<ExPresOccPddtVO> qryPresRecords(@Param("pkPresoccdt") String pkPresoccdt);

    @Select("SELECT *FROM ex_pres_occ occ INNER JOIN ex_pres_occ_dt dt ON dt.pk_presocc = occ.pk_presocc WHERE dt.pk_presoccdt = #{pkPresoccdt}")
    public ExPresOcc qryPresOccByPkdt(@Param("pkPresoccdt") String pkPresoccdt);

    public List<PdStDetail> QryPdStDts(Collection<String> values);

    /**
     * 对门诊处方明细进行查询,用于模板打印
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryOpPresPrints(Map<String, Object> map);
}
