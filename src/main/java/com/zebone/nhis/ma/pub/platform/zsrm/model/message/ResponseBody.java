package com.zebone.nhis.ma.pub.platform.zsrm.model.message;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.BusinessBase;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.platform.common.support.NHISUUID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponseBody extends BusinessBase implements Serializable {

    private BusinessBase businessBase;

    private ResponseBody(Builder builder) {
        setResourceType(builder.resourceType);
        setId(NHISUUID.getKeyId());
        setType(builder.type);
        setTimestamp(builder.timestamp);
        setEntry(builder.entry);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String resourceType;
        private String id;
        private String type;
        private Date timestamp;
        private List<Entry> entry = new ArrayList<>();

        private Builder() {
        }

        public Builder resourceType(String val) {
            resourceType = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder timestamp(Date val) {
            timestamp = val;
            return this;
        }

        public Builder entry(List<Entry> val) {
            entry = val;
            return this;
        }

        public ResponseBody build() {
            return new ResponseBody(this);
        }
    }
}
