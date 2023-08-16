package com.tl.tgGame.system;


import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.system.mapper.Config;
import com.tl.tgGame.system.mapper.ConfigMapper;
import com.tl.tgGame.util.AESUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ConfigService {
    @Value("${security.key}")
    private String securityKey;

    @Cacheable(value = "ConfigService.get", key = "#name")
    public String _get(String name) {
        Config config = configMapper.get(name);
        if (config == null) return null;
        else return config.getValue();
    }

    public String get(String name) {
        String config = _get(name);
        if (StringUtils.isEmpty(config)) ErrorEnum.NOT_OPEN.throwException(name + "配置未设置");
        return config;
    }

    public BigDecimal getDecimal(String name) {
        return new BigDecimal(get(name));
    }

    public Integer getInteger(String name){
        return Integer.valueOf(get(name));
    }

    public boolean setDecimal(String name, BigDecimal value) {
        return set(name, value.toPlainString());
    }

    public String getOrDefault(String name, String value) {
        String config = _get(name);
        if (config == null) {
            return value;
        }
        return config;
    }

    /**
     * 当获取的配置为加密配置的时候, 需要用到当前方法
     */
    @Cacheable(value = "ConfigService.getAndDecrypt", key = "#name")
    public String _getAndDecrypt(String name) {
        Config config = configMapper.get(name);
        if (config == null) return null;
        else return AESUtil.decrypt(config.getValue(), securityKey);
    }

    public String getAndDecrypt(String name) {
        String config = _getAndDecrypt(name);
        if (StringUtils.isEmpty(config)) ErrorEnum.NOT_OPEN.throwException(name + "未配置");
        return config;
    }

    public String _getAndDecryptOrDefault(String name, String value) {
        String config = _getAndDecrypt(name);
        if (config == null) {
            return value;
        }
        return config;
    }

    @CacheEvict(value = "ConfigService.get", key = "#config.name")
    public long insert(Config config) {
        return configMapper.insert(config);
    }

    @CacheEvict(value = "ConfigService.get", key = "#name")
    public boolean cas(String name, String oldValue, String newValue) {
        return configMapper.cas(name, oldValue, newValue) > 0L;
    }

    @CacheEvict(value = "ConfigService.get", key = "#name")
    public boolean set(String name, String value) {
        return configMapper.update(name, value) > 0L;
    }

    public List<Config> all() {
        return configMapper.all();
    }

    @Resource
    private ConfigMapper configMapper;

}
