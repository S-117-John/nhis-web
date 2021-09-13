package com.zebone.nhis.ma.pub.platform.zsrm.support;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.RequestBody;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

public class ImplicitRulesResolver {

    private static Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");
    private static ThreadLocal<ApiInfo> ApiInfo = new ThreadLocal<>();

    public static ApiInfo getImplicitRules(String param){
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        PhResource resource = null;
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        String sourceSign = null;
        if(requestBody== null || CollectionUtils.isEmpty(requestBody.getEntry())){
            resource = gson.fromJson(param, PhResource.class);
            sourceSign = resource.getSource()==null?null:resource.getSource().getEndpoint();
        } else {
            Optional<Entry> optionalEntry = getResource(requestBody.getEntry(),0);
            if(!optionalEntry.isPresent()){
                throw new BusException("Entry节点中未解析到包含implicitRules的resource节点");
            }
            resource = optionalEntry.get().getResource();
            //解析source节点
            optionalEntry = getResource(requestBody.getEntry(),1);
            sourceSign = optionalEntry.isPresent()?(optionalEntry.get().getResource().getSource().getEndpoint()):null;
        }
        if(StringUtils.isBlank(requestBody.getId())){
            throw new BusException("请求中缺少Id");
        }

//        if(StringUtils.isBlank(resource.getHospitalId())){
//            throw new BusException("implicitRules所在的resource节点中缺少hospitalId");
//        }

        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setHospitalId("a41476368e2943f48c32d0cb1179dab8");
        apiInfo.setImplicitRules(resource.getImplicitRules());
        apiInfo.setId(requestBody.getId());
        apiInfo.setSourceSign(sourceSign);
        return apiInfo;
    }

    private static Optional<Entry> getResource(List<Entry> entryList,int type){
        Optional<Entry> optionalEntry = entryList.stream()
                .filter(x -> x != null && x.getResource() != null
                        && (
                        (type == 0 && StringUtils.isNotBlank(x.getResource().getImplicitRules())
                                || (type == 1 && x.getResource().getSource() != null))))
                .findFirst();
        return optionalEntry;
    }

    public static void setApiInfo(ApiInfo apiInfo){
        ApiInfo.set(apiInfo);
    }

    public static ApiInfo getApiInfo(){
        ImplicitRulesResolver.ApiInfo info = ApiInfo.get();
        return info == null?new ApiInfo():info;
    }

    public static void clearApiInfo(){
        ApiInfo.remove();
    }

    public static class ApiInfo {
        /**his机构主键*/
        private String hospitalId;
        private String implicitRules;
        /** 最外层的ID*/
        private String id;
        /** 来源标志｛想用来区分不同的厂商｝*/
        private String sourceSign;

        public String getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(String hospitalId) {
            this.hospitalId = hospitalId;
        }

        public String getImplicitRules() {
            return implicitRules;
        }

        public void setImplicitRules(String implicitRules) {
            this.implicitRules = implicitRules;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSourceSign() {
            return sourceSign;
        }

        public void setSourceSign(String sourceSign) {
            this.sourceSign = sourceSign;
        }
    }
}
