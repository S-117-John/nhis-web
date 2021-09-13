package com.zebone.nhis.common.module.mybatis;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import java.lang.reflect.Method;
import java.sql.Timestamp;

public class CaseInsensitiveBean extends CaseInsensitiveMap {

    public CaseInsensitiveBean(int initialCapacity) {
        super(initialCapacity);
    }

    public CaseInsensitiveBean() {
        super();
    }

    public Object put(Object key, Object value) {
        if (key instanceof String) {
            if (((String)key).equals("ROWNUM_")) {
                return value;
            }

            key = this.getCamelCaseString((String)key, false);
        }

        if (value.getClass().getName().equals("oracle.sql.TIMESTAMP")) {
            Class clz = value.getClass();
            Method m = null;

            try {
                m = clz.getMethod("timestampValue");
                Timestamp t = (Timestamp)m.invoke(value);
                return super.put(key, t);
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        return super.put(key, value);
    }

    private String getCamelCaseString(String inputString, boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;

        for(int i = 0; i < inputString.length(); ++i) {
            char c = inputString.charAt(i);
            switch(c) {
                case ' ':
                case '#':
                case '$':
                case '&':
                case '-':
                case '/':
                case '@':
                case '_':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;
                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }
}
