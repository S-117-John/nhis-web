package com.zebone.nhis.webservice.pskq.model;

import com.zebone.platform.common.support.User;

public class ElectronicInvoice {

    private String pkPv;

    private String pkSettle;

    /**
     * 打印纸质票据
     */
    private String flagPrint;

    /**
     * bd_res_pc(计算机工作站定义)
     * addr
     * 计算机物理地址
     */
    private String address;
    
    private User user;

    public static class Builder{
        private String pkPv;
        private String pkSettle;
        private String flagPrint;
        private String address;
        private User user;

        public Builder pkPv(String pkPv) {
            this.pkPv = pkPv;
            return this;
        }

        public Builder pkSettle(String pkSettle) {
            this.pkSettle = pkSettle;
            return this;
        }

        public Builder flagPrint(String flagPrint) {
            this.flagPrint = flagPrint;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }
        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public ElectronicInvoice build() {
            return new ElectronicInvoice(this);
        }
        
        

    }

    private ElectronicInvoice(Builder builder) {
        this.pkPv = builder.pkPv;
        this.pkSettle = builder.pkSettle;
        this.flagPrint = builder.flagPrint;
        this.address = builder.address;
        this.user = builder.user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public String getFlagPrint() {
        return flagPrint;
    }

    public void setFlagPrint(String flagPrint) {
        this.flagPrint = flagPrint;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
