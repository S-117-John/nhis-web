package com.zebone.nhis.base.ou.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface TraineeSetMapper {
	
	/**
	 * 查询实习生信息
	 * @param pkEmpTeach
	 * @return
	 */
	public List<Map<String,Object>> qryTraineeSet(@Param("pkEmp")String pkEmp);
		
	/**
	 * 删除实习生
	 */
	public void delTrainee(Map<String, Object> map);		
	
	/**
	 * 更新实习生信息
	 */
	public void updateTraineeByPkEmp(Map<String, Object> map);			
	
	/**
	 * 根据PkEmp获得实习生
	 * @param pkEmp
	 * @return
	 */
	public List<Map<String,Object>> qryTraineeSetByPkEmp(@Param("pkEmp")String pkEmp);
	
	/**
	 * 得到导师名字
	 * @param pkEmp
	 * @return
	 */
	public String getTeacherName(@Param("pkEmp")String pkEmp);
	

	

}
