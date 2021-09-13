package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListReq;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListRes;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormRoomListMapper {

	List<RoomListRes> getRoomList(RoomListReq req);

}
