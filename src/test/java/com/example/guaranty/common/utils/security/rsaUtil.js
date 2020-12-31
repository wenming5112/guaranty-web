// 加密方法
function encodeFunc(param, publicKey) {
    let encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    // 密文
    return encrypt.encrypt(JSON.stringify(param));
}

function encodeLongFunc(param, publicKey) {
    let encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    // 密文
    return encrypt.encryptLong(JSON.stringify(param));
}

// 解密方法
function decodeFunc(encryptStr, privateKey) {
    // 使用私钥解密
    let decrypt = new JSEncrypt();
    decrypt.setPrivateKey(privateKey);
    let decodeStr = decrypt.decrypt(encryptStr);
    console.log('解密后:' + decodeStr);
}

function accountInfo(account, merchantId, publicKey) {
    let param = {
        "account": account,
        "merchantId": merchantId
    };

    // 密文
    let encryptStr = encodeFunc(param, publicKey);

    let formData = new FormData();
    formData.append("parameter", encryptStr);
    formData.append("merchantId", merchantId);

    return formData;
}
