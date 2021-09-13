package com.zebone.nhis.pro.zsba.mz.ins.zsba.service.sgs;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/*import net.sf.json.JSONArray;
import net.sf.json.JSONObject;*/
import org.dom4j.*;
import java.nio.ByteBuffer;
import java.util.List;
 
public class TestXml2Json {
    /**
     * xml转json
     * @param xmlStr
     * @return
     * @throws DocumentException
     */
    public static JSONObject xml2Json(String xmlStr) throws DocumentException {
        Document doc= DocumentHelper.parseText(xmlStr);
        JSONObject json=new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;
    }
 
    /**
     * xml转json
     * @param element
     * @param json
     */
    public static void dom4j2Json(Element element,JSONObject json){
        List<Element> chdEl=element.elements();
        
        if(chdEl.isEmpty()&&!isEmpty(element.getText())){//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }
 
        for(Element e:chdEl){//有子元素
            if(!e.elements().isEmpty()){//子元素也有子元素
                JSONObject chdjson=new JSONObject();
                dom4j2Json(e,chdjson);
                Object o=json.get(e.getName());
                if(o!=null){
                	JSONArray jsona=null;

            		if(o instanceof JSONObject){
                        JSONObject jsono=(JSONObject)o;
                        json.remove(e.getName());
                        jsona=new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    
                    if(o instanceof JSONArray){
                        jsona=(JSONArray)o;
                        jsona.add(chdjson);
                    }
                    
                    json.put(e.getName(), jsona);
                }else{
                    if(!chdjson.isEmpty()){
                    	if(chdjson.containsKey("row")){
                    		Object obj=chdjson.get("row");
                    		JSONArray arr = null;
                        	
                        	if(obj instanceof JSONObject){
                        		arr = new JSONArray();
                        		arr.add(obj);
                            }
                            
                            if(obj instanceof JSONArray){
                                arr=(JSONArray)obj;
                            }

                    		json.put(e.getName(), arr);
                    	}else {
                    		json.put(e.getName(), chdjson);
                    	}
                    }
                }

            }else{//子元素没有子元素
                if(!e.getText().isEmpty()){
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }
 
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }
}
