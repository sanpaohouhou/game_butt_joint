package com.tl.tgGame.system.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConfigMapper {
    @Select("SELECT * FROM `config` WHERE `name` = #{name}")
    Config get(String name);

    @Select("SELECT * FROM `config`")
    List<Config> all();

    @Insert("REPLACE INTO `config`(`name`, `value`, `node`) VALUES (#{name}, #{value}, #{node})")
    int insert(Config config);

    @Update("UPDATE `config` SET `value` = #{newValue} WHERE `name` = #{name} AND `value` = #{oldValue}")
    long cas(@Param("name") String name, @Param("oldValue") String oldValue, @Param("newValue") String newValue);

    @Update("UPDATE `config` SET `value` = #{value} WHERE `name` = #{name}")
    long update(@Param("name") String name, @Param("value") String value);

}
