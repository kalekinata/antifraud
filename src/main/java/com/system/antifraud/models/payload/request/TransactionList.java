package com.system.antifraud.models.payload.request;

import java.util.Date;

public class TransactionList {
    private String trid;
    private String region;
    private String country;
    private float sum;
    private float com;
    private ClSender clSender;
    public static class ClSender {
        private String clid;
        private String surname;
        private String name;
        private String patronymic;
        private String region;
        private String country;
        private Account account;

        public static class Account{

            private String accid;
            private String bic;

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }

            public String getBic() {
                return bic;
            }

            public void setBic(String bic) {
                this.bic = bic;
            }
        }
        public String getClid() {
            return clid;
        }
        public void setClid(String clid) {
            this.clid = clid;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
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

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }

    }

    private ClRecip clRecip;
    public static class ClRecip {
        private String clid;
        private String surname;
        private String name;
        private String patronymic;
        private Account account;
        public static class Account{
            private String accid;
            private String bic;

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }

            public String getBic() {
                return bic;
            }

            public void setBic(String bic) {
                this.bic = bic;
            }
        }

        public String getClid() {
            return clid;
        }
        public void setClid(String clid) {
            this.clid = clid;
        }
        public String getSurname() {
            return surname;
        }
        public void setSurname(String surname) {
            this.surname = surname;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getPatronymic() {
            return patronymic;
        }
        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }
        public Account getAccount() {
            return account;
        }
        public void setAccount(Account account) {
            this.account = account;
        }
    }

    public String getTrid() {
        return trid;
    }

    public void setTrid(String trid) {
        this.trid = trid;
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

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public float getCom() {
        return com;
    }

    public void setCom(float com) {
        this.com = com;
    }

    public ClSender getClSender() {
        return clSender;
    }

    public void setClSender(ClSender clSender) {
        this.clSender = clSender;
    }

    public ClRecip getClRecip() {
        return clRecip;
    }

    public void setClRecip(ClRecip clRecip) {
        this.clRecip = clRecip;
    }
}
