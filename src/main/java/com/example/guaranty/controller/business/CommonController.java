package com.example.guaranty.controller.business;

import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.common.utils.security.RsaUtils;
import com.example.guaranty.service.business.CommonService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 公共模块接口
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/15 16:23
 **/
@RestController
@RequestMapping("common")
@Api(tags = "通用接口")
@CrossOrigin
public class CommonController {

    @Resource
    private CommonService commonService;

    @PostMapping(value = "email/{email}")
//    @RequestLimit(count = 5, time = 300000, apiName = "common-email")
    @ApiOperation(value = "获取邮箱验证", notes = "获取邮箱验证", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱地址", dataType = "string", paramType = "path", required = true),
    })
    public ApiResult emailVerifyCode(@PathVariable String email) throws Exception {
        return commonService.emailVerifyCode(email);
    }

    @PostMapping(value = "file/{type}", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "文件上传(ali oss / local)", notes = "文件上传(ali oss / local)接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "上传类型;1-local;2-oss", dataType = "int", paramType = "path", required = true, allowableValues = "1", defaultValue = "1"),
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    public ApiResult upload(@Min(value = 1, message = "{common.notAllow}") @Max(value = 2, message = "{common.notAllow}") @PathVariable("type") Integer type,
                            @RequestParam(name = "file") @ApiParam(value = "文件", name = "file", type = "file", required = true) MultipartFile file) throws Exception {
        switch (type) {
            case 1:
                return commonService.uploadFile(file);
            default:
                throw new BusinessException("未知的上传类型");
        }
    }

    @DeleteMapping(value = "file/{type}/{name}")
    @ApiOperation(value = "删除文件(ali oss / local)", notes = "删除文件(ali oss / local)接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "上传类型;1-local;2-oss", dataType = "int", paramType = "path", required = true, allowableValues = "1", defaultValue = "1"),
            @ApiImplicitParam(name = "name", value = "文件名(包含后缀)", example = "10be4378303a45e6bb2a234784f893f2.gif", dataType = "string", paramType = "path", required = true)
    })
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResult deleteFile(@Min(value = 1, message = "{common.notAllow}") @Max(value = 2, message = "{common.notAllow}") @PathVariable("type") Integer type,
                                @PathVariable(name = "name") String name) throws Exception {
        switch (type) {
            case 1:
                return ApiResult.successOf("开发中!");
            default:
                throw new BusinessException("未知的上传类型");
        }
    }

    @Value("${rsa.private-key}")
    private String privateKey;

    @Value("${rsa.public-key}")
    private String publicKey;

    @PostMapping(value = "rsa")
    @ApiOperation(value = "rsa", notes = "rsa", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "encryptStr", value = "加密字符串", dataType = "string", paramType = "query", required = true, allowableValues = "1", defaultValue = "1"),
    })
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResult rsaTest(@RequestParam(name = "encryptStr") String encryptStr) {

        // 解密密文
        String decodeStr = RsaUtils.rsaDecrypt(encryptStr, privateKey);
        System.out.println(decodeStr);
        return ApiResult.successOfStrData(decodeStr);
    }

}
