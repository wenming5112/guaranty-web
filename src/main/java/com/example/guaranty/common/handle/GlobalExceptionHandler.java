package com.example.guaranty.common.handle;

import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.exception.RequestLimitException;
import com.example.guaranty.common.handle.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理
 *
 * @author ming
 * @date 2020/04/18
 */
@Slf4j
@Order()
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResult.failOf(e.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult businessExceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return ApiResult.failOf(e.getMessage());
    }

    /**
     * 接口限流 异常处理(实体对象传参)
     *
     * @param e RequestLimitException
     * @return ApiResult
     */
    @ExceptionHandler(value = RequestLimitException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult requestLimitExceptionHandler(RequestLimitException e) {
        log.error(String.format(">>>--- RequestLimit exception msg: %s", e.getMessage()), e);
        return ApiResult.failOf(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    /**
     * 未授权 异常处理(实体对象传参)
     *
     * @param e RequestLimitException
     * @return ApiResult
     */
    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult unauthenticatedExceptionHandler(UnauthenticatedException e) {
        log.error(String.format(">>>--- Unauthenticated exception msg: %s", e.getMessage()), e);
        return ApiResult.failOf(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    /**
     * 参数校验 异常处理(实体对象传参)
     *
     * @param e BindException
     * @return ApiResult
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        log.error(String.format(">>>--- MethodArgumentNotValid exception msg: %s", fieldErrors.get(0).getDefaultMessage()), fieldErrors.get(0));
        return ApiResult.failOf(fieldErrors.get(0).getDefaultMessage());
    }
}
