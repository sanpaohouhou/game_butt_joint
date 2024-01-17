package com.tl.tgGame.project.enums;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/23 , 11:44
 */
public enum GameBusiness {


//    FC,WL,EG
    FC("FC","FC电子","\uD83D\uDC9EFC电子"),
    WL("WL","WL棋牌","\uD83C\uDFB0WL棋牌"),
    WL_BJL("WL_BJL","WL百家乐","\uD83D\uDC9EWL百家乐"),
    WL_TY("WL_TY","WL体育","⚽\uFE0FWL体育"),
    FC_BY("FC_BY","FC捕鱼","\uD83C\uDF08FC捕鱼"),
    EG("EG","EG电子","\uD83D\uDC21EG电子"),
    BB("BB","BB游戏","\uD83D\uDC21BB游戏"),;
    private String key;

    private String gameName;

    private String pushName;


    GameBusiness(String key,String gameName,String pushName){
        this.key = key;
        this.gameName = gameName;
        this.pushName = pushName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPushName() {
        return pushName;
    }

    public void setPushName(String pushName) {
        this.pushName = pushName;
    }

    public static final String of(String key){
        for (GameBusiness business : GameBusiness.values()){
            if(business.getKey().equals(key)){
                return business.getGameName();
            }
        }
        return null;
    }

    public static final String pushName(String pushName){
        for (GameBusiness business: GameBusiness.values()) {
            if(business.getPushName().equals(pushName)){
                return business.getKey();
            }
        }
        return null;
    }

    public static final String gameName(String gameName){
        for (GameBusiness business: GameBusiness.values()) {
            if(business.getGameName().equals(gameName)){
                return business.getKey();
            }
        }
        return null;
    }



}
