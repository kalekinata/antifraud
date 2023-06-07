package com.system.antifraud.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Accounts {
    @Id
    @Column(name = "id")
    public String accid;
    public String clid;
    public String bic;

    public Accounts() {}

    public Accounts(String accid, String clid, String bic) {
        this.accid = accid;
        this.clid = clid;
        this.bic = bic;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getClid() {
        return clid;
    }

    public void setClid(String clid) {
        this.clid = clid;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }
}
