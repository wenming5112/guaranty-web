package com.example.guaranty.component;

import com.example.fabric.sdk.ca.CaManager;
import com.example.fabric.sdk.fabric.ChainCodeManager;
import com.example.fabric.sdk.fabric.FabricManager;
import com.example.fabric.sdk.fabric.IntermediateOrg;
import com.example.fabric.sdk.fabric.IntermediateUser;
import com.example.guaranty.config.fabric.SdkProperties;
import lombok.Data;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/8 15:55
 **/
@Component
public class FabricClientManager {

    private IntermediateUser admin;
    private CaManager caManager;
    private IntermediateOrg org1;
    private ChainCodeManager chainCodeManager;

    @Resource
    private SdkProperties sdkProperties;

    public IntermediateUser getCaAdmin() {
        admin = new IntermediateUser(sdkProperties.getCaAdminName(), sdkProperties.getCaAdminPass());
        admin.setAffiliation(sdkProperties.getCaDep());
        admin.setMspid(sdkProperties.getOrgMspId());
        return admin;
    }

    public CaManager getCaManager() {
        String caLocation = FabricManager.httpUrl(sdkProperties.getTls(), sdkProperties.getCaIp(), sdkProperties.getCaPort());
        caManager = new CaManager(sdkProperties.getCaName(), caLocation, getOrg());
        return caManager;
    }

    public IntermediateOrg getOrg() {
        org1 = new IntermediateOrg(sdkProperties.getOrgName(), sdkProperties.getOrgDomainName(), sdkProperties.getOrgMspId());
        org1.setOpenTls(sdkProperties.getTls());
        return org1;
    }

    public ChainCodeManager getChainCodeManager() throws Exception {
        ChainCodeManager.IntermediateChainCode chainCode = new ChainCodeManager.IntermediateChainCode(sdkProperties.getChainCodeName(),
                sdkProperties.getChainCodeSource(),
                null,
                sdkProperties.getChainCodeVersion(), TransactionRequest.Type.JAVA);
        chainCodeManager = new ChainCodeManager(getOrg(), sdkProperties.getChannelName(), chainCode);
        return chainCodeManager;
    }

    public IntermediateUser caEnroll(String username, String userPass) throws Exception {
        IntermediateUser newUser;
        newUser = new IntermediateUser(username, userPass);
        newUser.setAffiliation(sdkProperties.getCaDep());
        newUser.setMspid(sdkProperties.getOrgMspId());
        newUser = getCaManager().enroll(newUser);
        return newUser;
    }

    public Boolean caRegister(String username, String userPass) throws Exception {
        IntermediateUser newUser;
        newUser = new IntermediateUser(username, userPass);
        newUser.setAffiliation(sdkProperties.getCaDep());
        newUser.setMspid(sdkProperties.getOrgMspId());
        return getCaManager().register(getCaAdmin(), newUser);
    }


}
