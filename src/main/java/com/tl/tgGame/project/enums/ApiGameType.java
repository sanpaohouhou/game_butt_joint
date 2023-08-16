package com.tl.tgGame.project.enums;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 17:31
 */
public enum ApiGameType {

    BU_YU(1,"捕鱼机","21***"),
    LAO_HU(2,"老虎机","22***"),
    TUI_BI(7,"推币机","27***"),
    QI_PAI(8,"棋牌","28***"),;



    private Integer number;

    private String type;

    private String format;

    ApiGameType(Integer number,String type,String format){
        this.format = format;
        this.type = type;
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
