package com.example.guaranty.common.handle;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Global Return
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/14 14:30
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("统一响应类")
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 9024462031856060619L;
    @ApiModelProperty("code")
    private int code = 0;
    @ApiModelProperty("message")
    private String msg = "";
    @ApiModelProperty("data")
    private T data;

    public ApiResult() {
        super();
    }

    public ApiResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResult<String> successOf(String msg) {
        ApiResult<String> apiResult = new ApiResult<>();
        apiResult.setCode(HttpStatus.OK.value());
        apiResult.setMsg(msg);
        apiResult.setData("");
        return apiResult;
    }

    public static <T> ApiResult<T> successOf(T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setCode(HttpStatus.OK.value());
        apiResult.setMsg(HttpStatus.OK.getReasonPhrase());
        apiResult.setData(data);
        return apiResult;
    }

    public static ApiResult<String> successOfStrData(String data) {
        ApiResult<String> apiResult = new ApiResult<>();
        apiResult.setCode(HttpStatus.OK.value());
        apiResult.setMsg(HttpStatus.OK.getReasonPhrase());
        apiResult.setData(data);
        return apiResult;
    }

    public static <T> ApiResult<T> successOf(String msg, T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setCode(HttpStatus.OK.value());
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static <T> ApiResult<T> successOf(int code, String msg, T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static ApiResult<String> failOf(String msg) {
        ApiResult<String> apiResult = new ApiResult<>();
        apiResult.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiResult.setMsg(msg);
        apiResult.setData("");
        return apiResult;
    }

    public static ApiResult<String> failOf(int code, String msg) {
        ApiResult<String> apiResult = new ApiResult<>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData("");
        return apiResult;
    }

}

