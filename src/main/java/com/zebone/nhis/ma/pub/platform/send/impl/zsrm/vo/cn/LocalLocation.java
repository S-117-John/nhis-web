package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Location;

public class LocalLocation {
    private Location location;

    public LocalLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}