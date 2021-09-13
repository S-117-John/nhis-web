package com.zebone.nhis.ma.pub.platform.zsrm.factory.impl;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Response;
import com.zebone.nhis.ma.pub.platform.zsrm.annotation.EventResponse;
import com.zebone.nhis.ma.pub.platform.zsrm.factory.ResponseProcessor;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.ResponseBody;
import org.springframework.stereotype.Service;

@Service
@EventResponse("*")
public class DefaultResponseProcessor implements ResponseProcessor {

    @Override
    public ResponseBody create(Response response) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .build();

        responseBody.getEntry().add(new Entry(response));
        return responseBody;
    }
}
