package com.example.guaranty.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.guaranty.common.exception.BusinessException;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JsonHandleUtilTest {
    @Data
    private static class User {
        private String name;
        private String sex;
        private Integer age;

        User(String name, String sex, Integer age) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }
    }

    private static User user;
    private static String jsonStr;
    private static Map<String, Object> map;

    @Before
    public void setUp() {
        user = new User("小明", "男", 20);
        jsonStr = "{\"name\":\"小红\",\"sex\":\"女\",\"age\":18}";
        map = new HashMap<>(3);
        map.put("name", "小刚");
        map.put("sex", "男");
        map.put("age", 10);
    }

    @Test
    public void toJson() throws BusinessException {
        System.out.println(JsonHandleUtil.objToJsonStr(user));
    }

    @Test
    public void fromJson() throws BusinessException {
        User user_c = JsonHandleUtil.fromJson(jsonStr, User.class);
        System.out.println(user_c);
    }

    @Test
    public void fromJson1() {

    }

    @Test
    public void stringToJson() {
        JSONObject jsonObject = JsonHandleUtil.strToJsonObj(jsonStr);
        System.out.println(jsonObject.get("name"));
    }

    @Test
    public void mapToJson() {
        JSONObject jsonObject = JsonHandleUtil.mapToJsonObj(map);
        System.out.println(jsonObject.get("name"));
    }

    @Test
    public void mapToJsonStr() {
        System.out.println(JsonHandleUtil.mapToJsonStr(map));
    }
}