package com.zebone.nhis.ma.pub.zsba.vo.outflow;

import java.util.List;

public class PresUpResponse {
    private String success;
    private String fileId;
    private String fileUrl;
    private String recipeIdOutter;
    private String buyDrugCode;
    private List<PresStore> drugStores;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRecipeIdOutter() {
        return recipeIdOutter;
    }

    public void setRecipeIdOutter(String recipeIdOutter) {
        this.recipeIdOutter = recipeIdOutter;
    }

    public String getBuyDrugCode() {
        return buyDrugCode;
    }

    public void setBuyDrugCode(String buyDrugCode) {
        this.buyDrugCode = buyDrugCode;
    }

    public List<PresStore> getDrugStores() {
        return drugStores;
    }

    public void setDrugStores(List<PresStore> drugStores) {
        this.drugStores = drugStores;
    }

    public class PresStore{
        //dog is dog name is not equals, one is address,another is orgAddress...w t.f .
        private String storeName;
        private String address;

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
