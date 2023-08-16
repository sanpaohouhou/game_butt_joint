package com.tl.tgGame.exception;


public enum ErrorEnum {

    NO_LOGIN("3000", "未登录"),
    NO_BIND_TOTP("3001", "请先绑定谷歌验证器"),

    ROUND_NOT_END("4001", "该局未结束"),
    ROUND_NOT_STARTED("4002", "未开始,请等待"),
    INSUFFICIENT_BALANCE("4003", "余额不足"),
    ROUND_SEALED("4004", "已停止下注"),
    ROUND_ALREADY_BET("4005", "已停止下注"),
    MAXIMUM_BET_AMOUNT("4006", "下注失败,超出限红"),
    MINIMUM_BET_AMOUNT("4007", "下注失败,低于限红"),
    ROUND_STARTED_REQUIRED("4008", "必须得是下注状态才能流局"),
    RECHARGE_FAIL("4009", "充值失败，用户不存在"),
    WITHDRAW_FAIL("4010", "提现失败，用户可用余额不足"),
    TABLE_NOT_EXIST("4011", "赌桌不存在，或已关闭"),
    NO_BET("4012", "请先下注"),
    TABLE_MAXIMUM_BET_AMOUNT("4013", "台红低于限红,流局"),
    TABLE_MINIMUM_BET_AMOUNT("4014", "台红高于限红,流局"),
    TABLE_ID_EXIST("4015", "桌号已存在。"),
    TG_GROUP_EXIST("4016", "该群已有牌桌。"),

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
    PARTNER_NOT_JOIN("5005", "合伙人不存在"),
    BOT_NOT_ALLOW_ADD_PARTNER("5006", "机器人不允许成为合伙人"),
    NOT_SETTLED_AMOUNT("5007", "没有可供结算的记录"),
    NO_MERGE("5008", "有未结算的对局，无法合并"),
    H5_NO_BIND("5009", "H5账号未绑定，请先去绑定"),
    TG_NO_BIND("5009", "TG账号未绑定，请先去绑定"),
    H5_BIND("5010", "H5账号已绑定了其他账号"),
    TG_BIND("5010", "TG账号已绑定了其他账号"),
    GROUP_NO_OPEN("5011", "该群未开通"),
    ORDER_EXCEPTION("5012", "订单已审核"),
    LIVE_PERMISSION_MISS("5013","直播无权限"),
    LIVE_NOT_EXIST("5014","直播间不存在"),
    LIVE_CLOSE_SUCCESS("5015","直播间已经关闭,请勿重复操作"),
    LIVE_GAME_STATUS_NOT_CLOSE("5016","直播间游戏状态暂未结束,请勿关播"),
    PLEASE_AGAIN_OPEN_LIVE("5017","请勿重复开播"),
    CHARGE_ONLINE_NUMBER_FAIL("5018","改变在线人数失败"),
    ADD_FIELD_INCOME_FAIL("5019","增加本场收益失败"),
    ADD_DIAMOND_FAIL("5020","兑换钻石失败"),
    REDUCE_DIAMOND_FAIL("5021","钻石兑换usdt失败"),
    LIVE_STATUS_NOT_UPDATE("5022","直播间状态不允许更新"),
    EXCHANGE_AMOUNT_SMALL("5023","兑换金额太小,请调整"),


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

    public void throwException() {
        throw new APIException(this.code, "error." + this);
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