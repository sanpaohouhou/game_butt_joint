package com.tl.tgGame.exception;


public enum ErrorEnum {

    NO_LOGIN("3000", "未登录"),
    NO_BIND_TOTP("3001", "请先绑定谷歌验证器"),



    PERMISSION_MISS("5001", "权限不足"),
    LOGIN_FAIL("5002", "用户名或者密码错误"),
    OLD_PASSWORD_ERROR("5003", "旧密码错误"),
    USER_NOT_JOIN("5004", "用户未注册"),
    ADMIN_NOT_EXIST("5005", "管理员不存在"),
    USERNAME_ALREADY_USED("5006", "用户已注册"),
    BOT_NOT_ALLOW_WITHDRAW("5007", "机器人不可充值提现"),
    CREDIT_LACK("5008", "余额不足"),
    OBJECT_NOT_FOUND("5009", "找不到对象"),
    STATUS_NOT_ALLOW_UPDATE("5010","该状态不允许更新"),
    NOT_TEAM_PEOPLE("5011","团队无人员"),
    ORDER_TYPE_NOT_AUTH("5012","该订单类型不允许审核"),
    YOU_HAS_BEEN_BANNED("5013","您已被封禁"),
    REPEAT_SET_AGENT("5014", "重复设置代理商"),
    API_GAME_RECORD_ADD_FAIL("5015","添加游戏记录失败"),
    WITHDRAW_FAIL("5016","提现失败"),
    ORDER_EXCEPTION("5017","订单异常"),

    GAME_RECHARGE_FAIL("5018","fc充值失败"),
    GAME_GET_RECORD_LIST_FAIL("5019","fc获取记录失败"),

    TOP_EXIST_AGENT_NOT_APPLY("5020","已有上级代理，不可成为代理"),


    NOT_OPEN("6000", "配置未设置"),
    NETWORK_ERROR("6001", "网络错误"),
    SYSTEM_BUSY("6002", "系统繁忙"),
    CONTRACT_ADDRESS_ERROR("6003", "合约地址错误"),
    ADDRESS_ERROR("6004", "地址错误"),
    SYSTEM_ERROR("6005", "系统错误"),
    PARAM_ERROR("6006","参数错误"),
    CAPTCHA_ERROR("6007","图形验证码错误"),
    TOTP_ERROR("6007","谷歌验证码错误"),
    INTERNAL_ERROR("9999", "未知错误");

    private final String code;

    private final String message;


    public String getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }


    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public void throwException(Throwable throwable) {
        throw new APIException(this.code, "error." + this);
    }

//    public void throwException() {
//        throw new APIException(this.code, "error." + this);
//    }

    public void throwException() {
        throw new APIException(this.code, this.message);
    }
    public void throwException(String message) {
        throw new APIException(this.code, message);
    }

    public void throwExceptionOfObjects(Object... objects) {
        throw new APIException(this.code, "error." + this, objects);
    }

    public static void throwException(String code, String message) {
        throw new APIException(code, message);
    }

}