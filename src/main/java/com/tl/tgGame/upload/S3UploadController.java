package com.tl.tgGame.upload;


import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.util.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wangqiyun on 2018/7/31.
 */
@RestController
@RequestMapping("/api/upload")
public class S3UploadController {
    @Resource
    S3UploadFileService s3UploadFileService;
    @Resource
    HttpServletResponse httpServletResponse;

    @GetMapping("/file/pre-signed")
    public Response filePreSigned(@RequestParam String filename) {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        return Response.success(
                Maps.newHashMap("fields", s3UploadFileService.ossUpload(filename),
                        "url", s3UploadFileService.url()));
    }
}
