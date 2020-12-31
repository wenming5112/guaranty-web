package com.example.guaranty.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author ming
 * @version 1.0.0
 * @date 2019/4/9 10:54
 */
class JsonObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -3071178005886058408L;

    JsonObjectMapper() {
        // 允许单引号
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 字段和值都加引号
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

            @Override
            public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2)
                    throws IOException {
                arg1.writeString("");
            }
        });
    }

}