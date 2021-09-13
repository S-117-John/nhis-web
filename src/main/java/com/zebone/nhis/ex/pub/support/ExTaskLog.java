package com.zebone.nhis.ex.pub.support;

import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class ExTaskLog {

    private static Logger loggerTaskFee = LoggerFactory.getLogger("nhis.quartz");


    public static void log(Object... info) {
        if (info == null) {
            return;
        }
        String msg = JsonUtil.writeValueAsString(info);
        if (loggerTaskFee.isInfoEnabled()) {
            loggerTaskFee.info(msg);
        } else if (loggerTaskFee.isDebugEnabled()) {
            loggerTaskFee.debug(msg);
        } else {
            loggerTaskFee.error(msg);
        }
    }

}
