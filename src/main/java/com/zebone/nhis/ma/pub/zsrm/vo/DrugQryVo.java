package com.zebone.nhis.ma.pub.zsrm.vo;

import java.util.List;

public class DrugQryVo {

    private List<WesDrugConfVo> configs;

    private List<String> pkPressocc;

    private List<String> pkPdstores;

    public List<WesDrugConfVo> getConfigs() {
        return configs;
    }

    public void setConfigs(List<WesDrugConfVo> configs) {
        this.configs = configs;
    }

    public List<String> getPkPdstores() {
        return pkPdstores;
    }

    public void setPkPdstores(List<String> pkPdstores) {
        this.pkPdstores = pkPdstores;
    }

    public List<String> getPkPressocc() {
        return pkPressocc;
    }

    public void setPkPressocc(List<String> pkPressocc) {
        this.pkPressocc = pkPressocc;
    }

    public static class WesDrugConfVo{

        private String codeDept;
        private String wino;

        public String getCodeDept() {
            return codeDept;
        }

        public void setCodeDept(String codeDept) {
            this.codeDept = codeDept;
        }

        public String getWino() {
            return wino;
        }

        public void setWino(String wino) {
            this.wino = wino;
        }
    }
}
