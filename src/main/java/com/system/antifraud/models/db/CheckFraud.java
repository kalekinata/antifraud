package com.system.antifraud.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "check_fraud")
public class CheckFraud {
    @Id
    @Column(name = "id")
    public String checkid;
    public Date dadd;
    public String trid;
    public String status_check;
    public String description;

    public CheckFraud() {
    }

    public CheckFraud(String checkid, Date dadd, String trid, String status_check, String description) {
        this.checkid = checkid;
        this.dadd = dadd;
        this.trid = trid;
        this.status_check = status_check;
        this.description = description;
    }

    public String getCheckid() {
        return checkid;
    }

    public void setCheckid(String checkid) {
        this.checkid = checkid;
    }

    public Date getDadd() {
        return dadd;
    }

    public void setDadd(Date dadd) {
        this.dadd = dadd;
    }

    public String getTrid() {
        return trid;
    }

    public void setTrid(String trid) {
        this.trid = trid;
    }

    public String getStatus_check() {
        return status_check;
    }

    public void setStatus_check(String status_check) {
        this.status_check = status_check;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
