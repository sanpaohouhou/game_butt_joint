package com.tl.tgGame.tgBot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tl.tgGame.tgBot.enums.BotState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 10:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash
public class UserBot implements Serializable {

    @Id
    private Long botUserId;

    private Long chatId;

    private String text;

    private Boolean state;

}
