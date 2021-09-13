package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Code;

import java.util.List;

/**
 * 消息体
 */
public class BdPdMsg extends Outcome {
    private BdPdCoding code;

    private String status;

    private Code form;

    private List<ExtensionPd> extension;

    private MumeratorCode amount;

    private Manufacturer manufacturer;

    private Batch batch;

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public MumeratorCode getAmount() {
        return amount;
    }

    public void setAmount(MumeratorCode amount) {
        this.amount = amount;
    }

    public BdPdCoding getCode() {
        return code;
    }

    public void setCode(BdPdCoding code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Code getForm() {
        return form;
    }

    public void setForm(Code form) {
        this.form = form;
    }

    public List<ExtensionPd> getExtension() {
        return extension;
    }

    public void setExtension(List<ExtensionPd> extension) {
        this.extension = extension;
    }
}
