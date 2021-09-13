package com.zebone.nhis.ma.pub.zsba.vo;

import java.util.List;

/**
 * @Classname PackPdVoList
 * @Description 查询出的摆药机明细和人工药袋明细集合
 * @Date 2020-04-06 17:12
 * @Created by wuqiang
 */
public class PackPdVoList {

    /**
     * 摆药机药袋
     * */
   private  List<PackPdMedVo> packPdVoList ;

    /**
     * 人共药袋长期口服药
     * */
   private  List<PackPdMedVo> AltogetherPachPdVolistLong;

    /**
     * 人共药袋-临时口服药
     * */
  private    List<PackPdMedVo> AltogetherPachPdVolistTemporary;




    public List<PackPdMedVo> getAltogetherPachPdVolistTemporary() {
        return AltogetherPachPdVolistTemporary;
    }

    public void setAltogetherPachPdVolistTemporary(List<PackPdMedVo> altogetherPachPdVolistTemporary) {
        AltogetherPachPdVolistTemporary = altogetherPachPdVolistTemporary;
    }

    public List<PackPdMedVo> getAltogetherPachPdVolistLong() {
        return AltogetherPachPdVolistLong;
    }

    public void setAltogetherPachPdVolistLong(List<PackPdMedVo> altogetherPachPdVolistLong) {
        AltogetherPachPdVolistLong = altogetherPachPdVolistLong;
    }

    public List<PackPdMedVo> getPackPdVoList() {
        return packPdVoList;
    }

    public void setPackPdVoList(List<PackPdMedVo> packPdVoList) {
        this.packPdVoList = packPdVoList;
    }
}
