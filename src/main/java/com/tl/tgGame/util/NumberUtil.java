package com.tl.tgGame.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 13:59
 */
@Component
public class NumberUtil {

    /**
     * 判断字符串是否为数字
     */
    public static Boolean isParsable(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return NumberUtils.isParsable(str);
    }

    /**
     * 判断字符串是不是整数
     */
    public static Boolean isNumeric2(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }


}
