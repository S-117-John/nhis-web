package com.zebone.nhis.ma.pub.platform.pskq.utils;

import java.util.Objects;

public class DynamicJsonsFactory {



    public Value any(String key, Object value) {
        Value v = new Value();
        v.put(Objects.requireNonNull(key), Objects.requireNonNull(value));

        return v;
    }
}
