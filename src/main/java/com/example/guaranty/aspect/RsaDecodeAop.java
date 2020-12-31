//package com.example.guaranty.aspect;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.example.guaranty.common.exception.BusinessException;
//import com.example.guaranty.common.utils.security.RsaUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Rsa 解密切面类
// *
// * @author ming
// * @version 1.0.0
// * @date 2020/10/27 9:53
// **/
//@Slf4j
//@Aspect
//@Component
//public class RsaDecodeAop {
//
//    @Value("${rsa.private-key}")
//    private String privateKey;
//
//    public RsaDecodeAop() {
//    }
//
//    /**
//     * 注解切点
//     */
//    @Pointcut("@annotation(com.example.guaranty.annotation.RsaDecode)")
//    public void pointCut() {
//    }
//
//    @Around("pointCut()")
//    public Object around(ProceedingJoinPoint point) throws Throwable {
//        Object[] args = point.getArgs();
//        try {
//            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (ObjectUtils.isEmpty(servletRequestAttributes)) {
//                throw new BusinessException("不合法的请求，ServletRequestAttributes 为空！");
//            }
//            HttpServletRequest request = servletRequestAttributes.getRequest();
//            MethodSignature methodName = (MethodSignature) point.getSignature();
//            Method method = methodName.getMethod();
//
//            Signature signature = point.getSignature();
//            MethodSignature methodSignature = (MethodSignature) signature;
//            String[] argNames = methodSignature.getParameterNames();
//            String entryStr = request.getParameter("parameter");
//
//            if (StringUtils.isBlank(entryStr)) {
//                throw new BusinessException("参数不合法，无法获取加密参数。");
//            }
//
//            // 如果有参数
//            if (argNames.length > 0) {
//                // 参数名列表
//                List<String> paramNameList = new ArrayList<>(Arrays.asList(methodSignature.getParameterNames()).subList(0, argNames.length));
//                // 参数类型
//                List<String> paramTypeList = new ArrayList<>();
//                Parameter[] parameters = method.getParameters();
//                for (Parameter parameter : parameters) {
//                    String paramType = parameter.getType().getName();
//                    paramTypeList.add(paramType);
//                }
//
//                String decodeStr = RsaUtils.rsaDecrypt(entryStr, privateKey);
//                JSONObject param = JSON.parseObject(decodeStr);
//
//                args = new Object[paramNameList.size()];
//
//                for (int i = 0; i < paramNameList.size(); i++) {
//                    String type = paramTypeList.get(i);
//                    log.info(type);
//                    switch (type) {
//                        case "java.lang.String":
//                            if (StringUtils.equalsIgnoreCase(paramNameList.get(i), "pageNo") && StringUtils.isBlank(param.getString(paramNameList.get(i)))) {
//                                args[i] = 1;
//                            } else if (StringUtils.equalsIgnoreCase(paramNameList.get(i), "pageSize") && StringUtils.isBlank(param.getString(paramNameList.get(i)))) {
//                                args[i] = 10;
//                            } else if (StringUtils.equalsIgnoreCase(paramNameList.get(i), "sort") && StringUtils.isBlank(param.getString(paramNameList.get(i)))) {
//                                args[i] = "desc";
//                            } else {
//                                args[i] = param.getString(paramNameList.get(i));
//                            }
//                            break;
//                        case "java.lang.Integer":
//                            if (StringUtils.equalsIgnoreCase(paramNameList.get(i), "pageNo") && ObjectUtils.isEmpty(param.getString(paramNameList.get(i)))) {
//                                args[i] = 1;
//                            } else if (StringUtils.equalsIgnoreCase(paramNameList.get(i), "pageSize") && ObjectUtils.isEmpty(param.getString(paramNameList.get(i)))) {
//                                args[i] = 10;
//                            } else {
//                                args[i] = param.getInteger(paramNameList.get(i));
//                            }
//                            break;
//                        case "javax.servlet.http.HttpServletRequest":
//                            args[i] = request;
//                            break;
//                        default:
//                            args[i] = param.get(paramNameList.get(i));
//                            break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("" + e);
//            throw new BusinessException("请升级至2.2.9版本");
//        }
//        return point.proceed(args);
//    }
//}
