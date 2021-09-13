package com.zebone.nhis.ma.pub.zsrm.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * atf_yp_page_no
 **/
@Table(value="atf_yp_page_no")
@Component
public class AtfYpPageNo {
    @PK
    @Field(value = "ward_sn")
    private String wardSn;//病区编号
    @Field(value = "group_no")
    private String groupNo;//库房组号
    @Field(value = "submit_time")
    private Date submitTime;//提交时间,his写入时的时间
    @Field(value = "confirm_time")
    private Date confirmTime;//确认时间,包药时间
    @Field(value = "confirm_oper")
    private String confirmOper;//确认人,包药操作员
    @Field(value = "page_no")
    private String pageNo;//单号,含义同表atf_ypxx中的相关字段含义
    @Field(value = "flag")
    private String flag;//标记0未包1已包2重复包药
    @Field(value = "atf_no")
    private String atfNo;//atf编号

    
    public List<AtfYpPageNo> getAtfYpPageNoByDetail(List<AtfYpxxDetailVo> atfYpxxDetailVos){
        AtfYpxxDetailVo atfYpxxDetailVo = atfYpxxDetailVos.get(0);
        List<AtfYpPageNo> atfYpPageNos=new LinkedList<>();
            AtfYpPageNo atfYpPageNo=new AtfYpPageNo();
            atfYpPageNo.atfNo="1";
            atfYpPageNo.flag="0";
            atfYpPageNo.pageNo=atfYpxxDetailVo.getPageNo();
            atfYpPageNo.submitTime=new Date();
            atfYpPageNo.groupNo=atfYpxxDetailVo.getGroupNo();
            atfYpPageNo.wardSn=atfYpxxDetailVo.getWardSn();
            atfYpPageNos.add(atfYpPageNo);
        return atfYpPageNos;
    }

    public String getWardSn() {
        return wardSn;
    }

    public void setWardSn(String wardSn) {
        this.wardSn = wardSn;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmOper() {
        return confirmOper;
    }

    public void setConfirmOper(String confirmOper) {
        this.confirmOper = confirmOper;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAtfNo() {
        return atfNo;
    }

    public void setAtfNo(String atfNo) {
        this.atfNo = atfNo;
    }
}
