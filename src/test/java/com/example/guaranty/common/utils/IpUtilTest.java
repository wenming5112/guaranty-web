package com.example.guaranty.common.utils;

import com.example.guaranty.common.exception.BusinessException;
import org.junit.Test;

import java.io.IOException;

public class IpUtilTest {

    @Test
    public void getCityInfo() throws IOException {
        System.out.println(IpUtil.getCityInfo("47.107.33.249"));
    }

    @Test
    public void getIp() {

    }

    @Test
    public void analysisDomainName() {
        System.out.println(IpUtil.analysisDomainName("thyc.com"));
    }

    @Test
    public void isDomain() throws BusinessException {
        System.out.println(IpUtil.isDomain("thyc.com"));
    }
}