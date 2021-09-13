package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ConditionRecIdentifierDeserializer extends JsonDeserializer<Object> {
    private static Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try{
            JsonToken jsonToken = jsonParser.getCurrentToken();
            if(jsonToken == JsonToken.START_ARRAY){
                return jsonParser.readValueAs(new TypeReference<List<Identifier>>() {});
            }
            if(jsonToken == JsonToken.START_OBJECT){
                return jsonParser.readValueAs(new TypeReference<Identifier>() {});
            }
        } catch (Exception e){
            logger.error("Identifier-type字段反序列化处理异常：",e);
        }
        return null;
    }

}
