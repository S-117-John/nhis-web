package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.ElectronicInvoice;

public interface ElectronicInvoiceService {

    /**
     * 获取电子票据信息
     * @param electronicInvoice
     * @return
     */
    Object getElectronicInvoice(ElectronicInvoice electronicInvoice);
}
