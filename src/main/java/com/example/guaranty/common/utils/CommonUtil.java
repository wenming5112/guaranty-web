package com.example.guaranty.common.utils;



import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * @author ming
 * @version 1.0.0
 * @date 2019 19:45
 */
public class CommonUtil {

    public static final String SMS_RESPONSE_MSG_KEY = "Message";
    public static final String SMS_RESPONSE_MSG_VALUE = "OK";

    /**
     * 生成数字验证码
     *
     * @param number num
     * @return String
     */
    public static String getRandomNumCode(int number) {
        StringBuilder codeNum = new StringBuilder();
        int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            //目的是产生足够随机的数，避免产生的数字重复率高的问题
            int next = random.nextInt(10000);
            codeNum.append(numbers[next % 10]);
        }
        return codeNum.toString();
    }

    /**
     * 生成阿里云短信服务TemplateParam参数模板
     *
     * @param code    验证码
     * @param product 供应商
     * @return json string
     */
    public static String getSmsTemplateParam(String code, String product) {
        if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(product)) {
            return "{\"code\":\"" + code + "\", \"product\":\"" + product + "\"}";
        }
        return null;
    }


    /**
     * 生成阿里云短信服务TemplateParam参数模板
     *
     * @param code 验证码
     * @return json string
     */
    public static String getSmsTemplate(String code) {
        if (!StringUtils.isBlank(code)) {
            return "{\"code\":\"" + code + "\"}";
        }
        return null;
    }


    /**
     * 获取短信测试验证码
     *
     * @param customer 联系人
     * @return json string
     */
    public static String getSmsTestTemplate(String customer) {
        if (StringUtils.isNotBlank(customer)) {
            return "{\"customer\":\"" + customer + "\"}";
        }
        return null;
    }

    /**
     * 获取活动确认短信验证码
     *
     * @param code    验证码
     * @param product 供应商
     * @param item    活动描述
     * @return json string
     */
    public static String getSmsActiveConfirmTemplate(String code, String product, String item) {
        if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(product) && StringUtils.isNotBlank(item)) {
            return "{\"code\":\"" + code + "\", \"product\":\"" + product + "\", \"item\":\"" + item + "\"}";

        }
        return null;
    }

}
