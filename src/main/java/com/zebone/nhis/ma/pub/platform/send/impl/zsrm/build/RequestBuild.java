package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.platform.common.support.NHISUUID;

import java.util.ArrayList;
import java.util.Date;

public class RequestBuild {

    public static BusinessBase create(PhResource resource){
        String id = NHISUUID.getKeyId();
        BusinessBase businessBase = new BusinessBase();
        businessBase.setResourceType("Bundle");
        businessBase.setId(id);
        businessBase.setType("message");
        businessBase.setTimestamp(new Date());
        businessBase.setEntry(new ArrayList<>());

        Entry entry = new Entry();
        entry.setResource(new PhResource());
        PhResource rs = entry.getResource();
        rs.setResourceType("MessageHeader");
        rs.setId(id);
        rs.setSource(new Destination("HIS"));
        rs.setDestination(new ArrayList<>());
        rs.getDestination().add(new Destination("PT"));
        businessBase.getEntry().add(entry);

        if(resource !=null) {
            Entry etDt = new Entry();
            businessBase.getEntry().add(etDt);
            etDt.setResource(resource);
        }
        return businessBase;
    }

    public static void setExtension(PhResource resource){
        resource.setExtension(new ArrayList<>());
        Extension etDtExt1 = new Extension();
        etDtExt1.setUrl("source_system");
        etDtExt1.setValueString("HIS");
        Extension etDtExt2 = new Extension();
        etDtExt2.setUrl("method");
        etDtExt2.setValueString("PatientEncounter");
        resource.getExtension().add(etDtExt1);
        resource.getExtension().add(etDtExt2);
    }
}
