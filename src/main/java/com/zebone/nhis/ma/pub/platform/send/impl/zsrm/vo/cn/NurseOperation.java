package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.google.common.collect.Lists;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Location;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Participant;

import java.util.List;

public class NurseOperation extends PhResource {

    private String status;
    private List<Participant> participant;
    private List<LocalLocation> location;
    private List<Extension> extension;
    private Hospitalization hospitalization;


    public static class Hospitalization{

        private Numerator preAdmissionIdentifier;
        private Location origin;
        private Location destination;

        public Hospitalization(Location origin, Location destination) {
            this.origin = origin;
            this.destination = destination;
        }

        public Hospitalization(String origName,String originCode, String destName,String destinationCode) {
            this.origin = new Location();
            this.origin.setResourceType("Location");
            this.origin.setIdentifier(Lists.newArrayList(new Identifier(origName,originCode)));

            this.destination = new Location();
            this.destination.setResourceType("Location");
            this.destination.setIdentifier(Lists.newArrayList(new Identifier(destName,destinationCode)));
        }

        public Location getOrigin() {
            return origin;
        }

        public void setOrigin(Location origin) {
            this.origin = origin;
        }

        public Location getDestination() {
            return destination;
        }

        public void setDestination(Location destination) {
            this.destination = destination;
        }

        public Numerator getPreAdmissionIdentifier() {
            return preAdmissionIdentifier;
        }

        public void setPreAdmissionIdentifier(Numerator preAdmissionIdentifier) {
            this.preAdmissionIdentifier = preAdmissionIdentifier;
        }
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

    public List<LocalLocation> getLocation() {
        return location;
    }

    public void setLocation(List<LocalLocation> location) {
        this.location = location;
    }

    @Override
    public List<Extension> getExtension() {
        return extension;
    }

    @Override
    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }

    public Hospitalization getHospitalization() {
        return hospitalization;
    }

    public void setHospitalization(Hospitalization hospitalization) {
        this.hospitalization = hospitalization;
    }
}
