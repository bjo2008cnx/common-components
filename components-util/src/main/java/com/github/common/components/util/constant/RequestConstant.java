package com.github.common.components.util.constant;

import java.util.Locale;

/**
 * RequestConstant
 *
 * @author Michael.Wang
 * @date 2017/9/10
 */
public class RequestConstant {

    public static final String APPLICATION_JSON_VALUE = "application/json";
    public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";
    public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
    public static final String WECHAT_AGENT = "MicroMessenger".toLowerCase(Locale.CHINESE);
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_REAL_IP = "X-Real-IP";
    public static final String X_REQUESTED_WITH = "x-requested-with";
    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

}