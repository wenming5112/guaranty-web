package com.example.guaranty.common.utils;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

/**
 * JSON 处理工具
 *
 * @author ming
 * @version 1.0.0
 * @date 2019/05/29 18:37
 */
public final class JsonHandleUtil {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new JsonObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
    }

    public static <T> String objToJsonStr(T obj) throws RuntimeException {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String mapToJsonStr(Map<String, Object> map) {
        return new JSONObject(map).toJSONString();
    }

    public static JSONObject strToJsonObj(String str) {
        return JSON.parseObject(str);
    }

    public static JSONObject mapToJsonObj(Map<String, Object> map) {
        return new JSONObject(map);
    }

    public static <T> T fromJson(String json, Class<T> cls) throws RuntimeException {
        try {
            return objectMapper.readValue(json, cls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E, T extends Collection<E>> T fromJson(String json, Class<E> cls, Class<T> collectionCls) throws RuntimeException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionCls, cls);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
