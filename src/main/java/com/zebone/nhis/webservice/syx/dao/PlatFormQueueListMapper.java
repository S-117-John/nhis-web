package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import com.zebone.nhis.webservice.syx.vo.platForm.QueueListReq;
import com.zebone.nhis.webservice.syx.vo.platForm.QueueListRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormQueueListMapper {

	List<QueueListRes> getQueueList(QueueListReq req);

}
