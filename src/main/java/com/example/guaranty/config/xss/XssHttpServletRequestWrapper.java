package com.example.guaranty.config.xss;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Sql 注入检测
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/15 22:26
 **/
@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXss(values[i]);
        }
        return encodedValues;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     *
     * @param parameter parameter
     * @return Str
     */
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXss(value);
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return cleanXss(value);
    }

    private String cleanXss(String value) {
        log.debug(">>>--- cleanXss处理前：" + value);
        // You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\"\'][\\s]*javascript:(.*)[\"\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("[*]", "[" + "*]");
        value = value.replaceAll("[+]", "[" + "+]");
        value = value.replaceAll("[?]", "[" + "?]");

        // replace sql
        String[] values = value.split(" ");

        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|,|like|//|/|%|#";

        String[] badStrs = badStr.split("\\|");

        for (String item : badStrs) {
            for (int j = 0; j < values.length; j++) {
                if (values[j].equalsIgnoreCase(item)) {
                    values[j] = "forbid";
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i == values.length - 1) {
                sb.append(values[i]);
            } else {
                sb.append(values[i]).append(" ");
            }
        }
        value = sb.toString();
        log.debug(">>>--- cleanXss处理后：" + value);
        return value;
    }
}