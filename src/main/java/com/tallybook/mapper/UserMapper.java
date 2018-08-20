package com.tallybook.mapper;

import com.tallybook.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insert(User record);

    User findOneByPhone(String mobilePhone);

    int login(@Param("mobilePhone") String mobilePhone,@Param("password")  String password);

    int updatePassword(@Param("record") User record);
}