package com.github.common.components.util.lang;


/**
 * 时间格式
 */
public enum DateFormat {
    YYYY_MM_DDHH_MM_SS("yyyy-MM dd-HH-mm-ss"),
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    YYYY_MM_DD("yyyy-MM-dd"),
    YYYYSMMSDD("yyyy/MM/dd"),
    YYYYMMDD("yyyyMMdd"),
    YYMMDD("yyMMdd"),
    HH_MM_SS("HH:mm:ss"),
    HH_MM("HH:mm"),
    MM$DD$("MM月dd日"),
    YYYY$MM$DD$("yyyy年MM月dd日"),
    YYYY$MM$DD$_HH_MM("yyyy年MM月dd日 HH:mm"),
    YYYY$MM$("yyyy年MM月");

    private String format;

    DateFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}

