package com.yj.bj.constant;

/**
 * Created by bin on 2018/1/25.
 */
public enum BankCode {
    //BCCB("北京银行","BCCB","2"),
    ICBC("中国工商银行","ICBC","102","ICBC"), ABC("中国农业银行","ABC","103","ABC"), BOC("中国银行","BOC","104","BOC"), CCB("中国建设银行","CCB","105","CCB"),
    CMBCHINA("招商银行","CMBCHINA","308","CMB"), POST("中国邮政储蓄银行","POST","403","PSBC"), ECITIC("中信银行","ECITIC","302","CITIC"), CEB("中国光大银行","CEB","303","CEB"),
    BOCO("交通银行","BOCO","301","BCOM"), CIB("兴业银行","CIB","309","CIB"), CMBC("中国民生银行","CMBC","305","CMBC"), PINGAN("平安银行","PINGAN","307","PAB"),
    CGB("广东发展银行","CGB","306","CGB"),  HXB("华夏银行","HXB","304","HXB"), SPDB("上海浦东发展银行","SPDB","310","SPDB");
    private String name;
    private String yjCode;
    private String easyCode;
    private String cjCode;

    public static String getName(String yjCode){
        for(BankCode bankCode: BankCode.values()){
            if(bankCode.getYjCode().equals(yjCode)){
                return bankCode.getName();
            }
        }
        return "";
    }
    public static String getEasyCode(String yjCode){
        for(BankCode bankCode: BankCode.values()){
            if(bankCode.getYjCode().equals(yjCode)){
                return bankCode.getEasyCode();
            }
        }
        return "";
    }
    public static String getCjCode(String yjCode){
        for(BankCode bankCode: BankCode.values()){
            if(bankCode.getYjCode().equals(yjCode)){
                return bankCode.getCjCode();
            }
        }
        return "";
    }

    private BankCode(String name, String yjCode, String easyCode, String cjCode) {
        this.name = name;
        this.yjCode = yjCode;
        this.easyCode = easyCode;
        this.cjCode = cjCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYjCode() {
        return yjCode;
    }

    public void setYjCode(String yjCode) {
        this.yjCode = yjCode;
    }

    public String getEasyCode() {
        return easyCode;
    }

    public void setEasyCode(String easyCode) {
        this.easyCode = easyCode;
    }

    public String getCjCode() {
        return cjCode;
    }

    public void setCjCode(String cjCode) {
        this.cjCode = cjCode;
    }
}
