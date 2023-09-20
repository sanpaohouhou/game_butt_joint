package com.tl.tgGame.admin.role.entity;

import lombok.Getter;

public enum PermissionEnum {
    HOME("首页", null,"home"),
        HOME_ONE("主页",HOME,"index"),
    USER_MANAGE("用户管理", null,"usermanage"),
        USER_LIST("用户列表", USER_MANAGE,"userList"),
        USER_RECHARGE("用户充值", USER_MANAGE,"userRecharge"),
        USER_PROMOTION("用户推广", USER_MANAGE,"userPromotion"),
        USER_PROFIT("用户获利", USER_MANAGE,"userProfit"),

    FINANCE_MANAGE("财务管理", null, "financial"),

        FINANCE_BILL("帐变列表",FINANCE_MANAGE,"finanBill"),
        FINAN_MARGIN("保证金管理",FINANCE_MANAGE,"finanMargin"),
        USER_WITHDRAW("用户提现", FINANCE_MANAGE,"userWithdraw"),

    PROXY("代理管理",null,"proxy"),
        PROXY_MANAGE("代理管理",PROXY,"proxyManage"),
        PROXY_ASSET_MANAGE("代理资金管理",PROXY,"proxyAssetManage"),

    GAME_MANAGE("游戏管理", null,"game"),
        GAME_LIST("游戏列表", GAME_MANAGE,"gameList"),
        BET_LIST("游戏记录", GAME_MANAGE,"betRecord"),
        GAME_AN_TI("反彩比例",GAME_MANAGE,"gameAnti"),

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
