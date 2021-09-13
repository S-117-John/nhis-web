package com.zebone.nhis.webservice.lbzy.support;

import com.google.common.collect.Lists;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class LbzySelfTools {

    private static List<PayType> payTypeList = Lists.newArrayList();
    private static List<String[]> sexTable = Lists.newArrayList(new String[]{"1","02"},new String[]{"2","03"},new String[]{"9","04"});

    static {
        payTypeList.add(new PayType("0","1","现金"));
        payTypeList.add(new PayType("1","3","银行卡"));
        payTypeList.add(new PayType("2","99","预交金（账户金）"));
        payTypeList.add(new PayType("3","99","消费卡"));
        payTypeList.add(new PayType("4","99","三方"));
        payTypeList.add(new PayType("5","9","医保"));
        payTypeList.add(new PayType("6","8","支付宝"));
        payTypeList.add(new PayType("7","7","微信"));
    }

    /**
     *
     * @param type 0:传入HIS编码 1：传入自助机编码
     * @param code
     * @return 返回转义后的编码
     */
    public static String getSex(int type,String code){
        if(type !=0 && type !=1)
            throw new BusException("获取性别类型传入错误");
        return sexTable.stream().filter(vo -> StringUtils.equals(code,type==0?vo[1]:vo[0]))
                .findFirst().orElse(new String[]{"",""})[type];
    }

    /**
     *
     * @param type 0:传入HIS编码 1：传入自助机编码
     * @param code
     * @return
     */
    public static String getPayCode(int type,String code){
        PayType payType = getPayType(type, code);
        return type==0?payType.getSelfCode():payType.getHisCode();
    }

    /**
     *
     * @param type 0:传入HIS编码 1：传入自助机编码
     * @param code
     * @return
     */
    public static  String getPayName(int type,String code){
        return getPayType(type, code).getName();
    }

    private static PayType getPayType(int type,String code){
        Optional.ofNullable(code).orElseThrow(()->new BusException("支付类型编码不能空"));
        return payTypeList.stream().filter(vo -> StringUtils.equals(code,type==0?vo.getHisCode():vo.getSelfCode()))
                .findFirst().orElseThrow(()->new BusException("支付类型匹配异常"));
    }

    public static String getDefHp(){
        Map<String,Object> mapHp = DataBaseHelper.queryForMap("select pk_hp from bd_hp where eu_hptype=0 and del_flag=0");
        if(MapUtils.isEmpty(mapHp)) {
            throw new BusException("没有全自费医保类型");
        }
        return MapUtils.getString(mapHp, "pkHp");
    }

    public static String geResSuccess(List<?> list, Class<?> clas){
        String str = StringUtils.join(list.stream().map(vo -> beanToXml(vo, clas)).collect(Collectors.toList()),"");
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Response><ResultCode>0</ResultCode><List>"+str+"</List></Response>";
    }

    public static String beanToXml(Object obj, Class<?> load) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(load);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new BusException(load.getName() + "转成xml时解析错误");
        }
    }

    /**
     * 以自助机为准映射
     */
    private static class PayType{
        public PayType(String selfCode, String hisCode, String name) {
            this.selfCode = selfCode;
            this.hisCode = hisCode;
            this.name = name;
        }

        private String selfCode;
        private String hisCode;
        private String name;

        public String getSelfCode() {
            return selfCode;
        }

        public void setSelfCode(String selfCode) {
            this.selfCode = selfCode;
        }

        public String getHisCode() {
            return hisCode;
        }

        public void setHisCode(String hisCode) {
            this.hisCode = hisCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
