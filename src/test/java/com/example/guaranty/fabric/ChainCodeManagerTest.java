package com.example.guaranty.fabric;

import com.example.fabric.sdk.fabric.ChainCodeManager;
import com.example.fabric.sdk.fabric.FabricManager;
import com.example.fabric.sdk.fabric.IntermediateOrg;
import com.example.fabric.sdk.fabric.IntermediateUser;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ChainCodeManagerTest {

    private ChainCodeManager chainCodeManager;
    private IntermediateUser admin;
    private IntermediateOrg org1;
    private static final boolean TLS = true;

    @Before
    public void setUp() throws Exception {
        // ---
        // 组织
        String orgName = "Org1";
        String orgDomainName = "org1.example.com";
        String mspId = "Org1MSP";
        String dep = "org1.department1";
        org1 = new IntermediateOrg(orgName, orgDomainName, mspId);
        org1.setOpenTls(TLS);

        Map<String, String> ordererLocations = new HashMap<>();
        Map<String, String> peerLocations = new HashMap<>();
        ordererLocations.put("orderer.example.com", FabricManager.grpcUrl(TLS, "47.108.143.240", "7050"));
        peerLocations.put("peer0.org1.example.com", FabricManager.grpcUrl(TLS, "47.108.143.240", "7051"));
        peerLocations.put("peer1.org1.example.com", FabricManager.grpcUrl(TLS, "47.108.143.240", "8051"));

        // 通道加入的排序节点
        org1.setOrdererLocations(ordererLocations);
        // 通道加入的peer节点
        org1.setPeerLocations(peerLocations);

        // 管理员
        String adminName = "admin";
        String adminPassword = "admin";
        admin = new IntermediateUser(adminName, adminPassword);
        admin.setAffiliation(dep);
        admin.setMspid(org1.getMspId());

        // 安装新的业务代码
        ChainCodeManager.IntermediateChainCode chainCode = new ChainCodeManager.IntermediateChainCode("guaranty",
                "F:/wzm_work/IDEAProjects/guaranty",
                null,
                "4", TransactionRequest.Type.JAVA);

        chainCodeManager = new ChainCodeManager(org1, "score", chainCode);
    }

    @Test
    public void install() throws Exception {
        Boolean res = chainCodeManager.install();
        System.out.println("安装结果：" + res);
    }

    @Test
    public void instantiate() throws Exception {
        String[] args = {};
        String endorsementFilePath = "F:/wzm_work/IDEAProjects/guaranty/chaincodeendorsementpolicy.yaml";
        Boolean res = chainCodeManager.instantiate(args, endorsementFilePath);
        System.out.println("实例化结果：" + res);
    }

    @Test
    public void createUser() throws Exception {
        String[] args = {"1", "ming", "22", "an23gn@163.com", "18208201778"};
        String func = "USER_CREATE";
        Map res = chainCodeManager.invoke(admin, func, args);
        System.out.println("实例化结果：" + res);
    }

    @Test
    public void deleteUser() throws Exception {
        String[] args = {"1"};
        String func = "USER_DELETE";
        Map res = chainCodeManager.invoke(admin, func, args);
        System.out.println("实例化结果：" + res);
    }

    @Test
    public void realNameAuth() throws Exception {
        String[] args = {"1", "文中明", "51100000000000000"};
        String func = "USER_REAL_NAME_AUTH";
        Map res = chainCodeManager.invoke(admin, func, args);
        System.out.println("实例化结果：" + res);
    }

    @Test
    public void updateContactInfo() throws Exception {
//        String[] args = {"1", "1", "1299900000@qq.com"};
        String[] args = {"1", "2", "18208201111"};
        String func = "USER_UPDATE_CONTACT_INFO";
        Map res = chainCodeManager.invoke(admin, func, args);
        System.out.println("实例化结果：" + res);
    }

    @Test
    public void queryUser() throws Exception {
        String[] args = {"1"};
        String func = "USER_QUERY";
        Map res = chainCodeManager.query(admin, func, args);
        System.out.println("实例化结果：" + res);
    }





    @Test
    public void upgrade() throws Exception {
        // 需要修改版本
        // 先安装新版本
        // 再升级到新版本
//        String[] args = {"a", "100000", "b", "100000"};
        String[] args = {};
        String endorsementFilePath = "F:/wzm_work/IDEAProjects/guaranty/chaincodeendorsementpolicy.yaml";
        Map res = chainCodeManager.upgrade(args, endorsementFilePath);
        System.out.println("升级结果：" + res);
    }

    @Test
    public void upgradePrivate() throws Exception {
        // 需要修改版本
        // 先安装新版本
        // 再升级到新版本
        // 使用私有数据据，链码上也要做相应修改stub.PutPrivateData("COLLECTION_FOR_A", id, productBytes)/
        String[] args = {"a", "100000", "b", "100000"};
        String endorsementFilePath = "src/main/java/com/example/fabric/fixture/chaincode/gocc/chaincodeendorsementpolicy.yaml";
        String collectionConfigurationPath = "src/main/java/com/example/fabric/fixture/chaincode/gocc/PrivateData.yaml";
        Map res = chainCodeManager.upgradePrivateData(args, endorsementFilePath, collectionConfigurationPath);
        System.out.println("升级结果：" + res);
    }

}