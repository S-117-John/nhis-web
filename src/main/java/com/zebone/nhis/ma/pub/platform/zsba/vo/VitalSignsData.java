package com.zebone.nhis.ma.pub.platform.zsba.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Classname VitalSignsData
 * @Description 多体征数据
 * @Date 2021-06-16 16:02
 * @Created by wuqiang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public  class VitalSignsData {
    // 设备号
    private String dn;
    // 病人腕带号
    private String tag;
    // 护士工号
    private String tag2;
    // 测量时间戳
    private long timestamp;
    private String signType;
    private String refuse;
    private int refuseItem;
    // 归档时间戳
    private long specifyTime;
    // 归档类型
    private int specifyType;
    private String key;

    // 收缩压
    private int systolic;
    // 舒张压
    private int diastolic;
    // 平均压
    private int map;
    // 血压备注
    private String bpRemark;
    // 血压单位
    private String bpUnit;

    // 脉搏
    private int heartrate;
    // 心率
    private int heartrate2;

    // 体温测量值
    private float temperature;
    private String temperatureCW;
    // 测温位置
    private String temperatureMP;
    // 体温备注
    private String tempRemark;
    // 体温单位
    private String tempUnit;

    // 血糖测量值
    private float glucose;
    private String glucoseCode;
    private String glucosePeriod;
    // 血糖备注
    private String bgRemark;
    // 血糖单位
    private String glucoseUnit;

    // 血氧测量值
    private int oximetry;
    // 血氧灌注指数
    private float oximetryPI;
    // 血氧备注
    private String spo2Remark;

    // 大便次数
    private int stoolCount;
    // 大便次数（由中间件根据stoolCount格式化）
    private String stoolMsg;

    // 呼吸率
    private int respRate;

    // 尿量
    private int UPD;
    // 小便次数
    private String urineCount;

    // 身高
    private float height;
    // 体重
    private float weight;

    // （静息）疼痛指数
    private int pain;
    // 运动疼痛指数
    private int exercisePain;


    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getRefuse() {
        return refuse;
    }

    public void setRefuse(String refuse) {
        this.refuse = refuse;
    }

    public int getRefuseItem() {
        return refuseItem;
    }

    public void setRefuseItem(int refuseItem) {
        this.refuseItem = refuseItem;
    }

    public long getSpecifyTime() {
        return specifyTime;
    }

    public void setSpecifyTime(long specifyTime) {
        this.specifyTime = specifyTime;
    }

    public int getSpecifyType() {
        return specifyType;
    }

    public void setSpecifyType(int specifyType) {
        this.specifyType = specifyType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public String getBpRemark() {
        return bpRemark;
    }

    public void setBpRemark(String bpRemark) {
        this.bpRemark = bpRemark;
    }

    public String getBpUnit() {
        return bpUnit;
    }

    public void setBpUnit(String bpUnit) {
        this.bpUnit = bpUnit;
    }

    public int getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(int heartrate) {
        this.heartrate = heartrate;
    }

    public int getHeartrate2() {
        return heartrate2;
    }

    public void setHeartrate2(int heartrate2) {
        this.heartrate2 = heartrate2;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getTemperatureCW() {
        return temperatureCW;
    }

    public void setTemperatureCW(String temperatureCW) {
        this.temperatureCW = temperatureCW;
    }

    public String getTemperatureMP() {
        return temperatureMP;
    }

    public void setTemperatureMP(String temperatureMP) {
        this.temperatureMP = temperatureMP;
    }

    public String getTempRemark() {
        return tempRemark;
    }

    public void setTempRemark(String tempRemark) {
        this.tempRemark = tempRemark;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public float getGlucose() {
        return glucose;
    }

    public void setGlucose(float glucose) {
        this.glucose = glucose;
    }

    public String getGlucoseCode() {
        return glucoseCode;
    }

    public void setGlucoseCode(String glucoseCode) {
        this.glucoseCode = glucoseCode;
    }

    public String getGlucosePeriod() {
        return glucosePeriod;
    }

    public void setGlucosePeriod(String glucosePeriod) {
        this.glucosePeriod = glucosePeriod;
    }

    public String getBgRemark() {
        return bgRemark;
    }

    public void setBgRemark(String bgRemark) {
        this.bgRemark = bgRemark;
    }

    public String getGlucoseUnit() {
        return glucoseUnit;
    }

    public void setGlucoseUnit(String glucoseUnit) {
        this.glucoseUnit = glucoseUnit;
    }

    public int getOximetry() {
        return oximetry;
    }

    public void setOximetry(int oximetry) {
        this.oximetry = oximetry;
    }

    public float getOximetryPI() {
        return oximetryPI;
    }

    public void setOximetryPI(float oximetryPI) {
        this.oximetryPI = oximetryPI;
    }

    public String getSpo2Remark() {
        return spo2Remark;
    }

    public void setSpo2Remark(String spo2Remark) {
        this.spo2Remark = spo2Remark;
    }

    public int getStoolCount() {
        return stoolCount;
    }

    public void setStoolCount(int stoolCount) {
        this.stoolCount = stoolCount;
    }

    public String getStoolMsg() {
        return stoolMsg;
    }

    public void setStoolMsg(String stoolMsg) {
        this.stoolMsg = stoolMsg;
    }

    public int getRespRate() {
        return respRate;
    }

    public void setRespRate(int respRate) {
        this.respRate = respRate;
    }

    public int getUPD() {
        return UPD;
    }

    public void setUPD(int UPD) {
        this.UPD = UPD;
    }

    public String getUrineCount() {
        return urineCount;
    }

    public void setUrineCount(String urineCount) {
        this.urineCount = urineCount;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getPain() {
        return pain;
    }

    public void setPain(int pain) {
        this.pain = pain;
    }

    public int getExercisePain() {
        return exercisePain;
    }

    public void setExercisePain(int exercisePain) {
        this.exercisePain = exercisePain;
    }
}
