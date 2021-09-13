package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

import java.util.List;

/**
 * CiResPackageUpMedicalVo
 */
public class CiResPackageUpMedicalVo {

    /**
     * head
     */
    private CiHeadVo head;

    private List<CiResUpMedicalBodyVo> body;    

    private CiAdditionInfo additionInfo;

    public CiHeadVo getHead() {
        return head;
    }

    public void setHead(CiHeadVo head) {
        this.head = head;
    }

    public List<CiResUpMedicalBodyVo> getBody() {
        return body;
    }

    public void setBody(List<CiResUpMedicalBodyVo> body) {
        this.body = body;
    }

    public CiAdditionInfo getAdditionInfo() {
        return additionInfo;
    }

    public void setAdditionInfo(CiAdditionInfo additionInfo) {
        this.additionInfo = additionInfo;
    }
}
