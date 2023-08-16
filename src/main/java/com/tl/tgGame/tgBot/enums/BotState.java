package com.tl.tgGame.tgBot.enums;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 10:56
 */
public enum BotState {

    USER_RECHARGE("",1),;


    /**
     *
     */
    private Integer state;

    private String text;

    BotState(String text, Integer state){
        this.text = text;
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
