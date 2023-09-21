package com.tl.tgGame.project.enums;

/**
 * @version 1.0
 * @auther w
 * @date 2023/3/23 , 下午1:59
 */
public enum WlGameName {

    WALI(0,"⽡⼒⼤厅"),
    WALI_SX(80,"⽡⼒视讯⼤厅"),
    BUYU(1,"捕鱼"),
    DOU_DI_ZHU(2,"⽃地主"),
    ZHA_JIN_HUA(3,"炸⾦花"),
    BAI_REN_NIU_NIU(4,"百⼈⽜⽜"),
    Q_Z_NIU_NIU(5,"抢庄⽜⽜"),
    ER_REN_M_J(6,"⼆⼈⿇将"),
    RED_BLACK_D_Z(7,"红⿊⼤战"),
    D_Z_PU_KE(8,"德州扑克"),
    D_Z_BJL(9,"电⼦百家乐"),
    PAO_DE_KUAI(10,"跑得快"),
    LONG_HU_D_Z(12,"龙虎⼤战"),
    S_B(17,"骰宝"),
    B_C_B_M(18,"奔驰宝马"),
    F_Q_Z_S(19,"飞禽⾛兽"),
    L_H_C_B(22,"连环夺宝"),
    Q_H_B(23,"抢红包"),
    S_L_W_H(24,"森林舞会"),
    TWENTY_ONE(25,"21点"),
    Q_Z_S_G(26,"抢庄三公"),
    KSANZ_Q_Z_NIU_NIU(27,"看三张抢庄⽜⽜"),
    Q_Z_P_J(28,"抢庄牌九"),
    Q_Z_E_B_G(29,"抢庄⼆⼋杠"),
    T_B_NIU_NIU(30,"通⽐⽜⽜"),
    Y_X_X(32,"鱼虾蟹"),
    THIRTEEN_POINT(34,"⼗三⽔"),
    C_S_D(13,"财神到"),
    B_R_Z_J_H(36,"百⼈炸⾦花"),
    W_X_H_H(37,"五星宏辉"),
    Q_Z_Z_J_H(38,"抢庄炸⾦花"),
    L_Z_NIU_NIU(41,"癞⼦⽜⽜"),
    B_R_T_T_Z(44,"百⼈推筒⼦"),
    E_R_NIU_NIU(46,"⼆⼈⽜⽜"),
    KSIZ_Q_Z_NIU_NIU(48,"看四张抢庄⽜⽜"),
    FK_Q_Z_NIU_NIU(49,"疯狂抢庄⽜⽜"),
    S_R_NIU_NIU(50,"四⼈⽜⽜"),
    SXJD_BJL(81,"视讯经典百家乐"),
    SXJS_BJL(82,"视讯极速百家乐"),
    SXGM_BJL(83,"视讯特⾊百家乐"),
    SXJM_BJL(84,"视讯竞咪百家乐"),
    SX_DN(85,"视讯⽃⽜"),
    SX_ZJH(86,"视讯炸⾦花"),
    SX_NN(87,"视讯⽜⽜"),
    SX_LH(88,"视讯龙虎"),
    SX_SB(89,"视讯骰宝"),
    SX_LP(90,"视讯轮盘"),
    SX_SD(91,"视讯⾊碟"),
    SXBX_BJL(92,"视讯保险百家乐"),
    WALI_TY(100,"⽡⼒体育"),;




    private Integer game;

    private String gameName;

    public Integer getGame() {
        return game;
    }

    public void setGame(Integer game) {
        this.game = game;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    WlGameName(Integer game, String gameName){
        this.game = game;
        this.gameName = gameName;
    }

    public static String of(Integer game){
        WlGameName[] values = WlGameName.values();
        for (WlGameName type:values) {
            if(type.getGame().equals(game)){
                return type.getGameName();
            }
        }
        return null;
    }

}
