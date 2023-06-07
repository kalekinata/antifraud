package com.system.antifraud.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @Column(name = "id")
    public String trid;
    public Date dadd;
    public String region;
    public String country;
    public Float sum;
    public Float commission;
    public String clidSend;
    public String accidSend;
    public String clidRecip;
    public String accidResip;
    public String status;

    public Transaction() {}

    public Transaction(String trid, Date dadd, String region, String country, Float sum, Float commission, String clidSend, String accidSend, String clidRecip, String accidResip, String status) {
        this.trid = trid;
        this.dadd = dadd;
        this.region = region;
        this.country = country;
        this.sum = sum;
        this.commission = commission;
        this.clidSend = clidSend;
        this.accidSend = accidSend;
        this.clidRecip = clidRecip;
        this.accidResip = accidResip;
        this.status = status;
    }

    public String getTrid() {
        return trid;
    }

    public void setTrid(String trid) {
        this.trid = trid;
    }

    public Date getDadd() {
        return dadd;
    }

    public void setDadd(Date dadd) {
        this.dadd = dadd;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public Float getCommission() {
        return commission;
    }

    public void setCommission(Float commission) {
        this.commission = commission;
    }

    public String getClidSend() {
        return clidSend;
    }

    public void setClidSend(String clidSend) {
        this.clidSend = clidSend;
    }

    public String getAccidSend() {
        return accidSend;
    }

    public void setAccidSend(String accidSend) {
        this.accidSend = accidSend;
    }

    public String getClidRecip() {
        return clidRecip;
    }

    public void setClidRecip(String clidRecip) {
        this.clidRecip = clidRecip;
    }

    public String getAccidResip() {
        return accidResip;
    }

    public void setAccidResip(String accidResip) {
        this.accidResip = accidResip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
