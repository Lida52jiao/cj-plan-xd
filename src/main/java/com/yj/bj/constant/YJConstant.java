package com.yj.bj.constant;

public class YJConstant {
	public static final String SUCCESS_BIG = "SUCCESS";
	public static final String FAIL_BIG = "FAIL";

	public static final String SUCCESS_XJ = "C";
	public static final String FAIL_XJ = "B";

	public static final String SUCCESS_XJHK = "E";
	public static final String FAIL_XJHK = "F";

	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	/**
	 * 成功
	 */
	public static final String FAIL_CODE="8000";
	public static final String FAIL_MSG="失败";
	/**
	 * 成功
	 */
	public static final String SUCCESS_CODE="0000";
	public static final String SUCCESS_MSG="成功";
	/**
	 * 服务器异常
	 */
	public static final String SERVER_ERROR_CODE="0001";
	public static final String SERVER_ERROR_MSG="服务器异常";

	public final static String INVALID_CODE="0005";
	public final static String INVALID_MSG="token失效";

	public final static String AMOUNT_ERROR_CODE="2001";
	public final static String AMOUNT_ERROR_MSG="金额错误";

	public final static String PAYTYPE_ERROR_CODE="2002";
	public final static String PAYTYPE_ERROR_MSG="支付通道错误";

	public final static String MER_NULL_CODE="2003";
	public final static String MER_NULL_MSG="找不到商户";

	public final static String CARD_NULL_CODE="2004";
	public final static String CARD_NULL_MSG="找不到银行卡";

    public static final String NUMBER_ERROR_CODE = "2005";
	public static final String NUMBER_ERROR_MSG ="每天最多还款2笔至少1笔" ;

	public static final String MINAMOUNT_ERROR_CODE = "2006";
	public static final String MINAMOUNT_ERROR_MSG ="平均还款金额必须大于" ;

	public static final String DATE_ERROR_CODE = "2007";
	public static final String DATE_ERROR_MSG ="今日9点之后不能制定今日的计划" ;

	public static final String BALANCE_ERROR_CODE = "2008";
	public static final String BALANCE_ERROR_MSG ="余额不足" ;

	public static final String DELETE_ERROR_CODE = "2009";
	public static final String DELETE_ERROR_MSG ="无法删除" ;

	public static final String RATIO_ERROR_CODE = "2010";
	public static final String RATIO_ERROR_MSG ="请选择合适的还款所需占比，或其他制定方式" ;

	public static final String BALANCE_NOTFOUND_CODE = "2011";
	public static final String BALANCE_NOTFOUND_MSG ="余额变更失败" ;

	public static final String ERROR_CODE = "2012";
	public static final String ERROR_MSG ="当前无需付费" ;

	public static final String DEBITWITHDRAW_ERROR_CODE = "2013";
	public static final String DEBITWITHDRAW_ERROR_MSG ="提现需大于等于10元" ;

	public static final String CARDNUMBER_ERROR_CODE = "2014";
	public static final String CARDNUMBER_ERROR_MSG ="只能同时给3张卡制定计划" ;

	public static final String DAY_ERROR_CODE = "2015";
	public static final String DAY_ERROR_MSG ="可用还款天数不足" ;

	public static final String PLAN_ERROR_CODE = "2016";
	public static final String PLAN_ERROR_MSG ="一张卡只能执行一笔计划" ;

	public static final String SIGN_ERROR_CODE = "2017";
	public static final String SIGN_ERROR_MSG = "签名错误";

	public static final String ANEW_TIME_CODE = "2018";
	public static final String ANEW_TIME_MSG = "现在无法补单";

	public static final String IDENTITY_CODE = "2019";
	public static final String IDENTITY_MSG = "身份验证失败";

	public final static String BIND_NULL_CODE="2020";
	public final static String BIND_NULL_MSG="通道未绑卡";



}
