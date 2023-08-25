package com.tl.tgGame.project.enums;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/23 , 11:44
 */
public enum GameBusiness {


//    FC,WL,EG
    FC("FC","\uD83D\uDC44FC电子"),
    WL("WL","瓦力"),
    EG("EG","EG游戏"),;
    private String key;

    private String gameName;


    GameBusiness(String key,String gameName){
        this.key = key;
        this.gameName = gameName;
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


    public static final String of(String key){
        for (GameBusiness business : GameBusiness.values()){
            if(business.getKey().equals(key)){
                return business.getGameName();
            }
        }
        return null;
    }

}
