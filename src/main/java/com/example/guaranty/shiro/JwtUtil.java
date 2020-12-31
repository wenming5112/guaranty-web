package com.example.guaranty.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.enumeration.ResponseResultEnum;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.utils.SpringUtil;
import com.example.guaranty.config.redis.RedisCache;
import com.example.guaranty.vo.business.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Jwt utils
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/19
 */
@Slf4j
public class JwtUtil {

    private static RedisCache redisCache = SpringUtil.getBean(RedisCache.class);

    public static String getRedisToken() throws BusinessException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(servletRequestAttributes)) {
            throw new BusinessException("不合法的请求，JwtUtil --> ServletRequestAttributes 为空！");
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if (!ObjectUtils.isEmpty(request)) {
            return request.getHeader(WebConstant.TOKEN_HEADER);
        }
        throw new BusinessException(ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
    }

    public static UserInfoVO getUserFromRedis() throws BusinessException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(servletRequestAttributes)) {
            throw new BusinessException("不合法的请求，JwtUtil --> ServletRequestAttributes 为空！");
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if (!ObjectUtils.isEmpty(request)) {
            String redisToken = request.getHeader(WebConstant.TOKEN_HEADER);
            if (StringUtils.isEmpty(redisToken)) {
                throw new BusinessException(ResponseResultEnum.TOKEN_IS_NULL.getMsg());
            }
            UserInfoVO user = (UserInfoVO) redisCache.get(redisToken);
            if (ObjectUtils.isEmpty(user)) {
                throw new BusinessException(ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
            }
            return user;
        }
        throw new BusinessException(ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
    }

    public static UserInfoVO getUserByRedisToken(String redisToken) throws BusinessException {
        if (StringUtils.isEmpty(redisToken)) {
            throw new BusinessException(ResponseResultEnum.TOKEN_IS_NULL.getMsg());
        }
        UserInfoVO user = (UserInfoVO) redisCache.get(redisToken);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
        }
        return user;
    }

    public static String getUserNameFromRedis() throws BusinessException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(servletRequestAttributes)) {
            throw new BusinessException("不合法的请求，JwtUtil --> ServletRequestAttributes 为空！");
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if (!ObjectUtils.isEmpty(request)) {
            String redisToken = request.getHeader(WebConstant.TOKEN_HEADER);
            if (StringUtils.isEmpty(redisToken)) {
                throw new BusinessException(ResponseResultEnum.TOKEN_IS_NULL.getMsg());
            }
            UserInfoVO user = (UserInfoVO) redisCache.get(redisToken);
            if (ObjectUtils.isEmpty(user)) {
                throw new BusinessException(ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
            }
            return user.getUserName();
        }
        throw new BusinessException(ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
    }

    /**
     * 获取当前用户
     *
     * @param request 从请求头中获取
     * @return 用户名
     */
    public static String getUsernameByRedisToken(HttpServletRequest request) throws BusinessException {
        String redisToken = request.getHeader(WebConstant.TOKEN_HEADER);
        if (StringUtils.isEmpty(redisToken)) {
            throw new BusinessException(ResponseResultEnum.TOKEN_IS_NULL.getMsg());
        }
        UserInfoVO user = (UserInfoVO) redisCache.get(redisToken);
        if (user != null) {
            return getUsername(user.getJwtToken());
        } else {
            throw new BusinessException(ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
        }
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    private static String getUsername(String jwtToken) {
        try {
            if (jwtToken == null) {
                return null;
            }
            DecodedJWT jwt = JWT.decode(jwtToken);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret, long expireTime) {
        Date date = new Date(System.currentTimeMillis() + expireTime * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
