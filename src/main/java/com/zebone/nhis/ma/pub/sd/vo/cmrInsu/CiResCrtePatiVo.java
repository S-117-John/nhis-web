package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiResCrtePatiVo {

    /**
     * 结算方式
     * 数据字典：
     * 0-快赔
     * 1-部分直赔
     * 2-全部直赔
     */
    private String indemnitySign;

    /**
     * 提示信息
     */
    private String  promptInformation;

    public String getIndemnitySign() {
        return indemnitySign;
    }

    public void setIndemnitySign(String indemnitySign) {
        this.indemnitySign = indemnitySign;
    }

    public String getPromptInformation() {
        return promptInformation;
    }

    public void setPromptInformation(String promptInformation) {
        this.promptInformation = promptInformation;
    }
}
