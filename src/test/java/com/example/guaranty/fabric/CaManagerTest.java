package com.example.guaranty.fabric;

import com.example.fabric.sdk.ca.CaManager;
import com.example.fabric.sdk.fabric.FabricManager;
import com.example.fabric.sdk.fabric.IntermediateOrg;
import com.example.fabric.sdk.fabric.IntermediateUser;
import org.junit.Before;
import org.junit.Test;


public class CaManagerTest {
    private CaManager caManager;
    private IntermediateOrg org1;
    private IntermediateUser admin;
    private IntermediateUser newUser;
    private static final boolean TLS = true;

    @Before
    public void setUp() {
        String ca0Name = "ca-org1";
        String ca0Location = FabricManager.httpUrl(TLS, "47.108.143.240", "7054");

        // ---
        // 组织
        String orgName = "Org1";
        String orgDomainName = "org1.example.com";
        String mspId = "Org1MSP";
        String dep = "org1.department1";
        org1 = new IntermediateOrg(orgName, orgDomainName, mspId);
        org1.setOpenTls(TLS);
        caManager = new CaManager(ca0Name, ca0Location, org1);
        // ---
        // 管理员
        String adminName = "admin";
        String adminPassword = "admin";
        admin = new IntermediateUser(adminName, adminPassword);
        admin.setAffiliation(dep);
        admin.setMspid(org1.getMspId());
        // ---
        // 用户
        String newUserName = "m-1";
        String newUserPassword = "123456";
        newUser = new IntermediateUser(newUserName, newUserPassword);
        newUser.setAffiliation(dep);
        newUser.setMspid(org1.getMspId());
    }

    @Test
    public void register() throws Exception {
        boolean flag = caManager.register(admin, newUser);
        System.out.println("用户注册：" + flag);
    }

    @Test
    public void enrollAdmin() throws Exception {
        IntermediateUser admin = caManager.enroll(this.admin);
        System.out.println("管理员用户Enroll：" + admin);
    }

    @Test
    public void enrollNewUser() throws Exception {
        IntermediateUser newUser = caManager.enroll(this.newUser);
        System.out.println("普通用户Enroll：" + newUser);
    }

    @Test
    public void update() throws Exception {
        caManager.update(this.newUser, "1234566");
    }

    @Test
    public void getUserCertAndKey() throws Exception {
        String res = caManager.getUserCertAndKey(this.newUser);
        System.out.println("获取用户证书以及私钥：" + res);
    }

    @Test
    public void revoke() {
        String res = caManager.revoke(this.newUser, "Illegal behavior ");
        System.out.println("撤销结果：" + res);
    }
}
