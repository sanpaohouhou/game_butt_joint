package com.tl.tgGame.project.enums;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/3 , 10:47
 */
public enum EgGameName {


    MNLA("witchlove","魔女炼爱"),
    TXNP("nekomaid","甜心女仆"),
    XBDMX("adventureofsinbad","辛巴达冒险"),
    SBHJL("sicbovideo","骰宝黄金轮"),
    SDHJL("xocdiavideo","色碟黄金轮"),
    YXXHJL("hooheyhowvideo","鱼虾蟹黄金轮"),
    BLASTX("blastxp","BlastX"),
    PLINKO("plinkop","Plinko"),
    HILO("hilop","HiLo"),
    WHEEL("wheelp","Wheel"),
    DICE("dicep","Dice"),;

    private String gameId;
    private String gameName;
    EgGameName(String gameId, String gameName){
        this.gameId = gameId;
        this.gameName = gameName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public static String of(String gameId){
        EgGameName[] values = values();
        for (EgGameName nameEnum: values) {
            if(nameEnum.getGameId().equals(gameId)){
                return nameEnum.getGameName();
            }
        }
        return null;
    }
}
