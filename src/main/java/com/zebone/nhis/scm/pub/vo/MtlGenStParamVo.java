package com.zebone.nhis.scm.pub.vo;

public class MtlGenStParamVo {
    private String pkPd;
    private String pkStore;
    private double quanMin;
    private String pdCode;
    private String pdName;
    private String spec;
    private String spcode;
    private String factory;
    private String unitName;
    private String pkPdplandt;
    private String unitPd;
    private String flagUse;

    private MtlPdBatchVo mtlPdBatchVo;

    private MtlGenStParamVo(Builder builder) {
        pkPd = builder.pkPd;
        pkStore = builder.pkStore;
        quanMin = builder.quanMin;
        pdCode = builder.pdCode;
        pdName = builder.pdName;
        spec = builder.spec;
        spcode = builder.spcode;
        factory = builder.factory;
        unitName = builder.unitName;
        pkPdplandt = builder.pkPdplandt;
        unitPd = builder.unitPd;
        flagUse = builder.flagUse;
        mtlPdBatchVo = builder.mtlPdBatchVo;
    }

    public String getPkPd() {
        return pkPd;
    }

    public String getPkStore() {
        return pkStore;
    }

    public double getQuanMin() {
        return quanMin;
    }

    public String getPdCode() {
        return pdCode;
    }

    public String getPdName() {
        return pdName;
    }

    public String getSpec() {
        return spec;
    }

    public String getSpcode() {
        return spcode;
    }

    public String getFactory() {
        return factory;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getPkPdplandt() {
        return pkPdplandt;
    }

    public String getUnitPd() {
        return unitPd;
    }

    public String getFlagUse() { return  flagUse; }

    public MtlPdBatchVo getMtlPdBatchVo() {
        return mtlPdBatchVo;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String pkPd;
        private String pkStore;
        private double quanMin;
        private String pdCode;
        private String pdName;
        private String spec;
        private String spcode;
        private String factory;
        private String unitName;
        private String pkPdplandt;
        private String unitPd;
        private String flagUse;
        private MtlPdBatchVo mtlPdBatchVo;

        public Builder() {
        }

        public Builder pkPd(String val) {
            pkPd = val;
            return this;
        }

        public Builder pkStore(String val) {
            pkStore = val;
            return this;
        }

        public Builder quanMin(double val) {
            quanMin = val;
            return this;
        }

        public Builder pdCode(String val) {
            pdCode = val;
            return this;
        }

        public Builder pdName(String val) {
            pdName = val;
            return this;
        }

        public Builder spec(String val) {
            spec = val;
            return this;
        }

        public Builder spcode(String val) {
            spcode = val;
            return this;
        }

        public Builder factory(String val) {
            factory = val;
            return this;
        }

        public Builder unitName(String val) {
            unitName = val;
            return this;
        }

        public Builder pkPdplandt(String val) {
            pkPdplandt = val;
            return this;
        }

        public Builder unitPd(String val) {
            unitPd = val;
            return this;
        }

        public Builder flagUse(String val){
            flagUse = val;
            return  this;
        }

        public Builder mtlPdBatchVo(MtlPdBatchVo val) {
            mtlPdBatchVo = val;
            return this;
        }

        public MtlGenStParamVo build() {
            return new MtlGenStParamVo(this);
        }
    }
}
