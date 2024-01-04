package com.tl.tgGame.upload;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.Base64Util;
import com.tl.tgGame.util.Maps;
import com.tl.tgGame.util.crypto.Crypto;
import org.bouncycastle.crypto.util.DigestFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by wangqiyun on 2018/7/31.
 */
@Service
public class S3UploadFileService {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");


    public Map<Object, Object> ossUpload(String filename) {
        try {
            String type = Files.probeContentType(Paths.get(filename));
            if (type == null || !MimeType.MIME_TYPE.containsKey(type)) ErrorEnum.throwException("5001", "文件类型错误");
            String file_name = "file/" + UUID.randomUUID().toString() + "." + MimeType.MIME_TYPE.get(type);
            long now = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("+00:00"));
            String date = DATE_FORMAT.format(Instant.ofEpochMilli(now).atZone(ZoneId.of("+00:00")));
            String policy = Base64Util.encode(getPolicy(file_name, type, now).getBytes(StandardCharsets.UTF_8));
            String signature = new String(Hex.encode(HmacSHA256(policy,
                    getSignatureKey(AWSSecretAccessKey(), date, region(), "s3"))));
            return Maps.newHashMap("Policy", policy,
                    "key", file_name,
                    "Content-Type", type,
                    "x-amz-credential", AWSAccessKeyId() + "/" + date + "/" + region() + "/s3/aws4_request",
                    "x-amz-algorithm", "AWS4-HMAC-SHA256",
                    "x-amz-date", simpleDateFormat.format(new Date(now)),
                    "x-amz-signature", signature
            );
        } catch (IOException e) {
            ErrorEnum.INTERNAL_ERROR.throwException();
        }
        return null;
    }

    private String getPolicy(String key, String type, long now) throws JsonProcessingException {
        List<Object> conditions = new ArrayList<>(), content_length_range = new ArrayList<>();
        String date = DATE_FORMAT.format(Instant.ofEpochMilli(now).atZone(ZoneId.of("+00:00")));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("+00:00"));
        SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("+00:00"));
        content_length_range.add("content-length-range");
        content_length_range.add(0);
        content_length_range.add(1048576000);
        conditions.add(Maps.newHashMap("key", key));
        conditions.add(content_length_range);
        conditions.add(Maps.newHashMap("Content-Type", type));
        conditions.add(Maps.newHashMap("bucket", bucketName()));
        conditions.add(Maps.newHashMap("x-amz-algorithm", "AWS4-HMAC-SHA256"));
        conditions.add(Maps.newHashMap("x-amz-credential", AWSAccessKeyId() + "/" + date + "/" + region() + "/s3/aws4_request"));
        conditions.add(Maps.newHashMap("x-amz-date", dateSimpleDateFormat.format(new Date(now))));
        Map<Object, Object> data = Maps.newHashMap("expiration", simpleDateFormat.format(new Date(now + 12000000L))
                , "conditions", conditions);
        return new ObjectMapper().writeValueAsString(data);
    }

    public String region() {
        return configService.get("s3_region");
    }

    public String url() {
        return configService.get("s3_url");
    }

    private String bucketName() {
        return configService.get("s3_bucketName");
    }

    private String AWSAccessKeyId() {
        return configService.get("s3_AWSAccessKeyId");
    }

    private String AWSSecretAccessKey() {
        return configService.getAndDecrypt("s3_AWSSecretAccessKey");
    }

    static byte[] HmacSHA256(String data, byte[] key) {
        return Crypto.hmac(DigestFactory.createSHA256(), key, data);
    }

    static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) {
        byte[] kSecret = Utf8.encode("AWS4" + key);
        byte[] kDate = HmacSHA256(dateStamp, kSecret);
        byte[] kRegion = HmacSHA256(regionName, kDate);
        byte[] kService = HmacSHA256(serviceName, kRegion);
        return HmacSHA256("aws4_request", kService);
    }

    @Resource
    private ConfigService configService;
}
