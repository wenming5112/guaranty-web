package com.example.guaranty.bootstrap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Extends ObjectMapper Class
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/15
 */
class CustomMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    CustomMapper() {
        this.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 设置 SerializationFeature.FAIL_ON_EMPTY_BEANS 为 false
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}