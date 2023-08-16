package com.tl.tgGame.admin.role.entity;

import lombok.Getter;

public enum PermissionEnum {
    HOME("首页", null,"home"),
        HOME_ONE("主页",HOME,"index"),
    USER_MANAGE("用户管理", null,"usermanage"),
        USER_LIST("用户列表", USER_MANAGE,"userList"),

    GAMBLING_MANAGE("赌局管理", null,"match"),
        CARD_TABLE_LIST("牌桌列表", GAMBLING_MANAGE,"matchList"),
        GROUP_LIST("群组列表", GAMBLING_MANAGE,"groupList"),
        RECORDER_LIST("记录员管理", GAMBLING_MANAGE,"recoderManage"),
        BET_LIST("赌局记录", GAMBLING_MANAGE,"matchRecord"),

    PARTNER_MANAGE("合伙人管理", null,"partner"),
        PARTNER_LIST("合伙人列表", PARTNER_MANAGE,"partnerList"),
        MARGIN_WITHDRAWAL("保证金审核", PARTNER_MANAGE,"depositCheck"),

    FINANCE_MANAGE("财务管理", null,"finance"),
        FINANCE_OPERATING_TABLE("财务操作台", FINANCE_MANAGE,"operate"),
        UPDATE_OPEN_CARD_RESULT("修改开牌结果", FINANCE_MANAGE,"changeRecord"),
        FINANCE_DAILY_STATEMENT("财务日报表", FINANCE_MANAGE,"dailyReport"),
        AGGREGATION_MANAGE("归集管理", FINANCE_MANAGE,"collection"),
        HEAT_PURSE_RECORD("热钱包记录", FINANCE_MANAGE,"hotWallet"),

    LIVE_MANAGE("直播管理",null,"broadcast"),
        LIVE_AUTH("直播审核",LIVE_MANAGE,"liveAudit"),
        LIVE_TYPE("直播类型",LIVE_MANAGE,"liveType"),
        LIVE_LIST("直播列表",LIVE_MANAGE,"liveList"),
        GIFT_LIST("礼物列表",LIVE_MANAGE,"giftList"),
        LIVE_RECORD("直播记录",LIVE_MANAGE,"liveRecord"),
        GIFT_RECORD("赠送记录",LIVE_MANAGE,"giftRecord"),
        EXCHANGE_RECORD("兑换记录",LIVE_MANAGE,"exchangeRecord"),

    DATA_CENTER("数据中心",null ,"data"),
        PROFIT_LOSS_LIST("盈亏列表", DATA_CENTER,"profitlossList"),
        AGENT_LIST("充值列表", DATA_CENTER,"rechargeList"),
        WITHDRAWAL_LIST("提现列表", DATA_CENTER,"withdrawalList"),
        TRANSFER_LIST("转账列表", DATA_CENTER,"transferList"),
        SETTLEMENT_LIST("结算列表", DATA_CENTER,"settlementList"),

    SYS_MANAGE("系统管理", null,"account"),
        ROLE_MANAGE("角色管理", SYS_MANAGE,"role"),
        ADMIN_MANAGE("管理员管理", SYS_MANAGE,"admin"),
        CONFIG_MANAGE("配置管理", SYS_MANAGE,"setup");

    @Getter
    private String permissionName;

    @Getter
    private PermissionEnum parent;

    @Getter
    private String pathName;

    PermissionEnum(String permissionName, PermissionEnum parent,String pathName) {
        this.permissionName = permissionName;
        this.parent = parent;
        this.pathName = pathName;
    }


    public static void main(String[] args) {
        PermissionEnum[] values = PermissionEnum.values();
        StringBuilder builder = new StringBuilder();
        for (PermissionEnum vale: values) {
            builder.append(vale.name()).append(",");
        }
        System.out.println(builder);
    }


}
