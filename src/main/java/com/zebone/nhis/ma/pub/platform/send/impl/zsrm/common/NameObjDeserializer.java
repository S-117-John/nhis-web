package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.List;

/**
 * 反序列化时name是String或者List<TextElement>，最终都返回一个String
 * <br> 如果是集合，获取第一个
 */
public class NameObjDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if(jsonParser.getCurrentToken() == JsonToken.START_ARRAY){
            List<TextElement> list = jsonParser.readValueAs(new TypeReference<List<TextElement>>() {});
            if(list!=null && list.size()>0){
                return list.get(0).getText();
            }
            return null;
        }
        return jsonParser.getText();
    }
}
