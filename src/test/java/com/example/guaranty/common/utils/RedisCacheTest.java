package com.example.guaranty.common.utils;

import com.example.guaranty.bootstrap.GuarantyAdminApplication;
import com.example.guaranty.config.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GuarantyAdminApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedisCacheTest {

    @Resource
    private RedisCache redisCache;

    @Test
    public void generateCachePageKey() {
        System.out.println(String.format(">>>--- generateCachePageKey: %s", redisCache.generateCachePageKey("m", "fabric", 1, 10)));
    }

    @Test
    public void generateCacheKey() {
        System.out.println(String.format(">>>--- generateCacheKey: %s", redisCache.generateCacheKey("m", "fabric")));
    }

    @Test
    public void getExpire() {
        System.out.println(String.format(">>>--- getExpire: %s", redisCache.getExpire("m")));
    }

    @Test
    public void hasKey() {
        System.out.println(String.format(">>>--- hasKey: %s", redisCache.hasKey("m")));
    }

    @Test
    public void delete() {
        System.out.println(String.format(">>>--- delete: %s", redisCache.delete("m")));
    }

    @Test
    public void getString() {
        System.out.println(String.format(">>>--- getString: %s", redisCache.get("m")));
    }

    @Test
    public void setString() {
        redisCache.set("m", "是个很厉害的人");
    }

    @Test
    public void setString1() {
        redisCache.set("m", "是个很厉害的人", 20);
    }

    @Test
    public void getObject() {
    }

    @Test
    public void setObject() {
    }

    @Test
    public void setObject1() {
    }

    @Test
    public void incr() {
    }

    @Test
    public void decr() {
    }

    @Test
    public void hget() {
    }

    @Test
    public void hmget() {
    }

    @Test
    public void hmset() {
    }

    @Test
    public void hmset1() {
    }

}