package com.system.antifraud.learning_model;

public class CheckFraudRequest {

    private Integer suspicious;
    private String tranCountry;
    private String tranRegion;
    private Float tranSum;
    private String senderCountry;
    private String senderRegion;
    private Float senderAvgSum;
    private long time;
    private Integer marker;

    public CheckFraudRequest(Integer suspicious, String tranCountry,String tranRegion,
                             Float tranSum, String senderCountry, String senderRegion,
                             Float senderAvgSum, long time, Integer marker) {
        this.suspicious = suspicious;
        this.tranCountry = tranCountry;
        this.tranRegion = tranRegion;
        this.tranSum = tranSum;
        this.senderCountry = senderCountry;
        this.senderRegion = senderRegion;
        this.senderAvgSum = senderAvgSum;
        this.time = time;
        this.marker = marker;
    }

    public Integer getSuspicious() {
        return suspicious;
    }

    public void setSuspicious(Integer suspicious) {
        this.suspicious = suspicious;
    }

    public String getTranCountry() {
        return tranCountry;
    }

    public void setTranCountry(String tranCountry) {
        this.tranCountry = tranCountry;
    }

    public String getTranRegion() {
        return tranRegion;
    }

    public void setTranRegion(String tranRegion) {
        this.tranRegion = tranRegion;
    }

    public Float getTranSum() {
        return tranSum;
    }

    public void setTranSum(Float tranSum) {
        this.tranSum = tranSum;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getSenderRegion() {
        return senderRegion;
    }

    public void setSenderRegion(String senderRegion) {
        this.senderRegion = senderRegion;
    }

    public Float getSenderAvgSum() {
        return senderAvgSum;
    }

    public void setSenderAvgSum(Float senderAvgSum) {
        this.senderAvgSum = senderAvgSum;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
