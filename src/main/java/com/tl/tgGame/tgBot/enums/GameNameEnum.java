package com.tl.tgGame.tgBot.enums;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/14 , 18:20
 */
public enum GameNameEnum {

    DSBY("21003","大圣捕鱼"),
    BCBY("21004","宝船捕鱼"),
    JDBY("21006","激斗捕鱼"),
    FCBY("21007","发财捕鱼"),
    XJBY("21008","星际捕鱼"),
    JQB("22016","金钱豹"),
    SZXZ("22017","三只小猪"),
    GYS("22018","逛夜市"),
    XMLZ("22019","熊猫龙舟"),
    DGN("22020","大过年"),
    PPH("22021","碰碰胡"),
    JLYQ("22022","锦鲤跃钱"),
    DLM("22023","大乐门"),
    GTSC("22024","龟兔赛车"),
    HGPD("22026","火锅派对"),
    KXDB("22027","开心夺宝"),
    XBQH("22028","寻宝奇航"),
    XBFY("22029","西部风云"),
    HHJQB("22030","豪华金钱豹"),
    YZDZ("22031","宇宙大战"),
    MD("22032","魔豆"),
    TJL("22034","淘金乐"),
    FGDH("22036","富贵大亨"),
    JHMB("22037","巨海觅宝"),
    LMJJ("22038","罗马竞技"),
    LBH("22039","罗宾汉"),
    CFLL("22040","财富连连"),
    DGN2("22041","大过年2"),
    GMMB("22042","古墓秘宝"),
    MTBJ("22043","蜜糖爆击"),
    HCYMF("22045","合成与魔法"),
    SDJL("22047","神灯金灵"),
    FKYN("22048","疯狂野牛"),
    ZS("22049","宙斯"),
    MCJB("22050","喵财进宝"),
    CFJD("22051","财富金蛋"),
    QSTBJ("27001","钱树推币机"),
    MXTTBJ("27002","马戏团推币机"),
    FCTBJ("27003","发财推币机"),
    YCJF("27005","一触即发"),
    DCZB("27007","多彩骰宝"),
    LUKCY9("28001","LUCKY9");



    private String gameId;

    private String gameName;


    GameNameEnum(String gameId,String gameName){
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
        GameNameEnum[] values = values();
        for (GameNameEnum nameEnum: values) {
            if(nameEnum.getGameId().equals(gameId)){
                return nameEnum.getGameName();
            }
        }
        return null;
    }
}
